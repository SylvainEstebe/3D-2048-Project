package jeuConsole;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author sylvainestebe
 */
public class FXMLController implements Initializable {

    @FXML
    private MenuItem newPartie;
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
    private Label score;
    @FXML
    private MenuItem ia1;
    @FXML
    private MenuItem i2;
    @FXML
    private MenuItem i3;
    @FXML
    private MenuItem stat;
    @FXML
    private Label appuie;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        
    }

    @FXML
     /**
     * Méthode qui affiches des aides concernant les commandes du jeu sous forme d'alert      *
     * 
     */
    private void aidePopUp(MouseEvent event) {
  Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Bievenue dans le 2048");
        alert.setHeaderText("Quelques informations pour les commandes");
        alert.setContentText("Presser le bouton D pour allez à droite, G pour allez à gauche, B pour allez en bas, H pour allez en haut, E pour le niveau supérieur et Q pour le niveau inférieur!");
        alert.showAndWait();    }

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
    private void nouvellePartie(ActionEvent event) {
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

     /**
     * Prend la direction et en fonction ..*
     * 
     */
    @FXML
    private void Direction(KeyEvent event) {
        String direction = event.getText();
        appuie.setText("Tu as appuyé sur " + event.getText());
        tuile.setTranslateX(100);
        
    }

  
}
