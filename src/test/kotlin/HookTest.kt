package cadmium_test

import cadmium.Verbose
import cadmium.firefox.firefox
import java.net.URL
import kotlin.test.Test

internal class TestInteractionHooks {

    @Test
    fun testVerbosityPrinting() {
        val printMock = StringBuilder()
        val browser = firefox{
            hooks = Verbose { printMock.append("$it\n")}
            options.setHeadless(true)
        }

        browser.browse(URL("http://the-internet.herokuapp.com")) {
            //don't need to do anything for the test
        }
        assert(printMock.toString().contains("will navigate to http://the-internet.herokuapp.com."))
    }
}