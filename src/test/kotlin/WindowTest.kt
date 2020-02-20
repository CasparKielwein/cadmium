package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test-class of code handling multiple browser windows
 */
internal class TestWindow {

    @Test
    fun testTemporaryWindow() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/")) {
        assertEquals("http://the-internet.herokuapp.com/", currentUrl)
        inTempWindow(Link("A/B Testing")) {
            assertEquals("http://the-internet.herokuapp.com/abtest", currentUrl)
        }
        assertEquals("http://the-internet.herokuapp.com/", currentUrl)
    }

    @Test
    fun testSameWindow() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/")) {
        assertEquals("http://the-internet.herokuapp.com/", currentUrl)

        open(Link("A/B Testing"))
        assertEquals("http://the-internet.herokuapp.com/abtest", currentUrl)
    }
}
