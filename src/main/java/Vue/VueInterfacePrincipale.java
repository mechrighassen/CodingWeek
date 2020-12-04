package Vue;

import Gestionnaire.GestionnaireNote;
import Structure.Deck;
import Structure.Note;
import Structure.NoteType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;


public class VueInterfacePrincipale {

    private Parent rootCrea,rootAppr;

    @FXML
    private Pane modeCrea;

    @FXML
    private Pane modeAppr;

    @FXML
     private MenuItem FilteredButton;

    @FXML
    private ImageView imageDebut;


    private VueCreationMode vueCrea;



    private GestionnaireNote gestNotes;
    private URL crea;

    public VueInterfacePrincipale(GestionnaireNote gestNotes, URL crea, URL appr){
        this.gestNotes = gestNotes;
        this.crea = crea;
    }


    @FXML
    public void initialize() {
        modeCrea.setVisible(false);
        modeAppr.setVisible(false);
        imageDebut.setVisible(true);
        Image image = new Image("logo.png");
        if (image != null) imageDebut.setImage(image);
        vueCrea = new VueCreationMode(gestNotes,crea);
        modeCrea.getChildren().add(vueCrea);
        modeAppr.getChildren().add(VueApprentissage.createVue(gestNotes));
    }


    public void New() throws SQLException {
        ArrayList<Deck> decks= new ArrayList<>();
        decks.addAll(gestNotes.getObsDeck().values());
        for (Deck d:decks){
            gestNotes.removeDeck(d);
        }
        ArrayList<NoteType> notet= new ArrayList<>();
        notet.addAll(gestNotes.getObsNotesType().values());
        for (NoteType nt:notet){
            gestNotes.removeNoteType(nt);
        }


    }


    public void appButton() {
        if (modeAppr.isVisible()) {
            modeCrea.setVisible(false);
            modeAppr.setVisible(false);
            imageDebut.setVisible(true);
        }
        else {
            modeCrea.setVisible(false);
            modeAppr.setVisible(true);
            imageDebut.setVisible(false);
        }
    }

    public void creatButton() {
        vueCrea.clearSelection();
        if (modeCrea.isVisible()) {
            modeCrea.setVisible(false);
            modeAppr.setVisible(false);
            imageDebut.setVisible(true);
        }
        else {
            modeCrea.setVisible(true);
            modeAppr.setVisible(false);
            imageDebut.setVisible(false);
        }
    }

    public void creatFilteredDeckButton(){

            Stage stage=new Stage();
            Parent root = new VueFilteredDeckCreate(getClass().getClassLoader().getResource("FilteredDeck.fxml"),stage,gestNotes);
            stage.setScene(new Scene(root, 400, 400));
            stage.show();
    }

    public void close(){
        Platform.exit();
    }

    public void rewind() {
        for (Note n : gestNotes.getObsNotes().values()) {
            rewind(n);
        }
    }

    public void rewind(Note note) {
        long date = note.getDate();
        long dateInDay =TimeUnit.DAYS.convert(date, TimeUnit.MILLISECONDS);
        dateInDay-=1;
        date = TimeUnit.MILLISECONDS.convert(dateInDay, TimeUnit.DAYS);
        note.setDate(date);
        try {
            gestNotes.updateNoteName_Deck_rate(note);
        }
        catch (SQLException e) {
            System.out.println(e);
        }
    }

}
