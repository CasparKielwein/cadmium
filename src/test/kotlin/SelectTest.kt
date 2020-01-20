package cadmium_test

import cadmium.*
import cadmium.firefox.headlessFirefox
import java.net.URL
import kotlin.test.Test

internal class TestSelect {

    @Test
    fun testDropdown() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        option(Id("dropdown")).select(Text("Option 1"))

        assert(element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)

        option(Id("dropdown")).select(Text("Option 2"))

        assert(!element(Id("dropdown")).element(Value("1")).selected)
        assert(element(Id("dropdown")).element(Text("Option 2")).selected)
    }

    @Test
    fun testSelectByValue() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        option(Id("dropdown")).select(Value("1"))

        assert(element(Id("dropdown")).element(Text("Option 1")).selected)
    }


    @Test
    fun testSelectByIndex() = headlessFirefox().browse(URL("http://the-internet.herokuapp.com/dropdown")) {
        option(Id("dropdown")).select(Index(1))

        assert(element(Id("dropdown")).element(XPath("//option[@value=\"1\"]")).selected)
    }
}