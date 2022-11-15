/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package modele;

/**
 *
 * @author sylvainestebe
 */
public class Personne {

    String pseudo;
    String score;
    String temps;
    String déplacement;

    public Personne(String p, String s, String t, String d) {
        this.pseudo = p;
        this.score = s;
        this.temps = t;
        this.déplacement = d;
    }

    public String getPseudo() {
        return this.pseudo;
    }

    public String getScore() {
        return this.score;
    }

    public String getTemps() {
        return this.temps;
    }

    public String getDéplacement() {
        return this.déplacement;
    }
}
