/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

import java.util.Scanner;
import variables.Parametres;

/**
 *
 * @author Alexanne
 */
public class ThreadAffichIACons extends Thread implements Parametres{
    
    public ThreadAffichIACons(){
        
    }

    @Override
    public void run() {
        Scanner sc1 = new Scanner(System.in);
        String s=sc1.next();
        s = s.toLowerCase();
        boolean est_stoppe=false;
        while (!est_stoppe){
            
            if(s.equals("s")){
                est_stoppe=true;
                
            }
        }
        
    }
    
    
}
