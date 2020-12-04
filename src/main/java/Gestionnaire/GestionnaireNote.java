package Gestionnaire;

import Structure.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


public class GestionnaireNote {

    private Connection connection;
    private  ObservableMap<Integer, Note> notes;
    private ObservableMap<Integer, NoteType> notesTypes;
    private ObservableMap<String, Deck> decks;
    private ArrayList<Integer> fieldsId;
    private ObservableMap<String,Tag> tags;

    public GestionnaireNote(Connection conn) throws SQLException {

        connection=conn;
        notes = FXCollections.observableHashMap();
        notesTypes = FXCollections.observableHashMap();
        decks = FXCollections.observableHashMap();
        fieldsId=new ArrayList<>();
        tags = FXCollections.observableHashMap();


        //Test de base : on crée un NoteType fictif
      //  NormalDeck nDeck = new NormalDeck("Default");
      //  decks.put(nDeck.getDeckName(),nDeck);

        NoteType noteType = new NoteType(1, "The front : \nfield:1", "The back :\nfield:2", "Basique");
        noteType.addField(new FieldNoteTypeString("Recto",1,1));
        noteType.addField(new FieldNoteTypeString("Verso",2,1));
        notesTypes.put(noteType.getIdNoteType(),noteType);



        Statement stmt=connection.createStatement();

        ResultSet rsDeck=stmt.executeQuery("Select * from Deck");
        while (rsDeck.next()){
            String nom=rsDeck.getString(1);
            decks.put(nom,new NormalDeck(nom));
        }


        //on recupere toutes les notesTypes de la base de donnees

        String sqlnotetype="Select * from NoteType";
        ResultSet rsnotetype=stmt.executeQuery(sqlnotetype);
        while (rsnotetype.next()){
            int idnotety = rsnotetype.getInt("IDNotetype");
            String front =rsnotetype.getString("Front");
            String back =rsnotetype.getString("Back");
            String nom =rsnotetype.getString("Name");
            notesTypes.put(idnotety,new NoteType(idnotety,front,back,nom));
            boolean tr = !rsnotetype.getString("Reverse").equals("false");
            notesTypes.get(idnotety).setReserved(tr);
        }



        // on recupere tous les fieldnotestypes
        sqlnotetype="Select * from Field";
        ResultSet rsfdty=stmt.executeQuery(sqlnotetype);
        while (rsfdty.next()) {
            String nom = rsfdty.getString("Name");
            String ty = rsfdty.getString("Type");
            int idfd = rsfdty.getInt("IDField");
            int idntty = rsfdty.getInt("IDNoteType");
            if (ty.equals("String")) {
                FieldNoteTypeString ftypestring = new FieldNoteTypeString(nom, idfd, idntty);
                notesTypes.get(idntty).addField(ftypestring);
                fieldsId.add(idfd);
            }
            else if (ty.equals("Latex")) {
                FieldNoteTypeLatex ftypestring = new FieldNoteTypeLatex(nom, idfd, idntty);
                notesTypes.get(idntty).addField(ftypestring);
                fieldsId.add(idfd);
            }
            else if (ty.equals("image")){
                FieldNoteTypeImage ftypesimage = new FieldNoteTypeImage(nom, idfd, idntty);
                notesTypes.get(idntty).addField(ftypesimage);
                fieldsId.add(idfd);
            }
        }



        //on recupere toutes les notes
        String sqlnotes="Select * from Note";
        ResultSet rsnotes =stmt.executeQuery(sqlnotes);
        while(rsnotes.next()) {
            int idnt = rsnotes.getInt("IDNote");
            int idtype = rsnotes.getInt("IDNoteType");
            String nom = rsnotes.getString("Name");
            String deck=rsnotes.getString("NameDeck");
            Double rate=rsnotes.getDouble("KnowledgeRate");
            Long day=rsnotes.getLong("LastDay");
            Note note=new Note(idnt, notesTypes.get(idtype),nom);
            note.setDate(day);
            note.setNameDeck(deck);
            note.setKnowledgeRate(rate);
            this.addNote(note);
        }



        // on recupere tous les fieldnotestring
        String sqlfdnotes="Select * from FieldString";
        ResultSet rsnttys=stmt.executeQuery(sqlfdnotes);
        while (rsnttys.next()) {
            String cont = rsnttys.getString("Content");
            int idfd = rsnttys.getInt("IDField");
            int idnt = rsnttys.getInt("IDNote");
            FieldNoteString notestring = new FieldNoteString(idfd, idnt,cont);
            notes.get(idnt).addField(notestring);
        }

        // on recupere tous les fieldnotelatex
        sqlfdnotes="Select * from FieldLatex";
        rsnttys=stmt.executeQuery(sqlfdnotes);
        while (rsnttys.next()) {
            String cont = rsnttys.getString("Content");
            int idfd = rsnttys.getInt("IDField");
            int idnt = rsnttys.getInt("IDNote");
            FieldNoteLatex notestring = new FieldNoteLatex(idfd, idnt,cont);
            notes.get(idnt).addField(notestring);
        }


        // on recupere tous les fieldnoteImage
        String sqlfdnotei="Select * from FieldImage";
        ResultSet rsnttyi=stmt.executeQuery(sqlfdnotei);
        while (rsnttyi.next()) {
            String cont = rsnttyi.getString("Image");
            int idfd = rsnttyi.getInt("IDField");
            int idnt = rsnttyi.getInt("IDNote");
            FieldNoteImage noteimage = new FieldNoteImage(idfd, idnt,cont);
            notes.get(idnt).addField(noteimage);
        }



        //on recupere ts les tags
        String sqltag="Select * from Tag";
        ResultSet rstag=stmt.executeQuery(sqltag);
        while (rstag.next()) {
            String nom = rstag.getString("Name");
            tags.put(nom,new Tag(nom));
        }

        //on recupere toutes les correspondances tag note
        String sqlcor="Select * from TagCorrespondance";
        ResultSet rscor=stmt.executeQuery(sqlcor);
        while (rscor.next()) {
            String nom = rscor.getString("Name");
            int id=rscor.getInt("IDNote");
            notes.get(id).addTag(tags.get(nom));
        }

    }

    public List<Note> getNotesByNoteType(Integer idNT) {
        ArrayList<Note> notes = new ArrayList<>();
        for (Note n : this.notes.values()) {
            if (n.getNoteType().getIdNoteType().equals(idNT)) {
                notes.add(n);
            }
        }
        return notes;
    }

    public List<Note> getNoteByTypesAndDeck(Deck deck,Integer idNT) {
        ArrayList<Note> notes = new ArrayList<>();
        for (Note n : deck.getNoteList()) {
            if (n.getNoteType().getIdNoteType().equals(idNT)) {
                notes.add(n);
            }
        }
        return notes;
    }

    public void addNote(Note n) {
        NormalDeck d = (NormalDeck)getDecks().get(n.getNameDeck());
        if (d.getNoteList().indexOf(n) == -1) d.addNote(n);
        this.getObsNotes().put(n.getIdNote(),n);
    }

    public Note getNote(Integer id) {
        return notes.get(id);
    }

    public NoteType getNoteType(Integer id) {
        return notesTypes.get(id);
    }

    /*public Integer getFreeNoteID() {
        Set<Integer> setn=notes.keySet();
        Integer[] keysn = setn.toArray(new Integer[0]);
        sort(keysn);
        return keysn[keysn.length]+1;
        //return notes.values().size()*5;
        //MODIF NEEDED of course
    }*/

    public Integer getFreeID(String s) {
        //??? Cela marche-t-il ??? Risqué, à vérifier
        switch (s) {
            case "note":
                Integer maxn=0;
                for (Integer x : notes.keySet()){
                    if (maxn < x){ maxn=x; }
                }
                return maxn+1;
            case "field":
                Integer maxf=0;
                for (Integer i:fieldsId){
                    if (maxf < i){ maxf=i;}
                }
                return maxf+1;
            case "notetype":
                Integer maxt=0;
                for (Integer y :notesTypes.keySet()){
                    if (maxt < y){ maxt=y;}
                }
                return maxt+1;
            default:
                return 1;
        }
    }

    public ObservableMap<Integer, Note> getObsNotes() {
        return notes;
    }

    public ObservableMap<Integer, NoteType> getObsNotesType() {
        return notesTypes;
    }

    public ObservableMap<String,Deck> getObsDeck(){ return decks; }



    public void testAddNote() {
        Note noteTest = new Note(2, notesTypes.get(1));
        noteTest.addField(new FieldNoteString(1, 1, "RectoSt222ring"));
        noteTest.addField(new FieldNoteString(2,1,"Verso2222String"));
        notes.put(2,noteTest);
    }



    public  void insertUpdateNote(Note note) throws SQLException {
        Integer id=note.getIdNote();
        Statement stmt =connection.createStatement();
        ResultSet rtest=stmt.executeQuery("Select count(*) from Note where IDNote="+id+" ");
        insertUpdateNoteType(note.getNoteType());
        if (rtest.getInt(1)==0){
            String sql0;
            sql0= "Insert INTO Note Values("+id+", "+note.getNoteType().getIdNoteType()+", '"+note.getName()+"', '"+note.getNameDeck()+"', "+note.getKnowledgeRate()+", "+note.getLastDay()+" );";
            stmt.executeUpdate(sql0);
            for (Tag tag:note.getTags()){
                insertTagCorresp(note,tag);
            }
        } else if (rtest.getInt(1)>=1){
            String sql1="Update Note Set Name= '"+note.getName()+"', Knowledgerate = "+note.getKnowledgeRate()+", LastDay = "+note.getLastDay()+" where IDNote="+id+"";
            stmt.executeUpdate(sql1);
            String sql5="Delete from TagCorrespondance where IDNote = "+note.getIdNote()+" ;";
            stmt.executeUpdate(sql5);
            for (Tag tag:note.getTags()){
                insertTagCorresp(note,tag);
            }
        }
        String sql6="Delete from FieldString where IDNote = "+note.getIdNote()+" ;";
        stmt.executeUpdate(sql6);
        /*
        String sql7="Delete from FieldImage where IDNote = "+note.getIdNote()+" ;";
        stmt.executeUpdate(sql7);*/
        for (FieldNote f : note.getFields()) {
            insertUpdateFieldN(f);
        }

        for (Tag tag:note.getTags()){
            insertTag(tag);
            insertTagCorresp(note,tag);
        }

        String sql = "DELETE FROM Tag WHERE Name NOT IN (SELECT Distinct Name FROM TagCorrespondance)";
        stmt.execute(sql);

        ArrayList<Tag> tagClone = new ArrayList<>();
        tagClone.addAll(tags.values());

        for (Tag tag : tagClone){
            if (tag.getNotes().size()==0){
                tags.remove(tag.getName());
            }
        }
    }

    public Connection getConnection(){
        return connection;
    }

    public void setConnection(Connection conn){
        connection=conn;
    }

    public void updateNoteName_Deck_rate(Note note) throws SQLException {
        Integer id=note.getIdNote();
        Statement stmt =connection.createStatement();
        ResultSet rtest=stmt.executeQuery("Select count(*) from Note where IDNote="+id+" ");
        if (rtest.getInt(1)>=1) {
            String sql1 = "Update Note Set Name= '" + note.getName() + "', Knowledgerate = " + note.getKnowledgeRate() + ", LastDay = " + note.getLastDay() + " where IDNote=" + id + "";
            stmt.executeUpdate(sql1);
        }
    }

    public ArrayList<Integer> getFieldsId() {
        return this.fieldsId;
    }

    public void removeNote(Note note) throws SQLException {
        Statement stmt =connection.createStatement();
        ResultSet rs=stmt.executeQuery("Select count(*) from Note where IDNote = "+note.getIdNote()+" ");
        if (rs.getInt(1)>0){
            removeTagFromNote(note);
            for (FieldNote f: note.getFields()) {
                removeFieldN(f);
            }
            stmt.executeUpdate("Delete from Note where IDNote = "+note.getIdNote()+"");
            decks.get(note.getNameDeck()).getNoteList().remove(note);
            notes.remove(note.getIdNote());
        }
    }

    public  void insertUpdateNoteType(NoteType notetype) throws SQLException {
        Integer id=notetype.getIdNoteType();
        Statement stmt =connection.createStatement();
        ResultSet rs=stmt.executeQuery("Select count(*) from NoteType where IDNoteType="+id+" ");
        String bool = notetype.getIsReversed() ? "true" : "false";
        if (rs.getInt(1)==0){
            String sql3= "Insert INTO NoteType Values("+id+", '"+notetype.getFront()+"', '"+notetype.getBack()+"', '"+notetype.getName()+ "', '" + bool + "')";
            stmt.executeUpdate(sql3);
        }else if (rs.getInt(1)>=1){
            String sql2="Update NoteType Set Name = '"+notetype.getName()+"', Front = '"+notetype.getFront()+"', Back = '"+notetype.getBack()+"', Reverse='" + bool + "' where IDNoteType="+id+"";
            stmt.executeUpdate(sql2);
        }
        for (FieldNoteType fnt:notetype.getFields()) {
            insertUpdateFieldNT(fnt);
        }

    }

    public void removeNoteType(NoteType notetype) throws SQLException {
        if (notetype.getIdNoteType()!=1 && notetype.getIdNoteType()!=2){
            Statement stmt =connection.createStatement();
            ResultSet rs=stmt.executeQuery("Select count(*) from NoteType where IDNoteType = "+notetype.getIdNoteType()+" ");
            if (rs.getInt(1)>0){
                for (FieldNoteType ft: notetype.getFields()) {
                    removeFieldNT(ft);
                }
                stmt.executeUpdate("Delete from NoteType where IDNoteType = "+notetype.getIdNoteType()+"");
                notesTypes.remove(notetype.getIdNoteType());
            }
        }
    }

    private void insertUpdateFieldN(FieldNote fieldnote) throws SQLException {
        Statement stmt =connection.createStatement();
        if(fieldnote.getType().equals("string")) {
            ResultSet rs1 = stmt.executeQuery("Select count(*) from FieldString where IDField = "+fieldnote.getIdField()+" AND IDNote = " + fieldnote.getIdNote() + ";");
            if(rs1.getInt(1)==0){
                String sql1="Insert INTO FieldString Values('"+((FieldNoteString)fieldnote).getContent()+"', "+fieldnote.getIdField()+", "+fieldnote.getIdNote()+")";
                stmt.executeUpdate(sql1);
            }else{
                String sql2="Update FieldString Set Content = '"+((FieldNoteString)fieldnote).getContent()+"' where IDNote = "+fieldnote.getIdNote()+" and IDField = "+fieldnote.getIdField()+"";
                stmt.executeUpdate(sql2);
            }
        }
        else if(fieldnote.getType().equals("Latex")) {
            ResultSet rs1 = stmt.executeQuery("Select count(*) from FieldLatex where IDField = "+fieldnote.getIdField()+" AND IDNote = " + fieldnote.getIdNote() + ";");
            if(rs1.getInt(1)==0){
                String sql1="Insert INTO FieldLatex Values('"+((FieldNoteLatex)fieldnote).getContent()+"', "+fieldnote.getIdField()+", "+fieldnote.getIdNote()+")";
                stmt.executeUpdate(sql1);
            }else{
                String sql2="Update FieldLatex Set Content = '"+((FieldNoteLatex)fieldnote).getContent()+"' where IDNote = "+fieldnote.getIdNote()+" and IDField = "+fieldnote.getIdField()+"";
                stmt.executeUpdate(sql2);
            }
        } else if (fieldnote.getType().equals("image")){
            ResultSet rs2 = stmt.executeQuery("Select count(*) from FieldImage where IDField = "+fieldnote.getIdField()+" AND IDNote = " + fieldnote.getIdNote() + ";");
            if(rs2.getInt(1)==0){
                String sql3="Insert INTO FieldImage Values('"+((FieldNoteImage)fieldnote).getContent()+"', "+fieldnote.getIdField()+", "+fieldnote.getIdNote()+")";
                stmt.executeUpdate(sql3);
            }else{
                String sql4="Update FieldImage Set Image = '"+((FieldNoteImage)fieldnote).getContent()+"' where IDfield = "+fieldnote.getIdField()+" and IDNote = "+fieldnote.getIdNote()+";";
                stmt.executeUpdate(sql4);
            }
        }
    }

    private void removeFieldN(FieldNote fn) throws SQLException {
        Statement stmt =connection.createStatement();
        if (fn.getType().equals("string")) {
            stmt.executeUpdate("Delete from FieldString where IDField = " + fn.getIdField() + " and IDNote = "+fn.getIdNote()+"");
        }
        else if (fn.getType().equals("Latex")) {
            stmt.executeUpdate("Delete from FieldLatex where IDField = " + fn.getIdField() + " and IDNote = "+fn.getIdNote()+"");
        }
        else if (fn.getType().equals("image")) {
            stmt.executeUpdate("Delete from FieldImage where IDField = " + fn.getIdField() + " and IDNote = "+fn.getIdNote()+"");
        }
        fieldsId.remove(fn.getIdField());
    }

    private void insertUpdateFieldNT(FieldNoteType fieldnotetype) throws SQLException {
        Statement stmt =connection.createStatement();
        ResultSet rs = stmt.executeQuery("Select count(*) from Field where IDField = "+fieldnotetype.getIdField()+"");
        if(rs.getInt(1)==0){
            String sql1="Insert INTO Field Values('"+fieldnotetype.getName()+"', '"+fieldnotetype.getType()+"', "+fieldnotetype.getIdField()+", "+fieldnotetype.getIDNoteType()+")";
            stmt.executeUpdate(sql1);
        }else{
            String sql2="Update Field Set Name = '"+fieldnotetype.getName()+"' where IDField = "+fieldnotetype.getIdField()+"";
            stmt.executeUpdate(sql2);
        }
    }

    private void removeFieldNT(FieldNoteType fnt) throws SQLException {
        Statement stmt =connection.createStatement();
        stmt.executeUpdate("Delete from Field where IDField = " + fnt.getIdField() + "");
    }

    public void insertDeck(Deck deck) throws SQLException {
        if (deck instanceof NormalDeck) {
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery("Select count(*) from Deck where name = '" + deck.getName() + "'");
            if (rs.getInt(1) == 0) {
                String sql = "Insert INTO Deck Values('" + deck.getName() + "')";
                stmt.executeUpdate(sql);
                for (Note n : deck.getNoteList()) {
                    insertUpdateNote(n);
                }
            }
        }
    }

    public void removeDeck(Deck deck) throws SQLException {
        if (!deck.getName().equals("Par défaut")) {
            Statement stmt=connection.createStatement();
            ArrayList<Note> notess= (ArrayList<Note>) deck.getNoteList().clone();
            for (Note n: notess){
                removeNote(n);
            }
            stmt.executeUpdate("Delete from Deck where Name = '"+deck.getName()+"'");
            decks.remove(deck.getName());
        }
    }

    public void removeDeck(String s) throws SQLException {
        if (!s.equals("Par défaut")) {
            Deck deck = decks.get(s);
            removeDeck(deck);
        }
    }
    private void insertTag(Tag tag) throws SQLException {
        Statement stmt=connection.createStatement();
        ResultSet rs = stmt.executeQuery("Select count(*) from Tag where Name = '" + tag.getName() + "' ");
        if(rs.getInt(1)==0){
            String sql="Insert INTO Tag Values('"+tag.getName()+"')";
            stmt.executeUpdate(sql);
        }
    }

    private void insertTagCorresp(Note note, Tag tag) throws SQLException {
        Statement stmt=connection.createStatement();
        ResultSet rs = stmt.executeQuery("Select count(*) from TagCorrespondance where Name = '" + tag.getName() + "' and IDNote = "+note.getIdNote()+" ");
        if(rs.getInt(1)==0){
            String sql="Insert INTO TagCorrespondance Values('"+tag.getName()+"', "+note.getIdNote()+")";
            stmt.executeUpdate(sql);
        }
    }

    private void removeTagCorresp(Note note, Tag tag) throws SQLException {
        Statement stmt=connection.createStatement();
        stmt.executeUpdate("Delete from TagCorrespondance where Name = '"+tag.getName()+"' and IDNote = "+note.getIdNote()+"");
    }

    public void removeTagFromNote(Note note) throws SQLException {
        Statement stmt=connection.createStatement();
        stmt.executeUpdate("Delete from TagCorrespondance where IDNote = "+note.getIdNote()+"");
    }

    private ObservableMap<String, Deck> getDecks() {
        return decks;
    }

    public void addDeck(Deck deck){
        decks.put(deck.getName(),deck);
    }

    

    public ObservableMap<String,Tag> getTags() {
        return tags;
    }
}
