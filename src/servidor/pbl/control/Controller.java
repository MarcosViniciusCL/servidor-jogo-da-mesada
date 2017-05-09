/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.control;

import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.pbl.model.Sala;

/**
 * Faz o controller de todas as ações do servidor, entre elas estão criar salas, adicionar um
 * jogador a uma sala, informar salas disponiveis e etc.
 * @author marcos
 */
public class Controller {

    private static Controller controller;
    private final List<InetAddress> gruposMulticast; //Lista com os grupos que estão sendo usados.
    private final List<Sala> salas;

    private String[] endGrupDisp; //Fica salvo os enderencos de grupos que ainda não foi usado.
    private boolean[] endUsados; //Vetor que armazena quais endereços estão disponiveis. 

    public Controller() {
        this.gruposMulticast = new ArrayList();
        this.salas = new ArrayList();
    }

    //******************************************* METODOS PARA GERENCIAR O JOGO ****************************
    /**
     * Adiciona o jogador a uma partida existente ou cria uma caso não haja.
     *
     * @param maxJogadores
     * @param quantMeses
     * @param socketNovoJogador
     */
    public void entrarPartida(int maxJogadores, int quantMeses, Socket socketNovoJogador) {
        Sala sala = pesquisarSala(maxJogadores, quantMeses); //Pesquiasando se já existe uma sala com essas caracteristicas
        if (sala == null || sala.getQuantJogadoresSala() >= maxJogadores) {
            sala = novaSala(maxJogadores, quantMeses);
            sala.addJogador(socketNovoJogador);
        } else {
            sala.addJogador(socketNovoJogador);
        }
        
    }

    private Sala novaSala(int maxJogadores, int quantMeses) {
        InetAddress endGrupD = enderecoGrupDisp();
        Sala sala = new Sala(endGrupD, maxJogadores, quantMeses);
        salas.add(sala);
        return sala;
    }

    /**
     * Retorna o endereco multicast que estiver disponivel para criar um novo
     * grupo.
     *
     * @return InetAddress - Endereço multicast disponivel.
     */
    private InetAddress enderecoGrupDisp() {
        for (int i = 0; i < endUsados.length; i++) {
            if (endUsados[i] == false) {
                try {
                    return InetAddress.getByName(endGrupDisp[i]);
                } catch (UnknownHostException ex) {
                    Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        return null;
    }

    /**
     * Pesquisa salas que contem a mesma quantida de jogadores e meses de jogo.
     *
     * @return Sala
     */
    private Sala pesquisarSala(int maxJogadores, int quantMeses) {
        for (Sala sala : salas) {
            if (sala.getMaxJogadores() == maxJogadores && sala.getQuantMes() == quantMeses) {
                return sala;
            }
        }
        return null;
    }

    private void criarArrayValores() {
        this.endGrupDisp = new String[10];
        this.endUsados = new boolean[10];
        for (int i = 0; i < 10; i++) {    //Gerando valores de endereços para grupos(PROVISORIO)
            endGrupDisp[i] = "239.0.0." + i;
        }
        for (int i = 0; i < 10; i++) {    //Informando que nenhum endereco foi usado ainda(PROVISORIO)
            endUsados[i] = false;
        }
    }

    public static Controller getInstance() {
        if (controller == null) {
            Controller.controller = new Controller();
        }
        return controller;
    }

}
