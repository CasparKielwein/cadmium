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
 * @property driver Instance of Selenium WebDriver driving the Browser instance.
 * @param config BrowserConfig object use to configure this instance of Browser.
 * @property defaultWait default Wait, Methods use when searching for WebElements.
 * @sample cadmium_test.TestBrowser.testMinimalExample
 */
@UseExperimental(ExperimentalTime::class)
open class Browser(
    driver: WebDriver,
    config: BrowserConfig
) {

    val driver: WebDriver
    val defaultWait: WebDriverWait
    internal var hooks: BrowserEventListener

    init {
        //wrap normal WebDriver to catch fired events.
        val d = EventFiringWebDriver(driver)
        d.register(config.hooks)
        this.driver = d

        defaultWait = WebDriverWait(driver, config.defaultTimeout.inSeconds.toLong())
        //store hooks for events not provided by WebDriverEventListener
        hooks = config.hooks
    }

    /**
     * Opens a windows with the given URL.
     *
     * @param url URL of new page
     * @return Page opened at the given URl
     */
    fun open(url: URL): Window<UrlBackedPage> {
        val res = UrlBackedPage(url, this)
        res.open(url)
        return Window(res)
    }

    /**
     * Opens a window at given URL and executes given actions on Page.
     *
     * @param url URL of new page
     * @param actions Extension function Page executed after opening it.
     */
    fun browse(url: URL, actions: UrlBackedPage.() -> Unit) {
        try {
            open(url).page.actions()
        } catch (ex: Throwable) {
            hooks.onBrowsingError(ex)
            throw ex
        } finally {
            hooks.beforeClose()
            driver.close()
        }
    }
}

/**
 * Configuration Object to configure Browser Class
 *
 * @property defaultTimeout default duration used by WebDriver to wait for WebElements.
 * @property hooks hook functions executed on interactions with WebElements
 */
@UseExperimental(ExperimentalTime::class)
open class BrowserConfig(
    var defaultTimeout: Duration = 10.seconds,
    var hooks: BrowserEventListener = noHooks()
)

