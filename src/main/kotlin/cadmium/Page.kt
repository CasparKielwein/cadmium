package cadmium

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL

/**
 * Represents a single page opened with Selenium WebDriver
 *
 * Page is one of the core Classes of cadmium.
 * Extend it with your own classes to implement page objects.
 * Extend UrlBackedPage if you like opening pages with relative paths.
 *
 * @property b Browser instance driving this page
 * @property waiter implementation of Waiter used by this page.
 * @property autoScroll if true, cadmium will try to scroll WebElement into view
 * before click() or enter() are executed.
 */
open class Page(internal val b: Browser, private val waiter: Waiter = DefaultWaiterImpl(b)) :
    SearchContext, Waiter by waiter {

    var autoScroll : Boolean = false

    /**
     * Get a WebElement and apply given actions on it
     *
     * @param loc Locator used to identify the Element
     * @param actions Extension Function on WebElement which is executed on returned element
     * @return WebElement found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    override fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        //don't use element returned by until as that does not seem to trigger ChangeEvents.
        b.defaultWait.until(ExpectedConditions.presenceOfElementLocated(loc.by))
        val e = WebElement(
            b.driver.findElement(loc.by)!!,
            b.defaultWait,
            b.driver,
            autoScroll
        )
        e.actions()
        return e
    }

    /**
     * Find all elements within the current page using the given mechanism.
     *
     * @param loc The locating mechanism to use
     * @param waiter optionally controls how long WebDriver is supposed to wait until empty List is returned
     * @return A list of all WebElements, or an empty list if nothing matches
     * @see element
     */
    override fun elements(loc: Locator, waiter: WebDriverWait): List<WebElement> {
        return b.driver.findElements(loc.by).map { WebElement(it, waiter, b.driver, autoScroll) }
    }

    /**
     * overload of elements which uses defaultWait
     */
    override fun elements(loc: Locator): List<WebElement> = elements(loc, b.defaultWait)

    /**
     * Shorthand for click on element given by locator ( element(loc).click() )
     */
    fun click(loc: Locator) = element(loc).click()

    /**
     * Waits for an Alert dialog to appear
     *
     * @return handle to present Alert
     * @Todo convert this to a proper Waiter condition which still returns a cadmium.Alert
     */
    fun waitForAlert(): Alert {
        return Alert(waitUntil(ExpectedConditions.alertIsPresent()))
    }

    /**
     * The source of the current page
     *
     * If the page has been modified after loading (for
     * example, by Javascript) there is no guarantee that the returned text is that of the modified
     * page.
     *
     * @see org.openqa.selenium.WebDriver.getPageSource
     */
    val source: String
        get() = b.driver.pageSource!!

    /**
     * The title of the current page, with leading and trailing whitespace stripped, or null
     * if no title is set yet.
     */
    val title: String?
        get() = b.driver.title

    /**
     * Waiter condition which returns true when a pageload was triggered
     *
     * Idea is taken from:
     * <https://blog.codeship.com/get-selenium-to-wait-for-page-load/>
     */
    val pageLoad =
        ExpectedConditions.stalenessOf(b.driver.findElement(By.tagName("html")))!!

    /**
     * Current url of this page.
     */
    val currentUrl
        get() = b.driver.currentUrl!!
}

/**
 * Specialization of Page class which supports relative URLs.
 *
 * @property baseURL all relative URLs are assumed to be relative to this path.
 */
open class UrlBackedPage(private var baseURL: URL, b: Browser) : Page(b) {

    fun open(url: URL, actions: Page.() -> Unit = {}): Window<Page> {
        baseURL = url
        b.driver.get(baseURL.toString())
        actions()
        return Window(this)
    }

    /**
     * Open given relative URL from current baseURL
     *
     * @param relativeUrl relative URL as seen from current baseURL
     * @param actions executed on Page after opening URL
     * @sample cadmium_test.TestBrowser.testRelativeURL
     */
    fun open(relativeUrl: String, actions: Page.() -> Unit = {}): Window<Page> {
        b.driver.get("$baseURL/$relativeUrl")
        actions()
        return Window(this)
    }
}

/**
 * Apply given actions on page.
 *
 * Visit both as in visiting a page and the Visitor pattern
 *
 * @param actions applied to page object
 */
fun <T : Page> T.visit(actions: T.() -> Unit) {
    actions()
}

/**
 * Scope function which executes given action with autoscroll enabled
 *
 * @param actions applied to page object
 */
fun <T : Page> T.withAutoScroll(actions: T.() -> Unit) {
    autoScroll = true
    actions()
    autoScroll = false
}

/**
 * Provide access to WebDriver used in Page
 */
internal val Page.driver: WebDriver
    get() = b.driver

/**
 * Returns unwrapped selenium.WebDriver.
 *
 * Note: Unstable!
 * This function is intended for use when cadmium lacks a feature
 * and access to the selenium WebDriver is required.
 *
 * Anything done with the raw webdriver might be broken
 * by cadmium code later.
 */
fun Page.rawWebDriver() = driver
