package Vue;

import javafx.scene.image.ImageView;

public class VueFieldImage extends VueField {

    public VueFieldImage(String imagePath) {
        ImageView image = new ImageView("file:" + imagePath);
        image.setFitWidth(200);
        image.setFitHeight(75);
        this.getChildren().add(image);
    }
}
