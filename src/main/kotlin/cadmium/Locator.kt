package cadmium

import com.google.common.collect.Iterables.toArray
import org.openqa.selenium.By
import org.openqa.selenium.support.pagefactory.ByAll
import org.openqa.selenium.support.pagefactory.ByChained

/**
 * Mechanism used to locate WebElement within a Document.
 *
 * @property by mechanism implementation used by WebDriver
 *
 * In order to create your own locating mechanisms,
 * it is possible to subclass this class and override property by
 *
 * @see org.openqa.selenium.By
 */
abstract class Locator {
    abstract val by: By
}

// We assert non-null here as the java selenium by functions never return null

/**
 * @param v The value of the "name" attribute to search for.
 */
class Name(v: String) : Locator() {
    override val by = By.name(v)!!
}

/**
 * @param v The value of the "id" attribute to search for.
 */
class Id(v: String) : Locator() {
    override val by = By.id(v)!!
}

/**
 * Find elements based on xpath label containing a text
 *
 * @param text the text contained in label to search for.
 */
class Label(text: String) : Locator() {
    override val by = By.xpath("//input[@id=string(//label[contains(text(),\"$text\")]/@for)]")!!
}

/**
 *  @param expression The XPath to use.
 */
class XPath(expression: String) : Locator() {
    override val by = By.xpath(expression)!!
}

/**
 * Find elements based on the value of the "class" attribute.
 *
 * If an element has multiple classes, then this will match against each of them.
 * For example, if the value is "one two onone",
 * then the class names "one" and "two" will match.
 *
 * @param name The value of the "class" attribute to search for.
 */
class ClassName(name: String) : Locator() {
    override val by = By.className(name)!!
}

/**
 * Find elements via the driver's underlying W3C Selector engine.
 *
 * @param selector CSS expression.
 */
class Css(selector: String) : Locator() {
    override val by = By.cssSelector(selector)!!
}

/**
 * @param text The exact text of Link to match against.
 */
class Link(text: String) : Locator() {
    override val by = By.linkText(text)!!
}

/**
 * @param text The partial text of Link to match against
 */
class PartialLink(text: String) : Locator() {
    override val by = By.partialLinkText(text)!!
}

/**
 * @param v The element's tag name.
 */
class Tag(v: String) : Locator() {
    override val by = By.tagName(v)!!
}

/**
 * Mechanism to search for elements which fulfill all given requirements
 */
class AllOf(vararg mechanisms: Locator) : Locator() {
    override val by = makeChained(mechanisms)

    private fun makeChained(mechanisms: Array<out Locator>): ByChained {
        val bys: Array<By> = mechanisms.map{ it.by }.toTypedArray()
        return ByChained(*bys)
    }
}

/**
 * Mechanism to search for elements which fulfill any single one of the given requirements
 */
class AnyOf(vararg mechanisms: Locator) : Locator() {
    override val by = makeAll(mechanisms)

    private fun makeAll(mechanisms: Array<out Locator>): ByAll {
        val bys: Array<By> = mechanisms.map{ it.by }.toTypedArray()
        return ByAll(*bys)
    }
}

/**
 * Combine two Mechanisms to a new one which requires both to be fulfilled
 */
infix fun Locator.and(rhs: Locator) = AllOf(this, rhs)

/**
 * Combine two Mechanisms to a new one which requires any one to be fulfilled
 */
infix fun Locator.or(rhs: Locator) = AnyOf(this, rhs)