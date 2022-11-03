/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package application;


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

import javafx.stage.Stage;
import modele.Jeu;
import variables.Parametres;

/**
 * FXML Controller class
 *
 * @author sylvainestebe
 */
public class FXMLController implements Initializable, Parametres {

    
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
    private MenuItem ia1;
    @FXML
    private MenuItem i2;
    @FXML
    private MenuItem i3;
    @FXML
    private MenuItem stat;
    
    
    
    
    
    @FXML
    private MenuItem newPartie;
    
    private ArrayList<ArrayList<Label>> eltsGrilles;
    
    private Jeu jeuAppli;
    
    private ArrayList<GridPane> tabGrillesApp;
    
    @FXML
    private Label score;
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        // TODO
        
    }
    @FXML
    public void nouvellePartie(ActionEvent event) {
        tabGrillesApp=new ArrayList<GridPane>();
        tabGrillesApp.add(grilleH);
        tabGrillesApp.add(grilleM);
        tabGrillesApp.add(grilleB);
        jeuAppli=new Jeu();
        jeuAppli.lancementJeuAppli(); 
        this.majGrillesApp();
        
    }
    
    private void majGrillesApp(){
        //Boucle pour chaque grille
        for (int k=0;k<TAILLE;k++){
           for (int i=0;i<TAILLE;i++){
                for (int j=0;j<TAILLE;j++){
                    
                    Label caseJeu=new Label(""+jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur());
                    caseJeu.getStyleClass().add("caseJeu");
                    Pane caseJeuCouleur=new Pane();
                    
                    
                    //Gestion des bordures des grilles
                    if(j!=2 && i!=2){
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 0px 0px 1px ");
                    }
                    else if (j==2 && i!=2){
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 0px 1px 1px ");
                    }
                    else if (j!=2 &&i==2){
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 1px 0px 1px ");
                        
                    }
                    else{
                        caseJeuCouleur.setStyle("-fx-border-width : 1px 1px 1px 1px ");
                    }
                    
                    //Gestion des couleurs des cases
                    switch (jeuAppli.getGrilles().get(k).getGrille().get(j).get(i).getValeur()){
                        case 2 :
                            caseJeuCouleur.setStyle("-fx-background-color : #FFFADF;");
                            break;
                        case 4 :
                            caseJeuCouleur.setStyle("-fx-background-color : #F3E9BE;");
                            break;
                        case 8:
                            caseJeuCouleur.setStyle("-fx-background-color : #F3C076;");
                            break;
                        case 16:
                            caseJeuCouleur.setStyle("-fx-background-color : #FD9C4C;");
                            break;
                        case 32:
                            caseJeuCouleur.setStyle("-fx-background-color : #FF7440;");
                            break;
                        case 64:
                            caseJeuCouleur.setStyle("-fx-background-color : #FF513F;");
                            break;
                        case 128:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            break;
                        case 256:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            break;
                        case 512:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            break;
                        case 1024:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
                            break;
                        case 2048:
                            caseJeuCouleur.setStyle("-fx-background-color : #FFE76C;");
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
     * Méthode qui affiches des aides concernant les commandes du jeu sous forme d'alert      *
     * 
     */
    private void aidePopUp(MouseEvent event) {
        Stage fenetre_aide=new Stage();
        fenetre_aide.setTitle("Comment jouer?");
        BorderPane root = new BorderPane(); 
        root.getStyleClass().add("pane");
        
        //Titre
        Label titre_aide=new Label("Règles du jeu");
        titre_aide.getStyleClass().add("text_horsjeu");
        root.setTop(titre_aide);
        
        //Texte explicatif
        Pane boxText_expli=new Pane();
        boxText_expli.getStyleClass().add("boxStyle1");
        boxText_expli.setPrefWidth(400);
        
        Label text_expli=new Label(" Presser D pour aller à droite"
                + "\n Presser G pour aller à gauche."+"\n Presser H pour aller en haut."
                + "\n Presser B pour aller en bas.\n Presser E pour aller au niveau supérieur."
                + "\n Presser Q pour aller au niveau inférieur.");
        text_expli.getStyleClass().add("text_horsjeu2");
        boxText_expli.getChildren().add(text_expli);
        root.setLeft(boxText_expli);
        root.setMargin(boxText_expli, new Insets(10,10,10,10));
        
        final Scene scene = new Scene(root, 500, 200); 
        boolean add = scene.getStylesheets().add("css/style.css");
        fenetre_aide.setScene(scene); 
        fenetre_aide.show();
        
        /**Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Bievenue dans le 2048");
        alert.setHeaderText("Quelques informations pour les commandes");
        alert.setContentText("Presser le bouton D pour allez à droite, G pour allez à gauche, B pour allez en bas, H pour allez en haut, E pour le niveau supérieur et Q pour le niveau inférieur!");
        alert.showAndWait(); **/   
    }

     /**
     * Méthode qui lorsqu'on sélectionne une des ia dans le menu permet le déclenchement de l'algo assoscié
     * Également rend visible le bouton stop IA
     * 
     */
    @FXML
    private void ia(ActionEvent event) {
        stopIA.setDisable(false);
    }

    @FXML
    /**
     * Méthode qui affiches les statistiques dans l'interface      *
     * 
     */
    private void afficheStat(ActionEvent event) {
    }

    

    @FXML
    private void chargerPartie(ActionEvent event) {
    }

    @FXML
    private void sauvegarderPartie(ActionEvent event) {
        //Lorsqu'on sauvegarde, le bouton de chargement devient actif
        chargePartie.setDisable(false);
    }

    @FXML
    private void undo(MouseEvent event) {
    }

    public void majScoreApp(){
        score.setText(""+jeuAppli.getScoreFinal());
        score.setVisible(true);
    }


    @FXML
    private void mouvJoueur(KeyEvent event) {
        String direction = event.getText();
        boolean b=false;
        //Déplacement des cases selon la touche clavier
        if(direction.equals("g")){
            b=jeuAppli.deplacerCases3G(GAUCHE);
        }
        else if(direction.equals("d")){
            b=jeuAppli.deplacerCases3G(DROITE);  
        }
        else if(direction.equals("h")){
            b=jeuAppli.deplacerCases3G(HAUT); 
        }
        else if(direction.equals("b")){
            b=jeuAppli.deplacerCases3G(BAS);  
        }
        else if(direction.equals("q")){
            b=jeuAppli.deplacerCases3G(DESCG); 
        }
        else if(direction.equals("e")){
            b=jeuAppli.deplacerCases3G(MONTERG);
        }
        
        jeuAppli.choixNbCasesAjout(b);
        this.majScoreApp();
        this.majGrillesApp();
        if(jeuAppli.finJeu()){
            if (jeuAppli.getValeurMaxJeu() >= OBJECTIF) {
                this.victoireAppli();
            } 
            else {
                this.jeuPerduAppli();
            }
        }
        
    }

    public void victoireAppli(){
        Stage fenetre_aide=new Stage();
        fenetre_aide.setTitle("VICTOIRE");
        BorderPane root = new BorderPane(); 
        root.getStyleClass().add("pane");
        
        //Titre
        Label victoire=new Label("Félicitations, vous avez gagné!");
        Label scoreAff=new Label("Votre score : "+jeuAppli.getScoreFinal());
        victoire.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(victoire);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 100, 100); 
        boolean add = scene.getStylesheets().add("css/style.css");
        fenetre_aide.setScene(scene); 
        fenetre_aide.show();
    }
    
    public void jeuPerduAppli(){
        Stage fenetre_aide=new Stage();
        fenetre_aide.setTitle("DEFAITE");
        BorderPane root = new BorderPane(); 
        root.getStyleClass().add("pane");
        
        //Titre
        Label defaite=new Label("Mince, vous avez perdu!");
        Label scoreAff=new Label("Votre score : "+jeuAppli.getScoreFinal());
        defaite.getStyleClass().add("text_horsjeu");
        scoreAff.getStyleClass().add("text_horsjeu");
        root.setCenter(defaite);
        root.setTop(scoreAff);
        final Scene scene = new Scene(root, 100, 100); 
        boolean add = scene.getStylesheets().add("css/style.css");
        fenetre_aide.setScene(scene); 
        fenetre_aide.show();
    }
    
  
    
}


