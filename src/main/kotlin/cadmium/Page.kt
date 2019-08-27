package cadmium

import com.kizitonwose.time.Interval
import com.kizitonwose.time.TimeUnit
import com.kizitonwose.time.seconds
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL

class Page(val basURL: URL, private val b: Browser) {

    fun open(relativeUrl: URL): Page {
        b.driver.get("$basURL/$relativeUrl")
        return this
    }

    /**
     * Get WebElement by locator, the most used function to interact with pages
     *
     * @param loc Locator used to identify the Element
     * @return WebElement found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    fun element(loc: Locator): WebElement = WebElement(b.driver, b.defaultWait, loc.by, b.hooks)

    fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        val e = WebElement(b.driver, b.defaultWait, loc.by, b.hooks)
        e.actions()
        return e
    }

    /**
     * Shorthand for click on element given by locator ( element(loc).click() )
     */
    fun click(loc: Locator) = element(loc).click()

    fun click(text: String) = element(XPath("//input[@value=\"$text\"]")).click()

    /**
     * Waits for an Alert to appear
     *
     * @param timeOut max wait Time for Alert to appear
     * @throws NoAlertPresentException if no alert appears until timeOut
     * @return handle to present Alert
     */
    fun waitForAlert(timeOut: Interval<TimeUnit> = 10.seconds): Alert {
        assert(timeOut.inSeconds.longValue > 0)
        for (i in 0..timeOut.inSeconds.longValue) try {
            return Alert(b)
        } catch (e: NoAlertPresentException) {
            Thread.sleep(1000)
        }
        throw NoAlertPresentException()
    }

    /**
     * Waits for the complete page to load new
     *
     * Idea is taken from:
     * https://blog.codeship.com/get-selenium-to-wait-for-page-load/
     */
    fun waitForPageLoad(timeOut: Interval<TimeUnit> = 10.seconds) {
        val oldPage = b.driver.findElement(By.tagName("html"))
        WebDriverWait(b.driver, timeOut.longValue).until(ExpectedConditions.stalenessOf(oldPage))
    }
}