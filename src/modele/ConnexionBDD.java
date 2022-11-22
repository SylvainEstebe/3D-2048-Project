package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author castagno
 * Classe utilisée pour établir une connexion avec la base de données, interroger la base et insérer de nouveaux tuples dans la base
 */
public class ConnexionBDD {

    private final String host, port, dbname, username, password;
    private Connection con = null;

    public ConnexionBDD(String h, String po, String dbn, String u, String p) {
        this.host = h;
        this.port = po;
        this.dbname = dbn;
        this.username = u;
        this.password = p;
    }

    /*
     * Ouvre la connexion avec la base de données
     */
    private void openConnexion() {
        String connectUrl = "jdbc:mysql://" + this.host + ":" + this.port + "/" + this.dbname;
        if (this.con != null) {
            this.closeConnexion();
        }
        try {
            Class.forName("com.mysql.jdbc.Driver").getDeclaredConstructor().newInstance();
            this.con = DriverManager.getConnection(connectUrl, this.username, this.password);
            System.out.println("Database connection established.");
        } catch (ClassNotFoundException cnfe) {
            System.out.println("Cannot load db driver: com.mysql.jdbc.Driver");
            cnfe.printStackTrace();
        } catch (Exception e) {
            System.out.println("Erreur inattendue");
            e.printStackTrace();
        }
    }

    /*
     * Ferme la connexion avec la base de données
     */
    private void closeConnexion() {
        if (this.con != null) {
            try {
                this.con.close();
                System.out.println("Database connection terminated.");
            } catch (Exception e) { /* ignore close errors */ }
        }
    }

    /*
     * Interroge la base de données avec la requête passée en paramètre
     * et retourne les résultats sous forme d'une liste de String.
     * Il faut utiliser la méthode executeQuery dans la classe Statement (voir cours 12).
     * Indice : comme on ne sait pas à l'avance combien d'attributs (colonnes) on a dans nos tuples,
     * on peut utiliser la classe ResultSetMetaData (voir méthodes getMetaData() de la classe ResultSet et getColumnCount() de la classe ResultSetMetaData)
     */
    public ArrayList<String> getTuples(String query) {
        ArrayList<String> res = null;
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            ResultSetMetaData metadata = rs.getMetaData(); // permet de récupérer les noms des colonnes des tuples en sortie de la requête
            String tuple;
            res = new ArrayList<>();
            while (rs.next()) {
                tuple = "";
                for (int i = 1; i <= metadata.getColumnCount(); i++) {
                    tuple += rs.getString(i);
                    if (i<metadata.getColumnCount()) {
                        tuple +=";";
                    }
                }
                res.add(tuple);
            }
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Probleme avec la requete");
        } finally {
            this.closeConnexion();
        }
        return res;
    }
    
    /*
     * Insère un ou plusieurs tuples dans la base à partir de la requête passée en paramètre
     * Pour cela, il faut utiliser la méthode executeUpdate dans la classe Statement
     */
    public void insertTuples(String updateQuery) {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            int n = stmt.executeUpdate(updateQuery);
            System.out.println(n+" tuples inseres");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Probleme avec la requete d'insertion");
            System.out.println("Tuple deja existant");
        } finally {
            this.closeConnexion();
        }
    }
    
    public static void ajoutJoueur(int id, String pseudo, int scoreJoueurFin, long timeSpentPlaying, int deplacementBDD){
        
        // BDD infos
        String host = "localhost";
        String port = "3306";
        String dbname = "2048_game_Estebe";
        String username = "root";
        String password = "root";
        // BDD Connexion
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        // BDD requête ajout Joueur(int,string,int,int,int) Joueur(id,pseudo,score,temps,deplacement)
        String query = "INSERT INTO Joueur VALUES ('" + 0 + "','" + pseudo + "','" + scoreJoueurFin + "','" + timeSpentPlaying + "','" + deplacementBDD + "')";
        c.insertTuples(query);

    }
}
