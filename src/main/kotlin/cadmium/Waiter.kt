package cadmium

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedCondition
import org.openqa.selenium.support.ui.WebDriverWait
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


/**
 * Wait for a fixed amount of time
 */
@ExperimentalTime
fun waitFor(time: Duration) {
    Thread.sleep(time.inMilliseconds.toLong())
}

/**
 * Common Interface providing methods to wait for different Conditions
 *
 * @sample cadmium_test.TestBrowser.testWaiter
 */
interface Waiter {
    /**
     * Wait until a given condition is fulfilled or timeOut is reached.
     *
     * @param condition function evaluated to see of wait condition is fulfilled.
     * waitUntil will return when [condition] returns anything except null or false.
     *
     * @return result of condition closure after condition is fulfilled.
     */
    @UseExperimental(ExperimentalTime::class)
    fun <T> waitUntil(timeOut: Duration, condition: Waiter.() -> T): T

    /**
     * Wait until a given condition is fulfilled
     *
     * @param condition function evaluated to see of wait condition is fulfilled.
     * waitUntil will return when [condition] returns anything except null or false.
     *
     * @return result of condition closure after condition is fulfilled.
     */
    fun <T> waitUntil(condition: Waiter.() -> T): T

    /**
     * Overload of waitUntil for selenium.ExpectedCondition
     *
     * @see org.openqa.selenium.support.ui.ExpectedConditions
     */
    fun <T> waitUntil(condition: ExpectedCondition<T>): T
}

/**
 * Default Implementation based on cadmium Browser class and selenium wait api.
 *
 * Uses selenium Wait api under the hood.
 * By default NoSuchElementExceptions are ignored to allow waiting for existence of WebElements.
 * @see org.openqa.selenium.support.ui.Wait
 */
class DefaultWaiterImpl(b: Browser) : Waiter {
    private val wait: WebDriverWait =
        b.defaultWait.ignoring(org.openqa.selenium.NoSuchElementException::class.java) as WebDriverWait
    private val driver: WebDriver = b.driver

    @UseExperimental(ExperimentalTime::class)
    override fun <T> waitUntil(timeOut: Duration, condition: Waiter.() -> T): T =
        waitUntil(
            WebDriverWait(driver, timeOut.inSeconds.toLong())
                .ignoring(org.openqa.selenium.NoSuchElementException::class.java) as WebDriverWait,
            condition
        )

    override fun <T> waitUntil(condition: Waiter.() -> T): T =
        waitUntil(wait, condition)

    override fun <T> waitUntil(condition: ExpectedCondition<T>) =
        wait.until(condition)!!

    /**
     * uses seleniumWait to wait until a condition is true.
     * ignores NoSuchElementException to wait for element to pop up.
     */
    private fun <T> waitUntil(seleniumWait: WebDriverWait, condition: Waiter.() -> T): T {
        return seleniumWait.until { this.condition() }
    }
}