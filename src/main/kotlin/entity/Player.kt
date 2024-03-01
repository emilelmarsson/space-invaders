package entity

import Bullet
import action.LimitedAction
import action.RepeatedAction
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
    override var healthPoints: Int,
    override val animation: Animation,
    override var lastFired: Epoch = 0,
    override val firingDelay: Milliseconds,
) : LivingEntity, FiringEntity, AnimatedEntity {

    private val rocketFire: RocketFire = this.RocketFire()
    private val particles: MutableList<Particle> = mutableListOf()
    private val bullets: MutableList<Bullet> = mutableListOf()

    private val fireAction = LimitedAction(
        delay = 150,
        limitedAction = {
            TODO("")
        }
    )

    private val repeatedActions: List<RepeatedAction> = listOf(
        RepeatedAction(
            delay = 50,
            repeatedAction = {
                if (isMoving()) {
                    TODO("Spawn particles in moving direction")
                } else {
                    TODO("Spawn particles in random direction")
                }
            }
        )
    )

    fun isMovingLeft(): Boolean = dx < 0
    fun isMovingRight(): Boolean = dx > 0
    fun isMovingUp(): Boolean = dy < 0
    fun isMovingDown(): Boolean = dy > 0
    fun isMoving(): Boolean = dx != 0.0 || dy != 0.0

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

        // Update particles
        val iterator: MutableIterator<Particle> = particles.iterator()
        while (iterator.hasNext()) {
            val particle: Particle = iterator.next()
            if (with (Board) { particle.isOutOfBounds() }) {
                iterator.remove()
            } else {
                particle.update(elapsedTime)
            }
        }

        // Update rocket fire
        rocketFire.update(elapsedTime)

        // Perform actions
        for (repeatedAction: RepeatedAction in repeatedActions) {
            repeatedAction.perform()
        }
    }

    override fun render(graphics: Graphics2D) {
        super.render(graphics)
        TODO()
    }

    override fun deathAction() {

    }

    private inner class RocketFire (
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
    }
}