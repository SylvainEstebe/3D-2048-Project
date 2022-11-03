package modele;

import variables.Parametres;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;

/**
 *
 * @author Manon
 */
public class Jeu implements Parametres, Serializable {

    private ArrayList<Grille> grilles = new ArrayList<Grille>();
    private int scoreFinal = 0;
    private boolean existePartiePrecedente;

    /**
     * Constructeur qui initialise la liste des grilles
     */
    public Jeu() {
        Grille g, g1, g2;
        //Initialiser les 3 grilles 
        g = new Grille(this, GRILLEH);
        g1 = new Grille(this, GRILLEM);
        g2 = new Grille(this, GRILLEB);
        //ajouter les grilles
        grilles.add(g);
        grilles.add(g1);
        grilles.add(g2);
        existePartiePrecedente = false;

    }

    /**
     * Méthode qui retourne le score des 3 grilles au cours d'une partie
     *
     * @return le score des 3 grilles au cours d'une partie
     */
    public int getScoreFinal() {
        return this.scoreFinal;
    }

    /**
     * Méthode qui permet de modifier le score
     *
     * @param score le nouveau score effectué par le joueur suite à un
     * déplacement
     */
    public void setScoreFinal(int score) {
        this.scoreFinal = score;
    }

    /**
     * Méthode qui retourne les 3 grilles du jeu
     *
     * @return les 3 grilles
     */
    public ArrayList<Grille> getGrilles() {
        return grilles;
    }

    /**
     * Méthode qui modifie les 3 grilles du jeu
     *
     * @param nouvellesgrilles la nouvelle version des 3 grilles
     */
    public void setgrilles(ArrayList<Grille> nouvellesgrilles) {
        grilles = nouvellesgrilles;
    }

    /**
     * Methode qui permet d'afficher les grilles côte-à-côte
     *
     * @Override
     * @return String les 3 grilles du jeu et le score
     */
    public String toString() {
        String result = "\n\n";
        String result_interm = "";
        //On boucle sur le nombre de lignes à afficher
        for (int i = 0; i < TAILLE; i++) {
            //On boucle sur le nombre de grilles à afficher
            for (int j = 0; j < grilles.size(); j++) {
                result_interm = "";
                result_interm += "[";
                //Pour afficher une ligne
                for (int k = 0; k < TAILLE; k++) {
                    if (k != TAILLE - 1) {
                        result_interm += grilles.get(j).getGrille().get(i).get(k).getValeur() + " | ";
                    } else {
                        result_interm += grilles.get(j).getGrille().get(i).get(k).getValeur() + "]";
                    }
                }
                result += result_interm + "\t";

            }
            if (i == 0) {
                result += " \t Score : " + scoreFinal;
            }
            result += "\n";
        }
        return result;
    }

    /* 
    *Méthode qui incrémente les points gagnés au scoreFinal à chaque déplacement
     */
    public void majScore() {
        scoreFinal = grilles.get(0).getScoreG() + grilles.get(1).getScoreG() + grilles.get(2).getScoreG();
    }

    /**
     * Méthode qui ferme le programme et affiche un message lorsque la partie
     * est perdue
     *
     */
    public void jeuPerdu() {
        System.out.println("Vous avez perdu, retentez votre chance!");
        this.toString();
        System.exit(1);
    }

    /**
     * Méthode qui ferme le programme et affiche un message de victoire
     */
    public void victoire() {
        System.out.println("Vous avez gagné! Votre score est de :" + scoreFinal);
        this.toString();
        System.exit(0);
    }

    /**
     * Méthode qui serialise la dernière partie et ferme le programme quand le
     * joueur le décide
     */
    public void quitter() {
        this.existePartiePrecedente = true;
        this.serialiser();
        System.exit(0);
    }

    /**
     * Méthode qui donne la valeur maximale atteinte du jeu
     */
    public int getValeurMaxJeu() {
        int max = 0;
        for (int i = 0; i < TAILLE; i++) {
            if (grilles.get(i).getValeurMax() > max) {
                max = grilles.get(i).getValeurMax();
            }
        }
        return max;
    }

    /**
     * Méthode qui déplace les cases dans les 3 grilles selon la direction
     *
     * @param direction gauche,droite,haut,bas monterg, descg)b
     *
     * @return un booleen qui indique si on a déplacé toutes les cases ou non
     */
    public boolean deplacerCases3G(int direction) {
        boolean deplacement = false;
        if (direction == DROITE || direction == GAUCHE || direction == BAS || direction == HAUT) {
            for (int i = 0; i < TAILLE; i++) {
                boolean d = grilles.get(i).deplacerCases(direction);
                if (d) {
                    deplacement = d;
                }
            }
        }

        if (direction == MONTERG || direction == DESCG) {
            ArrayList<ArrayList<Case>> deplaceMonterEtDesc = new ArrayList<>();
            deplaceMonterEtDesc.add(new ArrayList<>()); //x = 0, y = 0
            deplaceMonterEtDesc.add(new ArrayList<>()); //x = 1, y = 0
            deplaceMonterEtDesc.add(new ArrayList<>()); //x = 2, y = 0
            deplaceMonterEtDesc.add(new ArrayList<>()); // x = 0, y = 1...
            deplaceMonterEtDesc.add(new ArrayList<>());
            deplaceMonterEtDesc.add(new ArrayList<>());
            deplaceMonterEtDesc.add(new ArrayList<>());// x = 0, y = 2...
            deplaceMonterEtDesc.add(new ArrayList<>());
            deplaceMonterEtDesc.add(new ArrayList<>());

            // On crée des tableaux de haut en bas pour mieux manipuler les valeurs
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {
                    for (int k = 0; k < TAILLE; k++) {
                        ArrayList<ArrayList<Case>> grille = grilles.get(k).getGrille();
                        int index = grille.get(i).get(j).getY() * 3 + grille.get(i).get(j).getX();
                        deplaceMonterEtDesc.get(index).add(grille.get(i).get(j));
                    }
                }
            }
            if (direction == MONTERG) {
                for (int i = 0; i < TAILLE * TAILLE; i++) {
                    for (int k = 0; k < TAILLE; k++) {
                        boolean d = deplacementUneCaseMD(MONTERG, deplaceMonterEtDesc.get(i), k);
                        if (d) {
                            deplacement = d;
                        }
                    }
                }
            }
            if (direction == DESCG) {
                for (int i = 0; i < TAILLE * TAILLE; i++) {
                    for (int k = TAILLE - 1; k >= 0; k--) {
                        boolean d = deplacementUneCaseMD(DESCG, deplaceMonterEtDesc.get(i), k);
                        if (d) {
                            deplacement = d;
                        }
                    }
                }
            }
        }
        return deplacement;
    }

    /**
     * Méthode qui permet de déplacer une case d'une grille à une autre
     *
     * @param direction2 la direction vers laquelle on déplace la
     * grille(MONTERG/DESCG)
     * @param deplaceMonterEtDesc liste des cases à déplacer
     * @param localisationCases
     * @return un booleen qui indique si on a déplacé une case ou non
     */
    private boolean deplacementUneCaseMD(int direction2, ArrayList<Case> deplaceMonterEtDesc, int localisationCases) {
        boolean deplacement = false;
        int caseVoisine;

        if (direction2 == MONTERG) {
            caseVoisine = localisationCases - 1;
        } else {
            caseVoisine = localisationCases + 1;
        }

        // Tant que mon voisin = 0, je me déplace avant de chercher à fusionner
        while (caseVoisine >= 0 && caseVoisine < TAILLE && deplaceMonterEtDesc.get(caseVoisine).getValeur() == 0) {
            deplaceMonterEtDesc.get(caseVoisine).setValeur(deplaceMonterEtDesc.get(localisationCases).getValeur());
            deplaceMonterEtDesc.get(localisationCases).setValeur(0);
            deplacement = true;
            // mise à jour de mon index et celui de mon voisin
            if (direction2 == MONTERG) {
                caseVoisine--;
                localisationCases--;
            } else {
                caseVoisine++;
                localisationCases++;
            }
        }

        if (caseVoisine >= 0 && caseVoisine < TAILLE) {
            //Si mon voisin a la même valeur que moi, je fusionne
            if (deplaceMonterEtDesc.get(caseVoisine).getValeur()
                    == deplaceMonterEtDesc.get(localisationCases).getValeur()) {
                deplaceMonterEtDesc.get(caseVoisine).getGrille().fusion(deplaceMonterEtDesc.get(caseVoisine));
                deplaceMonterEtDesc.get(localisationCases).setValeur(0);
                deplacement = true;
            }
        }

        return deplacement;
    }

    /**
     * Méthode qui permet d'ajouter une ou deux cases dans une ou 2 grilles par
     * hasard et selon les places libres dans les grilles
     *
     * @return booleen qui indique si une case a été ajoutée ou pas
     */
    public boolean ajoutCases() {
        boolean places[] = new boolean[3];
        places[0] = true;
        places[1] = true;
        places[2] = true;

        Random ra = new Random();
        int index = ra.nextInt(grilles.size());

        while (!grilles.get(index).nouvelleCase()) {
            places[index] = false;
            if (!(places[0] || places[1] || places[2])) {
                return false;
            }
            index = ra.nextInt(grilles.size());
        }

        return true;
    }

    /**
     * Méthode qui permet l'ordinateur d'effectuer un déplacement aléatoire pour
     * le prochain coup
     *
     * @return true si le mouvement aléatoire a été effectué
     */
    public boolean MouvementAlea() {
        Random ra = new Random();
        //on met les valeurs correspondant aux déplacements dans une liste
        ArrayList<Integer> valeurs = new ArrayList<>();
        valeurs.add(BAS);
        valeurs.add(DROITE);
        valeurs.add(GAUCHE);
        valeurs.add(HAUT);
        valeurs.add(MONTERG);
        valeurs.add(DESCG);
        boolean[] deplacements = new boolean[6];
        deplacements[0] = true;
        deplacements[1] = true;
        deplacements[2] = true;
        deplacements[3] = true;
        deplacements[4] = true;
        deplacements[5] = true;

        int index = ra.nextInt(valeurs.size());
        int chosenMove = valeurs.get(index);
        while (!this.deplacerCases3G(chosenMove)) {
            deplacements[index] = false; // On enregistre que le mouvement choisi est possible
            if (!(deplacements[0] || deplacements[1] || deplacements[2] || deplacements[3] || deplacements[4] || deplacements[5])) {
                return false; // Retourne faux si toutes mouvements sont impossibles
            }
            index = ra.nextInt(valeurs.size());
        }

        return true;
    }

    /**
     * Méthode qui vérifie si les déplacements MONTERG et DESCG sont possible ou
     * pas dans une partie
     *
     * @return un booléen qui indique si les déplacements MONTERG et DESCG sont
     * possible ou non
     */
    public boolean DeplacementFini3G() {

        for (int j = 0; j < TAILLE; j++) {
            for (int k = 0; k < TAILLE; k++) {
                for (int i = 0; i < grilles.size(); i++) {
                    if (grilles.get(i).getGrille().get(j).get(k).getValeur() == 0) {
                        return false;
                    }
                    if (grilles.get(i).getGrille().get(j).get(k).getVoisinDirect(MONTERG) != null) {
                        if (grilles.get(i).getGrille().get(j).get(k).valeurEgale(grilles.get(i).getGrille().get(j).get(k).getVoisinDirect(MONTERG))) {
                            return false;
                        }
                    }
                    if (grilles.get(i).getGrille().get(j).get(k).getVoisinDirect(DESCG) != null) {
                        if (grilles.get(i).getGrille().get(j).get(k).valeurEgale(grilles.get(i).getGrille().get(j).get(k).getVoisinDirect(DESCG))) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    /**
     * Méthode qui vérifie que la partie est terminée
     *
     * @return booleen qui retourne true la partie est terminée sinon false
     */
    public boolean finJeu() {
        if (grilles.get(0).DeplacementFiniG() && grilles.get(1).DeplacementFiniG() && grilles.get(2).DeplacementFiniG() && this.DeplacementFini3G()) {
            return true;
        } else {
            return false;
        }
    }

    public void lancementJeuAppli(){
        //le jeu commence avec 2 cases
        this.ajoutCases();
        this.ajoutCases();
        
    }
    
    public void choixNbCasesAjout(Boolean b2){
        Random ra = new Random();
        if (b2) {
            int random = ra.nextInt(2) + 1;
            for (int i = 0; i < random; i++) {
                this.ajoutCases();
            }
        }
        this.majScore();
        
    }
    
    
    /**
     * Méthode lancement et déroulement du jeu Affectuer des déplacements selon
     * une direction saisie par le joueur + synchronisation du score + aide:
     * l'odinateur joue un coup à la place du joueur s'il veut Fin du jeu
     */
    public void lancementJeuConsole() {
        Scanner sc1 = new Scanner(System.in);
        Random ra = new Random();
        if (this.existePartiePrecedente) {// si le joueur choisit de terminer une partie précédente
            System.out.println(this);
        } else { // si le joueur commence une nouvelle partie
            //le jeu commence avec 2 cases
            this.ajoutCases();
            this.ajoutCases();
            System.out.println("Début du jeu");
            System.out.println(this);
        }
        while (!this.finJeu()) {
            System.out.println("Déplacer vers la Droite (d), Gauche (g), Haut (h), ou Bas (b), niveau supérieur (e) niveau inférieur (q) ?");
            System.out.println("Si vous voulez nous laisser choisir pour vous, tapez '?' ");
            System.out.println("Pour quitter le jeu taper 'x'");
            String s = sc1.next();
            s = s.toLowerCase();
            if (s.equals("x")) {
                this.quitter();
            }
            if (s.equals("?")) {
                this.MouvementAlea();
                this.majScore();
                System.out.println(this);

            } else if (!(s.equals("d") || s.equals("droite")
                    || s.equals("g") || s.equals("gauche")
                    || s.equals("h") || s.equals("haut")
                    || s.equals("b") || s.equals("bas")
                    || s.equals("e") || s.equals("niveau supérieur")
                    || s.equals("q") || s.equals("niveau inférieur"))) {
                System.out.println("Vous devez écrire d pour Droite, g pour Gauche, h pour Haut ou b pour Bas, e pour supérieur, q pour inférieur");
            } else {
                int direction;
                direction = switch (s) {
                    case "q", "niveau inférieur" ->
                        DESCG;
                    case "e", "niveau supérieur" ->
                        MONTERG;
                    case "d", "droite" ->
                        DROITE;
                    case "g", "gauche" ->
                        GAUCHE;
                    case "h", "haut" ->
                        HAUT;
                    default ->
                        BAS;
                };

                boolean b2 = this.deplacerCases3G(direction);
                choixNbCasesAjout(b2);
                System.out.println(this);

            }

        }
        if (this.getValeurMaxJeu() >= OBJECTIF) {
            this.victoire();
        } else {
            this.finJeu();
        }

    }

    
    /**
     *Methode qui demande au joueur s'il veut terminer une partie précédente ou 
     * commencer une nouvelle
     * @return booléen ture: pour terminer une partie précedente et false: pour une nouvelle
     */
    public boolean rechargerPartie() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous reprendre la partie précepente ?(oui/non)");
        String s = sc.next();
        s.toLowerCase();
        while (!s.equals("oui") || !s.equals("non")) {
            if (s.equals("oui")) {
                this.deserialiser();
                return true;
            } else if (s.equals("non")) {
                return false;
            } else {
                System.out.println("vous devez saisir oui pour reprendre la partie précédente, sinon non");
                s = sc.next();
                s.toLowerCase();
            }
        }
        return false;
    }

    
    /**
     *Méthode pour sérialiser une partie non finie que le joueur l'a abandonnée
     * en tapant 'x'
     */
    public void serialiser() {
        ObjectOutputStream oos = null;
        try {
            final FileOutputStream fichier = new FileOutputStream("jeu.ser");
            oos = new ObjectOutputStream(fichier);
            oos.writeObject(this);
            oos.flush();
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (oos != null) {
                    oos.flush();
                    oos.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    
    /**
     *Methode qui déserialise une partie précédente (dernier état du jeu avant de quiter)
     * @return Jeu l'état des 3 grilles dans la partie précédente
     **/
    public Jeu deserialiser() {
        ObjectInputStream ois = null;
        try {
            final FileInputStream fichierIn = new FileInputStream("jeu.ser");
            ois = new ObjectInputStream(fichierIn);
            Jeu jeu = (Jeu) ois.readObject();
            return jeu;
        } catch (final java.io.IOException e) {
            e.printStackTrace();
        } catch (final ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (ois != null) {
                    ois.close();
                }
            } catch (final IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

}
