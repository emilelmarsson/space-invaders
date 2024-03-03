package graphics

import game.Board
import game.Game
import java.awt.Dimension
import java.awt.Graphics2D
import java.awt.RenderingHints
import java.awt.image.BufferedImage
import javax.swing.JFrame
import javax.swing.JPanel
import javax.swing.WindowConstants

class GamePanel : JPanel(), Runnable {
    private val graphics: Graphics2D
    private val game: Game

    init {
        preferredSize = Dimension(Board.WIDTH, Board.HEIGHT)
        setFocusable(true)
        requestFocus()

        val image = BufferedImage(Board.WIDTH, Board.HEIGHT, BufferedImage.TYPE_INT_RGB)
        graphics = image.graphics as Graphics2D
        graphics.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON)
        graphics.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON)

        game = Game(graphics)
    }

    override fun run() {
        game.run()
    }
}

fun main() {
    val window = JFrame("Space Invaders")
    window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE)
    window.contentPane = GamePanel()
    window.setResizable(false)
    window.pack()
    window.isVisible = true
    window.setLocationRelativeTo(null)
}