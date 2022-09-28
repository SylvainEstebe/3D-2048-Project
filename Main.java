
import java.util.Scanner;


/**
 * Classe principale qui lance le programme
 * @author Alexanne WORM
 */
public class Main implements Parametres{
    public static void main(String[] args) {
        Grille g=new Grille();
        Scanner sc = new Scanner(System.in);
        
        g.nouvelleCase();
        System.out.println("Debut du jeu");
        System.out.println(g);
        while (!g.partieFinieG()) {
            System.out.println("Déplacer vers la Droite (d), Gauche (g), Haut (h), ou Bas (b) ?");
            String s = sc.nextLine();
            s.toLowerCase();
            if (!(s.equals("d") || s.equals("droite")
                    || s.equals("g") || s.equals("gauche")
                    || s.equals("h") || s.equals("haut")
                    || s.equals("b") || s.equals("bas"))) {
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
        g.gameOver();
    }
      
}

