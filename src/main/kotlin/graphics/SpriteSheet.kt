package graphics

import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO

object SpriteSheet {
    private const val SPRITE_WIDTH: Int = 16
    private val SHEET: BufferedImage = ImageIO.read(File("src/main/resources/spritesheet.png"))

    operator fun get(x: Byte, y: Byte, xWidth: Byte = 1, yWidth: Byte = 1): BufferedImage {
        assert(x in 0..15)
        assert(y in 0..15)
        assert(xWidth in 0..15)
        assert(yWidth in 0..15)
        return SHEET.getSubimage(x * SPRITE_WIDTH, y * SPRITE_WIDTH, xWidth * SPRITE_WIDTH, yWidth * SPRITE_WIDTH)
    }
}