/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

/**
 *
 * @author emerson
 */
public class Endereco {
    String ip;
    boolean isAvailable;
    
    /**
     * Inicializa o endereço tornando-o disponivel.
     * @param ip 
     */
    public Endereco(String ip){
        this.ip = ip;
        isAvailable = true;
    }
    
    /**
     * Torna o endereço indisponivel.
     * @return o endereco
     */
    public String usarEndereco(){
        if(isAvailable){
            isAvailable = false;
            return ip;
        }
        return null;
    }
    
    /**
     * Verifica se o endereço está disponivel
     * @return verdadeiro ou falso
     */
    public boolean isAvailable(){
        return isAvailable;
    }
    
    /**
     * Deixa o endereço disponivel
     */
    public void liberarEndereco(){
        isAvailable = true;
    }
    
    /**
     * Retorna esse endereço
     * @return 
     */
    public String getIp(){
        return ip;
    }
}
