package cadmium

import cadmium.util.modifierKey
import org.openqa.selenium.Keys
import org.openqa.selenium.WebDriver
import org.openqa.selenium.interactions.Actions
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Core Class representing a single Web Element and its possible Interactions
 *
 * Construction of WebElement can throw Exceptions, when the element is not found
 * All interactions can throw Exceptions when Element has become stale.
 * Interactions, which require the element to be visible will throw execptions if it is not.
 *
 * WebElement forwards most calls to selenium.WebElement.
 * Use its Documentation for detailed information.
 * @see org.openqa.selenium.WebElement
 *
 * @property autoScroll if true, calls to enter and click will try to scroll
 * the element into view if it is not displayed.
 * @property wait default Wait, Methods use when trying to interact with WebElements
 * @property rawElement org.openqa.selenium.WebElement wrapped in this object.
 * Access to rawElement is possible for cases where selenium functionality not provided
 * by cadmium is required. Access to rawElement is considered to be unstable
 * and can be broken without a change in major version number.
 */
class WebElement(val rawElement: org.openqa.selenium.WebElement,
                 internal var wait: WebDriverWait,
                 internal val driver: WebDriver,
                 var autoScroll: Boolean) : SearchContext {

    private fun findNested(loc: Locator): org.openqa.selenium.WebElement {
        //FIXME this might match wider than the find call for the nested element.
        wait.until(ExpectedConditions.presenceOfElementLocated(loc.by))
        return rawElement.findElement(loc.by)!!
    }

    /**
     * Get a nested WebElement and apply given actions on it
     *
     * @param loc Locator used to identify the Element
     * @param actions Extension Function on WebElement which is executed on returned element
     * @return WebElement nested in this element found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    override fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        val e = WebElement(findNested(loc), wait, driver, autoScroll)
        e.actions()
        return e
    }

    /**
     * Find all elements within the current element using the given mechanism.
     *
     * @param loc The locating mechanism to use
     * @param waiter optionally controls how long WebDriver is supposed to wait until empty List is returned
     * @return A list of all WebElements, or an empty list if nothing matches
     * @see element
     */
    override fun elements(loc: Locator, waiter: WebDriverWait): List<WebElement> {
        return rawElement
            .findElements(loc.by)
            .map { WebElement(it, waiter, driver, autoScroll) }
    }

    /**
     * overload of elements without dedicated wait parameter
     */
    override fun elements(loc: Locator): List<WebElement> = elements(loc, wait)

    /**
     * Click this element, wait default timeout for it to become visible
     *
     * The element must be visible and it must have a height and width
     * greater then 0.
     */
    fun click() {
        if (autoScroll && !displayed)
            scrollIntoView()
        wait.until(ExpectedConditions.elementToBeClickable(rawElement))
        rawElement.click()
    }

    /**
     * Enter text or key-presses to this element, wait default timeout for it to become visible
     *
     * @param text character sequence to send to the element
     * @param replace if true, [text] will replace the current content of WebElement.
     * Use replace instead of a separate clear call, if the additional onChange event triggered by clear causes problems.
     */
    fun enter(vararg text: CharSequence, replace: Boolean = false) {
        if (autoScroll && !displayed)
            scrollIntoView()
        if (replace) {
            //select all current text by pressing control/command + a and delete it
            rawElement.sendKeys(Keys.chord(modifierKey(), "a"), Keys.DELETE)
        }
        rawElement.sendKeys(*text)
    }

    /**
     * If this element is a text entry element, this will clear the value.
     *
     * Wait default timeout for the element to become visible.
     *
     * @return this, to allow chaining of operation.
     */
    fun clear(): WebElement {
        rawElement.clear()
        return this
    }

    /**
     * If this current element is a form, or an element within a form, then this will be submitted to
     * the remote server. If this causes the current page to change, then this method will block until
     * the new page is loaded.
     *
     * @throws org.openqa.selenium.NoSuchElementException If the given element is not within a form
     * @return this to allow chaining of operations
     */
    fun submit(): WebElement {
        rawElement.submit()
        return this
    }

    /**
     * Will scroll until the WebElement is in the current view.
     */
    fun scrollIntoView() = Actions(driver).moveToElement(rawElement)

    /**
     * Get Text of WebElement
     */
    val text: String
        get() = rawElement.text

    /**
     * Determine whether or not this element is selected or not.
     *
     * This operation only applies to input elements such as checkboxes, options in a select and radio buttons.
     */
    val selected: Boolean
        get() = rawElement.isSelected

    /**
     * Determine if an element is enabled or not.
     *
     * It is true if element is enabled (All elements apart from disabled input elements) and false if otherwise.
     */
    val enabled: Boolean
        get() = rawElement.isEnabled

    /**
     * Is this element displayed or not?
     */
    val displayed: Boolean
        get() = rawElement.isDisplayed

    /**
     * Attribute value of element or null if no value exists
     *
     * This is a shortcut for getAttribute("value")
     */
    val value: String?
        get() = getAttribute("value")

    /**
     * Get the value of the given attribute of the element.
     *
     * @param[name] The name of the attribute
     *
     * @returns the attribute/property's current value or null if the value is not set.
     *
     * See: https://www.seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html#getAttribute-java.lang.String-
     */
    fun getAttribute(name: String): String? = rawElement.getAttribute(name)

    /**
     * Executes action on this. Visitor for DSL functions
     *
     * @param action will be executed on this element
     * @return value returned by action
     */
    fun <T> with(action: WebElement.() -> T) = this.action()
}

/**
 * @param attribute Name of the attribute to search for
 * @return true if the attribute exists for the Element, false if not
 */
fun WebElement.has(attribute: String) = this.getAttribute(attribute) != null
