package Structure;

import Builder.FieldNoteTypeBuilder;



public interface FieldNoteType {
    Integer getIdField();
    Integer getIDNoteType();
    String getName();
    FieldNoteTypeBuilder getBuilder();
    FieldNote createEmptyField(Integer idNote);
    String getType();
}
