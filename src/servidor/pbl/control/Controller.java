/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor.pbl.control;

import java.util.List;

/**
 *
 * @author marcos
 */
public class Controller {
    private static Controller controller;
    private List grupoMulticast; //Lista com os grupos que estão sendo usados.
    
    
    
    public static Controller getInstance(){
        if(controller == null)
            Controller.controller = new Controller();
        return controller;
    } 


}
