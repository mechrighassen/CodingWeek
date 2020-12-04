package Structure;

import Builder.FieldNoteTypeBuilder;
import Vue.VueFieldType;
import Vue.VueFieldTypeImage;
import Vue.VueFieldTypeString;



public class FieldNoteTypeImage implements FieldNoteType {
    private Integer idField;
    private Integer idNoteType;
    private String name;
    private FieldNoteTypeBuilder builder;

    public FieldNoteTypeImage(String name, Integer idField, Integer idNoteType) {
        this.name=name;
        this.idField=idField;
        this.idNoteType=idNoteType;
        builder = new FieldNoteTypeBuilder() {
            @Override
            public VueFieldType build(FieldNote fNote) {
                return new VueFieldTypeImage(fNote, name);
            }
        };
    }

    @Override
    public Integer getIdField() {
        return idField;
    }

    @Override
    public Integer getIDNoteType() {
        return idNoteType;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public FieldNoteTypeBuilder getBuilder() {
        return builder;
    }

    @Override
    public FieldNote createEmptyField(Integer idNote) {
        FieldNoteImage fNote = new FieldNoteImage(idField, idNote);
        fNote.setImage("Empty");
        return fNote;
    }

    @Override
    public String getType() {
        return "image";
    }
}
