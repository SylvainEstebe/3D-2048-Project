package jeuConsole;


import jeuConsole.Jeu;
import java.util.Scanner;


/**
 * Classe principale qui lance le programme
 * @author Alexanne WORM
 */
public class Main implements Parametres{
    public static void main(String[] args) {
        Jeu j = new Jeu();
        j.lancementJeu();
    }   
}

