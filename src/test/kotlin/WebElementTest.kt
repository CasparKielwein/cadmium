package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test
import kotlin.test.assertEquals

internal class TestWebElement {

    @Test
    fun testEnterReplace() = headlessFirefox().browse(URL("https://en.wikipedia.org/wiki")) {
        element(Id("searchInput")). enter("cheese")
        assertEquals("cheese", element(Id("searchInput")).value)

        element(Id("searchInput")). enter("cheese")
        assertEquals("cheesecheese", element(Id("searchInput")).value)

        element(Id("searchInput")). enter("cheesecake", replace = true)
        assertEquals("cheesecake", element(Id("searchInput")).value)
    }

    @Test
    fun testHasElement() {
        val mainPage = headlessFirefox().open(URL("https://en.wikipedia.org/wiki"))

        assert(!mainPage.hasElement(Id("arglblargl")))
        assert(mainPage.hasElement(Id("From_today's_featured_article")))
    }

    @Test
    fun testElementsList()  = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/drag_and_drop")) {
        val columns = elements(ClassName("column"))

        assertEquals(2, columns.size)
        assertEquals("A", columns[0].text)
        assertEquals("B", columns[1].text)
    }

    @Test
    fun testNestedList() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/drag_and_drop")) {
        val form = element(Id("columns"))
        val columns = form.elements(ClassName("column"))

        assertEquals(2, columns.size)
        assertEquals("A", columns[0].text)
        assertEquals("B", columns[1].text)
    }
}