package animation

import game.Milliseconds
import java.awt.image.BufferedImage
import java.lang.IllegalStateException

data class Animation (val frames: List<Frame>, val repeat: Boolean = true) {
    init {
        assert(frames.isNotEmpty()) { "Animations must have at least one frame" }
    }

    companion object {
        fun frames(vararg frames: Frame): Animation {
            return Animation(frames = frames.toList())
        }
    }

    private var time: Milliseconds = 0
    private val totalDuration: Milliseconds = frames.sumOf(Frame::duration)

    fun currentFrame(): Frame {
        var frameTime: Milliseconds = 0
        for (frame: Frame in frames) {
            if (time in frameTime..(frameTime + frame.duration)) {
                return frame
            }
            frameTime += frame.duration
        }
        throw IllegalStateException("Could not find animation frame at time $time ms")
    }

    fun frameWidth() = currentFrame().image.width

    fun frameHeight() = currentFrame().image.width

    fun update(elapsedTime: Milliseconds) {
        time = (time + elapsedTime) % totalDuration
    }

    data class Frame (val image: BufferedImage, val duration: Milliseconds)
}