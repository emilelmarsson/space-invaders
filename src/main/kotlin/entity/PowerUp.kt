package entity

import game.Milliseconds
import graphics.Images
import java.awt.image.BufferedImage

sealed class PowerUp : RenderedEntity {
    override val width: Double = 50.0
    override val height: Double = 50.0
    override var speed: Double = 0.0
    override var dx: Double = 0.0
    override var dy: Double = 0.0

    protected var consumed: Boolean = false
        set(value) {
            if (field) {
                return
            }
            field = value
        }
    protected abstract val powerUp: (Player) -> Unit

    private val spawnTime: Milliseconds = System.currentTimeMillis()
    protected open val lifeTime: Milliseconds = 6000

    open fun applyTo(player: Player) {
        if (isExhausted()) {
            return
        } else {
            consumed = true
            powerUp.invoke(player)
        }
    }

    protected fun isExhausted(): Boolean {
        return consumed || (System.currentTimeMillis() - spawnTime >= lifeTime)
    }

    override fun update(elapsedTime: Milliseconds) = Unit
}

data class HealthPack(
    override var x: Double,
    override var y: Double
) : PowerUp() {

    override val image: BufferedImage = Images.HEALTH_PACK

    override val powerUp: (Player) -> Unit = { player: Player ->
        val maxHealth: Int = player.maxHealthPoints
        if (player.healthPoints + 1 <= maxHealth) {
            player.healthPoints++
        }
    }
}

sealed class TimedPowerUp : PowerUp() {
    private var startTime: Milliseconds = 0
    abstract val length: Milliseconds

    override fun applyTo(player: Player) {
        if (isExhausted()) {
            return
        } else {
            consumed = true
            powerUp.invoke(player)
            startTime = System.currentTimeMillis()
        }
    }

    fun isActive(): Boolean {
        return isExhausted() && (System.currentTimeMillis() - startTime < length)
    }
}

data class RapidFire(
    override var x: Double,
    override var y: Double,
) : TimedPowerUp() {

    override val image: BufferedImage = Images.RAPID_FIRE
    override val length: Milliseconds = TODO()

    override val powerUp: (Player) -> Unit = { player: Player ->
        TODO("Lower firing delay")
    }
}