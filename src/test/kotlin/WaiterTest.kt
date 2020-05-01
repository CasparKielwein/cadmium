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
}