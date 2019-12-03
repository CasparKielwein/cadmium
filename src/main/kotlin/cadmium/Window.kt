package cadmium

import org.openqa.selenium.Keys
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
     * Closes this window. close invalidates this object.
     */
    fun close() {
        if (d.windowHandle != handle)
            d.switchTo().window(handle)

        d.close()
    }
}

/**
 * Opens a link with in window specified by target attribute of the link.
 *
 * @return window with page corresponding to [link].
 * Might be the current window.
 *
 * Might open a new window depending on target attribute of link.
 * In this case the new window will have to be closed manually after use.
 */
fun SearchContext.open(link: Link) {
    element(link).click()
}

/**
 * Opens a link in a new window and closes the window after use.
 *
 */
@UseExperimental(ExperimentalTime::class)
fun Page.openInTempWindow(link: Link, action: Page.() -> Unit) {
    element(link).enter(Keys.chord(Keys.CONTROL, Keys.RETURN))
    val oldHandle = driver.windowHandle
    // we depend on the fact that the implementation of getWindowHandles() constructs a LinkedHashSet,
    // which guarantees order of iteration is equal to order of insertion.
    waitUntil(10.seconds) { driver.windowHandles.last() != oldHandle }

    val latestHandle = driver.windowHandles.last()

    assert(oldHandle != latestHandle)
    driver.switchTo().window(latestHandle)

    action()
   // driver.close()
    assert(driver.windowHandles.isNotEmpty())
    driver.switchTo().window(oldHandle)
}
