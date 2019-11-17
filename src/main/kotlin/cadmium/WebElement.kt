package cadmium

import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Core Class representing a single Web Element and its possible Interactions
 *
 * WebElement interactions are lazy.
 * The selenium findElement functions are only called on interactions like click or enter.
 * They are not called on construction of WebElement itself.
 *
 * This means that all interactions throw Exceptions when Element has become stale or cannot be found.
 *
 * WebElement forwards most calls to selenium.WebElement.
 * Use its Documentation for detailed information.
 * @see org.openqa.selenium.WebElement
 *
 * @property find ElementLocator used to search for the element when interacting with it
 * @property wait default Wait, Methods use when trying to interact with WebElements
 * @property locator Selenium Locator used to retrieve Element
 */
class WebElement(
    private val find: ElementLocator,
    private var wait: WebDriverWait,
    private val locator: Locator
) : SearchContext {

    /**
     * Get a nested WebElement and apply given actions on it
     *
     * @param loc Locator used to identify the Element
     * @param actions Extension Function on WebElement which is executed on returned element
     * @return WebElement nested in this element found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    override fun element(loc: Locator, actions: WebElement.() -> Unit): WebElement {
        val e = WebElement(NestedLocator(this), wait, loc)
        e.actions()
        return e    }

    /**
     * Find all elements within the current element using the given mechanism.
     *
     * @param loc The locating mechanism to use
     * @param waiter optionally controls how long WebDriver is supposed to wait until empty List is returned
     * @return A list of all WebElements, or an empty list if nothing matches
     * @see element
     */
    override fun elements(loc: Locator, waiter: WebDriverWait): List<WebElement> {
        return find(locator)
            .findElements(loc.by)
            .map { WebElement(NestedLocator(this), waiter, loc) }
    }

    /**
     * overload of elements without dedicated wait parameter
     */
    override fun elements(loc: Locator): List<WebElement>  = elements(loc, wait)

    /**
     * Click this element, wait default timeout for it to become visible
     *
     * @return this to allow chaining of operations
     *
     * The element must be visible and it must have a height and width
     * greater then 0.
     */
    fun click(): WebElement {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator.by))
        find(locator).click()
        return this
    }

    /**
     * Enter text or key-presses to this element, wait default timeout for it to become visible
     *
     * @param[text] character sequence to send to the element
     * @return this to allow chaining of operations
     */
    fun enter(vararg text: CharSequence): WebElement {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator.by))
        find(locator).sendKeys(*text)
        return this
    }

    /**
     * If this element is a text entry element, this will clear the value.
     *
     * @return this to allow chaining of operations
     */
    fun clear(): WebElement {
        wait.until(ExpectedConditions.visibilityOfElementLocated(locator.by))
        find(locator).clear()
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
        find(locator).submit()
        return this
    }

    /**
     * Get Text of WebElement
     */
    val text: String
        get() = find(locator).text

    /**
     * Determine whether or not this element is selected or not.
     *
     * This operation only applies to input elements such as checkboxes, options in a select and radio buttons.
     */
    val selected: Boolean
        get() = find(locator).isSelected

    /**
     * Determine if an element is enabled or not.
     *
     * It is true if element is enabled (All elements apart from disabled input elements) and false if otherwise.
     */
    val enabled: Boolean
        get() = find(locator).isEnabled

    /**
     * Is this element displayed or not?
     */
    val displayed: Boolean
        get() = find(locator).isDisplayed

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
    fun getAttribute(name: String): String? = find(locator).getAttribute(name)

    /**
     * Executes action on this. Visitor for DSL functions
     *
     * @param action will be executed on this element
     * @return value returned by action
     */
    fun <T>with(action: WebElement.() -> T) = this.action()

    /**
     * org.openqa.selenium.Webelement contained in this
     */
    internal val actualElement : org.openqa.selenium.WebElement
        get() = find(locator)
}

/**
 * Bundles location functions with objects needed
 */
sealed class ElementLocator {
    abstract operator fun invoke(loc: Locator): org.openqa.selenium.WebElement
}

/**
 * Locate Elements through WebDriver
 */
class DriverLocator(private val driver: WebDriver) : ElementLocator() {
    override fun invoke(loc: Locator) = driver.findElement(loc.by)!!
}

/**
 * Locate Elements nested in other Element
 */
class NestedLocator(private val element: WebElement) : ElementLocator() {
    override fun invoke(loc: Locator)  = element.actualElement.findElement(loc.by)!!
}