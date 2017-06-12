/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcos
 */
public class Sala {

    private InetAddress endGrupo; //Endereço do grupo multicast da sala.
    private int porta; //Porta do grupo multicast.
    private MulticastSocket grupoMulticast;
    private final List<Jogador> jogadores; //Lista com os sockets de todos os jogadores da sala
    private int maxJogadores; //Quantidade maxima de jogadores na sala.
    private int quantMes; //Quantidade de meses dessa partida;

    public Sala(InetAddress endGrupo, int maxJogadores, int quantMes) {
        this.endGrupo = endGrupo;
        this.porta = 12123;
        try {
            this.grupoMulticast = new MulticastSocket(porta);
        } catch (IOException ex) {
            Logger.getLogger(Sala.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.jogadores = new ArrayList<>();
        this.maxJogadores = maxJogadores;
        this.quantMes = quantMes;
    }

    public void addJogador(Jogador jogador) {
        this.jogadores.add(jogador);
    }

    public void remJogador(Jogador jogador) {
        int index = jogadores.indexOf(jogador);
        jogadores.remove(index);
    }

    /**
     * Retorna a quantidade de jogadores que está na sala.
     *
     * @return
     */
    public int getQuantJogadoresSala() {
        return this.jogadores.size();
    }

    public InetAddress getEndGrupo() {
        return endGrupo;
    }

    public void setEndGrupo(InetAddress endGrupo) {
        this.endGrupo = endGrupo;
    }

    /**
     * Retorna a quantidade maxima de jogadores nessa sala.
     *
     * @return
     */
    public int getMaxJogadores() {
        return maxJogadores;
    }

    public void setMaxJogadores(int maxJogadores) {
        this.maxJogadores = maxJogadores;
    }

    public int getQuantMes() {
        return quantMes;
    }

    public void setQuantMes(int quantMes) {
        this.quantMes = quantMes;
    }

    public void iniciarPartida() {
        try {
            Thread.sleep(500);
            enviarMensGRP("0;111;"+gerarInfJogadores()); //iniciar partida;
        } catch (InterruptedException ex) {
            Logger.getLogger(Sala.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    public void finalizarPartida(String msg){
        enviarMensGRP("0;112;"+msg);
    }

    private String gerarInfJogadores(){
        String aux = "";
        aux += this.jogadores.size()+";";
        for (Jogador jogador : jogadores) {
            aux += jogador.getIdentificacao()+";";
            aux += jogador.getNome()+";";
        }
        return aux;
    }
    /**
     * Envia mensagem para para todos os clientes que estão na sala.
     * @param mens 
     */
    private void enviarMensGRP(String mens) {
        try {
            DatagramPacket env = new DatagramPacket(mens.getBytes(), mens.length(), this.endGrupo, this.porta);
            grupoMulticast.send(env);
        } catch (IOException ex) {
            Logger.getLogger(Sala.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public int indexJogador(Object novoJogador) {
        return this.jogadores.indexOf(novoJogador);
    }
    
    /**
     * Verifica se a sala está cheia.
     * @return verdadeiro ou falso.
     */
    public boolean isFull(){
        return jogadores.size()==maxJogadores;
    }

}
