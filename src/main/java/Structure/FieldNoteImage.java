package Structure;

import Builder.FieldNoteBuilder;
import Vue.VueField;
import Vue.VueFieldImage;
import javafx.scene.image.Image;

public class FieldNoteImage implements FieldNote {
    private Integer idField;
    private Integer idNote;
    private FieldNoteBuilder builder;
    private String image;

    public FieldNoteImage(Integer idFIeld, Integer idNote, String imagepath) {
        this.idField=idFIeld;
        this.idNote = idNote;
        this.image=imagepath;
        builder =new FieldNoteBuilder() {
            @Override
            public VueField build() {
                return new VueFieldImage(image);
            }
        };
    }

    public FieldNoteImage(Integer idField, Integer idNote) {
        this(idField,idNote,"");
    }

    public void setImage(String s) {
        image=s;
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
        return "image";
    }

    public String getContent() {
        return image;
    }

    public Image getImage(){
        String imagepath="file:"+this.getContent();
        return new Image(imagepath);
    }
    public FieldNote copy() {
        FieldNoteImage newfield = new FieldNoteImage(this.getIdField(), this.getIdNote(), this.getContent());
        return newfield;
    }
}
