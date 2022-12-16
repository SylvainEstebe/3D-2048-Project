package application;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.ObjectInputStream;
import static java.lang.Math.abs;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import java.util.Optional;
import java.util.Timer;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Menu;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import modele.Case;
import modele.Jeu;
import modele.MainConsole;
import modele.Personne;
import static modele.Personne.recupPersonne;
import multijoueur.client.Client;
import multijoueur.serveur.Serveur;
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
import static variables.Parametres.TAILLE;

/**
 * Contrôleur de l'interface, gère tous les évènements qui se déroulent sur
 * l'interface
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
    private GridPane grilleH;
    @FXML
    private GridPane grilleM;
    @FXML
    private Pane instructionJeu;
    private Pane tuile;
    private Pane case8;
    @FXML
    private Pane case2, case32;
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
    /**
     * Ensemble des panes qui correspondent aux cases
     */
    private ArrayList<Pane> eltsGrilles = null;

    /**
     * Le jeu relié à l'interface
     */
    private Jeu jeuAppli = null;
    /**
     * Le nombre de retours possibles sur l'interface
     */
    private int nbRetour = 0;
    /**
     * Booléen qui indique si tous les retours ont été utilisés
     */
    private boolean retourUtilise = false;
    /**
     * Grilles de l'interface
     */
    private ArrayList<GridPane> tabGrillesApp;

    @FXML
    private Label score;
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
    private ImageView descendre;
    @FXML
    private ImageView monter;
    int deplacementBDD;
    @FXML
    private Label chrono;
    long chronos;
    long temps;

    private Timer timer;

    @FXML
    /**
     * Fond de la grille
     */
    private Pane fond;
    /**
     * Coordonnées qui servent à déterminer les coordonnées des cases/panes et à
     * générer leurs déplacements
     */
    private final int minYCase = 15, minXCaseGH = 14, longCase = 100, minXCaseGM = 353, minXCaseGB = 692;
    private int xCase = 0, yCase = 0, compteurTask = 0, nbTask = 0;

    @FXML
    private Pane fondGrille;
    /**
     * Ensemble des threads qui déplacent dynamniquement les grilles
     */
    private ArrayList<Thread> threadDepl = null;
    /**
     * Ensemble des tasks qui permettent de déplacer dynamiquement les cases
     */
    private ArrayList<Task> deplacementCases = new ArrayList<Task>();

    @FXML
    private ImageView gauche;
    @FXML
    private MenuItem multijoueur;

    /**
     * Statut multijoueur de l'interface
     */
    private boolean estMulti = false;

    /**
     * Statut serveur de l'interface
     */
    private boolean estServeur = false;

    /**
     * Serveur
     */
    private Serveur serveur;

    /**
     * Client
     */
    private Client client;

    /**
     * Adresse du serveur multijoueur
     */
    private String adresse;

    /**
     * Port du serveur multijoueur
     */
    private int port;

    /**
     * Champ de saisi du port pour créer le serveur
     */
    private TextField portServeur;

    /**
     * Erreur formulaire serveur
     */
    private Label erreurServeur;

    /**
     * Champ de saisi de l'adresse pour se connecter au serveur
     */
    private TextField adresseClient;

    /**
     * Champ de saisi du port pour se connecter au serveur
     */
    private TextField portClient;

    /**
     * Erreur formulaire client
     */
    private Label erreurClient;

    /**
     * Racine de la fenêtre multijoueur
     */
    private BorderPane multiRoot;

    /**
     * Conteneur des formulaires serveur et client
     */
    private GridPane formulaireConteneur;

    /**
     * Bouton de mode de jeu coop
     */
    private ToggleButton coopButton;

    /**
     * Bouton de mode de jeu compétitif
     */
    private ToggleButton versusButton;
    @FXML
    private ImageView gauche1;

    /**
     * Permet d'initialiser le contrôleur
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // verification de l'existence d'une partie précedente 
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
        i2.setDisable(true);
        i3.setDisable(true);
        ia1.setDisable(true);
    }

    /**
     * Permet de commencer une nouvelle partie
     *
     * @param event qui correspond à l'event sur le bouton de la nouvelle partie
     */
    @FXML
    public void nouvellePartie(ActionEvent event) {
        estMulti = false;
        estServeur = false;

        deplacementBDD = 0;
        chronos = java.lang.System.currentTimeMillis();
        jeuAppli = new Jeu();
        jeuAppli.lancementJeuAppli();
        sauvegardePartie.setDisable(false);
        this.majGrillesApp();

        mouvOrdi.setDisable(false);
        nbRetour = 0;
        retourUtilise = false;
        i2.setDisable(false);
        i3.setDisable(false);
        ia1.setDisable(false);
    
    }

    /**
     * Permet de charger une partie déjà existante et sauvegardée
     */
    @FXML
    private void chargerPartie(ActionEvent event) {
        estMulti = false;
        estServeur = false;

        chargePartie.setDisable(true);
        jeuAppli.deserialiser();
        this.majGrillesApp();
        this.sauvegardePartie.setDisable(false);
        mouvOrdi.setDisable(false);
        mouvOrdi.setDisable(false);
        nbRetour = 0;
        retourUtilise = false;
        tuile.setVisible(false);
        case8.setVisible(false);
        case2.setVisible(false);
        case32.setVisible(false);
    }

    /**
     * Permet de sauvegarder une partie en cours
     */
    @FXML
    private void sauvegarderPartie(ActionEvent event) {
        //Lorsqu'on sauvegarde, le bouton de chargement devient actif
        jeuAppli.serialiser();
        chargePartie.setDisable(false);
    }

    /**
     * Permet de revenir à un état précédent du jeu
     */
    @FXML
    private void retour(MouseEvent event) {
        if (!jeuAppli.getEtatsPrecedents().isEmpty()) {
            retourUtilise = true;
            jeuAppli.retour();
            this.majGrillesApp();
            this.majScoreApp();
            nbRetour++;
            if (nbRetour == 5 || jeuAppli.getEtatsPrecedents().isEmpty()) {
                retour.setDisable(true);
            }
        } else {
            retour.setDisable(true);
        }
    }

    /**
     * Méthode qui permet de mettre à jour l'état du jeu sur l'interface
     */
    private void majGrillesApp() {

        tabGrillesApp = new ArrayList<GridPane>();
        tabGrillesApp.add(grilleH);
        tabGrillesApp.add(grilleM);
        tabGrillesApp.add(grilleB);
        eltsGrilles = null;
        eltsGrilles = new ArrayList<Pane>();
        fondGrille.getChildren().clear();
        //Boucle pour chaque grille
        for (int k = 0; k < TAILLE; k++) {
            for (int i = 0; i < TAILLE; i++) {
                for (int j = 0; j < TAILLE; j++) {
                    Case caseModele = jeuAppli.getGrilles().get(k).getGrille().get(i).get(j);
                    //On n'affiche la case que si elle est non nulle
                    if (caseModele.getValeur() != 0) {
                        Label caseJeu = new Label("" + caseModele.getValeur());
                        caseJeu.getStyleClass().add("caseJeu");
                        Pane caseJeuCouleur = new Pane();
                        caseJeuCouleur.getChildren().add(caseJeu);
                        //On change la couleur de la case
                        caseJeuCouleur.getStyleClass().add("couleurCase");
                        eltsGrilles.add(caseJeuCouleur);
                        this.couleursCases(caseJeuCouleur, caseModele.getValeur());
                        //On met la case au bon endroit
                        this.positionPane(caseJeuCouleur, caseModele, caseJeu, k);
                        fondGrille.getChildren().add(caseJeuCouleur);
                    }
                }
            }
        }
    }

    /**
     * Méthode qui permet de placer un pane/case au bon endroit dans la grille
     *
     * @param caseJeuCouleur le pane dont on veut modifier la position
     * @param caseModele la case reliée au pane
     * @param caseJeu le label de la case
     * @param k permet de placer le pane dans la bonne grille
     */
    public void positionPane(Pane caseJeuCouleur, Case caseModele, Label caseJeu, int k) {
        if (caseModele.getGrille().getType() == GRILLEH) {
            xCase = minXCaseGH + caseModele.getY() * longCase;
        } else if (caseModele.getGrille().getType() == GRILLEM) {
            xCase = minXCaseGM + caseModele.getY() * longCase;
        } else {
            xCase = minXCaseGB + caseModele.getY() * longCase;
        }
        yCase = minYCase + caseModele.getX() * longCase;
        caseJeuCouleur.setPrefSize(100, 100);
        caseJeuCouleur.setLayoutX(xCase);
        caseJeuCouleur.setLayoutY(yCase);
        caseJeu.setVisible(true);
        caseJeuCouleur.setVisible(true);

        caseJeuCouleur.setOpacity(1);
        caseJeu.layoutXProperty().bind(caseJeuCouleur.widthProperty().subtract(caseJeu.widthProperty()).divide(2));
        tabGrillesApp.get(k).setHalignment(caseJeu, HPos.CENTER);
    }

    /**
     * Permet de changer les couleurs des cases en fonction du type de jeu qu'on
     * choisit
     *
     * @param caseJeuCouleur le pane dont on veut changer la couleur
     * @param valeur la valeur de la case qui détermine sa couleur
     */
    public void couleursCases(Pane caseJeuCouleur, int valeur) {
        if (classique.isDisable()) {
            //Gestion des couleurs des cases
            switch (valeur) {
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
            switch (valeur) {
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
            switch (valeur) {
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

    }

    @FXML
    /**
     * Permet de faire l'affichage daltonien
     */
    private void affichageDaltonien(ActionEvent event) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/daltonien.css");
        this.dyslexique.setDisable(false);
        this.daltonien.setDisable(true);
        this.classique.setDisable(false);
        help.setVisible(true);
        help.setDisable(false);
        instructionJeu.setVisible(false);
        if(eltsGrilles!=null){
            majGrillesApp();
        }
        
    }

    @FXML
    /**
     * Permet de faire l'affichage classique
     */
    private void affichageClassique(ActionEvent event) {
        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/classique.css");
        this.dyslexique.setDisable(false);
        this.daltonien.setDisable(false);
        this.classique.setDisable(true);
        help.setVisible(true);
        help.setDisable(false);
        instructionJeu.setVisible(false);
        if(eltsGrilles!=null){
            majGrillesApp();
        }

    }

    @FXML
    /**
     * Permet de faire l'affichage dyslexique
     */
    private void affichageDyslexique(ActionEvent event) {

        rootPane.getStylesheets().clear();
        rootPane.getStylesheets().add("css/dyslexique.css");
        this.help.setDisable(true);
        this.dyslexique.setDisable(true);
        this.daltonien.setDisable(false);
        this.classique.setDisable(false);
        instructionJeu.setVisible(true);
        help.setVisible(false);
        if(eltsGrilles!=null){
            majGrillesApp();
        }

    }

    @FXML
    /**
     * Méthode qui affiches des aides concernant les commandes du jeu sous forme
     * d'alert
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
                + "\n Presser Q pour aller à gauche." + "\n Presser Z pour aller en haut."
                + "\n Presser S pour aller en bas.\n Presser R pour aller au niveau supérieur."
                + "\n Presser F pour aller au niveau inférieur.");
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

    @FXML
    /**
     * Méthode affiche une fenêtre du classement des joueurs
     *
     *
     */
    private void afficheStat(ActionEvent event) {
        ObservableList<Personne> listePerso = FXCollections.observableArrayList();
        listePerso = recupPersonne();
        // Création du tableau
        Stage stat = new Stage();
        TableView<Personne> table = new TableView<Personne>();
        final ObservableList<Personne> data = listePerso;
        Scene scene = new Scene(new Group(), 500, 500);
        stat.setMinHeight(200);
        stat.setMinWidth(500);

        stat.setTitle("Statistique de ");
        stat.setMinHeight(500);
        stat.setMinWidth(300);
        final Label classement = new Label("Classement");
        classement.setFont(new Font("Arial", 20));
        table.setEditable(true);
        scene.getStylesheets().add("css/classique.css");
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
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10, 0, 0, 0));
        vbox.getChildren().addAll(classement, table);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stat.setWidth(table.getWidth());
        stat.setHeight(table.getHeight());
        stat.setScene(scene);
        stat.show();
    }

    @FXML
    /**
     * Méthode qui permet de quitter le jeu correctement
     */
    private void quitter(ActionEvent event) {

        Stage abandonner = new Stage();
        abandonner.setTitle("Voulez-vous vraiment quitter le jeu ? ");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");
        Label message;
        if (jeuAppli.finJeu()) {
            message = new Label("\n  Est-ce-que vous êtes sûr de vouloir quitter le jeu  ?");
        } else {
            message = new Label("\t\t\t\t la partie n'est pas finie!"
                    + "\n  Est-ce-que vous êtes sûr de vouloir quitter le jeu  ?");
        }
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

    /**
     * Méthode qui met à jour le score
     */
    public void majScoreApp() {
        score.setText("" + jeuAppli.getScoreFinal());
        score.setVisible(true);
    }

    @FXML
    /**
     * Méthode qui permet de réaliser un déplacement en fonction des ordres du
     * joueur
     */
    private void mouvJoueur(KeyEvent event) {
        if (threadDepl == null || threadDepl.size() == 0) {
            deplacementBDD = deplacementBDD + 1;
            if (dyslexique.isDisable()) {
                instructionJeu.setVisible(true);
                help.setVisible(false);
            } else {
                instructionJeu.setVisible(false);
                help.setVisible(true);
            }
            String directionInput = event.getText();

            int direction;
            direction = switch (directionInput) {
                case "f" ->
                    DESCG;
                case "r" ->
                    MONTERG;
                case "d" ->
                    DROITE;
                case "q" ->
                    GAUCHE;
                case "z" ->
                    HAUT;
                case "s" ->
                    BAS;
                default ->
                    0;
            };

            if (direction != 0) {
                jeuAppli.enregistrement();

                boolean b = jeuAppli.deplacerCases3G(direction);
                deplacementThread(direction, b, 0);

                if (b) {
                    jeuAppli.validerEnregistrement();
                } else {
                    jeuAppli.annulerEnregistrement();
                }

                //this.majScoreApp();
                if (jeuAppli.finJeu()) {
                    if (jeuAppli.getValeurMaxJeu() >= OBJECTIF) {
                        this.victoireAppli();
                    } else {
                        this.jeuPerduAppli();
                    }
                }

                if (jeuAppli.getEtatsPrecedents().size() >= 1 && !retourUtilise) {
                    retour.setDisable(false);
                } else {
                    retour.setDisable(true);
                }
            }
        }
    }

    /**
     * Affichage de la victoire dans l'application
     */
    public void victoireAppli() {
        Stage fenetre_aide = new Stage();
        fenetre_aide.setTitle("VICTOIRE");
        BorderPane root = new BorderPane();
        root.getStyleClass().add("pane");
         enregistrementBDD();
        Label victoire = new Label("Félicitations, vous avez gagné!");
        Label scoreAff = new Label("Votre score : " + jeuAppli.getScoreFinal());
        victoire.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(victoire);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 500, 200);
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

    /**
     * Affichage de la défaite dans l'application
     */
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
        final Scene scene = new Scene(root, 500, 200);
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

    /**
     * Méthhode qui permet l'enregistrement du pseudonyme, du temps de partie,
     * du score, du nombre de déplacement du Joueur.
     *
     */
    private void enregistrementBDD() {
        // Récupération du temps de jeux et conversion en seconde 
        long chrono2 = java.lang.System.currentTimeMillis();
        long timeSpentPlaying = (chrono2 - chronos) / 1000;

        // Récupératon du pseudonyme avec l'obligation de rentrer au moins une lettre ou un chiffre
        TextInputDialog td = new TextInputDialog();
        td.setTitle("Pseudonyme");
        td.setHeaderText("Veuillez rentrer un pseudonyme");

        Optional<String> resultat = td.showAndWait();
        if (resultat.isPresent()) {
            while (td.getEditor().getText().isEmpty() && resultat.isPresent()) {

                Alert alert = new Alert(AlertType.WARNING);
                alert.setHeaderText("Veuillez mettre au minimum une lettre/chiffre pour le pseudonyme");
                alert.showAndWait();
                td.setTitle("Pseudonyme");
                td.setHeaderText("Veuillez rentrer un pseudonyme");
                resultat = td.showAndWait();
            }
            if (resultat.isPresent()) {
                String pseudo = td.getEditor().getText();
                // Récupération du score de la partie actuelle du joueur
                int scoreJoueurFin = Integer.valueOf(score.getText());
                // Le compteur de déplacement est situé dans la méthode mouvJoueur
                // Confirmation de l'enregistrement
                Personne.ajoutPersonne(0, pseudo, scoreJoueurFin, timeSpentPlaying, deplacementBDD);
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setHeaderText("Vous êtes bien enregistrer, votre score est de " + scoreJoueurFin);
                alert.showAndWait();
            }
        }
    }

    @FXML
    /**
     * Méthode qui réalise le mouvement aléatoire de l'ordi dans l'application
     */
    private void mouvOrdiApp(MouseEvent event) {
        if (jeuAppli != null) {
            boolean b2 = jeuAppli.mouvementAlea();
            int direction = jeuAppli.getDirectionMouvAleo();
            this.deplacementThread(direction, b2, 0);
            this.majScoreApp();

            if (jeuAppli.finJeu()) {
                if (jeuAppli.getValeurMaxJeu() >= OBJECTIF) {
                    this.victoireAppli();
                } else {
                    this.jeuPerduAppli();
                }
            }
        }

    }

    /**
     * Méthode qui permet le déplacement dynamique des cases dans le jeu
     *
     * @param direction la direction dans laquelle les cases doivent être
     * déplacées
     * @param b booléen qui indique s'il est possible de déplacer les cases
     * @param ia int qui ne permet pas ajouter une case quand l'algo min-max et
     * alpha-beta sont entrain de jouer
     */
    public void deplacementThread(int direction, boolean b, int ia) {
        deplacementCases = new ArrayList<Task>();
        threadDepl = new ArrayList<Thread>();
        deplacementCases.clear();
        threadDepl.clear();
        int compteur = 0;
        if (b) {
            for (int k = 0; k < TAILLE; k++) {
                for (int i = 0; i < TAILLE; i++) {
                    for (int j = 0; j < TAILLE; j++) {
                        Case caseBouge = jeuAppli.getGrilles().get(k).getGrille().get(i).get(j);
                        if (caseBouge.getValAv() != 0) {
                            int depl = caseBouge.getNbDeplac();
                            int deplObj = 0;
                            switch (direction) {
                                case HAUT ->
                                    deplObj = minYCase + caseBouge.getX() * longCase - (abs(depl)) * longCase;
                                case BAS ->
                                    deplObj = minYCase + caseBouge.getX() * longCase + depl * longCase;
                                case GAUCHE -> {
                                    switch (caseBouge.getGrille().getType()) {
                                        case GRILLEH ->
                                            deplObj = minXCaseGH + caseBouge.getY() * longCase - (abs(depl) * longCase);

                                        case GRILLEM ->
                                            deplObj = minXCaseGM + caseBouge.getY() * longCase - (abs(depl) * longCase);
                                        case GRILLEB ->
                                            deplObj = minXCaseGB + caseBouge.getY() * longCase - (abs(depl) * longCase);
                                    }
                                }
                                case DROITE -> {
                                    switch (caseBouge.getGrille().getType()) {
                                        case GRILLEH ->
                                            deplObj = minXCaseGH + caseBouge.getY() * longCase + depl * longCase;
                                        case GRILLEM ->
                                            deplObj = minXCaseGM + caseBouge.getY() * longCase + depl * longCase;
                                        case GRILLEB ->
                                            deplObj = minXCaseGB + caseBouge.getY() * longCase + depl * longCase;
                                    }
                                }
                                case DESCG -> {
                                    if (caseBouge.getGrille().getType() == GRILLEH) {
                                        switch (caseBouge.getGrilleApDepl()) {
                                            case GRILLEH ->
                                                deplObj = minXCaseGH + caseBouge.getY() * longCase;
                                            case GRILLEM ->
                                                deplObj = minXCaseGM + caseBouge.getY() * longCase;
                                            case GRILLEB ->
                                                deplObj = minXCaseGB + caseBouge.getY() * longCase;
                                        }
                                    } else if (caseBouge.getGrille().getType() == GRILLEM) {
                                        switch (caseBouge.getGrilleApDepl()) {
                                            case GRILLEM ->
                                                deplObj = minXCaseGM + caseBouge.getY() * longCase;
                                            case GRILLEB ->
                                                deplObj = minXCaseGB + caseBouge.getY() * longCase;
                                        }
                                    } else {
                                        deplObj = minXCaseGB + caseBouge.getY() * longCase;
                                    }
                                }
                                case MONTERG -> {
                                    if (caseBouge.getGrille().getType() == GRILLEH) {
                                        deplObj = minXCaseGH + caseBouge.getY() * longCase;
                                    } else if (caseBouge.getGrille().getType() == GRILLEM) {
                                        switch (caseBouge.getGrilleApDepl()) {
                                            case GRILLEH ->
                                                deplObj = minXCaseGH + caseBouge.getY() * longCase;
                                            case GRILLEM ->
                                                deplObj = minXCaseGM + caseBouge.getY() * longCase;
                                            case GRILLEB ->
                                                deplObj = minXCaseGB + caseBouge.getY() * longCase;

                                        }
                                    } else {
                                        switch (caseBouge.getGrilleApDepl()) {
                                            case GRILLEH ->
                                                deplObj = minXCaseGH + caseBouge.getY() * longCase;
                                            case GRILLEM ->
                                                deplObj = minXCaseGM + caseBouge.getY() * longCase;
                                            case GRILLEB ->
                                                deplObj = minXCaseGB + caseBouge.getY() * longCase;

                                        }
                                    }
                                }
                                default -> {
                                }
                            }
                            xCase = (int) eltsGrilles.get(compteur).getLayoutX();
                            yCase = (int) eltsGrilles.get(compteur).getLayoutY();
                            Pane caseABouge = eltsGrilles.get(compteur);

                            DeplacementTask d = new DeplacementTask(xCase, yCase, deplObj, caseABouge, direction, this);
                            deplacementCases.add(d);
                            compteur++;
                        }
                    }
                }
            }
            CountDownLatch startSignal = new CountDownLatch(1);
            CountDownLatch doneSignal = new CountDownLatch(deplacementCases.size());
            for (int i = 0; i < deplacementCases.size(); i++) {
                DeplacementTask d = (DeplacementTask) deplacementCases.get(i);
                d.setDebut(startSignal);
                d.setFin(doneSignal);
                Thread th = new Thread(deplacementCases.get(i)); // on crée un contrôleur de Thread
                threadDepl.add(th);
                th.setDaemon(true); // le Thread s'exécutera en arrière-plan (démon informatique)
                th.start();

            }
            // IA1 et IA3 l'ajout d'une case ne se fait pas aléatoirement
            if ((ia != 3) || (ia != 1)) {
                jeuAppli.choixNbCasesAjout(b);
            }
            startSignal.countDown();
        }
    }

    /**
     * Ouvre le popup multijoueur
     *
     * @param event Clic sur le bouton
     */
    @FXML
    private void multi(ActionEvent event) {
        Stage fenetreMulti = new Stage();
        fenetreMulti.setTitle("Multijoueur");
        this.multiRoot = new BorderPane();
        this.multiRoot.getStyleClass().add("pane");

        //Titre
        Label titreMulti = new Label("Jouer en multijoueur");
        titreMulti.getStyleClass().add("text_horsjeu");
        this.multiRoot.setTop(titreMulti);

        // Formulaire création serveur
        GridPane formulaireServeur = new GridPane();
        formulaireServeur.getStyleClass().add("form-pane");
        formulaireServeur.getStyleClass().add("border-bottom");
        formulaireServeur.setAlignment(Pos.CENTER);
        formulaireServeur.setHgap(10);
        formulaireServeur.setVgap(10);

        Label titreServeur = new Label("Serveur");
        titreServeur.getStyleClass().add("form-titre");
        formulaireServeur.add(titreServeur, 0, 0, 2, 1);

        Label portServeurLabel = new Label("Port");
        formulaireServeur.add(portServeurLabel, 0, 1);

        this.portServeur = new TextField();
        formulaireServeur.add(this.portServeur, 1, 1);

        Button creerServeur = new Button("Créer le serveur");
        HBox hbCreerServeur = new HBox(10);
        hbCreerServeur.setAlignment(Pos.BOTTOM_RIGHT);
        hbCreerServeur.getChildren().add(creerServeur);
        formulaireServeur.add(hbCreerServeur, 1, 2);

        this.erreurServeur = new Label();
        this.erreurServeur.getStyleClass().add("texte-erreur");
        formulaireServeur.add(this.erreurServeur, 0, 3, 2, 1);

        creerServeur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                creerServeur();
            }
        });

        // Formulaire connexion serveur
        GridPane formulaireClient = new GridPane();
        formulaireClient.getStyleClass().add("form-pane");
        formulaireClient.setAlignment(Pos.CENTER);
        formulaireClient.setHgap(10);
        formulaireClient.setVgap(10);

        Label titreClient = new Label("Client");
        titreClient.getStyleClass().add("form-titre");
        formulaireClient.add(titreClient, 0, 0);

        Label adresseClientLabel = new Label("Adresse");
        formulaireClient.add(adresseClientLabel, 0, 1);

        this.adresseClient = new TextField();
        formulaireClient.add(this.adresseClient, 1, 1);

        Label portClientLabel = new Label("Port");
        formulaireClient.add(portClientLabel, 0, 2);

        this.portClient = new TextField();
        formulaireClient.add(this.portClient, 1, 2);

        Button connecterServeur = new Button("Se connecter au serveur");
        HBox hbConnecterServeur = new HBox(10);
        hbConnecterServeur.setAlignment(Pos.BOTTOM_RIGHT);
        hbConnecterServeur.getChildren().add(connecterServeur);
        formulaireClient.add(hbConnecterServeur, 1, 3);

        this.erreurClient = new Label();
        this.erreurClient.getStyleClass().add("texte-erreur");
        formulaireClient.add(this.erreurClient, 0, 4, 2, 1);

        connecterServeur.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                connecterServeur();
            }
        });

        // Conteneur des formulaires
        this.formulaireConteneur = new GridPane();
        this.formulaireConteneur.setAlignment(Pos.CENTER);
        this.formulaireConteneur.add(formulaireServeur, 0, 0);
        this.formulaireConteneur.add(formulaireClient, 0, 1);

        this.multiRoot.setCenter(this.formulaireConteneur);

        final Scene scene = new Scene(this.multiRoot, 400, 400);

        if (classique.isDisable()) {
            scene.getStylesheets().add("css/classique.css");
        } else if (daltonien.isDisable()) {
            scene.getStylesheets().add("css/daltonien.css");
        }
        fenetreMulti.setScene(scene);
        fenetreMulti.setResizable(false);
        fenetreMulti.show();
    }

    /**
     * Création du serveur
     */
    private void creerServeur() {
        // Vérification de la saisie
        this.port = 0;
        String p = this.portServeur.getText();
        if (p == null || p.equals("")) {
            this.erreurServeur.setText("Veuillez saisir un numéro de port");
        } else {
            try {
                this.port = Integer.parseInt(p);
                this.erreurServeur.setText("");
            } catch (NumberFormatException e) {
                this.erreurServeur.setText("Veuillez saisir un numéro de port");
            }
        }

        if (this.port != 0) {
            this.estMulti = true;
            this.estServeur = true;

            // Lancement du serveur
            this.serveur = new Serveur(this.port, 4);
            this.serveur.setDebug(true);
            new Thread(this.serveur).start();

            // Attente d'une seconde pour que le serveur ait le temps de se lancer
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(MainConsole.class.getName()).log(Level.SEVERE, null, ex);
            }

            this.adresse = this.serveur.getHost();
            this.port = this.serveur.getPort();

            // Lancement du client
            this.client = new Client(this.adresse, this.port, this.estServeur, this);
            this.client.setDebug(true);
            this.jeuAppli = new Jeu(this.client, this.estMulti);
            this.client.setJeu(this.jeuAppli);
            new Thread(this.client).start();
        }
    }

    /**
     * Connexion au serveur
     */
    private void connecterServeur() {
        // Vérification de la saisie
        this.port = 0;
        String a = this.adresseClient.getText();
        String p = this.portClient.getText();
        if ((a == null || a.equals("")) && (p == null || p.equals(""))) {
            this.erreurClient.setText("Veuillez saisir une adresse et un numéro de port");
        } else if (a == null || a.equals("")) {
            this.erreurClient.setText("Veuillez saisir une adresse");
        } else if (p == null || p.equals("")) {
            this.erreurClient.setText("Veuillez saisir un numéro de port");
        } else {
            try {
                this.port = Integer.parseInt(p);
                this.adresse = a;
                this.erreurClient.setText("");
            } catch (NumberFormatException e) {
                this.erreurClient.setText("Veuillez saisir un numéro de port");
            }
        }

        if (this.port != 0 && this.adresse != null) {
            this.estMulti = true;
            this.estServeur = false;

            // Lancement du client
            this.client = new Client(this.adresse, this.port, this.estServeur, this);
            this.client.setDebug(true);
            this.jeuAppli = new Jeu(this.client, this.estMulti);
            this.client.setJeu(this.jeuAppli);
            new Thread(this.client).start();
        }
    }

    /**
     * Affichage d'un message d'erreur en cas de problème de connexion au
     * serveur
     *
     * @param message Message à afficher
     */
    public void connexionClientErreur(String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                if (erreurClient != null) {
                    erreurClient.setText(message);
                }
            }
        });
    }

    /**
     * Affichage du salon d'attente
     *
     * @param estS Afficher la version serveur ou client du salon d'attente
     */
    public void salonAttente(boolean estS) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                multiRoot.getChildren().remove(formulaireConteneur);

                VBox attente = new VBox(10);
                attente.setPadding(new Insets(10));
                attente.setAlignment(Pos.TOP_CENTER);

                GridPane formulaireAttente = new GridPane();
                formulaireAttente.setVgap(10);
                formulaireAttente.setHgap(10);

                int row = 0;

                if (estS) {
                    Label connecterA = new Label("Adresse : " + adresse + "\nPort : " + port);
                    formulaireAttente.add(connecterA, 0, row, 4, 1);
                    row++;
                }

                Label modeJeuLabel = new Label("Mode de jeu");
                formulaireAttente.add(modeJeuLabel, 0, row);
                ToggleGroup selectionModeJeu = new ToggleGroup();
                coopButton = new ToggleButton("Coopératif");
                coopButton.setDisable(!estS);
                versusButton = new ToggleButton("Compétitif");
                versusButton.setDisable(!estS);
                coopButton.setToggleGroup(selectionModeJeu);
                versusButton.setToggleGroup(selectionModeJeu);

                if (estS) {
                    coopButton.setSelected(true);
                    coopButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            client.getConnexion().envoyerCompetitif(false);
                        }
                    });

                    versusButton.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent t) {
                            client.getConnexion().envoyerCompetitif(true);
                        }
                    });
                }

                formulaireAttente.add(coopButton, 1, row);
                formulaireAttente.add(versusButton, 2, row);
                row++;

                Label pseudoLabel = new Label("Pseudo");
                formulaireAttente.add(pseudoLabel, 0, row);
                TextField pseudo = new TextField();
                formulaireAttente.add(pseudo, 1, row, 2, 1);
                Button enregistrerPseudo = new Button("Enregistrer");
                formulaireAttente.add(enregistrerPseudo, 3, row);
                row++;

                Label erreurPseudo = new Label();
                erreurPseudo.getStyleClass().add("texte-erreur");
                formulaireAttente.add(erreurPseudo, 0, row, 4, 1);
                row++;

                enregistrerPseudo.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent t) {
                        String p = pseudo.getText();
                        if (p == null || p.equals("")) {
                            erreurPseudo.setText("Veuillez saisir votre pseudo");
                        } else {
                            erreurPseudo.setText("");
                            boolean resultat = client.getConnexion().enregistrerJoueur(p);
                            if (resultat) {
                                pseudo.setDisable(true);
                                enregistrerPseudo.setDisable(true);
                            } else {
                                erreurPseudo.setText("Pseudo déjà pris, veuillez en saisir un autre");
                            }
                        }
                    }
                });

                attente.getChildren().add(formulaireAttente);

                // TODO : affichage de la liste des joueurs connectés, leur pseudo et s'ils sont prêts
                multiRoot.setCenter(attente);

                if (!estS) {
                    client.getConnexion().estCompetitif();
                }
            }
        });
    }

    /**
     * Actualiser l'affichage du mode de jeu pour les clients
     *
     * @param versus permet de savoir si on est en mode compétitif ou coopératif
     */
    public void actualiserModeJeu(boolean versus) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                coopButton.setSelected(!versus);
                versusButton.setSelected(versus);
            }
        });
    }

    /**
     * Récupérer le jeu en lien avec l'application
     *
     * @return le jeu
     */
    public Jeu getJeuAppli() {
        return this.jeuAppli;
    }

    /**
     * Permet de récupérer le fond des grilles
     *
     * @return un pane qui est le fond des grilles
     */
    public Pane getFondGrille() {
        return this.fondGrille;
    }

    /**
     * Récupérer les cases du jeu sous forme de pane
     *
     * @return le tebleau des cases sous forme de pane
     */
    public ArrayList<Pane> getEltsGrilles() {
        return this.eltsGrilles;
    }

    /**
     * Modifier le tableau des cases sous forme de pane
     *
     * @param l le nouveau tableau de panes
     */
    public void setEltsGrilles(ArrayList<Pane> l) {
        this.eltsGrilles = l;
    }

    /**
     * Changer le fond des grilles
     *
     * @param p le nouveau fond
     */
    public void setFonfGrille(Pane p) {
        fondGrille = p;
    }

    /**
     * Réinitialiser le fond des grilles
     */
    public void resetFondGrille() {
        fondGrille.getChildren().clear();
    }

    /**
     * Réinitialiser le tableau des cases sous forme de pane
     */
    public void resetEltsGrilles() {
        eltsGrilles.clear();
    }

    /**
     * Permet de récupérer le tableau de threads responsables du déplacement des
     * grilles
     *
     * @return le tableau de threads
     */
    public ArrayList<Thread> getDeplThread() {
        return threadDepl;
    }

    /**
     *
     * Méthode qui lorsqu'on sélectionne une des ia dans le menu permet le
     * déclenchement d'un des algorithmes d'IA avec un affichage dynamique.
     *
     */
    @FXML
    private void ia1(ActionEvent event) {
        if (eltsGrilles == null) {
            deplacementBDD = 0;
            chronos = java.lang.System.currentTimeMillis();
            jeuAppli = new Jeu();
            jeuAppli.lancementJeuAppli();
            sauvegardePartie.setDisable(true);
            this.majGrillesApp();
            nbRetour = 0;
            retourUtilise = false;
        }
        retour.setDisable(true);
        stopIA.setDisable(false);
        mouvOrdi.setDisable(true);
        this.multijoueur.setDisable(true);
        this.ia1.setDisable(true);
        this.i3.setDisable(true);
        this.i2.setDisable(true);
        this.stat.setDisable(true);
        timer = new Timer();
        IAThreadApp task = new IAThreadApp(this, 1);
        timer.schedule(task, 1000, 1500);
    }

    @FXML

    private void ia2(ActionEvent event) {
        if (eltsGrilles == null) {
            deplacementBDD = 0;
            chronos = java.lang.System.currentTimeMillis();
            jeuAppli = new Jeu();
            jeuAppli.lancementJeuAppli();
            sauvegardePartie.setDisable(true);
            this.majGrillesApp();
            nbRetour = 0;
            retourUtilise = false;
        }
        retour.setDisable(true);
        stopIA.setDisable(false);
        mouvOrdi.setDisable(true);
        this.multijoueur.setDisable(true);
        this.ia1.setDisable(true);
        this.i3.setDisable(true);
        this.i2.setDisable(true);
        this.stat.setDisable(true);
        timer = new Timer();
        timer.schedule(new IAThreadApp(this, 2), 1000, 1500);
    }

    @FXML
    private void ia3(ActionEvent event) {
        if (eltsGrilles == null) {
            deplacementBDD = 0;
            chronos = java.lang.System.currentTimeMillis();
            jeuAppli = new Jeu();
            jeuAppli.lancementJeuAppli();
            sauvegardePartie.setDisable(true);
            this.majGrillesApp();
            nbRetour = 0;
            retourUtilise = false;

        }
        retour.setDisable(true);
        stopIA.setDisable(false);
        mouvOrdi.setDisable(true);
        this.multijoueur.setDisable(true);
        this.ia1.setDisable(true);
        this.i3.setDisable(true);
        this.i2.setDisable(true);
        this.stat.setDisable(true);
        timer = new Timer();
        timer.schedule(new IAThreadApp(this, 3), 1000, 1500);
    }

    @FXML
    private void stopperIA(ActionEvent event) {
        timer.cancel();
        stopIA.setDisable(true);
        if (!retourUtilise && nbRetour != 0) {
            retour.setDisable(false);
        }
        mouvOrdi.setDisable(false);
        this.ia1.setDisable(false);
        this.i3.setDisable(false);
        this.i2.setDisable(false);
        this.stat.setDisable(false);
    }

}
