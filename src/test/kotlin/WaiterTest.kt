package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import org.junit.jupiter.api.Tag
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime
import kotlin.time.seconds
import org.junit.jupiter.api.Assertions.assertThrows
import org.openqa.selenium.Keys


/**
 * Test-class of code handling waiting and timeouts
 */
internal class TestWaiter {

    @UseExperimental(ExperimentalTime::class)
    @Test
    @Tag("slow")
    fun testWaitUntil() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/entry_ad")) {

        //make sure that waitUntil does not stop normal behaviour
        val testText = waitUntil(1.seconds) { element(Id("restart-ad")).text}
        assertEquals("click here", testText)

        assertThrows(org.openqa.selenium.TimeoutException::class.java) {
            waitUntil(1.seconds) { element(Id("arglblargl")).text}
       }

        //call this to make sure we actually wait
        //disabled since it would make tests run extremely slow
        //waitUntil(100.seconds) { element(Id("arglblargl")).text}
    }

    @Test
    fun testWaitDsl() {
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