package cadmium

import cadmium.util.modifierKey
import org.openqa.selenium.Dimension
import org.openqa.selenium.Keys
import org.openqa.selenium.Point
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * Represents a single open Browser Window or Tab
 */
class Window<T : Page>(val page: T) : SearchContext by page {

    private val handle = page.driver.windowHandle
    private val d
        get() = page.driver

    /**
     * Closes this window and invalidates this object.
     */
    fun close() {
        if (d.windowHandle != handle)
            d.switchTo().window(handle)

        d.close()
    }

    /**
     * Size of the current window.
     *
     * Changing it will change the outer window dimension,
     * not just the view port, synonymous to window.resizeTo() in JS.
     */
    var size: Dimension
        get() = d.manage().window().size!!
        set(value) {
            d.manage().window().size = value
        }

    /**
     * Position of the current window.
     *
     * This is relative to the upper left corner of the screen.
     */
    var position: Point
        get() = d.manage().window().position!!
        set(value) {
            d.manage().window().position = value
        }

    /**
     * Maximizes the current window if it is not already maximized
     */
    fun maximize() = d.manage().window().maximize()

    /**
     * Set the current window to full-screen if it is not already at full-screen
     */
    fun fullscreen() = d.manage().window().fullscreen()
}

/**
 * Opens a link with in window specified by target attribute of the link.
 *
 * @param link Locator which specifies the target
 * @return window with page corresponding to [link].
 * Might be the current window.
 *
 * Might open a new window depending on target attribute of link.
 * In this case the new window will have to be closed manually after use.
 */
fun <T : Page> T.open(link: Locator): Window<T> {
    element(link).click()
    return Window(this)
}

/**
 * Opens a link in a new window and closes the window after use.
 *
 * Currently the opened page will not be backed by an URL
 * and thus not support relative paths for opening new Pages.
 *
 * @param link Locator which specifies the target
 * @param timeout wait for this long for new window to open.
 * @param action Execute on page in new window
 */
@UseExperimental(ExperimentalTime::class)
fun Page.inTempWindow(link: Locator, timeout: Duration = 10.seconds, action: Page.() -> Unit) {
    element(link).enter(Keys.chord(modifierKey(), Keys.RETURN))
    val oldHandle = driver.windowHandle
    // we depend on the fact that the implementation of getWindowHandles() constructs a LinkedHashSet,
    // which guarantees order of iteration is equal to order of insertion.
    waitUntil(timeout) { driver.windowHandles.last() != oldHandle }
    val latestHandle = driver.windowHandles.last()

    assert(oldHandle != latestHandle)
    driver.switchTo().window(latestHandle)

    try {
        Page(b).action()
    } catch (ex: Throwable) {
        b.hooks.onBrowsingError(ex)
        throw ex
    } finally {
        b.hooks.beforeClose()
        driver.close()
        assert(driver.windowHandles.isNotEmpty())
        driver.switchTo().window(oldHandle)
    }
}
