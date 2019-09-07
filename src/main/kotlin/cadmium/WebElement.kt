package cadmium

import org.openqa.selenium.By
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
 * @property driver Instance of Selenium WebDriver driving parent Browser instance
 * @property defaultWait default Wait, Methods use when trying to interact with WebElements
 * @property locator Selenium Locator used to retrieve Element
 * @property hooks hook functions executed on interactions Element
 */
class WebElement(
    private val driver: WebDriver,
    private val defaultWait: WebDriverWait,
    private val locator: By,
    private val hooks: InteractionHooks
) {
    /**
     * Click this element, wait default timeout for it to become visible
     *
     * @return this to allow chaining of operations
     *
     * The element must be visible and it must have a height and width
     * greater then 0.
     */
    fun click(): WebElement {
        hooks.onClick(locator)
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).click()
        return this
    }

    /**
     * Enter text or key-presses to this element, wait default timeout for it to become visible
     *
     * @param[text] character sequence to send to the element
     * @return this to allow chaining of operations
     */
    fun enter(text: CharSequence): WebElement {
        hooks.onEnter(locator, text)
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).sendKeys(text)
        return this
    }

    /**
     * If this element is a text entry element, this will clear the value.
     *
     * @return this to allow chaining of operations
     */
    fun clear(): WebElement {
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).clear()
        return this
    }

    /**
     * Get Text of WebElement
     */
    val text: String
        get() = driver.findElement(locator).text

    /**
     * Determine whether or not this element is selected or not.
     *
     * This operation only applies to input elements such as checkboxes, options in a select and radio buttons.
     */
    val selected: Boolean
        get() = driver.findElement(locator).isSelected

    /**
     * Determine if an element is enabled or not.
     *
     * It is true if element is enabled (All elements apart from disabled input elements) and false if otherwise.
     */
    val enabled: Boolean
        get() = driver.findElement(locator).isEnabled

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
    fun getAttribute(name: String): String? = driver.findElement(locator).getAttribute(name)
}
