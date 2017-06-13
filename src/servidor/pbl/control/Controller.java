/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.control;

import servidor.pbl.model.Jogador;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import servidor.pbl.exceptions.LimiteDeSalasExcedidoException;
import servidor.pbl.exceptions.SalaInexistenteException;
import servidor.pbl.model.JogadorFinal;
import servidor.pbl.model.Sala;
import servidor.pbl.util.GerenciadorDeEndereco;

/**
 * Faz o controller de todas as ações do servidor, entre elas estão criar salas, adicionar um
 * jogador a uma sala, informar salas disponiveis e etc.
 * @author marcos
 */
public class Controller {

    private static Controller controller;
    private final List<InetAddress> gruposMulticast; //Lista com os grupos que estão sendo usados.
    private final List<Sala> salas;

    private static GerenciadorDeEndereco gerenciadorDeEndereco;
    /*private String[] endGrupDisp; //Fica salvo os enderencos de grupos que ainda não foi usado.
    private boolean[] endUsados; //Vetor que armazena quais endereços estão disponiveis. 
    */
    
    public Controller() {
        this.gruposMulticast = new ArrayList();
        this.salas = new ArrayList();
        //this.criarArrayValores();
        this.gerenciadorDeEndereco = new GerenciadorDeEndereco(10);
    }

    //******************************************* METODOS PARA GERENCIAR O JOGO ****************************
    /**
     * Adiciona o jogador a uma partida existente ou cria uma caso não haja.
     *
     * @param maxJogadores
     * @param quantMeses
     * @param novoJogador
     * @throws servidor.pbl.exceptions.LimiteDeSalasExcedidoException
     */
    public synchronized void entrarPartida(int maxJogadores, int quantMeses, Jogador novoJogador) throws LimiteDeSalasExcedidoException {
        Sala sala = pesquisarSalaDisponivel(maxJogadores, quantMeses); //Pesquiasando se já existe uma sala com essas caracteristicas
        if (sala == null) { //verifica se não encontrou a sala, ou se a sala encontrada está cheia
            sala = novaSala(maxJogadores, quantMeses); //Cria uma nova sala;
            sala.addJogador(novoJogador); //Adiciona um novo jogador;
            novoJogador.setIdentificacao(sala.indexJogador(novoJogador)+1); //Identifica o jogador com o numero de sua posiçãõ na lista+1
            novoJogador.assinarGrupo(sala.getEndGrupo().getHostName(), 12123); //Manda o cliente se cadastrar no grupo que foi adicionado
        } else {
            sala.addJogador(novoJogador); //Adiciona novo jogador na sala;
            novoJogador.setIdentificacao(sala.indexJogador(novoJogador)+1); //Identifica o jogador com o numero de sua posiçãõ na lista+1
            novoJogador.assinarGrupo(sala.getEndGrupo().getHostName(), 12123); //Manda o cliente se cadastrar no grupo que foi adicionado
            if(sala.isFull()){ //verifica se a sala está cheia
                sala.iniciarPartida();
            }
        }
        
    }
    
    /**
     * Remove um jogador da sala
     * @param jogador jogador que deseja abandonar a partida
     * @param endSala endereço da sala do jogador
     */
    public synchronized void sairPartida(Jogador jogador, InetAddress endSala) throws SalaInexistenteException{
        Sala sala = buscarSalasCadastradas(endSala);
        if(sala !=null){
            sala.remJogador(jogador);
        }
        throw new SalaInexistenteException();
    }
    
    /**
     * Cria uma nova sala.
     * @param maxJogadores A quantidade maxima de jogadores da nova sala.
     * @param quantMeses A quantidade de meses do tabuleiro da sala.
     * @return A sala criada.
     */
    private Sala novaSala(int maxJogadores, int quantMeses) throws LimiteDeSalasExcedidoException {
        InetAddress endGrupD = enderecoGrupDisp(); //Retorna o endereço que disponivel para criar um novo grupo.
        if(endGrupD == null){
            throw new LimiteDeSalasExcedidoException();
        }
        Sala sala = new Sala(endGrupD, maxJogadores, quantMeses); //Cria uma nva sala.
        salas.add(sala); //Adiciona a sala na lista
        return sala;
    }

    /**
     * Devolve um endereço disponivel para criação de sala 
     * @return 
     */
    private InetAddress enderecoGrupDisp(){
        String ip = gerenciadorDeEndereco.getEnderecoAvailable();
        if(ip==null){
            return null;
        }
        try {
            return InetAddress.getByName(ip);
        } catch (UnknownHostException ex) {
            Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    /**
     * Pesquisa salas que contem a mesma quantida de jogadores e meses de jogo.
     *
     * @return Sala
     */
    private Sala pesquisarSalaDisponivel(int maxJogadores, int quantMeses) {
        for (Sala sala : salas) {
            if (sala.getMaxJogadores() == maxJogadores && sala.getQuantMes() == quantMeses && !sala.isFull()) {
                return sala;
            }
        }
        return null;
    }
     
    /**
     * Procura uma sala já cadastrada pelo endereço
     * @param end 
     * @return 
     */
    private Sala buscarSalasCadastradas(InetAddress end){
        for(Sala sala : salas){
            if(sala.getEndGrupo().equals(end)){
                return sala;
            }
        }
        return null;
    }

    public static Controller getInstance() {
        if (controller == null) {
            Controller.controller = new Controller();
        }
        return controller;
    }

    public void finalizarPartida(String[] dado) throws UnknownHostException {
       List <JogadorFinal> jogadoresFinals = new ArrayList<>();
       
       Sala sala = buscarSalasCadastradas(InetAddress.getByName(dado[1]));
       
       for(int i= 2; i<dado.length; i= i+3){
           int id = Integer.parseInt(dado[i].trim());
           String nome = dado[i+1].trim();
           double saldo = Double.parseDouble(dado[i+2].trim());
           JogadorFinal j = new JogadorFinal(id, nome, saldo);
           jogadoresFinals.add(j);
       }
       
       Collections.sort(jogadoresFinals);
       
       String mensagem = "";
       
       for(JogadorFinal j: jogadoresFinals){
           mensagem += j.getId()+";"+j.getNome()+";"+j.getSaldo()+";";
       }
       
       sala.finalizarPartida(mensagem);
       salas.remove(sala);
    }

    public void encerrarServico() {
        this.salas.clear();
        Controller.gerenciadorDeEndereco.zerarEndereco();
    }

}
