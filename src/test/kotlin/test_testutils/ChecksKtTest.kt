package test_testutils

import cadmium.Id
import cadmium.firefox.headlessFirefox
import test.hasElement
import java.net.URL
import kotlin.test.Test

internal class TestCheckHelpers {

    @Test
    fun testHasElement() {
        val mainPage = headlessFirefox().open(URL("https://en.wikipedia.org/wiki"))

        assert(!mainPage.hasElement(Id("arglblargl")))
        assert(mainPage.hasElement(Id("From_today's_featured_article")))
    }
}