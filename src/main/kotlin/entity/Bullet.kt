package entity;

import java.awt.image.BufferedImage

data class Bullet(
    override var x: Double,
    override var y: Double,
    override var speed: Double,
    override var dx: Double,
    override var dy: Double,
    override val width: Double,
    override val height: Double,
    override val image: BufferedImage
) : RenderedEntity