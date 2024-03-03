package game

import animation.Animations
import entity.Bullet
import entity.Enemy
import entity.Entity
import entity.Particle
import entity.Player
import java.awt.Graphics2D


class Game (
    val graphics: Graphics2D,
    var running: Boolean = true,
    var paused: Boolean = false
) : Runnable {
    companion object {
        const val FPS: Int = 60
        const val FRAME_RATE: Milliseconds = (1000L / FPS)
    }

    private val player: Player = Player(
        x = Board.WIDTH / 2.0,
        y = Board.HEIGHT / 2.0,
        width = 16.0,
        height = 16.0,
        firingDelay = 500L,
        speed = 3.0,
        maxHealthPoints = 6,
        animation = Animations.PLAYER_ANIMATION
    )
    private val playerBullets: MutableList<Bullet> = mutableListOf()
    private val particles: MutableList<Particle> = mutableListOf()
    private val enemies: MutableList<Enemy> = mutableListOf()

    private fun entities(): List<Entity> {
        return enemies + playerBullets + particles + player
    }

    override fun run() {
        gameLoop { elapsedTime: Milliseconds ->
            render(graphics)
            update(elapsedTime)
        }
    }

    private inline fun gameLoop(gameLogicFunction: (elapsedTime: Milliseconds) -> Unit) {
        var startTime: Milliseconds = System.currentTimeMillis()
        var elapsedTime: Milliseconds

        while (running) {
            elapsedTime = System.currentTimeMillis() - startTime
            if (elapsedTime < FRAME_RATE) {
                val sleep: Long = FRAME_RATE - elapsedTime
                try {
                    Thread.sleep(sleep)
                } catch (e: InterruptedException) {
                    e.printStackTrace()
                }
            }

            gameLogicFunction(elapsedTime)

            startTime += elapsedTime
        }
    }

    private fun update(elapsedTime: Milliseconds) {
        for (entity: Entity in entities()) {
            entity.update(elapsedTime)
        }
    }

    private fun render(graphics: Graphics2D) {
        for (entity: Entity in entities()) {
            entity.render(graphics)
        }
    }
}