package Structure;

import Builder.FieldNoteBuilder;
import javafx.scene.image.Image;


public interface FieldNote {
    Integer getIdField();
    Integer getIdNote();
    FieldNoteBuilder getBuilder();
    String getType();
    FieldNote copy();
}
