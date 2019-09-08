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
        //constructs object representing running browser
        val firefox = headlessFirefox()
        //get Page object pointing to wikipedia and use it
        firefox.browse(URL("https://en.wikipedia.org/wiki")) {
            //get element searchInput and interact with it
            element(Id("searchInput")) {
                enter("cheese")
                enter(Keys.ENTER)
            }

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