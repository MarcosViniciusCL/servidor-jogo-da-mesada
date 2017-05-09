/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.model;

import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcos
 */
public class Sala {
    private InetAddress endGrupo; //Endereço do grupo multicast da sala.
    private final List<Socket> jogadores; //Lista com os sockets de todos os jogadores da sala
    private int maxJogadores; //Quantidade maxima de jogadores na sala.
    private int quantMes; //Quantidade de meses dessa partida;

    public Sala(InetAddress endGrupo, int maxJogadores, int quantMes) {
        this.endGrupo = endGrupo;
        this.jogadores = new ArrayList<>();
        this.maxJogadores = maxJogadores;
        this.quantMes = quantMes;
    }

    public void addJogador(Socket socket){
        this.jogadores.add(socket);
    }
    
    public void remJogador(Socket socket){
        int index = jogadores.indexOf(socket);
        jogadores.remove(index);
    }
    
    /**
     * Retorna a quantidade de jogadores que está na sala.
     * @return 
     */
    public int getQuantJogadoresSala(){
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
    
    
    
    
}
