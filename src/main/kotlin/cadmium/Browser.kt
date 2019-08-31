package cadmium

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL

/**
 * Core Class representing a running browser instance
 *
 * @property driver Instance of Selenium WebDriver driving the Browser instance
 * @property defaultWait default Wait, Methods use when searching for WebElements
 * @property hooks hook functions executed on interactions with WebElements
 */
open class Browser(
    val driver: WebDriver,
    val defaultWait: WebDriverWait = WebDriverWait(driver, 10),
    var hooks: InteractionHooks = InteractionHooks()
) {
    /**
     * Opens a windows with the given URL
     *
     * @return Page opened at the given URl
     *
     */
    fun open(url: URL) : Page {
        return Page(url,this)
    }

    fun browse(url: URL, actions: Page.() -> Unit) {
        open(url).actions()
        driver.close()
    }
}
