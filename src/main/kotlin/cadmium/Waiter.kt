package cadmium

import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds


/**
 * Wait for a fixed amount of time
 */
@ExperimentalTime
fun waitFor(time: Duration) {
    Thread.sleep(time.inMilliseconds.toLong())
}

/**
 * Common Interface providing methods to wait for different Conditions
 */
interface Waiter {
    /**
     * Wait until a given condition is fulfilled or timeOut is reached.
     */
    @UseExperimental(ExperimentalTime::class)
    fun <T> waitUntil(timeOut: Duration = 10.seconds, condition: Waiter.() -> T) : T

    /**
     * Wait until a given condition is fulfilled
     */
    fun <T, P : Waiter> waitUntil(condition: P.() -> T) : T

    /**
     * Overload of waitUntil for selenium.ExpectedCondition
     *
     * @see org.openqa.selenium.ui.ExpectedCondition
     */
    fun waitUntil(condition: ExpectedCondition<Boolean>)
}

/**
 * Default Implementation based on cadmium Browser class.
 */
class DefaultWaiterImpl(private val b: Browser) : Waiter {

    @UseExperimental(ExperimentalTime::class)
    override fun <T> waitUntil(timeOut: Duration, condition: Waiter.() -> T) : T {
        return WebDriverWait(b.driver, timeOut.inSeconds.toLong()).until { condition() }
    }

    override fun <T, P : Waiter> waitUntil(condition: P.() -> T) : T {
        return b.defaultWait.until {
            @Suppress("UNCHECKED_CAST") //cast back to generic is safe
            (this as P).condition()
        }
    }

    override fun waitUntil(condition: ExpectedCondition<Boolean>) {
        b.defaultWait.until(condition)
    }
}