package entity

import Bullet
import animation.Animation
import game.Epoch
import game.Milliseconds
import java.awt.Color
import java.awt.Graphics2D
import java.awt.image.BufferedImage

sealed interface Entity {
    var x: Double
    var y: Double
    val width: Double
    val height: Double
    var speed: Double
    var dx: Double
    var dy: Double

    infix fun collidesWith(other: Entity): Boolean {
        TODO()
    }

    fun update(elapsedTime: Milliseconds)

    fun render(graphics: Graphics2D)
}

interface LivingEntity : Entity {
    val maxHealthPoints: Int
    var healthPoints: Int
    fun isAlive(): Boolean = healthPoints > 0
    fun isDead(): Boolean = !isAlive()
    fun deathAction() {}
}

interface ColoredEntity : Entity {
    var color: Color

    override fun render(graphics: Graphics2D) {
        graphics.color = color
        graphics.fillRect(x.toInt(), y.toInt(), width.toInt(), height.toInt())
    }
}

interface RenderedEntity : Entity {
    val image: BufferedImage

    override fun render(graphics: Graphics2D) {
        graphics.drawImage(image, x.toInt(), y.toInt(), width.toInt(), height.toInt(), null)
    }
}

interface AnimatedEntity : RenderedEntity {
    override val image: BufferedImage
        get() {
            val currentFrame: Animation.Frame = animation.currentFrame()
            return currentFrame.image
        }
    val animation: Animation

    override fun update(elapsedTime: Milliseconds) {
        animation.update(elapsedTime)
    }
}

interface FiringEntity : Entity {
    var lastFired: Epoch
    val firingDelay: Milliseconds
    fun fire(angle: Double): List<Bullet>?
}
