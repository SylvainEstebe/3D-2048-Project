package modele;

import ia.IA2;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import java.util.Scanner;
import variables.Parametres;

/**
 * Classe qui instancie un modèle de jeu 2048 3D
 *
 * @author Manon
 */
public class Jeu implements Parametres, Serializable {

    /**
     * Tableau des grilles du jeu
     */
    private ArrayList<Grille> grilles = new ArrayList<Grille>();
    /**
     * Score final du jeu
     */
    private int scoreFinal = 0;
    /**
     * indique s'il existe une version antérieure de ce jeu
     */
    private boolean existePartiePrecedente;
    /**
     * Tableau des états précédents du jeu
     */
    private LinkedList<Jeu> etatsPrecedents = new LinkedList<>();

    private int directionMouvAleo = 0;

    /**
     * Constructeur qui initialise le jeu
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
     * Constructeur qui permet la copie d'un jeu
     *
     * @param j Jeu à copier
     */
    private Jeu(Jeu j) {
        this.grilles.add(j.grilles.get(0).clone(this));
        this.grilles.add(j.grilles.get(1).clone(this));
        this.grilles.add(j.grilles.get(2).clone(this));
        this.scoreFinal = j.scoreFinal;
        this.existePartiePrecedente = j.existePartiePrecedente;
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
     * Méthode qui retourne le booléen existePartiePrecedente : faux si elle n'y
     * en a pas
     *
     * @return faux s'il n'y en a pas de partie précédente
     */
    public boolean getExistePartiePrecedente() {
        return existePartiePrecedente;
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
     * Méthode qui permet de modifier le booléen existePartiePrecedente
     *
     * @param e : le nouveau booléen qui indique si une partie précédente existe
     * ou non
     */
    public void setExistePartiePrecedente(boolean e) {
        this.existePartiePrecedente = e;
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
        boolean deplacement = false,fusionFaite=false;
        int caseVoisine;
        //System.out.println("Val importante"+deplaceMonterEtDesc.get(localisationCases).getValeur());
        deplaceMonterEtDesc.get(localisationCases).setValAv(deplaceMonterEtDesc.get(localisationCases).getValeur());
        int gardeLocCase = localisationCases;
        if (deplaceMonterEtDesc.get(localisationCases).getValeur() > 0) { // Déplacement uniquement s'il s'agit d'une vraie case (avec une valeur)
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
                    boolean b = deplaceMonterEtDesc.get(caseVoisine).getGrille().fusion(deplaceMonterEtDesc.get(localisationCases), deplaceMonterEtDesc.get(caseVoisine));
                    if (b) {
                        deplacement = true;
                    }
                    if(direction2==MONTERG){
                        localisationCases--;
                    }
                    else{
                        localisationCases++;
                    }
                }
            }
        }
//        System.out.println("Avant "+deplaceMonterEtDesc.get(gardeLocCase).getGrille().getType());
//        System.out.println("Après"+ deplaceMonterEtDesc.get(localisationCases).getGrille().getType());
//        System.out.println("Localisation case"+(localisationCases-gardeLocCase));
        deplaceMonterEtDesc.get(gardeLocCase).setGrilleApDepl(deplaceMonterEtDesc.get(localisationCases).getGrille().getType());
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
    public boolean mouvementAlea() {
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

        directionMouvAleo = valeurs.get(index);
        return true;
    }

    public int getDirectionMouvAleo(){
    return directionMouvAleo;
    }
    /**
     * Méthode qui vérifie si les déplacements MONTERG et DESCG sont possible ou
     * pas dans une partie
     *
     * @return un booléen qui indique si les déplacements MONTERG et DESCG sont
     * possible ou non
     */
    public boolean deplacementFini3G() {

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
     * Méthode qui réinitialise l'attribut fusionne pour chaque grille
     */
    public void resetFusion() {
        for (int i = 0; i < TAILLE; i++) {
            this.grilles.get(i).resetFusion();
        }
    }

    /**
     * Méthode qui vérifie que la partie est terminée
     *
     * @return booleen qui retourne true la partie est terminée sinon false
     */
    public boolean finJeu() {
        if (grilles.get(0).deplacementFiniG() && grilles.get(1).deplacementFiniG() && grilles.get(2).deplacementFiniG() && this.deplacementFini3G()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Méthode qui lance le jeu de manière à ce qu'il soit utilisable dans
     * l'application
     */
    public void lancementJeuAppli() {
        //le jeu commence avec 2 cases
        this.ajoutCases();
        this.ajoutCases();

    }

    /**
     * Méthode qui permet de choisir le nombre de cases à ajouter dans le jeu et
     * les ajoute
     *
     * @param b2 pour savoir si on peut ajouter des cases
     */
    public void choixNbCasesAjout(Boolean b2) {
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
     * l'ordinateur joue un coup à la place du joueur s'il veut Fin du jeu
     */
    public void lancementJeuConsole() {
        Scanner sc1 = new Scanner(System.in);
        Random ra = new Random();
        boolean retour = false; //faux car le retour n'a pas encore été utilisé

        if (this.existePartiePrecedente) {// si le joueur choisit de terminer une partie précédente
            System.out.println(this);
        } else { // si le joueur commence une nouvelle partie
            //le jeu commence avec 2 cases
            this.ajoutCases();
            this.ajoutCases();
            System.out.println("Début du jeu");
            System.out.println(this);
        }
        //Tant que le jeu n'est pas fini
        while (!this.finJeu()) {
            reinitNbDepl();
            //Affichage des différentes fonctionnalités
            System.out.println("Déplacer vers la Droite (d), Gauche (q), Haut (z), ou Bas (s), niveau supérieur (r) niveau inférieur (f) ?");
            System.out.println("Si vous voulez nous laisser choisir pour vous, tapez '?' ");
            System.out.println("Pour quitter le jeu taper 'x'");
            System.out.println("Taper ii pour laisser l'IA jouer à votre place.");
            if (!retour && etatsPrecedents.size() > 0) { //on ne peut pas retourner en arrière si on l'a déjà fait ou si on n'a pas encore joué
                System.out.println("Retourner en arrière ? Tapez b : vous pouvez retourner jusqu'à 5 coups en arrière. Attention ! Vous ne pouvez utiliser le retour en arrière qu'une fois par partie !");
            }

            String s = sc1.next();
            s = s.toLowerCase();
            //Action de l'IA 2
            if (s.equals("ii")) {
                IA2 ia2 = new IA2(this);
                ia2.jeuIA2();
            }
            //Quitter le jeu
            if (s.equals("x")) {
                this.quitter();
            }
            //Annulation de coups
            if (s.equals("b") && !retour && etatsPrecedents.size() > 0) {
                this.undo();
                System.out.println(this);
                retour = true;
            }
            //Mouvement aléatoire de l'ordinateur
            if (s.equals("?")) {
                boolean b2 = this.mouvementAlea();
                choixNbCasesAjout(b2);
                this.majScore();
                System.out.println(this);
                //Déplacement fait par un joueur
            } else if (!(s.equals("d") || s.equals("droite")
                    || s.equals("q") || s.equals("gauche")
                    || s.equals("z") || s.equals("haut")
                    || s.equals("s") || s.equals("bas")
                    || s.equals("r") || s.equals("niveau supérieur")
                    || s.equals("f") || s.equals("niveau inférieur"))) {
                System.out.println("Vous devez écrire d pour Droite, q pour Gauche, z pour Haut ou s pour Bas, r pour supérieur, f pour inférieur");
            } else {
                int direction;
                direction = switch (s) {
                    case "f", "niveau inférieur" ->
                        DESCG;
                    case "r", "niveau supérieur" ->
                        MONTERG;
                    case "d", "droite" ->
                        DROITE;
                    case "q", "gauche" ->
                        GAUCHE;
                    case "z", "haut" ->
                        HAUT;
                    default ->
                        BAS;
                };

                enregistrement();
                boolean b2 = this.deplacerCases3G(direction);
                choixNbCasesAjout(b2);
                if (b2) {
                    validerEnregistrement();
                } else {
                    annulerEnregistrement();
                }
                this.majScore();
                this.resetFusion();
                System.out.println(this);
            }

        }
        //Fin du jeu, affichage de l'état du jeu
        if (this.getValeurMaxJeu() >= OBJECTIF) {
            this.victoire();
        } else {
            this.jeuPerdu();
        }

    }

    /**
     * Methode qui demande au joueur s'il veut terminer une partie précédente ou
     * commencer une nouvelle
     *
     * @return booléen ture: pour terminer une partie précedente et false: pour
     * une nouvelle
     */
    public boolean rechargerPartie() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Voulez-vous reprendre la partie précédente ? (oui/non)");
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
     * Méthode pour sérialiser une partie non finie que le joueur l'a abandonnée
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
     * Methode qui déserialise une partie précédente (dernier état du jeu avant
     * de quiter)
     *
     * @return Jeu l'état des 3 grilles dans la partie précédente
     *
     */
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

    /**
     * méthode qui enregistre l'état actuel de la partie
     */
    public LinkedList<Jeu> enregistrement() {
        etatsPrecedents.addFirst(this.clone());
        return etatsPrecedents;
    }

    /**
     * Méthode qui valide l'enregistrement du coup L'enregistrement est valide
     * si des cases ont bien été déplacées La méthode supprime le 6ème état
     * enregistré dès que la liste est supérieure à 5
     */
    public void validerEnregistrement() {
        if (etatsPrecedents.size() > 5) { //on n'a besoin que de 5 états en arrière
            etatsPrecedents.remove(5); //on supprime le 6ème
        }

        /*
        for (int i = 0; i < etatsPrecedents.size(); i++) {
            System.out.println("Index " +i);
            System.out.println(etatsPrecedents.get(i));
        }*/
    }

    /**
     * Méthode qui annule un enregistrement qui n'a pas été validé = le joueur a
     * choisi une direction mais aucune case n'a été déplacée
     */
    public void annulerEnregistrement() {
        etatsPrecedents.remove(0);
    }

    /**
     * Méthode qui retourne en arrière, à utiliser une seule fois dans la partie
     * La méthode gère jusqu'à 5 retours consécutifs
     */
    public void undo() {
        Scanner sc = new Scanner(System.in);
        String s = "oui";

        //tant qu'on veut encore retourner en arrière et qu'on n'a pas encore utilisé les 5 retours
        while (s.equals("oui") && etatsPrecedents.size() > 0) {
            retour();

            int size = etatsPrecedents.size();

            if (size > 0) {
                System.out.println(size + " retour(s) encore possible(s)");

                System.out.println("Retourner encore en arrière ? Tapez oui/non");
                s = sc.next();
                s.toLowerCase();

                while (!(s.equals("oui") || s.equals("non"))) {
                    System.out.println("Vous devez saisir oui pour continuer à revenir en arrière, sinon non");
                    s = sc.next();
                    s.toLowerCase();
                }
            } else {
                System.out.println("Vous ne pouvez plus revenir en arrière !");
            }
        }
    }

    /**
     * Méthode qui effectue le retour en arrière : restauration de l'état
     * précédent et suppression de son enregistrement
     */
    public void retour() {
        /*
        System.out.println("AVANT RETOUR");
        System.out.println(this);*/

        //retour au coup d'avant
        this.setgrilles(etatsPrecedents.get(0).getGrilles());
        this.setScoreFinal(etatsPrecedents.get(0).getScoreFinal());
        this.setExistePartiePrecedente(etatsPrecedents.get(0).getExistePartiePrecedente());  //à tester
        etatsPrecedents.remove(0);

        /*
        //affichage de l'état précédent que le joueur a demandé 
        System.out.println("APRÈS RETOUR");
        System.out.println(this);*/
    }

    //Méthode qui clone un jeu
    public Jeu clone() {
        return new Jeu(this);
    }

    //Méthode qui réinitialise le nombre de déplacements pour chaque case du jeu
    public void reinitNbDepl() {
        for (int k = 0; k < TAILLE; k++) {
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {
                    grilles.get(k).getGrille().get(i).get(j).setNbDeplac(0);
                    grilles.get(k).getGrille().get(i).get(j).setGrilleApDepl(grilles.get(k).getGrille().get(i).get(j).getGrille().getType());
                    grilles.get(k).getGrille().get(i).get(j).setValAv(0);
                }
            }
        }
    }
}
