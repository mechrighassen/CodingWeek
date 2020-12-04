package Structure;


import java.util.ArrayList;
import java.util.Iterator;

public class FilteredDeck extends Deck {

    private String name;
    private ArrayList<Tag> tagList;
    private ArrayList<Deck> decksList;

    private Iterator<Note> toStudy;

    public FilteredDeck(String name,ArrayList<Tag> tagList){
        this.name=name;
        this.tagList=tagList;
        decksList=new ArrayList<>();
    }

    public FilteredDeck(ArrayList<Tag> tagList){
        this("Filtered Deck",tagList);
    }

    public FilteredDeck(){
        this("Filtered Deck",null);
    }


    @Override
    public String getName() {
        return name;
    }

    //@Override
    public ArrayList<Deck> getNormalDecksList() {
        return decksList;
    }

    @Override
    public ArrayList<Note> getNoteList() {

        ArrayList<Note> notes = new ArrayList<>();
        for (Tag tag: tagList) {
            notes.addAll(tag.getNotes());
        }


        return notes;
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
    public boolean isStudied() {
        System.out.println(toStudy == null || !toStudy.hasNext());
        return toStudy == null || !toStudy.hasNext();
    }


    public void addTag(Tag tag){
        tagList.add(tag);
    }

    public ArrayList<Tag> getTagList(){
        return tagList;
    }

    @Override
    public void beginStudy() {
        toStudy = this.getNoteList().iterator();
    }

    @Override
    public boolean studiedToday() {
        for (Note n : this.getNoteList()) {
            if (n.diffSinceLastDay() > 0) {
                return false;
            }
        }
        return true;
    }
}
