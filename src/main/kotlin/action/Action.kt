package action

import game.Milliseconds

fun interface Action {
    fun perform()
}

class LimitedAction(
    private val delay: Milliseconds,
    private val limitedAction: () -> Unit
) : Action {

    private var time: Milliseconds = System.currentTimeMillis()

    fun isLimited(): Boolean {
        return System.currentTimeMillis() - time >= delay
    }

    override fun perform() {
        if (isLimited()) {
            return
        }
        limitedAction.invoke()
        time = System.currentTimeMillis()
    }
}

class RepeatedAction(
    private val delay: Milliseconds,
    private val repeatedAction: () -> Unit
) : Action {

    private var time: Milliseconds = System.currentTimeMillis()

    override fun perform() {
        if (System.currentTimeMillis() - time >= delay) {
            repeatedAction.invoke()
            time = System.currentTimeMillis()
        }
    }
}
