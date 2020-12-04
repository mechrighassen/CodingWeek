package Structure;

import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import javafx.scene.control.Alert;


import java.util.*;
import java.util.concurrent.TimeUnit;

public class Note {
    private Integer idNote;
    private NoteType noteType;
    private HashMap<Integer, FieldNote> fields  ;
    private String name;
    private double knowledgeRate = 0;
    private long lastDay;
    private String nameDeck;
    private Deck deck;
    private ObservableMap<String,Tag> tags;
    public boolean isReversed = false;

    public Note(Integer id,NoteType notetype, String name){
        fields=new HashMap<>();
        this.idNote=id;
        this.noteType=notetype;
        this.name=name;
        tags= FXCollections.observableHashMap();
    }

    public Note(Integer id, NoteType notetype) {
        this(id, notetype, id.toString());
    }

    public String getNameDeck() {
        return nameDeck;
    }

    public void setNameDeck(String nameDeck) {
        this.nameDeck=nameDeck;
    }

    public Integer getIdNote() {
        return idNote;
    }

    public NoteType getNoteType() {
        return noteType;
    }

    public void setDate(long date){
        lastDay=date;
    }

    public long getDate() {
        return lastDay;
    }

    public void addNote(FieldNote f) {
        fields.put(f.getIdField(),f);
    }

    public FieldNote getField(int i) {
        return fields.get(i);
    }
    public Collection<FieldNote> getFields(){
        return fields.values();
    }

    public void addField(FieldNote f) {
        //CHECK IF IT'S A VALID FIELD
        fields.put(f.getIdField(), f);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name=name;
    }

    public double getEffectiveKnowledgeRate() {
        double k = knowledgeRate;
        k-=this.diffSinceLastDay()*50/(k+1);

        if (k < 0) k = 0;
        if (k > 100) k = 100;
        return k;
    }

    public double getKnowledgeRate(){
        return knowledgeRate;
    }

    public void setKnowledgeRate(double k) {
        knowledgeRate = k;
        if (knowledgeRate < 0) knowledgeRate = 0;
        if (knowledgeRate > 100) knowledgeRate = 100;
    }

    public void updateKnowledgeRate(double coef) {
        double newK = getEffectiveKnowledgeRate();
        newK += coef;
        setKnowledgeRate(newK);
        updateDate();
    }

    private void updateDate() {
        lastDay = new java.util.Date().getTime();
    }

    public long diffSinceLastDay() {
        long diff = Math.abs((new java.util.Date().getTime()) - lastDay);
        return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
    }

    private Note copy() {
        Note note = new Note(this.getIdNote(), this.getNoteType(), this.getName());
        for (FieldNote f : this.getFields()) {
            FieldNote f2 = f.copy();
            note.addField(f2);
        }
        return note;
    }

    public Note getReversed() {
        Note note = this.copy();
        note.setReversed(true);
        return note;
    }

    private void setReversed(boolean c) {
        isReversed=c;
    }

    public void addTag(Tag tag) {
        tags.put(tag.getName(),tag);
        tag.addNote(this);
    }

    public Collection<Tag> getTags(){
        return tags.values();
    }

    public void removeTag(Tag tag){
        tags.remove(tag.getName());
    }

    public long getLastDay() {
        return lastDay;
    }

    public void clearTags(){
        for (Tag t : tags.values()) {
            t.removeNote(this);
        }
        tags.clear();
    }
    public Deck getDeck(){
        return deck;
    }
}
