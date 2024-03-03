package graphics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object SpriteSheet {
    private const val SPRITE_WIDTH: Int = 16
    private val SHEET: BufferedImage = ImageIO.read(File("src/main/resources/spritesheet.png"))

    operator fun get(x: Int, y: Int): BufferedImage {
        return SHEET.getSubimage(x * SPRITE_WIDTH, y * SPRITE_WIDTH, SPRITE_WIDTH, SPRITE_WIDTH)
    }
}