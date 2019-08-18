import org.openqa.selenium.Keys
import kotlin.test.Test
import org.openqa.selenium.firefox.FirefoxBinary
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import kotlin.test.assertEquals

fun setUpFirefox(): FirefoxDriver {
    val firefoxBinary = FirefoxBinary()
    firefoxBinary.addCommandLineOptions("--headless", "--no-remote")

    val firefoxOptions = FirefoxOptions()
    firefoxOptions.binary = firefoxBinary

    return FirefoxDriver(firefoxOptions)
}

class TestBrowser {

    @Test
    fun testMinimalExample() {
        Browser(setUpFirefox()).browse("https://en.wikipedia.org/wiki") {
            element(Id("searchInput"))
                .enter("cheese")
                .enter(Keys.ENTER)
            waitFor(PageLoad())

            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
    }

}