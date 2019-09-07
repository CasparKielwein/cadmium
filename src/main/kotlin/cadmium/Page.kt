package cadmium

import com.kizitonwose.time.Interval
import com.kizitonwose.time.TimeUnit
import com.kizitonwose.time.seconds
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL

/**
 * Represents a single page opened with Selenium WebDriver
 *
 * Page is one of the core Classes of cadmium.
 * Extend it with your own classes to implement page objects.
 *
 * @property baseURL property that is used to resolve all relative URL
 * @property b Browser instance driving this page
 */
open class Page(private val baseURL: URL, private val b: Browser) : SearchContext {

    /**
     * Open browser on baseURL
     */
    init {
        b.driver.get(baseURL.toString())
        b.hooks.afterOpen(baseURL)
    }

    /**
     * Open given relative URL from current baseURL
     *
     * @param relativeUrl relative URL as seen from current baseURL
     * @param actions executed on Page after opening URL
     * @sample cadmium_test.TestBrowser.testRelativeURL
     */
    fun open(relativeUrl: String, actions: Page.() -> Unit = {}): Page {
        b.driver.get("$baseURL/$relativeUrl")
        b.hooks.afterOpen(URL("$baseURL/$relativeUrl"))
        actions()
        return this
    }

    /**
     * Get a WebElement and apply given actions on it
     *
     * @param loc Locator used to identify the Element
     * @param actions Extension Function on WebElement which is executed on returned element
     * @return WebElement found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    override fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        val e = WebElement(DriverLocator(b.driver), b.defaultWait, loc, b.hooks)
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
     *
     * At the moment elements is eager and the WebElements returned are not evaluated lazily
     * as claimed in their documentation.
     * Todo: return a lazily evaluated range of WebElements instead
     */
    override fun elements(loc: Locator, waiter: WebDriverWait): List<WebElement> {
        return b.driver.findElements(loc.by).map { WebElement(DriverLocator(b.driver), waiter, loc, b.hooks) }
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
     * Shorthand for clicking on input element with given test as value
     */
    fun click(text: String) = element(XPath("//input[@value=\"$text\"]")).click()

    /**
     * Waits for an Alert dialog to appear
     *
     * @param timeOut max wait Time for Alert to appear
     * @throws org.openqa.selenium.NoAlertPresentException if no alert appears until timeOut
     * @return handle to present Alert
     */
    fun waitForAlert(timeOut: Interval<TimeUnit> = 10.seconds): Alert {
        assert(timeOut.inSeconds.longValue > 0)
        for (i in 0..timeOut.inSeconds.longValue) try {
            return Alert(b.driver.switchTo().alert())
        } catch (e: NoAlertPresentException) {
            Thread.sleep(1000)
        }

        b.hooks.onFail(NoAlertPresentException())
        throw NoAlertPresentException()
    }

    /**
     * Waits for the complete page to load new
     *
     * Idea is taken from:
     * https://blog.codeship.com/get-selenium-to-wait-for-page-load/
     *
     * @return this to allow chaining of method calls
     */
    fun waitForPageLoad(timeOut: Interval<TimeUnit> = 10.seconds): Page {
        val oldPage = b.driver.findElement(By.tagName("html"))
        WebDriverWait(b.driver, timeOut.longValue).until(ExpectedConditions.stalenessOf(oldPage))
        return this
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
        get() = b.driver.pageSource

    /**
     * The title of the current page, with leading and trailing whitespace stripped, or null
     * if one is not already set
     */
    val title: String?
        get() = b.driver.title
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
