/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.sql.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import modele.ConnexionBDD;
import modele.Jeu;
import modele.Main;
import modele.Personne;
import variables.Parametres;

/**
 * FXML Controller class
 *
 * @author sylvainestebe
 */
public class FXMLController implements Initializable, Parametres {

    @FXML
    private AnchorPane rootPane;
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
    private Pane instructionJeu;
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
    private MenuItem dyslexique;
    @FXML
    private MenuItem classique;

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
    // private int compteurCoups = 0;
    private int nbRetour = 0;
    private boolean retourUtilise = false;
    private ArrayList<GridPane> tabGrillesApp;
    private LinkedList<Jeu> etatsPrecedents = null;

    @FXML
    private Label score;
    @FXML
    private Button boutonBDD;
    @FXML
    private Button boutonBDD2;
    @FXML
    private Button retour;

    int compteMouv;
    @FXML
    private Menu fichier;
    @FXML
    private Label txtBDD;
    @FXML
    private ImageView haut;
    @FXML
    private ImageView bas;
    @FXML
    private ImageView gauhe;
    @FXML
    private ImageView droite;
    @FXML
    private ImageView descendre;
    @FXML
    private ImageView monter;
    int deplacementBDD;
    @FXML
    private Button stopchrono;
    @FXML
    private Label chrono;
    long chronos;
    long temps;

    /**
     * Initializes the controller class.
     */
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
            } catch (final ClassNotFoundException e) {

            }
        }

    }

    @FXML
    public void nouvellePartie(ActionEvent event) {
        deplacementBDD = 0;
        chronos = java.lang.System.currentTimeMillis();

        jeuAppli = new Jeu();
        jeuAppli.lancementJeuAppli();
        sauvegardePartie.setDisable(false);
        this.majGrillesApp();
        mouvOrdi.setDisable(false);
        nbRetour = 0;
        retourUtilise = false;

    }

    @FXML
    private void chargerPartie(ActionEvent event) {
        chargePartie.setDisable(true);
        jeuAppli.deserialiser();
        this.majGrillesApp();
        this.sauvegardePartie.setDisable(false);
        mouvOrdi.setDisable(false);
        mouvOrdi.setDisable(false);
        nbRetour = 0;
        retourUtilise = false;
    }

    @FXML
    private void sauvegarderPartie(ActionEvent event) {
        //Lorsqu'on sauvegarde, le bouton de chargement devient actif
        jeuAppli.serialiser();
        chargePartie.setDisable(false);
    }

    @FXML
    private void retour(MouseEvent event) {
        if (!etatsPrecedents.isEmpty()) {
            retourUtilise = true;
            etatsPrecedents.removeFirst();
            jeuAppli = etatsPrecedents.getFirst();
            this.majGrillesApp();
            this.majScoreApp();
            etatsPrecedents.removeFirst();
            nbRetour++;
            if (nbRetour == 5) {
                retour.setDisable(true);
            }
        } else {
            retour.setDisable(true);
        }
    }

    /**
     * Méthode pour afficher les 3 grilles du jeu
     *
     */
    private void majGrillesApp() {
        tabGrillesApp = new ArrayList<GridPane>();
        tabGrillesApp.add(grilleH);
        tabGrillesApp.add(grilleM);
        tabGrillesApp.add(grilleB);
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

                    if (classique.isDisable()) {
                        //Gestion des couleurs des cases
                        switch (jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur()) {
                            case 2 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFFADF;");
                            case 4 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #F3E9BE;");
                            case 8 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #F3C076;");
                            case 16 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FD9C4C;");
                            case 32 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FF7440;");
                            case 64 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FF513F;");
                            case 128 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 256 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 512 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 1024 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 2048 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                        }
                    } else if (daltonien.isDisable()) {
                        switch (jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur()) {
                            case 2 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #E0431C;");
                            case 4 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #00926a;");
                            case 8 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #c67bd5;");
                            case 16 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFD53A;");
                            case 32 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                            case 64 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FF513F;");
                            case 128 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #00926a;");
                            case 256 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #c67bd5;");
                            case 512 ->
                                caseJeuCouleur.setStyle("-fx-background-color :#bae1f5;");
                            case 1024 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                            case 2048 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #bae1f5;");
                        }

                    } else if (dyslexique.isDisable()) {
                        switch (jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur()) {
                            case 2 ->
                                caseJeuCouleur.setStyle("-fx-background-color :  #00FA9A;");
                            case 4 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #7FFFD4;");
                            case 8 ->
                                caseJeuCouleur.setStyle("-fx-background-color :  #f2b179 ;");
                            case 16 ->
                                caseJeuCouleur.setStyle("-fx-background-color :#DDA0DD;");
                            case 32 ->
                                caseJeuCouleur.setStyle("-fx-background-color :  #5F9EA0;");
                            case 64 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #90EE90;");
                            case 128 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #5F9EA0;");
                            case 256 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 512 ->
                                caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            case 1024 ->
                                caseJeuCouleur.setStyle("-fx-background-color :  #90EE90;");
                            case 2048 ->
                                caseJeuCouleur.setStyle("-fx-background-color :  #90EE90;");
                        }
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
    private void affichageDaltonien(ActionEvent event) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/daltonien.css");
        this.dyslexique.setDisable(false);
        this.daltonien.setDisable(true);
        this.classique.setDisable(false);
        help.setVisible(true);
        help.setDisable(false);
        instructionJeu.setVisible(false);
    }

    @FXML
    private void affichageClassique(ActionEvent event) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/classique.css");
        this.dyslexique.setDisable(false);
        this.daltonien.setDisable(false);
        this.classique.setDisable(true);
        help.setVisible(true);
        help.setDisable(false);
        instructionJeu.setVisible(false);

    }

    @FXML
    private void affichageDyslexique(ActionEvent event) {

        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/dyslexique.css");
        this.help.setDisable(true);
        this.dyslexique.setDisable(true);
        this.daltonien.setDisable(false);
        this.classique.setDisable(false);
        instructionJeu.setVisible(true);
        help.setVisible(false);

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

        if (classique.isDisable()) {
            scene.getStylesheets().add("css/classique.css");
        } else if (daltonien.isDisable()) {
            scene.getStylesheets().add("css/daltonien.css");
        }
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
        // Connection à la base de donnée
        String host = "localhost";
        String port = "3306";
        String dbname = "2048_game_Estebe";
        String username = "root";
        String password = "root";
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        String infos;
        ObservableList<Personne> listePerso = FXCollections.observableArrayList();

        // Requête pour la base de donnée
        String queryScore = "SELECT pseudo, score, temps, nombreDéplacement FROM Joueur ORDER BY score ASC";
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
            ArrayList<Personne> listePersonne = new ArrayList<Personne>();
            // Création de personne avec la base de donnée
            listePerso.add(p);

            listePersonne.add(p);
            // On affiche
            txtBDD.setText("" + listePersonne.get(0).getPseudo() + listePersonne.get(0).getScore());

        }
        Stage stat = new Stage();
        TableView<Personne> table = new TableView<Personne>();
        final ObservableList<Personne> data = listePerso;
        Scene scene = new Scene(new Group());
        stat.setTitle("Table View Sample");
        stat.setWidth(600);
        stat.setHeight(1000);
        final Label label = new Label("Classement");
        label.setFont(new Font("Arial", 20));
        table.setEditable(true);

        TableColumn pseudoCol = new TableColumn("Pseudo");
        pseudoCol.setCellValueFactory(
                new PropertyValueFactory<Personne, String>("Pseudo"));

        TableColumn scoreCol = new TableColumn("Score");
        scoreCol.setCellValueFactory(
                new PropertyValueFactory<Personne, String>("Score"));

        TableColumn tempsCol = new TableColumn("Temps");
        tempsCol.setCellValueFactory(
                new PropertyValueFactory<Personne, String>("Temps"));

        TableColumn deplacementCol = new TableColumn("Déplacement");
        deplacementCol.setCellValueFactory(
                new PropertyValueFactory<Personne, String>("Déplacement"));

        table.getColumns().addAll(pseudoCol, scoreCol, tempsCol, deplacementCol);
        table.setItems(data);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stat.setScene(scene);
        stat.show();
    }

    @FXML
    private void quitter(ActionEvent event) {

        Stage abandonner = new Stage();
        abandonner.setTitle("Voulez-vous vraiment quitter le jeu ? ");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");
        Label message = new Label("\t\t\t\t la partie n'est pas finie!"
                + "\n  Est-ce-que vous êtes sûr de vouloir quitter le jeu  ?");
        root.getStyleClass().add("message");
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
        if (classique.isDisable()) {
            scene.getStylesheets().add("css/classique.css");
        } else if (daltonien.isDisable()) {
            scene.getStylesheets().add("css/daltonien.css");
        } else if (dyslexique.isDisable()) {
            scene.getStylesheets().add("css/dyslexique.css");
        }

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
        deplacementBDD = deplacementBDD + 1;
        if (dyslexique.isDisable()) {
            instructionJeu.setVisible(true);
            help.setVisible(false);
        } else {
            instructionJeu.setVisible(false);
            help.setVisible(true);
        }
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
        } else {
            System.out.println("Déplacement impossible");
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
        if (b) {
            etatsPrecedents = new LinkedList<Jeu>();
            etatsPrecedents = jeuAppli.enregistrement();
        }
        
        if (etatsPrecedents.size() >= 1 && !retourUtilise) {
            retour.setDisable(false);
        } else {
            retour.setDisable(true);
        }

    }


    public void victoireAppli() {
        Stage fenetre_aide = new Stage();
        fenetre_aide.setTitle("VICTOIRE");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");

        Label victoire = new Label("Félicitations, vous avez gagné!");
        Label scoreAff = new Label("Votre score : " + jeuAppli.getScoreFinal());
        victoire.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(victoire);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 100, 100);
        if (classique.isDisable()) {
            scene.getStylesheets().add("css/classique.css");
        } else if (daltonien.isDisable()) {
            scene.getStylesheets().add("css/daltonien.css");
        } else if (dyslexique.isDisable()) {
            scene.getStylesheets().add("css/dyslexique.css");
        }

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
        if (classique.isDisable()) {
            scene.getStylesheets().add("css/classique.css");
        } else if (daltonien.isDisable()) {
            scene.getStylesheets().add("css/daltonien.css");
        } else if (dyslexique.isDisable()) {
            scene.getStylesheets().add("css/dyslexique.css");
        }

        fenetre_aide.setScene(scene);
        fenetre_aide.show();
    }

    // Enregistrement lors du clic sur le bouton BDD
    @FXML
    private void enregistrementBDD(MouseEvent event) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        // Stockage du temps
        long chrono2 = java.lang.System.currentTimeMillis();
        long temps = chrono2 - chronos;
        chrono.setText("" + (temps / 1000));
        //Enregistrement du pseudo
        // create a tile pane
        TilePane r = new TilePane();
        // create a label to show the input in text dialog
        Label l = new Label("Rentrez votre pseudo");
        // create a text input dialog
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Veuillez rentrer votre pseudonyme");
        // create a button
        Button d = new Button("click");
        // show the text input dialog
        td.showAndWait();
        // set the text of the label
        l.setText(td.getEditor().getText());
        String pseudo = l.getText();

        // Stockage du score du joueur
        int scoreJoueurFin = Integer.valueOf(score.getText());

        // Création d'une condition si le pseudo existe dèja
        // Récupérer les données de score, temps (existe pas encors) et déplacement (existe pas encore)
        // BDD infos
        String host = "localhost";
        String port = "3306";
        String dbname = "2048_game_Estebe";
        String username = "root";
        String password = "root";

        // BDD Connexion
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        // BDD requête ajout Joueur(int,string,int,int,int) Joueur(id,pseudo,score,temps,deplacement)
        String query = "INSERT INTO Joueur VALUES ('" + 0 + "','" + pseudo + "','" + scoreJoueurFin + "','" + temps + "','" + deplacementBDD + "')";
        c.insertTuples(query);

    }

    @FXML
    private void consultationBDD(MouseEvent event) {
        // Connection à la base de donnée
        String host = "localhost";
        String port = "3306";
        String dbname = "2048_game_Estebe";
        String username = "root";
        String password = "root";
        ConnexionBDD c = new ConnexionBDD(host, port, dbname, username, password);
        String infos;

        // Requête pour la base de donnée
        String queryScore = "SELECT pseudo, score, temps, nombreDéplacement FROM Joueur ORDER BY score ASC";
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
            ArrayList<Personne> listePersonne = new ArrayList<Personne>();
            listePersonne.add(p);
            // On affiche
            txtBDD.setText("" + listePersonne.get(0).getPseudo() + listePersonne.get(0).getScore());

        }
    }

    @FXML
    private void mouvOrdiApp(MouseEvent event){
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

    @FXML
    private void stopchronometre(MouseEvent event) {
        long chrono2 = java.lang.System.currentTimeMillis();
        long temps = chrono2 - chronos;
        chrono.setText("" + (temps / 1000));
    }

}
