package jeuConsole;


import jeuConsole.Jeu;
import java.util.Scanner;


/**
 * Classe principale qui lance le programme
 * @author Alexanne WORM
 */
public class Main implements Parametres{
    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);
        Jeu j = new Jeu();
        
        System.out.println("Début du jeu");
        j.addTiles();
        System.out.println(j.toString());
       
        j.randomMove();
        j.addTiles();
        j.addTiles();
        j.addTiles();
        j.addTiles();
        
        
        
        System.out.println(j.toString());
        
 
        j.randomMove();
        System.out.println(j.toString());
        
         
        /**
        // Début de la partie
        System.out.println("Début du jeu");
        System.out.println(g);
        while (!g.partieFinieG()) {
            System.out.println("Déplacer vers la Droite (d), Gauche (g), Haut (h), ou Bas (b), niveau supérieur(e) niveau inférieur (q) ?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals("d") || s.equals("droite")
                    || s.equals("g") || s.equals("gauche")
                    || s.equals("h") || s.equals("haut")
                    || s.equals("b") || s.equals("bas")
                    || s.equals("e") || s.equals("niveau supérieur")
                    || s.equals("q") || s.equals("niveau inférieur")
                    )) {
                System.out.println("Vous devez écrire d pour Droite, g pour Gauche, h pour Haut ou b pour Bas");
            } else {
                int direction;
                if (s.equals("d") || s.equals("droite")) {
                    direction = DROITE;
                } else if (s.equals("g") || s.equals("gauche")) {
                    direction = GAUCHE;
                } else if (s.equals("h") || s.equals("haut")) {
                    direction = HAUT;
                } else {
                    direction = BAS;
                }
                
                boolean b2 = g.deplacerCases(direction);
                
                g.nouvelleCase();
               
                System.out.println(g);
                if (g.getScoreG()>=OBJECTIF) g.victory();
            }
        }
        
        // Fin de la partie
        g.gameOver();
         **/
        
        
    }
      
}

