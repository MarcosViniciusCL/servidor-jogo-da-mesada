/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.util;

/**
 *Gerencia até 254 enderecos ips
 * @author emerson
 */
public class GerenciadorDeEndereco {
    private int quantEndereco;
    private Endereco [] enderecos;
    
    public GerenciadorDeEndereco(int quantidade){
        this.quantEndereco = quantidade;
        enderecos = new Endereco[quantidade];
        criarEnderecos(quantidade);
    }
    
    /**
     * Criar os endereço de acordo com a quantidade
     * @param quantidade 
     */
    private void criarEnderecos(int quantidade){
        if(quantidade<255){
            for(int i = 0; i<quantidade; i++){
                enderecos[i] = new Endereco("239.0.0."+(i+1));
            }
        }
    }
    
    /**
     * Devolve um endereco disponivel.
     * @return 
     */
    public String getEnderecoAvailable(){
        for (Endereco endereco : enderecos) {
            if (endereco.isAvailable) {
                endereco.usarEndereco();
                return endereco.getIp();
            }
        }
        return null;
    }
    
    /**
     * Libera um endereco para ser usado
     * @param ip 
     */
    public void liberarEndereco(String ip){
        for (Endereco endereco : enderecos) {
            if (endereco.getIp().equals(ip)) {
                endereco.liberarEndereco();
            }
        }
    }

    public void zerarEndereco() {
        criarEnderecos(this.quantEndereco);
    }
}
