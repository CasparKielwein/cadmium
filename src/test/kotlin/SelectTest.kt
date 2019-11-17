package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test

internal class TestSelect {

    @Test
    fun testDropdown() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        withOption(Id("dropdown")).select(ByText("Option 1"))

        assert(element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)

        withOption(Id("dropdown")).select(ByText("Option 2"))

        assert(!element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)
        assert(element(Id("dropdown")).element(XPath("//option[@value=\"2\"]")).selected)
    }

    @Test
    fun testSelectByValue() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        withOption(Id("dropdown")).select(ByValue("1"))

        assert(element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)
    }


    @Test
    fun testSelectByIndex() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        withOption(Id("dropdown")).select(ByIndex(1))

        assert(element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)
    }
}