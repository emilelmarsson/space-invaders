package animation

import game.Milliseconds
import java.awt.image.BufferedImage

data class Animation (val frames: List<Frame>, val repeat: Boolean = true) : Iterable<Animation.Frame> {
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

    operator fun get(frameIndex: Int): Frame {
        return frames[frameIndex]
    }

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

    fun frameWidth(): Int = this[0].image.width

    fun frameHeight(): Int = this[0].image.height

    fun update(elapsedTime: Milliseconds) {
        time = (time + elapsedTime) % totalDuration
    }

    override fun iterator(): Iterator<Frame> {
        return frames.iterator()
    }

    data class Frame (val image: BufferedImage, val duration: Milliseconds)
}