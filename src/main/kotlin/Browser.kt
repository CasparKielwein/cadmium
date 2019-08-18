import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Core Class representing a running browser instance
 *
 * @property driver Instance of Selenium WebDriver driving the Browser instance
 * @property defaultWait default Wait, Methods use when searching for WebElements
 * @property hooks hook functions executed on interactions with WebElements
 */
class Browser(
    val driver: WebDriver,
    val defaultWait: WebDriverWait = WebDriverWait(driver, 10),
    var hooks: SeleniumHooks = SeleniumHooks()
) {
    fun open(url: String) {
        driver.get(url)
    }

    fun browse(url: String, actions: Browser.() -> Unit) {
        open(url)
        actions()
        driver.close()
    }

    fun element(loc: Locator): WebElement = WebElement(driver, defaultWait, loc.by, hooks)

    fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        val e = WebElement(driver, defaultWait, loc.by, hooks)
        e.actions()
        return e
    }

    fun click(loc: Locator) = element(loc).click()

    fun click(text: String) = element(XPath("//input[@value=\"$text\"]")).click()
}

/**
 * Provides Hooks for Callbacks which are executed on browser actions.
 *
 * These can be used to add debug output or slow actions down to make them more observable.
 */
data class SeleniumHooks(
    val onClick: (locator: By) -> Unit = {},
    val onEnter: (locator: By, text: CharSequence) -> Unit = { _: By, _: CharSequence -> }
)
