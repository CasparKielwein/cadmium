package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.time.ExperimentalTime

/**
 * Testclass of Code handling multiple browser windows
 */
internal class TestWindow {

    @ExperimentalTime
    @Test
    fun testTemporaryWindow() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/")) {
        assertEquals("http://the-internet.herokuapp.com/", currentUrl)
        openInTempWindow(Link("A/B Testing")) {
            assertEquals("http://the-internet.herokuapp.com/abtest", currentUrl)
        }
        assertEquals("http://the-internet.herokuapp.com/", currentUrl)
    }
}
