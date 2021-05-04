package de.omilke.bankingfx.resources;

import javafx.scene.image.Image;

/**
 * Created by olli on 27.11.2015.
 */
public class ImageProvider {

    public static Image readImageFromMetaInf(ImageType type) {

        return new Image(ImageProvider.class.getClassLoader().getResourceAsStream("META-INF/img/" + type.getValue()));
    }
}
