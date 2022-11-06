package application;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMain.java to edit this template
 */
import java.io.IOException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author sylvainestebe
 */
public class NewFXMain extends Application {

    @FXML
    private MenuItem daltolien;
    private Stage stage = new Stage();

    @Override
    public void start(Stage stage) throws IOException {
        //Parent root = FXMLLoader.load(getClass().getResource("/application/daltonien.fxml"));
        //  Parent root = FXMLLoader.load(getClass().getResource("/application/dyslexique.fxml"));
        Parent root = FXMLLoader.load(getClass().getResource("/application/FXML.fxml"));
        Scene scene = new Scene(root);
        stage.setResizable(false);
        stage.setTitle("Jeu 2048 3D");
        boolean add = scene.getStylesheets().add("css/style.css");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);

    }

}
