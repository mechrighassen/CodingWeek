package Vue;

import Builder.FieldNotFoundException;
import Gestionnaire.GestionnaireNote;
import Structure.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class VueCreationNoteType extends Parent {

    @FXML
    private TextArea recto;

    @FXML
    private TextArea verso;

    @FXML
    private TextField champ;

    @FXML
    private TextField noteNameText;

    @FXML
    private ListView<String> fieldList;

    @FXML
    private ListView<String> fieldTypeList;

    @FXML
    private CheckBox reverseNote;




    private Stage stage;

    private ObservableList<String> typeList;
    private ObservableList<String> nameList;
    private GestionnaireNote gn;
    private NoteType currentNoteType;
    private ObservableList<String> FieldNoteTypeList;

    public VueCreationNoteType( Stage stage, GestionnaireNote gn){
        typeList=FXCollections.observableArrayList();
        nameList=FXCollections.observableArrayList();
        FieldNoteTypeList= FXCollections.observableArrayList();
        currentNoteType = new NoteType(null,null,null,null);
        this.stage=stage;
        this.gn=gn;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("CreationNoteType.fxml"));
        fxmlLoader.setController(this);
        try {
            this.getChildren().add(fxmlLoader.load());
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }


    }



    public void getFieldsRectoVerso(){

        if ((recto.getText()!="")&&(verso.getText()!="")&&(champ.getText()!="")&&(noteNameText.getText()!="")&&fieldList.getItems()!=null) {
            String rectoText = recto.getText().replaceAll("'", "\\'");
            String versoText = verso.getText().replaceAll("'", "\\'");
            int noteTypeId=gn.getFreeID("notetype");
            currentNoteType = new NoteType(noteTypeId, rectoText, versoText, noteNameText.getText());
            for (int i = 0 ; i < typeList.size() ; i++) {
                int idField =gn.getFreeID("field");
                if (typeList.get(i).equals("String")) {
                    FieldNoteType newType = new FieldNoteTypeString(nameList.get(i), idField, noteTypeId);
                    gn.getFieldsId().add(idField);
                    currentNoteType.addField(newType);
                }else if(typeList.get(i).equals("image")){
                    FieldNoteType newType = new FieldNoteTypeImage(nameList.get(i), idField, noteTypeId);
                    gn.getFieldsId().add(idField);
                    currentNoteType.addField(newType);
                }else if(typeList.get(i).equals("Latex")){
                    FieldNoteType newType =new FieldNoteTypeLatex(nameList.get(i),idField , noteTypeId);
                    gn.getFieldsId().add(idField);
                    currentNoteType.addField(newType);
                }else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error ");
                    alert.setHeaderText(null);
                    alert.setContentText("Error");
                }
            }
            try {
                Builder.FrontBackTraducteur.tradStringToId(currentNoteType);
                currentNoteType.setReserved(reverseNote.isSelected());
                gn.getObsNotesType().put(noteTypeId, currentNoteType);
                gn.insertUpdateNoteType(currentNoteType);
            }
            catch (FieldNotFoundException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error ");
                alert.setHeaderText(null);
                alert.setContentText(e.getMessage());
                System.out.println();
            }
            catch (SQLException e2) {
                System.out.println(e2);
            }
        }

        stage.close();

    }

    public void getName(){
        String name;
        String type;
        if (fieldTypeList.getSelectionModel().getSelectedItem()!=null) {
            name = champ.getText();
            type = fieldTypeList.getSelectionModel().getSelectedItem();
            if (!nameList.contains(name)) {
                nameList.add(name);
                typeList.add(type);
            }
            fieldList.setItems(nameList);
        }

    }

    @FXML
    public void deleteField(){
        if (fieldList.getSelectionModel().getSelectedItem()!=null) {
            String name = fieldList.getSelectionModel().getSelectedItem();
            typeList.remove(nameList.indexOf(name));
            nameList.remove(name);
            fieldList.setItems(nameList);
        }
    }

    @FXML
    public void  initialize(){
        this.FieldNoteTypeList.add("String");
        this.FieldNoteTypeList.add("image");
        fieldTypeList.setItems(FieldNoteTypeList);
    }





}
