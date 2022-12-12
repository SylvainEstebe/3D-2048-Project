package modele;

import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Classe qui instancie un joueur pour la base de données, permet de ajouter et récupèrer des Personnes
 *
 * @author sylvainestebe
 */
public class Personne {

    /**
     * Pseudo du joueur
     */
    private String pseudo;
    /**
     * Score du joueur
     */
    private String score;
    /**
     * Temps qu'a mis le joueur
     */
    private String temps;
    /**
     * Nombre de déplacements qu'a réalisé le joueur
     */
    private String déplacement;

    /**
     * Construteur d'une personne
     *
     * @param p pseudo
     * @param s score
     * @param t temps
     * @param d déplacement
     */
    public Personne(String p, String s, String t, String d) {
        this.pseudo = p;
        this.score = s;
        this.temps = t;
        this.déplacement = d;
    }

    /**
     * Donne le pseudo du joueur
     *
     * @return pseudo
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Donne le score du joueur
     *
     * @return le score
     */
    public String getScore() {
        return this.score;
    }

    /**
     * Donne le temps qu'a mis le joueur
     *
     * @return le temps
     */
    public String getTemps() {
        return this.temps;
    }

    /**
     * Donne le nombre de déplacements qu'a effectué le joueur
     *
     * @return le nombre de déplacements
     */
    public String getDéplacement() {
        return this.déplacement;
    }

    /**
     * Ajoute une personne dans la base de donnée
     *
     * @param id identification dans la base de donnée
     * @param pseudo pseudonyme choisit par le joueur
     * @param scoreJoueurFin score en fin de partie du joueur
     * @param timeSpentPlaying temps de la partie
     * @param deplacementBDD nombre de déplacement
     */
    public static void ajoutPersonne(int id, String pseudo, int scoreJoueurFin, long timeSpentPlaying, int deplacementBDD) {

        // BDD infos
        String host = "mysql-estebe.alwaysdata.net";
        String port = "3306";
        String dbname = "estebe_2048_game";
        String username = "estebe";
        String password = "pepignon";
        // BDD Connexion
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        // BDD requête ajout Joueur(int,string,int,int,int) Joueur(id,pseudo,score,temps,deplacement)
        String query = "INSERT INTO Joueur VALUES ('" + 0 + "','" + pseudo + "','" + scoreJoueurFin + "','" + timeSpentPlaying + "','" + deplacementBDD + "')";
        c.insertTuples(query);
    }

    /**
     * Récupère les personnes qui sont stockées dans la base de donnée
     *
     * @return ObservableList<Personne> Une liste de personne
     */
    public static ObservableList<Personne> recupPersonne() {
        // Connection à la base de donnée
        String host = "mysql-estebe.alwaysdata.net";
        String port = "3306";
        String dbname = "estebe_2048_game";
        String username = "estebe";
        String password = "pepignon";
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        String infos;
        ObservableList<Personne> listePerso = FXCollections.observableArrayList();

        // Requête pour la base de donnée
        String queryScore = "SELECT pseudo, score, temps, nombreDéplacement FROM Joueur ORDER BY score DESC";
        // Récupération d'une liste de string avec toute les informations
        ArrayList<String> pseudo = c.getTuples(queryScore);

        // Création de la liste de joueur
        for (int i = 0; i < pseudo.size(); i++) {

            // Récupération d'une personne 
            String j = pseudo.get(i);

            // Séparation du pseudo, score, temps, nombre déplacement
            String[] pseudotab = new String[5];
            pseudotab = j.split(";", 4);
            String pseudoP = pseudotab[0];
            String scoreP = pseudotab[1];
            String tempsP = pseudotab[2];
            String nombreP = pseudotab[3];
            // Création d'une personne
            Personne p = new Personne(pseudoP, scoreP, tempsP, nombreP);
            // On met cette personne dans une liste
           // ArrayList<Personne> listePersonne = new ArrayList<Personne>();
            // Création de personne avec la base de donnée
            listePerso.add(p);

           // listePersonne.add(p);
        }
        return listePerso;

    }
}
