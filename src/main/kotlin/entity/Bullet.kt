package entity;

import java.awt.image.BufferedImage

data class Bullet(
    override var x: Int,
    override var y: Int,
    override var dx: Int,
    override var dy: Int,
    override val width: Int,
    override val height: Int,
    override val image: BufferedImage
) : Entity {
    override val round: Boolean = true
}