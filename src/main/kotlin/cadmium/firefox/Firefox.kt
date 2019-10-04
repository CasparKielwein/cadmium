package cadmium.firefox

import cadmium.Browser
import cadmium.BrowserConfig
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

/**
 * Represents Firefox Webbroser
 */
class Firefox(config: FirefoxConfig) : Browser(FirefoxDriver(config.options), config)

/**
 * Configuration object for Firefox
 *
 * @see org.openqa.selenium.firefox.FirefoxBinary
 * @see org.openqa.selenium.firefox.FirefoxOptions
 */
class FirefoxConfig(
    var options: FirefoxOptions,
    var binary: FirefoxBinary
) : BrowserConfig()

/**
 * Initialize a Firefox instance with custom configuration
 *
 * @param configAction executed on FirefoxConfig object before that is used to initialize FirefoxWebDriver
 * @return Firefox configured with given configuration
 * @sample headlessFirefox
 */
fun firefox(configAction: FirefoxConfig.() -> Unit = {}): Firefox {
    val config = FirefoxConfig(FirefoxOptions(), FirefoxBinary())
    //apply user configurations
    config.configAction()

    config.options.binary = config.binary
    return Firefox(config)
}

/**
 * Shortcut to start Firefox in headless mode
 */
fun headlessFirefox() = firefox {
    options.setHeadless(true)
}

