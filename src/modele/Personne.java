package modele;

/**
 * Classe qui instancie un joueur pour la base de données
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
     * @return pseudo
     */
    public String getPseudo() {
        return this.pseudo;
    }

    /**
     * Donne le score du joueur
     * @return le score
     */
    public String getScore() {
        return this.score;
    }

    /**
     * Donne le temps qu'a mis le joueur
     * @return le temps
     */
    public String getTemps() {
        return this.temps;
    }

    /**
     * Donne le nombre de déplacements qu'a effectué le joueur
     * @return le nombre de déplacements
     */
    public String getDéplacement() {
        return this.déplacement;
    }
}
