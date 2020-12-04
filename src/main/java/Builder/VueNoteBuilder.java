package Builder;

import Structure.FieldNote;
import Structure.Note;
import Vue.VueField;
import Vue.VueFieldString;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VueNoteBuilder {

    public static List<VueField> getFront(Note note) throws FieldNotFoundException {
        if (!note.isReversed)
        return getFromString(note, note.getNoteType().getFront());
        else return getFromString(note, note.getNoteType().getBack());
    }

    public static List<VueField> getBack(Note note) throws FieldNotFoundException {
        if (!note.isReversed)
        return getFromString(note, note.getNoteType().getBack());
        else return getFromString(note, note.getNoteType().getFront());
    }

    private static List<VueField> getFromString(Note note, String text) throws FieldNotFoundException {
        List<VueField> listeVueField = new ArrayList<>();

        Scanner scanner = new Scanner(text);
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String ligne = scanner.next();
            if (ligne.startsWith("field:")) {
                String debutLigne = ligne.substring(6);
                try {
                    int id = Integer.parseInt(debutLigne);
                    FieldNote fieldNote = note.getField(id);
                    if (fieldNote == null) {
                        throw new FieldNotFoundException(debutLigne);
                    }
                    else {
                        listeVueField.add(fieldNote.getBuilder().build());
                    }
                }
                catch (NumberFormatException e) {
                    throw new FieldNotFoundException(debutLigne);
                }

            }
            else {
                listeVueField.add(new VueFieldString(ligne));
            }

        }
        return listeVueField;
    }

}
