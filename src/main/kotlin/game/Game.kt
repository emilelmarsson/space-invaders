package game

import entity.Bullet
import entity.Enemy
import entity.Entity
import entity.LivingEntity
import entity.Particle
import entity.Player
import java.awt.Graphics2D

class Game (
    private val graphics: Graphics2D
) : Runnable {

    companion object {
        const val FPS: Int = 60
    }

    private var running = true
    private var paused = false

    private val player: Player = TODO()
    private val playerBullets: MutableList<Bullet> = mutableListOf()
    private val particles: MutableList<Particle> = mutableListOf()
    private val enemies: MutableList<Enemy> = mutableListOf()

    private fun entities(): List<Entity> {
        return enemies + playerBullets + particles + player
    }

    override fun run() {
        gameLoop { elapsedTime: Milliseconds ->
            update(elapsedTime)
            // render(graphics)
        }
    }

    private fun gameLoop(gameLogicFunction: (elapsedTime: Milliseconds) -> Unit) {
        var startTime: Milliseconds = System.currentTimeMillis()
        val frame: Milliseconds = (1000L * 1000L / FPS)
        var elapsedTime: Milliseconds = 0

        while (running) {
            gameLogicFunction(elapsedTime)

            elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < frame) {
                val sleep: Long = (frame - elapsedTime) / (1000L * 1000L)
                try {
                    Thread.sleep(sleep)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }
            startTime += elapsedTime
        }
    }

    private fun update(elapsedTime: Milliseconds) {
        for (entity: Entity in entities()) {
            entity.update(elapsedTime)

            if (entity is LivingEntity && entity.isDead()) {
                entities()
            }
        }
    }

    private fun render(graphics: Graphics2D) {
        for (entity: Entity in entities()) {
            entity.render(graphics)
        }
    }
}