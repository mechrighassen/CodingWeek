package Vue;

import javafx.scene.control.Label;

public class VueFieldLatex extends VueField{

    public  VueFieldLatex(String s) {
        String latex = "\\begin{array}{l}";
        latex+=s;
        latex += "\\end{array}";

        Label lc = new Label(latex);
        this.getChildren().add(lc);
    }
}