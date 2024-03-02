package entity

import game.Milliseconds
import java.awt.image.BufferedImage

sealed class PowerUp : RenderedEntity {
    override val width: Double = 50.0
    override val height: Double = 50.0
    override var speed: Double = 0.0
    override var dx: Double = 0.0
    override var dy: Double = 0.0

    abstract fun applyTo(player: Player)
}

sealed class TimedPowerUp : PowerUp() {
    protected var startTime: Milliseconds = 0
    abstract val length: Milliseconds
    abstract fun isActive(): Boolean
}

data class RapidFire(
    override var x: Double,
    override var y: Double,
    override val image: BufferedImage,
    override val length: Milliseconds
) : TimedPowerUp() {

    override fun isActive(): Boolean {
        TODO("Not yet implemented")
    }

    override fun applyTo(player: Player) {
        startTime = System.currentTimeMillis()
        TODO("Not yet implemented")
    }
}