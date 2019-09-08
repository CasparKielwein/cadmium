package cadmium

import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@ExperimentalTime
fun waitFor(time: Duration) {
    Thread.sleep(time.inMilliseconds.toLong())
}

fun waitFor(v: () -> Boolean) {
    TODO(v.toString())
}

