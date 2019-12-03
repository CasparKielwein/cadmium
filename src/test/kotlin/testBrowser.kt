package cadmium_test

import cadmium.*
import cadmium.firefox.firefox
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

            waitUntil(pageLoad)
            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
    }

    @Test
    fun testRelativeURL() {
        val mainPage = headlessFirefox().open(URL("https://en.wikipedia.org/wiki")).page

        val cheesePage = mainPage.open("cheese")

        assertEquals("Cheese", cheesePage.element(Id("firstHeading")).text)
    }

    @Test
    fun testWaiter() {
        //constructs object representing running browser
        val firefox = headlessFirefox()
        //get Page object pointing to wikipedia and use it
        firefox.browse(URL("https://en.wikipedia.org/wiki")) {
            //get element searchInput and interact with it
            element(Id("searchInput")) {
                enter("cheese")
                enter(Keys.ENTER)
            }

            //wit until pageload has been triggered, using waitUntil dsl
            waitUntil(pageLoad)
            assertEquals("Cheese", element(Id("firstHeading")).text)

            open("Main_Page")

            element(Id("searchInput")).enter("Bacon", Keys.ENTER)

            //wait until header changed to "Bacon" using waitUntil dsl
            waitUntil{ element(Id("firstHeading")).with { text == "Bacon" }}
            waitUntil{ with(element(Id("firstHeading"))) { text == "Bacon"}}
            waitUntil{ element(Id("firstHeading")).text == "Bacon"}

            assertEquals("Bacon", element(Id("firstHeading")).text)
        }
    }
}