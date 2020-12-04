package Builder;

import Structure.FieldNoteType;
import Structure.NoteType;


import java.util.Scanner;

public class FrontBackTraducteur {



    public static void tradIdToString(NoteType note) throws FieldNotFoundException {
        note.setFront(tradIdToString(note, note.getFront()));
        note.setBack(tradIdToString(note, note.getBack()));
    }

    private static String tradIdToString(NoteType note, String text) throws FieldNotFoundException {
        StringBuilder newText = new StringBuilder();

        Scanner scanner = new Scanner(text);
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String ligne = scanner.next();
            if (ligne.startsWith("field:")) {
                String debutLigne = ligne.substring(6);
                String name;
                try {
                    int id = Integer.parseInt(debutLigne);
                    FieldNoteType fieldNote = note.getField(id);
                    if (fieldNote == null) throw new FieldNotFoundException(debutLigne);
                    else {
                        name = fieldNote.getName();
                    }
                }
                catch (NumberFormatException e) {
                    throw new FieldNotFoundException(debutLigne);
                }
                newText.append("field:");
                newText.append(name);
            }
            else {
                newText.append(ligne);
            }
            newText.append('\n');
        }
        newText.deleteCharAt(newText.lastIndexOf("\n"));
        return newText.toString();
    }

    public static void tradStringToId(NoteType note) throws FieldNotFoundException {
        note.setFront(tradStringToId(note, note.getFront()));
        note.setBack(tradStringToId(note, note.getBack()));
    }

    //TODO
    //Penser à faire en sorte que, pour un NoteType donné, le nom de champ doit être unique!
    //Ie, UNIQUE (IDNoteType, name)
    private static String tradStringToId(NoteType note, String text) throws FieldNotFoundException {
        StringBuilder newText = new StringBuilder();

        Scanner scanner = new Scanner(text);
        scanner.useDelimiter("\n");
        while (scanner.hasNext()) {
            String ligne = scanner.next();
            if (ligne.startsWith("field:")) {
                String debutLigne = ligne.substring(6);
                int id = -1;
                for (FieldNoteType f : note.getFields()) {
                    if (f.getName().equals(debutLigne)) {
                        id = f.getIdField();
                        break;
                    }
                }
                if (id == -1) {
                    throw new FieldNotFoundException(debutLigne);
                }
                newText.append("field:");
                newText.append(id);
            }
            else {
                newText.append(ligne);
            }
            newText.append('\n');
        }
        newText.deleteCharAt(newText.lastIndexOf("\n"));
        return newText.toString();
    }
}
