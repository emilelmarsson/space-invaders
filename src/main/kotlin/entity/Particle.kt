package entity

import java.awt.Color

data class Particle(
    override var x: Double,
    override var y: Double,
    override val width: Double,
    override val height: Double,
    override var speed: Double,
    override var dx: Double,
    override var dy: Double,
    override var color: Color,
) : ColoredEntity