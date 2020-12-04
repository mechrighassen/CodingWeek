import Gestionnaire.GestionnaireNote;
import Vue.VueInterfacePrincipale;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class MainApplication extends Application {



    @Override
    public void start(Stage primaryStage) throws Exception{
        GestionnaireNote gestNotes = Main.gestNote;

        FXMLLoader loader = new FXMLLoader(getClass().getResource("interfacePrincipale.fxml"));

        URL crea = getClass().getResource("CreationMode.fxml");
        URL appr = getClass().getResource("apprentissage.fxml");
        loader.setControllerFactory(e-> new VueInterfacePrincipale(gestNotes, crea, appr));
        Parent root = loader.load();

        primaryStage.setTitle("The Flashcards APP");
        primaryStage.setScene(new Scene(root, 860, 570));
        primaryStage.setMinHeight(570);
        primaryStage.setMinWidth(860);
        primaryStage.setMaxWidth(860);
        primaryStage.setMaxHeight(570);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}