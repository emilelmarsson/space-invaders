package entity

import animation.Animation
import animation.Animations
import game.Board
import game.Epoch
import game.Milliseconds
import java.awt.Graphics2D

data class Player(
    override var x: Double,
    override var y: Double,
    override val width: Double,
    override val height: Double,
    override var speed: Double,
    override var dx: Double = 0.0,
    override var dy: Double = 0.0,
    override val maxHealthPoints: Int,
    override var healthPoints: Int = maxHealthPoints,
    override val animation: Animation,
    override var lastFired: Epoch = 0,
    override var firingDelay: Milliseconds,
) : LivingEntity, FiringEntity, AnimatedEntity {

    //private val rocketFire: RocketFire = this.RocketFire()
    private val particles: MutableList<Particle> = mutableListOf()
    private val bullets: MutableList<Bullet> = mutableListOf()
    private val activePowerUps: MutableSet<TimedPowerUp> = mutableSetOf()

    fun isMovingLeft(): Boolean = dx < 0
    fun isMovingRight(): Boolean = dx > 0
    fun isMovingUp(): Boolean = dy < 0
    fun isMovingDown(): Boolean = dy > 0
    fun isMoving(): Boolean = dx != 0.0 || dy != 0.0

    fun isPowered(): Boolean {
        return activePowerUps.isNotEmpty()
    }

    fun hasRapidFire(): Boolean {
        return activePowerUps.isNotEmpty() && activePowerUps.any { it is RapidFire }
    }

    fun consume(powerUp: PowerUp) {
        if (powerUp is TimedPowerUp && powerUp !in activePowerUps && !powerUp.isExhausted()) {
            activePowerUps += powerUp
        }

        if (!powerUp.isExhausted()) {
            powerUp.applyTo(this)
        }
    }

    override fun fire(angle: Double): List<Bullet>? {
        val now: Epoch = System.currentTimeMillis()
        if (now - lastFired <= firingDelay) {
            return null
        }
        TODO()
    }

    override fun update(elapsedTime: Milliseconds) {
        // Update animation
        animation.update(elapsedTime)

        // Update position
        x += dx * elapsedTime
        y += dy * elapsedTime
        boundsCheck()

        // Update particles
        val particleIterator: MutableIterator<Particle> = particles.iterator()
        while (particleIterator.hasNext()) {
            val particle: Particle = particleIterator.next()
            if (with (Board) { particle.isOutOfBounds() }) {
                particleIterator.remove()
            } else {
                particle.update(elapsedTime)
            }
        }

        // Update bullets
        val bulletIterator: MutableIterator<Bullet> = bullets.iterator()
        while (bulletIterator.hasNext()) {
            val bullet: Bullet = bulletIterator.next()
            if (with (Board) { bullet.isOutOfBounds() }) {
                bulletIterator.remove()
            } else {
                bullet.update(elapsedTime)
                TODO("Bullet - enemy hit detection")
            }
        }

        // Remove inactive power-ups
        val powerUpIterator: MutableIterator<TimedPowerUp> = activePowerUps.iterator()
        while (particleIterator.hasNext()) {
            val powerUp: TimedPowerUp = powerUpIterator.next()
            if (!powerUp.isActive()) {
                powerUpIterator.remove()
                TODO("Reverse power-up effect")
            }
        }

        // Update rocket fire
        // rocketFire.update(elapsedTime)
    }

    private fun boundsCheck() {
        if (x < 0) {
            x = 0.0
        }
        if (y < 0) {
            y = 0.0
        }
        if (x + width > Board.WIDTH) {
            x = Board.WIDTH - width
        }
        if (y + height > Board.HEIGHT) {
            y = Board.HEIGHT - height
        }
    }

    override fun render(graphics: Graphics2D) {
        super.render(graphics)
    }

    override fun deathAction() {

    }

    /*private inner class RocketFire (
        override var speed: Double = 0.0,
        override var dx: Double = 0.0,
        override var dy: Double = 0.0,
        override val width: Double = TODO(),
        override val height: Double = TODO(),
        override val animation: Animation = Animations.ROCKET_ANIMATION,
    ) : AnimatedEntity {

        override var x: Double
            get() {
                TODO()
            }
            set(_) {}
        override var y: Double
            get() {
                TODO()
            }
            set(_) {}

        override fun render(graphics: Graphics2D) {
            if (this@Player.isMoving()) {
                TODO("Render rocket fire animation")
            }
        }

        override fun update(elapsedTime: Milliseconds) {
            if (this@Player.isMoving()) {
                super.update(elapsedTime)
            }
        }
    }*/
}