package cadmium.chrome

import cadmium.Browser
import cadmium.BrowserConfig
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Represents the Chrome Webbrowser
 */
class Chrome(config: ChromeConfig) : Browser(ChromeDriver(config.options), config)

/**
 * Configuration object for Chrome
 *
 * @see org.openqa.selenium.chrome.ChromeOptions
 */
class ChromeConfig(
    var options: ChromeOptions
) : BrowserConfig()


/**
 * Initialize a Chrome instance with custom configuration
 *
 * @param configAction executed on ChromeConfig object before that is used to initialize ChromeWebDriver
 * @return Chrome configured with given configuration
 * @sample headlessChrome
 */
fun chrome(configAction: ChromeConfig.() -> Unit = {}): Chrome {
    val config = ChromeConfig(ChromeOptions())
    //set default parameter
    config.options.setBinary("/usr/bin/chromium-browser") //this only works on linux atm

    //apply user configurations
    config.configAction()

    return Chrome(config)
}

/**
 * Initialize Chrome in headless mode
 */
fun headlessChrome() = chrome {
    options.setHeadless(true)
}