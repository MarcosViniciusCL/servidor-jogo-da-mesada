/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.pbl.control.Controller;

/**
 *
 * @author marcos
 */
public class ServidorThread implements Runnable {

    private Socket socket;
    private Thread servidorCliente;
    private boolean ativo;
    private PrintStream enviar;
    private BufferedReader receber;
    private Controller controller;

    public ServidorThread(Socket socket) {
        super();
        this.socket = socket;
        configurar();
    }

    @Override
    public void run() {
        String enderecoIp = this.socket.getInetAddress().getHostAddress();
        System.out.println("Conexão iniciada: "+enderecoIp);
        while (ativo) {
            seletorAcao((String) receberMensagem());
        }
        System.out.println("Conexão encerrada: "+enderecoIp);

    }

    /**
     * Seleciona o método que será executado a partir do dado recebido do
     * cliente.
     *
     * @param valor - Hexadecimal usado para executar uma ação.
     */
    private void seletorAcao(String dados) {
        if (dados != null) {
            String[] dado = dados.split(";");

            switch (dado[0]) {
                case "101": //Informa que quer participar de uma partida
                    entrarPartida(dado);
                    break;
                case "0xff": //Desconectar;
                    desconectar();
                    break;
            }
        }
    }

    /**
     * Interrompe o funcionamento do servido
     * @throws InterruptedException 
     */
    public void stop() throws InterruptedException {
        this.ativo = false;
        this.close();
        this.servidorCliente.join();
    }

    /**
     * Inicia o servidor.
     */
    public void start() {
        if (!ativo) {
            return;
        }

        servidorCliente = new Thread(this);
        servidorCliente.start();
    }

    /**
     * Encerra conexão do socket.
     */
    public void closeSocket() {
        try {
            this.socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Retorna mensagem recebidas pelo cliente conectado.
     *
     * @return Object
     */
    public Object receberMensagem() {
        try {
            return receber.readLine();
        } catch (IOException ex) {
        }
        return null;
    }

    /**
     * Enviar mensagem para cliente conectado.
     *
     * @param mens
     */
    public void enviarMensagem(Object mens) {
        enviar.println(mens);
    }


    /**
     * Configura o servidor para receber mensagens do cliente conectado.
     */
    private void configurar() {
        try {
            this.enviar = new PrintStream(socket.getOutputStream());
            this.receber = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.controller = Controller.getInstance();
            this.ativo = true;
        } catch (IOException ex) {
            close();
        }
    }

    /**
     * Encerra a conexão.
     */
    private void close() {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        if (receber != null) {
            try {
                receber.close();
            } catch (IOException ex) {
                Logger.getLogger(ServidorThread.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if (enviar != null) {
            enviar.close();
        }

        receber = null;
        enviar = null;
        socket = null;

        ativo = false;
    }



    /**
     * Desconecta do cliente conectado.
     */
    private void desconectar() {
        enviarMensagem("0xff");
        close();
    }

    //Chama o controller para poder adicionar o cliente a uma partida ou criar uma
    //nova caso não exita.
    private void entrarPartida(String[] dado) {
        controller.entrarPartida(Integer.parseInt(dado[1]), Integer.parseInt(dado[2]), this.socket);
    }


}
