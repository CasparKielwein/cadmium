package cadmium.chrome

import cadmium.Browser
import cadmium.BrowserConfig
import java.io.File
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

/**
 * Represents the Chrome Webbrowser
 */
class Chrome(config: ChromeConfig) : Browser(ChromeDriver(config.options), config)

/**
 * Configuration object for Chrome
 *
 * @property options specific options of ChromeDriver
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
    val chromiumPath = findChromiumExecutable()  // this only works on linux atm
    config.options.setBinary(chromiumPath)

    //apply user configurations
    config.configAction()

    return Chrome(config)
}

fun findChromiumExecutable(): String {
    val paths = arrayOf("/usr/bin/chromium-browser", "/usr/bin/chromium")
    return paths.find { path -> File(path).exists()}
        ?: throw Exception("Cannot find a Chromium binary at ${paths.joinToString(", ")}")
}

/**
 * Initialize Chrome in headless mode
 */
fun headlessChrome() = chrome {
    options.setHeadless(true)
}
