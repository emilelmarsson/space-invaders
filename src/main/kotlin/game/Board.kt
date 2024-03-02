package game

import entity.Entity

data object Board {
    const val WIDTH: Int = 512
    const val HEIGHT: Int = 512

    fun Entity.isOutOfBounds(): Boolean {
        return (this.x - this.width < 0) || (this.y - this.height < 0) || (this.x > WIDTH) || (this.y > HEIGHT)
    }
    fun Entity.isInBounds(): Boolean = !this.isOutOfBounds()
}