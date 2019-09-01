package cadmium.firefox

import cadmium.Browser
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions

class Firefox(driver: FirefoxDriver) : Browser(driver)

fun headlessFirefox(): Firefox {
    val firefoxBinary = FirefoxBinary()
    firefoxBinary.addCommandLineOptions("--headless", "--no-remote")

    val firefoxOptions = FirefoxOptions()
    firefoxOptions.binary = firefoxBinary

    return Firefox(FirefoxDriver(firefoxOptions))
}
