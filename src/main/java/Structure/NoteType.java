package Structure;


import java.util.Collection;
import java.util.HashMap;

public class NoteType {
    private Integer idNoteType;
    private HashMap<Integer,FieldNoteType> fields;
    private String front,back;
    private String name;
    private boolean isReversed = false;

    public NoteType(Integer idNoteType,String front,String back, String name) {
        this.idNoteType=idNoteType;
        this.fields = new HashMap<>();
        this.back=back;
        this.front=front;
        this.name=name;
    }

    public NoteType(Integer idNoteType,String front,String back) {
        this(idNoteType, front, back, idNoteType.toString());
    }

    public Integer getIdNoteType() {
        return idNoteType;
    }

    public Collection<FieldNoteType> getFields() {
        return fields.values();
    }

    public void addField(FieldNoteType f) {
        fields.put(f.getIdField(), f);
    }

    public String getName() {
        return name;
    }

    public String getFront() {
        return front;
    }

    public String getBack() {
        return back;
    }

    public void setFront(String front) {
        this.front=front;
    }

    public void setBack(String back) {
        this.back=back;
    }

    public FieldNoteType getField(int i) {
        return fields.get(i);
    }

    public boolean getIsReversed() {
        return isReversed;
    }

    public void setReserved(boolean b) { isReversed = b; }
}
