package Structure;

import Builder.FieldNoteBuilder;
import Vue.VueField;
import Vue.VueFieldString;
import javafx.scene.image.Image;

public class FieldNoteString implements FieldNote {
    private Integer idField;
    private Integer idNote;
    private FieldNoteBuilder builder;
    private String content;

    public FieldNoteString(Integer idFIeld, Integer idNote, String cont) {
        this.idField=idFIeld;
        this.idNote = idNote;
        this.content=cont;
        builder = new FieldNoteBuilder() {
            @Override
            public VueField build() {
                return new VueFieldString(content);
            }
        };
    }

    public FieldNoteString(Integer idField, Integer idNote) {
        this(idField,idNote,"");
    }

    public void setContent(String s) {
        content=s;
    }

    public String getContent() {
        return content;
    }

    @Override
    public Integer getIdField() {
        return idField;
    }

    @Override
    public Integer getIdNote() {
        return idNote;
    }

    @Override
    public FieldNoteBuilder getBuilder() {
        return builder;
    }

    @Override
    public String getType() {
        return "string";
    }

    public FieldNote copy() {
        return new FieldNoteString(this.getIdField(), this.getIdNote(), this.content);
    }
}
