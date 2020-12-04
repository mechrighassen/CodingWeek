package Builder;

import Gestionnaire.GestionnaireNote;
import Structure.Tag;
import Structure.Note;

import java.util.Scanner;

public class TagsBuilder {

    public static String tagsToString(Note note){
        StringBuilder sb =new StringBuilder();
        for(Tag t : note.getTags()){
            sb.append(t.getName());
            sb.append(";");
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() -1);
        }
        return sb.toString();
    }


    public static void parseTags(String s, Note note, GestionnaireNote gn){
        note.clearTags();
        if(!s.equals("") ){
            Scanner scanner = new Scanner(s);
            scanner.useDelimiter(";");
            while((scanner.hasNext())){
                String tagString = scanner.next();

                boolean tagExist= false;
                for(Tag t : gn.getTags().values()){
                    if(tagString.equals(t.getName())){
                        note.addTag(t);
                        tagExist=true;
                    }
                }if(!tagExist){
                    Tag t=new Tag(tagString);
                    gn.getTags().put(tagString,t);
                    note.addTag(t);
                }
            }
        }
    }
}
