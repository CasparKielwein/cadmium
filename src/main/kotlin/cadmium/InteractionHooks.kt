package cadmium

import org.openqa.selenium.By
import org.openqa.selenium.OutputType
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.events.AbstractWebDriverEventListener
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds
import org.openqa.selenium.WebElement as SeleniumWebElement

/**
 * Provides Hooks for Callbacks which are executed on browser actions.
 *
 * Provides all events from selenium.WebDriverEventListener and some more.
 * By default all event hooks are empty and do nothing.
 */
open class BrowserEventListener : AbstractWebDriverEventListener() {

    /**
     * called before closing a window
     */
    open fun beforeClose() {}
}

/**
 * Prints to a logging stream
 *
 * Prints before actions and on failure.
 * Assume that silence after "before*" means success.
 * Use VeryVerbose if you everything printed.
 */
sealed class Verbose : BrowserEventListener() {
    var log = { text : String -> System.err.println(text) }

    override fun beforeClose() {
        log("closes current window.")
    }

    override fun beforeAlertAccept(webDriver: WebDriver) {
        log("will accept alert.")
    }
    override fun beforeAlertDismiss(driver: WebDriver) {
        log("will dismiss alert.")
    }

    override fun beforeNavigateTo(url: String, driver: WebDriver) {
        log("will navigate to ${url}.")
    }

    override fun beforeNavigateBack(driver: WebDriver) {
        log("will navigate back.")
    }

    override fun beforeNavigateForward(driver: WebDriver) {
        log("will navigate forward.")
    }

    override fun beforeNavigateRefresh(driver: WebDriver) {
        log("will refresh page.")
    }

    override fun beforeFindBy(by: By, element: SeleniumWebElement?, driver: WebDriver) {
        if (element != null)
            log("will try to find $by nested in $element")
        else
            log("will try to find $by")
    }

    override fun beforeClickOn(element: SeleniumWebElement, driver: WebDriver) {
        log("will click on $element")
    }

    override fun beforeChangeValueOf(element: SeleniumWebElement, driver: WebDriver, keysToSend: Array<CharSequence>) {
        log("will send: $keysToSend to $element")
    }

    override fun beforeScript(script: String, driver: WebDriver) {
        log("will execute script: $script")
    }

    override fun beforeSwitchToWindow(windowName: String, driver: WebDriver) {
        log("will switch to window: $windowName")
    }

    override fun onException(throwable: Throwable, driver: WebDriver) {
        log("throws exception: $throwable")
    }

    override fun beforeGetText(element: SeleniumWebElement, driver: WebDriver) {
        log("tries to get text of element: $element")
    }
}

/**
 * VeryVerbose logs every possible action
 */
class VeryVerbose : Verbose() {

    override fun afterAlertAccept(driver: WebDriver) {
        log("has accepted alert.")
    }

    override fun afterAlertDismiss(driver: WebDriver) {
        log("has dismissed alert.")
    }

    override fun afterNavigateTo(url: String, driver: WebDriver) {
        log("has navigated to $url.")
    }

    override fun afterNavigateBack(driver: WebDriver) {
        log("has navigated back.")
    }

    override fun afterNavigateForward(driver: WebDriver) {
        log("has navigated forward.")
    }

    override fun afterNavigateRefresh(driver: WebDriver) {
        log("has refreshed page.")
    }

    override fun afterFindBy(by: By, element: SeleniumWebElement?, driver: WebDriver) {
        if (element != null)
            log("has found elment: $by nested in $element.")
        else
            log("has found elment: $by.")
    }

    override fun afterClickOn(element: SeleniumWebElement, driver: WebDriver) {
        log("has clicked on element $element.")
    }

    override fun afterChangeValueOf(element: SeleniumWebElement, driver: WebDriver, keysToSend: Array<CharSequence>) {
        log("has entered: $keysToSend to element $element.")
    }

    override fun afterScript(script: String, driver: WebDriver) {
        log("has executed script: $script.")
    }

    override fun afterSwitchToWindow(windowName: String, driver: WebDriver) {
        log("has switched to window: $windowName.")
    }

    override fun <X> beforeGetScreenshotAs(target: OutputType<X>) {
        log("tries to take screenshot.")
    }

    override fun <X> afterGetScreenshotAs(target: OutputType<X>, screenshot: X) {
        log("has taken screenshot.")
    }

    override fun afterGetText(element: SeleniumWebElement, driver: WebDriver, text: String) {
        log("has retreived text: $text from element: $element.")
    }
}

/**
 * Waits given time before every change.
 */
@UseExperimental(ExperimentalTime::class)
class SlowDown(private val waitTime: Duration = 250.milliseconds) : BrowserEventListener() {
    override fun beforeAlertAccept(webDriver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }
    override fun beforeAlertDismiss(driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeNavigateTo(url: String, driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeNavigateBack(driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeNavigateForward(driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeNavigateRefresh(driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeClickOn(element: SeleniumWebElement, driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeChangeValueOf(element: SeleniumWebElement, driver: WebDriver, keysToSend: Array<CharSequence>) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeScript(script: String, driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }

    override fun beforeSwitchToWindow(windowName: String, driver: WebDriver) {
        Thread.sleep(waitTime.toLongMilliseconds())
    }
}

/**
 * Hooks that do nothing
 */
fun noHooks() = BrowserEventListener()

