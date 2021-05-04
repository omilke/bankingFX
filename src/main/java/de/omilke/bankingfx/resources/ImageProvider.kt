package de.omilke.bankingfx.resources

import javafx.scene.image.Image

/**
 * Created by olli on 27.11.2015.
 */
object ImageProvider {

    fun readImageFromMetaInf(type: ImageType): Image {

        return Image(ImageProvider::class.java.classLoader.getResourceAsStream("META-INF/img/" + type.value))
    }
}