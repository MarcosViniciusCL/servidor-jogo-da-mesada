/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.model;

/**
 *
 * @author emerson
 */
public class JogadorFinal implements Comparable<JogadorFinal>{
    
    private int id;
    private String nome;
    private double saldo;
    
    public JogadorFinal(int id, String nome, double saldo){
        this.id = id;
        this.nome = nome;
        this.saldo = saldo;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public double getSaldo() {
        return saldo;
    }

    public void setSaldo(double saldo) {
        this.saldo = saldo;
    }

    @Override
    public int compareTo(JogadorFinal o) {
        if(saldo==o.getSaldo())
            return 0;
        else if(saldo<o.getSaldo())
            return 1;
        else
            return -1;
    }
}
