package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

/**
 * Test-class of code handling iFrames
 */
internal class TestFrame {

    @Test
    fun testTemporaryFrame() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/iframe")) {
       //element should not be available outside of frame
        assert(!hasElement(ClassName("mce-content-body")))
        //test access of frame by index
        inFrame(Index(0)) {
            assert(hasElement(ClassName("mce-content-body")))
        }

        //test access of frame by element locator
        inFrame(Id("mce_0_ifr")) {
            assert(hasElement(ClassName("mce-content-body")))
        }
    }

    @Test
    fun testNestedFrames() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/nested_frames")) {
        inFrame(Name("frame-top")) {
            inFrame(Name("frame-left")) {
                assertEquals("LEFT", element(Tag("body")).text)
            }
        }
    }
}
