package modele;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * Classe utilisée pour établir une connexion avec la base de
 * données, interroger la base et insérer de nouveaux tuples dans la base
 * @author Sylvain 
 * 
 */
public class ConnexionBDD {

    private final String host, port, dbname, username, password;
    private Connection con = null;
    
      /**
     * Construteur de la classe connexionBDD
     * @param h hote de connexion
     * @param po port de connexion
     * @param dbn nom de la base de donnée
     * @param u nom de l'utilisateur
     * @param p mot de passe
     */
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
            } catch (Exception e) {
                /* ignore close errors */ }
        }
    }

      /**
     * Interroge la base de donnée et récupère les informations
     * @param query requête sql
     * @return retourne une liste de String avec les informations à la suite
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
                    if (i < metadata.getColumnCount()) {
                        tuple += ";";
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

      /**
     * Insert dans la base de donnée 
     * @param updateQuery requête sql
     */
    public void insertTuples(String updateQuery) {
        try {
            this.openConnexion();
            Statement stmt = this.con.createStatement();
            int n = stmt.executeUpdate(updateQuery);
            System.out.println(n + " tuples inseres");
            stmt.close();
        } catch (SQLException e) {
            System.out.println("Probleme avec la requete d'insertion");
            System.out.println("Tuple deja existant");
        } finally {
            this.closeConnexion();
        }
    }


    
}
