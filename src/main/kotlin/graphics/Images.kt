package graphics

import java.awt.image.BufferedImage

object Images {
    val CURSOR: BufferedImage = SpriteSheet[1, 1]
    val CLICKED_CURSOR: BufferedImage = SpriteSheet[2, 1]

    val HEALTH_PACK_ICON: BufferedImage = SpriteSheet[0, 3]
    val POWER_ICON: BufferedImage = SpriteSheet[1, 3]
    val RAPID_FIRE_ICON: BufferedImage = SpriteSheet[2, 3]
    val HOURGLASS_ICON: BufferedImage = SpriteSheet[3, 3]
}