package cadmium_test.cadmium.chrome

import cadmium.*
import cadmium.chrome.headlessChrome
import org.openqa.selenium.Keys
import kotlin.test.Test
import java.net.URL
import kotlin.test.assertEquals

internal class TestChrome {

    @Test
    fun testMinimalExample() {
        headlessChrome().browse(URL("https://en.wikipedia.org/wiki")) {
            element(Id("searchInput")) {
                enter("cheese")
                enter(Keys.ENTER)
            }

            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
    }
}