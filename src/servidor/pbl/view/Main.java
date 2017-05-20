/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.view;

import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author marcos
 */
public class Main {

    public static void main(String[] args) throws Exception {
        Servidor servidor = null;
        boolean sair = false;
        while (sair == false) {
            System.out.println("MENU SERVIDOR BANCARIO\n\n");
            System.out.println("1 - INICIAR SERVIDOR\n2 - PARAR SERVIDOR\n3 - SAIR");
            String resp = teclado();
            if (resp.equals("1")) {
                String  porta = "12345";
                servidor = new Servidor(Integer.parseInt(porta));
                servidor.start();
            }
            if (resp.equals("2")) {
                if (servidor != null) {
                    servidor.pararServidor();
                    System.out.println("Servidor parado.");
                    servidor = null;
                } else {
                    System.out.println("Servidor não está em execução.");
                }
            }
            if (resp.equals("3")) {
                if (servidor != null) {
                    servidor.pararServidor();
                }
                System.out.println("Servidor parado.");
                sair = true;
            }
            //clear();
        }

    }

    private static String teclado() {
        Scanner teclado = new Scanner(System.in);
        return teclado.next();
    }
    
}
