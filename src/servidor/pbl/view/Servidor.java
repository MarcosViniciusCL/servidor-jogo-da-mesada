/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.view;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import servidor.pbl.model.ServidorThread;

/**
 *
 * @author marcos
 */
public class Servidor implements Runnable {

    private ServerSocket servidor;
    private Thread threadServidor;
    private final ArrayList<ServidorThread> clientes;
    private boolean ativo;
 

    public Servidor(int porta) throws Exception {
        this.clientes = new ArrayList<>();
        this.ativo = false;

        this.iniciarServidor(porta);
    }

    @Override
    public void run() {
        System.out.println("Aguardando conexão!");
        
        while(ativo){
            try{
                servidor.setSoTimeout(2500);
                Socket socket = servidor.accept();
                ServidorThread atendente = new ServidorThread(socket);
                atendente.start();
                
                clientes.add(atendente);
            }catch (SocketTimeoutException e){
                //ignorar
            }catch(IOException e){
                System.out.println(e);
                break;
            }
        }
    }

    private void iniciarServidor(int porta) throws Exception {
        this.servidor = new ServerSocket(porta);
        ativo = true;
    }

    private void encerrarConexoes() throws InterruptedException {
        for (ServidorThread cliente : clientes){
            cliente.stop();
        }
    }

    public void pararServidor() throws InterruptedException, IOException {
        ativo = false;
        encerrarConexoes();
        servidor.close();
        if(threadServidor != null){
            threadServidor.join();
        }

    }

    public void start() {
        if (!ativo) {
            return;
        }

        ativo = true;
        threadServidor = new Thread(this);
        threadServidor.start();
    }

}
