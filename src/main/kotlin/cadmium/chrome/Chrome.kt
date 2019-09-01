package cadmium.chrome

import cadmium.Browser
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions

class Chrome(driver: ChromeDriver) : Browser(driver)

fun headlessChrome(): Chrome {
    val options = ChromeOptions()
    options.setBinary("/usr/bin/chromium-browser")
    options.addArguments("--headless")
    return Chrome(ChromeDriver(options))
}