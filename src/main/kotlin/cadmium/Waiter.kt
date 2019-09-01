package cadmium

import com.kizitonwose.time.Interval
import com.kizitonwose.time.TimeUnit


fun waitFor(time: Interval<TimeUnit>) {
    Thread.sleep(time.inMilliseconds.longValue)
}

fun waitFor(v: () -> Boolean) {
    TODO(v.toString())
}

