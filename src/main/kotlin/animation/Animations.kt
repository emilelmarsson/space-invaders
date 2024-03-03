package animation

import graphics.SpriteSheet

object Animations {
    val PLAYER_ANIMATION = Animation.frames(
        Animation.Frame(duration = 150L, image = SpriteSheet[0, 0]),
        Animation.Frame(duration = 150L, image = SpriteSheet[1, 0]),
        Animation.Frame(duration = 150L, image = SpriteSheet[2, 0]),
        Animation.Frame(duration = 150L, image = SpriteSheet[3, 0]),
    )
}