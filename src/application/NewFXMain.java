package application;

import java.io.IOException;
import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Classe qui démarre l'interface graphique
 * @author sylvainestebe
 */
public class NewFXMain extends Application {


    @Override
    public void start(Stage stage) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/application/FXML.fxml"));
        Scene scene = new Scene(root);
        //stage.setResizable(false);
        stage.setTitle("Jeu 2048 3D");
        boolean add = scene.getStylesheets().add("css/classique.css");
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
