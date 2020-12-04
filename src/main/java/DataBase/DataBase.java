package DataBase;


import javafx.scene.control.Alert;

import java.io.File;
import java.sql.*;

public class DataBase {
    public static Connection getConnection(String filename) throws SQLException {
        Connection res;
        //File f =new File(System.getProperty("user.dir")+"/src/main/database/"+filename);
        File f = new File(filename);
        if (f.exists()){
            res=connect(filename);
        }else{
            createDatabase(filename);
            res=connect(filename);
            addDefaultTables(res);
            addDefaultNoteType(res);
            addDefaultDeck(res);
        }
        return res;
    }

    private static void createDatabase(String fileName) throws SQLException{

        String url = "jdbc:sqlite:" + fileName;

        Connection conn = DriverManager.getConnection(url);
        if (conn != null) {
            DatabaseMetaData meta = conn.getMetaData();
            conn.close();
        }

    }

    private static Connection connect(String fileName) throws SQLException{
        return DriverManager.getConnection("jdbc:sqlite:" + fileName);
    }

    private static void addDefaultTables(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();
        stmt.execute("CREATE TABLE NoteType\n" +
                "(\n" +
                "  IDNoteType INT NOT NULL,\n" +
                "  Front VARCHAR(100) NOT NULL,\n" +
                "  Back VARCHAR(100) NOT NULL,\n" +
                "  Name VARCHAR(20),\n"+
                "  Reverse BOOLEAN,\n" +
                "  PRIMARY KEY (IDNoteType)\n" +
                ");\n");
        stmt.execute("CREATE TABLE Field\n" +
                "(\n" +
                "  Name VARCHAR(20) NOT NULL,\n" +
                "  Type VARCHAR(20) NOT NULL,\n" +
                "  IDField INT NOT NULL,\n" +
                "  IDNoteType INT NOT NULL,\n" +
                "  PRIMARY KEY (IDField),\n" +
                "  FOREIGN KEY (IDNoteType) REFERENCES NoteType(IDNoteType)\n" +
                ");");
        stmt.execute("CREATE TABLE Deck\n" +
                "(\n" +
                "  Name VARCHAR(20) NOT NULL,\n" +
                "  PRIMARY KEY (Name)\n" +
                ");\n");
        stmt.execute("CREATE TABLE Note\n" +
                "(\n" +
                "  IDNote INT NOT NULL,\n" +
                "  IDNoteType INT NOT NULL,\n" +
                "  Name VARCHAR(20),\n"+
                "  NameDeck VARCHAR(20) NOT NULL,\n"+
                "  Knowledgerate DOUBLE DEFAULT 0,\n"+
                "  LastDay BIGINT, \n"+
                "  PRIMARY KEY (IDNote),\n" +
                "  FOREIGN KEY (IDNoteType) REFERENCES NoteType(IDNoteType),\n" +
                "  FOREIGN KEY (NameDeck) REFERENCES Deck(Name)\n"+
                ");\n");
        stmt.execute("CREATE TABLE FieldString\n" +
                "(\n" +
                "  Content VARCHAR(150) NOT NULL,\n" +
                "  IDField INT NOT NULL,\n" +
                "  IDNote INT NOT NULL,\n" +
                "  PRIMARY KEY (IDField, IDNote),\n" +
                "  FOREIGN KEY (IDField) REFERENCES Field(IDField),\n" +
                "  FOREIGN KEY (IDNote) REFERENCES Note(IDNote)"+
                ");");
        stmt.execute("CREATE TABLE FieldLatex\n" +
                "(\n" +
                "  Content VARCHAR(150) NOT NULL,\n" +
                "  IDField INT NOT NULL,\n" +
                "  IDNote INT NOT NULL,\n" +
                "  PRIMARY KEY (IDField, IDNote),\n" +
                "  FOREIGN KEY (IDField) REFERENCES Field(IDField),\n" +
                "  FOREIGN KEY (IDNote) REFERENCES Note(IDNote)"+
                ");");
        stmt.execute("CREATE TABLE FieldImage\n" +
                "(\n" +
                "  Image VARCHAR(10) NOT NULL,\n" +
                "  IDField INT NOT NULL,\n" +
                "  IDNote INT NOT NULL,\n" +
                "  PRIMARY KEY (IDField, IDNote),\n" +
                "  FOREIGN KEY (IDField) REFERENCES Field(IDField),\n" +
                "  FOREIGN KEY (IDNote) REFERENCES Note(IDNote)\n" +
                ");");
        stmt.execute("CREATE TABLE Tag\n"+
                "("+
                "  Name VARCHAR(20) NOT NULL,\n"+
                "  PRIMARY KEY (Name)\n"+
                ");"
        );
        stmt.execute("CREATE TABLE TagCorrespondance\n" +
                "(\n" +
                "  Name VARCHAR(20) NOT NULL,\n" +
                "  IDNote INT, \n" +
                "  PRIMARY KEY (Name,IDNote),\n" +
                "  FOREIGN KEY (IDNote) REFERENCES Note(IDNote),\n"+
                "  FOREIGN KEY (Name) REFERENCES Tag(Name)\n"+
                ");"
                );
    }

    private static void addDefaultNoteType(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        String typeBasique = "INSERT INTO NoteType(IDNoteType,Front,Back,Name,Reverse)"+
                " VALUES (1, \"Recto :\nfield:1\", \"Verso : \nfield:2\", \"Basique\",\"false\");";
        stmt.execute(typeBasique);

        typeBasique = "INSERT INTO Field(Name, Type, IDField, IDNoteType)" +
                " VALUES (\"Recto\", \"String\", 1, 1);";
        stmt.execute(typeBasique);

        typeBasique = "INSERT INTO Field(Name, Type, IDField, IDNoteType)" +
                " VALUES (\"Verso\", \"String\", 2, 1);";
        stmt.execute(typeBasique);

        String typeImage = "INSERT INTO NoteType(IDNoteType,Front,Back,Name, Reverse)"+
                " VALUES (2, \"Recto :\nfield:3\", \"Verso : \nfield:4\", \"Images\", \"false\");";
        stmt.execute(typeImage);

        typeImage = "INSERT INTO Field(Name, Type, IDField, IDNoteType)" +
                " VALUES (\"Recto\", \"image\", 3, 2);";
        stmt.execute(typeImage);

        typeImage = "INSERT INTO Field(Name, Type, IDField, IDNoteType)" +
                " VALUES (\"Verso\", \"image\", 4, 2);";
        stmt.execute(typeImage);

    }

    private static void addDefaultDeck(Connection conn) throws SQLException {
        Statement stmt = conn.createStatement();

        String deckdef = "INSERT INTO DECK(Name)" +
                "VALUES ('Par défaut')";
        stmt.execute(deckdef);
    }

}
