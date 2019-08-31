package cadmium_test

import cadmium.*
import org.openqa.selenium.Keys
import kotlin.test.Test
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import java.net.URL
import kotlin.test.assertEquals

fun setUpFirefox(): Browser {
    val firefoxBinary = FirefoxBinary()
    firefoxBinary.addCommandLineOptions("--headless", "--no-remote")

    val firefoxOptions = FirefoxOptions()
    firefoxOptions.binary = firefoxBinary

    return Browser(FirefoxDriver(firefoxOptions))
}

class TestBrowser {

    @Test
    fun testMinimalExample() {
        setUpFirefox().browse(URL("https://en.wikipedia.org/wiki")) {
            element(Id("searchInput"))
                .enter("cheese")
                .enter(Keys.ENTER)
            waitForPageLoad()

            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
    }
}