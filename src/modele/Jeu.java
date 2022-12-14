package modele;

import ia.IA1;
import ia.IA2;
import ia.IA3;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Random;
import java.util.Scanner;

import ia.IA1;
import ia.IA2;
import multijoueur.client.Client;
import variables.Parametres;
import static variables.Parametres.BAS;
import static variables.Parametres.DESCG;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.GRILLEB;
import static variables.Parametres.GRILLEH;
import static variables.Parametres.GRILLEM;
import static variables.Parametres.HAUT;
import static variables.Parametres.MONTERG;
import static variables.Parametres.OBJECTIF;
import static variables.Parametres.TAILLE;

/**
 * Classe qui instancie un modèle de jeu 2048 3D
 *
 * @author Manon
 */
public class Jeu implements Parametres, Serializable, Runnable {

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
     * Indique si le retour a déjà été utilisé ou non
     */
    private boolean retour = false; //faux car le retour n'a pas encore été utilisé
    /**
     * Tableau des états précédents du jeu
     */
    private LinkedList<Jeu> etatsPrecedents = new LinkedList<>();

    private int directionMouvAleo = 0;

    private transient Client client;
    
    /**
     * Option pour indiquer qu'il s'agit d'un jeu multijoueurs (false par
     * défaut)
     */
    private boolean multi = false;
    /**
     * Option pour indique qu'il s'agit d'un jeu compétitif (false par défaut)
     */
    private boolean competitif = false;

    /**
     * Détermine si la partie coop est démarrée ou non
     */
    private boolean coopDemarre = false;
    
    /**
     * Détermine si la partie coop est finie ou non
     */
    private boolean coopFini = false;

    /**
     * Constructeur qui initialise le jeu
     *
     * @param c Client du jeu
     * @param m Statut multijoueur du jeu
     */
    public Jeu(Client c, boolean m) {
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

        // Présence du client pour une partie multijoueurs
        if (c != null && m) {
            this.client = c;
            this.multi = m;
        }
    }

    /**
     * Surcharge du constructeur pour une partie solo
     */
    public Jeu() {
        this(null, false);
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
        this.client = j.client;
        this.multi = j.multi;
        this.coopDemarre = j.coopDemarre;
        this.coopFini = j.coopFini;
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
    public void setGrilles(ArrayList<Grille> nouvellesgrilles) {
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
     * Setter pour le statut multijoueurs du jeu
     *
     * @param m Statut multijoueurs
     */
    public void setMulti(boolean m) {
        this.multi = m;
    }

    /**
     * Setter pour le statut compétitif du jeu
     *
     * @param c Statut compétitif
     */
    public void setCompetitif(boolean c) {
        this.competitif = c;
    }

    /**
     * Methode qui permet d'afficher les grilles côte-à-côte
     *
     * @return String les 3 grilles du jeu et le score
     */
    @Override
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
     *
     * @return Valeur maximale du jeu
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
     * Getter des états précédents
     *
     * @return Etats précédents
     */
    public LinkedList<Jeu> getEtatsPrecedents() {
        return this.etatsPrecedents;
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
        boolean deplacement = false, fusionFaite = false;
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
                    if (direction2 == MONTERG) {
                        localisationCases--;
                    } else {
                        localisationCases++;
                    }
                }
            }
        }
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

    public int getDirectionMouvAleo() {
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
        return grilles.get(0).deplacementFiniG() && grilles.get(1).deplacementFiniG() && grilles.get(2).deplacementFiniG() && this.deplacementFini3G();
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
    @Override
    public synchronized void run() {
        Scanner sc1 = new Scanner(System.in);

        if (this.existePartiePrecedente) {// si le joueur choisit de terminer une partie précédente
            System.out.println(this);
        } else if (this.multi && !this.competitif) { // si le joueur commence une partie coopérative
            // Attente que le client du jeu soit notifié pour pousuivre l'exécution (tour par tour)
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // Démarrage pour le premier joueur à joueur
            if (!this.coopDemarre) {
                this.coopDemarre = true;
                this.ajoutCases();
                this.ajoutCases();
                System.out.println("Début du jeu");
                System.out.println(this);
            }
        } else { // si le joueur commence une nouvelle partie solo ou compétitive
            //le jeu commence avec 2 cases
            this.ajoutCases();
            this.ajoutCases();
            System.out.println("Début du jeu");
            System.out.println(this);
        }
        //Tant que le jeu n'est pas fini
        while (!this.finJeu() && this.getValeurMaxJeu() < OBJECTIF) {
            reinitNbDepl();
            //Affichage des différentes fonctionnalités
            System.out.println("Déplacer vers la Droite (d), Gauche (q), Haut (z), ou Bas (s), niveau supérieur (r) niveau inférieur (f) ?");
            System.out.println("Si vous voulez nous laisser choisir pour vous, tapez '?' ");
            System.out.println("Pour quitter le jeu taper 'x'");
            System.out.println("Taper ii pour laisser l'IA jouer à votre place.");
            System.out.println("Taper (e) pour enregistrer votre partie.");
            if (!this.retour && etatsPrecedents.size() > 0) { //on ne peut pas retourner en arrière si on l'a déjà fait ou si on n'a pas encore joué
                System.out.println("Retourner en arrière ? Tapez b : vous pouvez retourner jusqu'à 5 coups en arrière. Attention ! Vous ne pouvez utiliser le retour en arrière qu'une fois par partie !");
            }

            String s = sc1.next();
            s = s.toLowerCase();
            //Action de l'IA 
            if (s.equals("ii")) {
                System.out.println("Taper le numéro d'algo que vous voulez :");
                System.out.println("1. Algo minmax");
                System.out.println("2. Algo Monte Carlo");
                System.out.println("3. Algo Alpha Beta ");
                String ia = sc1.next();
                if (ia.equals("1")) {
                    IA1 ia1 = new IA1(this);
                    ia1.jeuIA1();
                } else if (ia.equals("2")) {
                    IA2 ia2 = new IA2(this);
                    ia2.jeuIA2();
                } else if (ia.equals("3")) {
                    IA3 ia3 = new IA3(this);
                    ia3.jeuIA3();
                } else {
                    System.out.println("Attention, saisie incorrecte!");
                    System.out.println("Il faut taper 1 pour l'algo minmax ");
                    System.out.println("Il faut taper 2 pour l'algo Monte Carlo ");
                    System.out.println("Il faut taper 3 pour l'algo Alpha Beta ");
                }
            }
            //Quitter le jeu
            if (s.equals("x")) {
                this.quitter();
            }
            //Sauvegarder un partie
            if (s.equals("e")) {
                this.serialiser();
                System.out.println("Votre partie a été bien enregistrer! ");
            }
            //Annulation de coups
            if (s.equals("b") && !this.retour && etatsPrecedents.size() > 0) {
                this.undo();
                System.out.println(this);
                this.retour = true;
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
                    case "s", "bas" ->
                        BAS;
                    default ->
                        0;
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

                if (this.multi && this.competitif) this.client.getConnexion().envoyerScore(this.scoreFinal, this.getValeurMaxJeu());
                
                if (this.multi && b2 && !this.competitif && !this.finJeu() && this.getValeurMaxJeu() < OBJECTIF) {
                    this.client.getConnexion().envoyerJeu();
                    try {
                        wait();
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        //Fin du jeu, affichage de l'état du jeu
        if (this.getValeurMaxJeu() >= OBJECTIF) {
            if (!this.multi && !this.competitif) this.victoire();
            
            // Si mode versus, envoi de la notification de victoire
            if (this.multi && this.competitif) this.client.getConnexion().envoyerVictoireVersus();
            
            // Si mode coop, envoi de la grille finale et de la notification de victoire
            if (this.multi && !this.competitif && !this.coopFini) {
                System.out.println("Vous avez terminé la partie !!!");
                this.coopFini = true;
                this.client.getConnexion().envoyerJeu();
                this.client.getConnexion().envoyerVictoireCoop();
            }
        } else {
            if (!this.multi && !this.competitif) this.jeuPerdu();
            
            // Si mode versus, envoi de la notification de défaite
            if (this.multi && this.competitif) this.client.getConnexion().envoyerDefaiteVersus();
            
            // Si mode coop, envoi de la grille finale et de la notification de défaite
            if (this.multi && !this.competitif && !this.coopFini) {
                System.out.println("Vous avez perdu...");
                this.client.getConnexion().envoyerJeu();
                this.coopFini = true;
                this.client.getConnexion().envoyerDefaiteCoop();
            }
        }
        
        if (this.multi && !this.competitif) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Jeu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            if (this.getValeurMaxJeu() >= OBJECTIF) {
                System.exit(0);
            } else {
                System.exit(1);
            }
        }
    }

    /**
     * Methode qui demande au joueur s'il veut terminer une partie précédente ou
     * commencer une nouvelle
     *
     * @return booléen true: pour terminer une partie précedente et false: pour
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
    public void enregistrement() {
        etatsPrecedents.addFirst(this.clone());
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
        //retour au coup d'avant
        this.setGrilles(etatsPrecedents.get(0).getGrilles());
        this.setScoreFinal(etatsPrecedents.get(0).getScoreFinal());
        this.setExistePartiePrecedente(etatsPrecedents.get(0).getExistePartiePrecedente());  //à tester
        etatsPrecedents.remove(0);
        
        // Affichage de la grille après retour en arrière
        System.out.println(this);
    }

    /**
     * Actualise le jeu depuis un autre lors d'une partie en coop
     * 
     * @param j Autre jeu 
     */
    public void actualiserDepuisAutreJeu(Jeu j) {
        this.grilles = j.grilles;
        this.scoreFinal = j.scoreFinal;
        this.existePartiePrecedente = j.existePartiePrecedente;
        this.retour = j.retour;
        this.etatsPrecedents = j.etatsPrecedents;
        this.coopDemarre = j.coopDemarre;
        this.coopFini = j.coopFini;
        
        // Affichage du jeu après actualisation
        System.out.println(this);
    }

    //Méthode qui clone un jeu
    @Override
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
    // Méthode qui retourne la listes des cases vides pour les 3 grilles

    public ArrayList<Case> listeCaseVideMultiGrille() {
        ArrayList<Case> listeCaseVideMulti = new ArrayList<>();
        for (int k = 0; k < TAILLE; k++) {
            listeCaseVideMulti.addAll(grilles.get(k).getListeCaseVide());
        }

        return listeCaseVideMulti;
    }

    public void ajoutCase(Case c) {
        if (c != null) {
            int type = c.getGrille().getType();
            int index;

            if (type == GRILLEH) {
                index = 0;
            } else if (type == GRILLEM) {
                index = 1;
            } else {
                index = 2;
            }

            this.getGrilles().get(index).getGrille().get(c.getX()).get(c.getY()).setValeur(c.getValeur());
        }
    }

    public int scoreDispersion() {
        int dispersionScore = 0;
        int[] voisins = {-2, -1, 0, 1, 2};
        for (int p = 0; p < TAILLE; ++p) {
            for (int i = 0; i < TAILLE; ++i) {
                for (int j = 0; j < TAILLE; ++j) {
                    Case c = grilles.get(p).getGrille().get(i).get(j);
                    if (c.getValeur() == 0) {
                        continue; //ignore empty cells
                    }
                    int nbvoisins = 0;
                    int somme = 0;
                    for (int k : voisins) {
                        int x = c.getX() + k;
                        if (x < 0 || x >= TAILLE) {
                            continue;
                        }
                        for (int l : voisins) {
                            int y = c.getY() + l;
                            if (y < 0 || y >= TAILLE) {
                                continue;
                            }
                            for (int b : voisins) {
                                int z = p + b;
                                if (z < 0 || z >= TAILLE) {
                                    continue;
                                }
                                if (c.getValeur() > 0) {
                                    nbvoisins++;
                                    somme += Math.abs(c.getValeur() - grilles.get(p).getGrille().get(x).get(y).getValeur());
                                }

                            }
                        }
                    }
                    dispersionScore += somme / nbvoisins;
                }
            }
        }

        return dispersionScore;

    }

    /**
     * Méthode qui calcule le score heuristique
     *
     * @param scoreGeneral : Le score actuel
     * @param nbCasesVides : Le nombre de case vide
     * @return scoreCases : Le score de dispersion
     */
    public static int scoreHeuristique(int scoreGeneral, int nbCasesVides, int scoreCases) {
        int score = (int) (scoreGeneral + Math.log(scoreGeneral) * nbCasesVides - scoreCases);
        return Math.max(score, Math.min(scoreGeneral, 1));
    }

}
