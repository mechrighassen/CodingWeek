package Vue;


import Gestionnaire.GestionnaireNote;
import Structure.FilteredDeck;
import Structure.NoteType;
import Structure.Tag;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

import javafx.scene.control.TextField;




public class VueFilteredDeckCreate extends Parent {

    @FXML
    private GridPane grid;

    @FXML
    private TextField deckName;

    @FXML
    private TextField tagList;

    private Stage stage;
    private GestionnaireNote gn;

    public VueFilteredDeckCreate(URL a, Stage stage, GestionnaireNote gn){
        this.stage=stage;
        this.gn=gn;
        FXMLLoader fxmlLoader = new FXMLLoader(a);
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }

    public void appuiButton() {
        int comp = 0;
        int ind = 0;
        FilteredDeck newDeck = new FilteredDeck(deckName.getText(), new ArrayList<>());

        Scanner scanner = new Scanner(tagList.getText());
        scanner.useDelimiter(";");
        while (scanner.hasNext()) {
            String tag = scanner.next();
            Tag t = gn.getTags().get(tag);
            newDeck.addTag(t);
        }
            gn.addDeck(newDeck);
            stage.close();




        }
    }

    /*@FXML
    public void initialize() throws IOException{
        grid.setVisible(false);

    }*/


