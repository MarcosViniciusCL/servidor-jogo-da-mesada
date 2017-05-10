/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.control;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 *
 * @author marcos
 */
public class Jogador {
    private String nome;
    private Socket socket;
    private PrintStream enviar;

    public Jogador(String nome, Socket socket) {
        this.nome = nome;
        this.socket = socket;
        configurar();
    }
    
    /**
     * Informa ao cliente qual grupo multicast deve assinar;
     * @param endGrupo
     * @param porta 
     */
    public void assinarGrupo(String endGrupo, int porta){
        enviarMensagem("121;"+endGrupo+";"+porta);
    }
    
    public void enviarMensagem(Object mens) {
        enviar.println(mens);
    }
    
    private void configurar() {
        try {
            this.enviar = new PrintStream(socket.getOutputStream());
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public PrintStream getEnviar() {
        return enviar;
    }

    public void setEnviar(PrintStream enviar) {
        this.enviar = enviar;
    }
    
    
}
