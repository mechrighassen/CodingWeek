import Gestionnaire.GestionnaireNote;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static GestionnaireNote gestNote;

    public static void main(String[] args) {
        Connection conn;
        try {
            conn = DataBase.DataBase.getConnection("db.db");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return;
        }

        try {
            gestNote = new GestionnaireNote(conn);
        }
        catch (SQLException e) {

            System.out.println(e);
        }

        MainApplication.main(args);

        try {
            conn.close();
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error ");
            alert.setHeaderText(null);
            alert.setContentText(e.getMessage());

            alert.showAndWait();
        }
    }
}
