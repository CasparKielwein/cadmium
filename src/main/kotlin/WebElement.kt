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
 * Use its Documentation for detailed informations.
 * @see https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html
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
     */
    fun click() {
        hooks.onClick(locator)
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).click()
    }

    /**
     * Enter text or key-presses to this element, wait default timeout for it to become visible
     *
     * @param text character sequence to send to the element
     */
    fun enter(text: CharSequence): WebElement {
        hooks.onEnter(locator, text)
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).sendKeys(text)
        return this
    }

    /**
     * If this element is a text entry element, this will clear the value.
     */
    fun clear(): WebElement {
        defaultWait.until(ExpectedConditions.visibilityOfElementLocated(locator))
        driver.findElement(locator).clear()
        return this
    }

    val text: String
        get() = driver.findElement(locator).text

    /**
     * Determine whether or not this element is selected or not.
     *
     * This operation only applies to input elements such as checkboxes, options in a select and radio buttons.
     */
    val selected: Boolean
        get() = driver.findElement(locator).isSelected

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
     * returns the attribute/property's current value or null if the value is not set.
     * @param name The name of the attribute
     *
     * @see https://seleniumhq.github.io/selenium/docs/api/java/org/openqa/selenium/WebElement.html#getAttribute-java.lang.String-
     */
    fun getAttribute(name: String): String? = driver.findElement(locator).getAttribute(name)
}
