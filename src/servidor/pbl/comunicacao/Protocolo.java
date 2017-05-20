/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.comunicacao;

import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.pbl.control.Controller;
import servidor.pbl.exceptions.LimiteDeSalasExcedidoException;
import servidor.pbl.model.Jogador;

/**
 *
 * @author emerson
 */
public abstract class Protocolo {
    private static Controller controller = Controller.getInstance();
    
    /**
     *
     * @param socket
     * @param nome
     * @param qtdMaxJogadores
     * @param qtdMeses
     * @param qtdMax
     */
    public static String entrarSala(Socket socket, String nome, String MaxJogadores, String quantMeses){
        try {
            int qtdJogadores = Integer.getInteger(MaxJogadores);
            int qtdMeses = Integer.getInteger(quantMeses);
            
            Jogador jogador = new Jogador(nome, socket);
            controller.entrarPartida(qtdJogadores, qtdMeses, jogador);
            return ""; //Ok
        } catch (LimiteDeSalasExcedidoException ex) {
            return ""; //erro
        }
    }
}
