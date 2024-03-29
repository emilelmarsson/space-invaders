package entity

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

    fun collidesWith(that: Entity): Boolean {
        val thisRight: Double = this.x + this.width
        val thisBottom: Double = this.y + this.height
        val thatRight: Double = that.x + that.width
        val thatBottom: Double = that.y + that.height

        return !(this.x > thatRight || thisRight < that.x || this.y > thatBottom || thisBottom < that.y)
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

interface SpriteEntity : Entity {
    val image: BufferedImage

    override fun render(graphics: Graphics2D) {
        graphics.drawImage(image, x.toInt(), y.toInt(), width.toInt(), height.toInt(), null)
    }
}

interface AnimatedEntity : SpriteEntity {
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
    var firingDelay: Milliseconds
    fun fire(angle: Double): List<Bullet>?
}
