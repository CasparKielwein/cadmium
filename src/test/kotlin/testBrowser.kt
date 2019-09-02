package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import org.openqa.selenium.Keys
import kotlin.test.Test
import java.net.URL
import kotlin.test.assertEquals

internal class TestBrowser {

    @Test
    fun testMinimalExample() {
        headlessFirefox().browse(URL("https://en.wikipedia.org/wiki")) {
            element(Id("searchInput"))
                .enter("cheese")
                .enter(Keys.ENTER)
            waitForPageLoad()

            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
    }

    @Test
    fun testRelativeURL() {
        val mainPage = headlessFirefox().open(URL("https://en.wikipedia.org/wiki"))

        val cheesePage = mainPage.open("cheese")

        assertEquals("Cheese", cheesePage.element(Id("firstHeading")).text)
    }
}