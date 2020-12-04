package Structure;

import Builder.FieldNoteBuilder;
import Vue.VueField;
import Vue.VueFieldLatex;
import Vue.VueFieldString;
import javafx.scene.image.Image;

public class FieldNoteLatex implements FieldNote {
    private Integer idField;
    private Integer idNote;
    private FieldNoteBuilder builder;
    private  String content;

    public FieldNoteLatex(Integer idFIeld, Integer idNote, String cont) {
        this.idField=idFIeld;
        this.idNote = idNote;
        this.content=cont;
        builder = new FieldNoteBuilder() {
            @Override
            public VueField build() {
                return new VueFieldLatex(content);
            }
        };
    }

    public FieldNoteLatex(Integer idField, Integer idNote) {
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
        return "Latex";
    }
    public FieldNote copy() {

        return new FieldNoteLatex(this.getIdField(), this.getIdNote(), this.getContent());
    }

}
