package cadmium

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.events.EventFiringWebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.net.URL
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.seconds

/**
 * Core Class representing a running browser instance
 *
 * @property driver Instance of Selenium WebDriver driving the Browser instance
 * @property defaultWait default Wait, Methods use when searching for WebElements
 * @property hooks hook functions executed on interactions with WebElements
 * @sample cadmium_test.TestBrowser.testMinimalExample
 */
@UseExperimental(ExperimentalTime::class)
open class Browser(
    driver: WebDriver,
    config: BrowserConfig
) {

    val driver: WebDriver
    var defaultWait: WebDriverWait
    var hooks: BrowserEventListener

    init {
        val d = EventFiringWebDriver(driver)
        d.register(config.hooks)
        this.driver = d

        defaultWait = WebDriverWait(driver, config.defaultTimeout.inSeconds.toLong())
        //store hooks for events not provided by WebDriverEventListener
        hooks = config.hooks
    }

    /**
     * Opens a windows with the given URL
     *
     * @param url URL of new page
     * @return Page opened at the given URl
     */
    fun open(url: URL): Page {
        return Page(url, this)
    }

    /**
     * Opens a window at given URL and executes given actions on Page
     *
     * @param url URL of new page
     * @param actions Extension function Page executed after opening it.
     */
    fun browse(url: URL, actions: Page.() -> Unit) {
        open(url).actions()
        hooks.beforeClose()
        driver.close()
    }
}

@UseExperimental(ExperimentalTime::class)
open class BrowserConfig(
    var defaultTimeout: Duration = 10.seconds,
    var hooks: BrowserEventListener = noHooks()
)
