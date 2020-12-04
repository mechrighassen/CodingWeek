package Structure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.util.Collection;

public class Tag {


    private ObservableMap<Integer,Note> notes;
    private String name;

    public Tag(String nom){
        this.name=nom;
        notes= FXCollections.observableHashMap();
    }

    public void addNote(Note note){
        notes.put(note.getIdNote(),note);
    }

    public String getName() {
        return name;
    }

    public Collection<Note> getNotes(){
        return notes.values();
    }

    public void removeNote(Note note){
        notes.remove(note.getIdNote());
    }

}
