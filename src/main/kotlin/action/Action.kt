package action

import game.Milliseconds

fun interface Action {
    fun perform()
}

class DelayedAction(
    private var time: Milliseconds = System.currentTimeMillis(),
    private val delay: Milliseconds,
    private val delayedAction: () -> Unit
) : Action {

    override fun perform() {
        if (System.currentTimeMillis() - time >= delay) {
            delayedAction.invoke()
            time = System.currentTimeMillis()
        }
    }
}
