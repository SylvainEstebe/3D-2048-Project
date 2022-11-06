/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.ConnexionBDD;
import modele.Jeu;
import variables.Parametres;
import static variables.Parametres.BAS;
import static variables.Parametres.DESCG;
import static variables.Parametres.DROITE;
import static variables.Parametres.GAUCHE;
import static variables.Parametres.HAUT;
import static variables.Parametres.MONTERG;
import static variables.Parametres.OBJECTIF;
import static variables.Parametres.TAILLE;

/**
 * FXML Controller class
 *
 * @author Mouna
 */
public class daltonienController implements Initializable, Parametres {
@FXML
    private Button mouvOrdi;
    @FXML
    private MenuItem chargePartie;
    @FXML
    private MenuItem sauvegardePartie;
    @FXML
    private Button help;
    @FXML
    private Button stopIA;
    @FXML
    private Button undo;
    @FXML
    private GridPane grilleH;
    @FXML
    private GridPane grilleM;
    @FXML
    private Pane tuile;

    @FXML
    private Label valTuile;
    @FXML
    private GridPane grilleB;
    @FXML
    private Label meilleurScore;
    @FXML
    private MenuItem daltonien;

    @FXML
    private MenuItem ia1;
    @FXML
    private MenuItem i2;
    @FXML
    private MenuItem i3;
    @FXML
    private MenuItem stat;

    @FXML
    private MenuItem newPartie;
    @FXML
    private MenuItem quitter;
    private ArrayList<ArrayList<Label>> eltsGrilles;

    private Jeu jeuAppli = null;

    private ArrayList<GridPane> tabGrillesApp;

    @FXML
    private Label score;
    @FXML
    private Button boutonBDD;
    @FXML
    private Button boutonBDD2;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // verification de l'existance d'une partie précedente 
        boolean existe = false;
        ObjectInputStream ois = null;
        try {
            FileInputStream fichierIn = new FileInputStream("jeu.ser");
            existe = (fichierIn != null);
        } catch (FileNotFoundException ex) {
        }
        if (existe == false) { //il n'y a pas une partie enregistrée -->  on lance une nouvelle 
            chargePartie.setDisable(true);
            sauvegardePartie.setDisable(true);
        } else {
            try {
                chargePartie.setDisable(false);
                final FileInputStream fichierIn = new FileInputStream("jeu.ser");
                ois = new ObjectInputStream(fichierIn);
                Jeu jeu = (Jeu) ois.readObject();
                jeuAppli = jeu;
            } catch (final java.io.IOException e) {
                e.printStackTrace();
            } catch (final ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void nouvellePartie(ActionEvent event) {
        tabGrillesApp = new ArrayList<GridPane>();
        tabGrillesApp.add(grilleH);
        tabGrillesApp.add(grilleM);
        tabGrillesApp.add(grilleB);
        jeuAppli = new Jeu();
        jeuAppli.lancementJeuAppli();
        sauvegardePartie.setDisable(false);
mouvOrdi.setDisable(false);
        this.majGrillesApp();

    }

    /**
     * Méthode pour afficher les 3 grilles du jeu
     *
     */
    private void majGrillesApp() {
        //Boucle pour chaque grille
        for (int k = 0; k < TAILLE; k++) {
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {

                    Label caseJeu = new Label("" + jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur());
                    caseJeu.getStyleClass().add("caseJeu");
                    Pane caseJeuCouleur = new Pane();
                    //Gestion des bordures des grilles
                    if (j != 2 && i != 2) {
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 0px 0px 1px ");
                    } else if (j == 2 && i != 2) {
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 0px 1px 1px ");
                    } else if (j != 2 && i == 2) {
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 1px 0px 1px ");

                    } else {
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 1px 1px 1px ");
                    }

                    //Gestion des couleurs des cases
                    switch (jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur()) {
                        case 2:
                            caseJeuCouleur.setStyle("-fx-background-color : #E0431C;");
                            break;
                        case 4:
                            caseJeuCouleur.setStyle("-fx-background-color : #00926a;");
                            break;
                        case 8:
                            caseJeuCouleur.setStyle("-fx-background-color : #c67bd5;");
                            break;
                        case 16:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFD53A;");
                            break;
                        case 32:
                            caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                            break;
                        case 64:
                            caseJeuCouleur.setStyle("-fx-background-color : #FF513F;");
                            break;
                        case 128:
                            caseJeuCouleur.setStyle("-fx-background-color : #00926a;");
                            break;
                        case 256:
                            caseJeuCouleur.setStyle("-fx-background-color : #c67bd5;");
                            break;
                        case 512:
                            caseJeuCouleur.setStyle("-fx-background-color :#bae1f5;");
                            break;
                        case 1024:
                            caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                            break;
                        case 2048:
                            caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                            break;
                    }

                    caseJeuCouleur.getStyleClass().add("couleurCase");
                    caseJeu.setVisible(true);
                    caseJeuCouleur.setVisible(true);
                    tabGrillesApp.get(k).setHalignment(caseJeu, HPos.CENTER);
                    tabGrillesApp.get(k).add(caseJeuCouleur, i, j);
                    tabGrillesApp.get(k).add(caseJeu, i, j);
                }
            }

        }

    }

    @FXML
    /**
     * Méthode qui affiches des aides concernant les commandes du jeu sous forme
     * d'alert *
     *
     */
    private void aidePopUp(MouseEvent event) {
        Stage fenetre_aide = new Stage();
        fenetre_aide.setTitle("Comment jouer?");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");

        //Titre
        Label titre_aide = new Label("Règles du jeu");
        titre_aide.getStyleClass().add("text_horsjeu");
        root.setTop(titre_aide);

        //Texte explicatif
        Pane boxText_expli = new Pane();
        boxText_expli.getStyleClass().add("boxStyle1");
        boxText_expli.setPrefWidth(400);

        Label text_expli = new Label(" Presser D pour aller à droite"
                + "\n Presser G pour aller à gauche." + "\n Presser H pour aller en haut."
                + "\n Presser B pour aller en bas.\n Presser E pour aller au niveau supérieur."
                + "\n Presser Q pour aller au niveau inférieur.");
        text_expli.getStyleClass().add("text_horsjeu2");
        boxText_expli.getChildren().add(text_expli);
        root.setLeft(boxText_expli);
        root.setMargin(boxText_expli, new Insets(10, 10, 10, 10));

        final Scene scene = new Scene(root, 500, 200);
        boolean add = scene.getStylesheets().add("css/daltonien.css");
        fenetre_aide.setScene(scene);
        fenetre_aide.show();

    }

    /**
     * Méthode qui lorsqu'on sélectionne une des ia dans le menu permet le
     * déclenchement de l'algo assoscié Également rend visible le bouton stop IA
     *
     */
    @FXML
    private void ia(ActionEvent event) {
        stopIA.setDisable(false);
    }

    @FXML
    /**
     * Méthode qui affiches les statistiques dans l'interface *
     *
     */
    private void afficheStat(ActionEvent event) {
    }

    @FXML
    private void chargerPartie(ActionEvent event) {
        chargePartie.setDisable(true);
        jeuAppli.deserialiser();
        tabGrillesApp = new ArrayList<GridPane>();
        tabGrillesApp.add(grilleH);
        tabGrillesApp.add(grilleM);
        tabGrillesApp.add(grilleB);
        this.majGrillesApp();
        this.sauvegardePartie.setDisable(false);
         chargePartie.setDisable(true);
        mouvOrdi.setDisable(false);

    }

    @FXML
    private void sauvegarderPartie(ActionEvent event) {
        //Lorsqu'on sauvegarde, le bouton de chargement devient actif
        jeuAppli.serialiser();
         chargePartie.setDisable(false);
    }

    @FXML
    private void undo(MouseEvent event) {
    }

    @FXML
    private void quitter(ActionEvent event) {

        Stage abandonner = new Stage();
        abandonner.setTitle("Voulez-vous vraiment quitter le jeu ? ");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");

        //Titre
        Label message = new Label("\t\t\t\t la partie n'est pas finie!"
                + "\n  Est-ce-que vous êtes sûr de vouloir quitter le jeu  ?");
        message.setFont(new Font("Serif", 18));
        root.setTop(message);
        Pane decision = new Pane();
        decision.setPrefWidth(200);
        Button oui = new Button();
        Button non = new Button();
        oui.setText("Quitter");
        non.setText("Retour");
        oui.setLayoutX(120);
        oui.setLayoutY(20);
        non.setLayoutX(220);
        non.setLayoutY(20);
        decision.getChildren().addAll(oui, non);
        root.setCenter(decision);
        root.setMargin(decision, new Insets(10, 10, 10, 10));
        final Scene scene = new Scene(root, 420, 120);
        boolean add = scene.getStylesheets().add("css/daltonien.css");
        abandonner.setScene(scene);
        abandonner.show();
        non.setOnAction(actionEvent -> abandonner.close());
        oui.setOnAction(actionEvent -> System.exit(0));

    }

    public void majScoreApp() {
        score.setText("" + jeuAppli.getScoreFinal());
        score.setVisible(true);
    }

    @FXML
    private void mouvJoueur(KeyEvent event) {
        String direction = event.getText();
        boolean b = false;
        //Déplacement des cases selon la touche clavier
        if (direction.equals("g")) {
            b = jeuAppli.deplacerCases3G(GAUCHE);
        } else if (direction.equals("d")) {
            b = jeuAppli.deplacerCases3G(DROITE);
        } else if (direction.equals("h")) {
            b = jeuAppli.deplacerCases3G(HAUT);
        } else if (direction.equals("b")) {
            b = jeuAppli.deplacerCases3G(BAS);
        } else if (direction.equals("q")) {
            b = jeuAppli.deplacerCases3G(DESCG);
        } else if (direction.equals("e")) {
            b = jeuAppli.deplacerCases3G(MONTERG);
        }

        jeuAppli.choixNbCasesAjout(b);
        this.majScoreApp();
        this.majGrillesApp();
        if (jeuAppli.finJeu()) {
            if (jeuAppli.getValeurMaxJeu() >= OBJECTIF) {
                this.victoireAppli();
            } else {
                this.jeuPerduAppli();
            }
        }

    }

    public void victoireAppli() {
        Stage fenetre_aide = new Stage();
        fenetre_aide.setTitle("VICTOIRE");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");

        //Titre
        Label victoire = new Label("Félicitations, vous avez gagné!");
        Label scoreAff = new Label("Votre score : " + jeuAppli.getScoreFinal());
        victoire.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(victoire);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 100, 100);
        boolean add = scene.getStylesheets().add("css/style.css");
        fenetre_aide.setScene(scene);
        fenetre_aide.show();
    }

    public void jeuPerduAppli() {
        Stage fenetre_aide = new Stage();
        fenetre_aide.setTitle("DEFAITE");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");

        //Titre
        Label defaite = new Label("Mince, vous avez perdu!");
        Label scoreAff = new Label("Votre score : " + jeuAppli.getScoreFinal());
        defaite.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(defaite);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 100, 100);
        boolean add = scene.getStylesheets().add("css/style.css");
        fenetre_aide.setScene(scene);
        fenetre_aide.show();
    }

    // Enregistrement lors du clic sur le bouton BDD
    @FXML
    private void enregistrementBDD(MouseEvent event) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        // ça serait chouette de demander lors d'une nouvelle partie le pseudo du joueur
        // Le pseudo en dessous est juste pour le test
        String pseudo = "Sylvain";

        // Stockage du score du joueur
        int scoreJoueurFin = Integer.valueOf(score.getText());

        // Récupérer les données de score, temps (existe pas encors) et déplacement (existe pas encore)
        // BDD infos
        String host = "localhost";
        String port = "8889";
        String dbname = "2048_game_Estebe";
        String username = "java";
        String password = "projetjava";
        // BDD Connexion
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        // BDD requête ajout Joueur(int,string,int,int,int) Joueur(id,pseudo,score,temps,deplacement)
        String query = "INSERT INTO Joueur VALUES ('" + 0 + "','" + pseudo + "','" + scoreJoueurFin + "','" + 0 + "','" + 0 + "')";
        c.insertTuples(query);

    }

    @FXML
    private void consultationBDD(MouseEvent event) {
        String host = "localhost";
        String port = "8889";
        String dbname = "2048_game_Estebe";
        String username = "java";
        String password = "projetjava";
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);

        // On faira différentes méthodes en fonction des besoins et différentes conditions
        String queryScore = "SELECT  pseudo, scores FROM Joueur ORDER BY scores DESC ";
        String queryTemps = "SELECT  pseudo, temps FROM Joueur ORDER BY temps ASC";
        String queryDéplacement = "SELECT  pseudo, nombreDéplacement FROM Joueur ORDER BY nombreDéplacement ASC ";
        c.getTuples(queryScore);

    }

    @FXML
    private void mouvOrdiApp(MouseEvent event) {
        if (jeuAppli != null) {
            boolean b2 = jeuAppli.MouvementAlea();
            jeuAppli.choixNbCasesAjout(b2);
            this.majScoreApp();
            this.majGrillesApp();
            if (jeuAppli.finJeu()) {
                if (jeuAppli.getValeurMaxJeu() >= OBJECTIF) {
                    this.victoireAppli();
                } else {
                    this.jeuPerduAppli();
                }
            }
        }

    }

}
