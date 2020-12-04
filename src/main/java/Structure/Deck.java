package Structure;


import java.util.ArrayList;

public abstract class Deck {
    public abstract String getName();
    public abstract ArrayList<Note> getNoteList();
    public abstract Note getNextNote();
    public abstract boolean isStudied();
    public abstract void beginStudy();
    public abstract boolean studiedToday();
    public double getStudyDone() {
        double x = 0;
        int i = 0;
        for (Note note : this.getNoteList()) {
            x += note.getEffectiveKnowledgeRate();
            i += 1;
        }
        if (i != 0) x = x / i;
        return x;
    }
}
