package Vue;

import Gestionnaire.GestionnaireNote;
import Structure.NormalDeck;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;

public class VueNormalDeckCreate extends Parent {

    @FXML
    private TextField text;


    private Stage stage;
    private GestionnaireNote gn;

    public VueNormalDeckCreate(URL u, Stage stage, GestionnaireNote gn){
        this.stage=stage;
        this.gn=gn;
        FXMLLoader fxmlLoader = new FXMLLoader(u);
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void add(){
        NormalDeck deck=new NormalDeck(text.getText());
        try {
            gn.insertDeck(deck);
            gn.addDeck(deck);
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        stage.close();
    }



}
