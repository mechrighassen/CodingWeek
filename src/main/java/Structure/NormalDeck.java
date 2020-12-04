package Structure;

import java.util.ArrayList;
import java.util.Iterator;

public class NormalDeck extends Deck {
    private String name;
    private ArrayList<Note> notes;
    private Iterator<Note> toStudy;

    public NormalDeck(String name){
        this.name=name;
        notes=new ArrayList<>();
    }
    public NormalDeck(){
        this("NormalDeck");
        notes=new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }
    public ArrayList<Note> getNoteList(){
        return notes;
    }

    public void addNote(Note note){
        notes.add(note);
    }

    @Override
    public Note getNextNote() {
        if (toStudy != null && toStudy.hasNext()) {
            return toStudy.next();
        }
        else {
            return null;
        }
    }

    @Override
    public void beginStudy() {
        ArrayList<Note> notes = (ArrayList<Note>)this.notes.clone();
        notes.sort((a,b) -> a.getEffectiveKnowledgeRate() <= b.getEffectiveKnowledgeRate() ? -1 : 1);
        for (Note n : this.notes) {
            if (n.getNoteType().getIsReversed()) {
                notes.add(n.getReversed());
            }
        }
        toStudy=notes.iterator();
    }


    @Override
    public boolean isStudied() {
        return toStudy == null || !toStudy.hasNext();
    }

    @Override
    public boolean studiedToday() {
        for (Note n : notes) {
            if (n.diffSinceLastDay() > 0) {
                return false;
            }
        }
        return true;
    }
}
