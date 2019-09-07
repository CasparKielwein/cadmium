package cadmium

import org.openqa.selenium.support.ui.WebDriverWait

/**
 * Common Interface for Objects which allow searching for a WebElement within their context
 */
interface SearchContext {
    /**
     * Get a WebElement and apply given actions on it
     *
     * @param loc Locator used to identify the Element
     * @param actions Extension Function on WebElement which is executed on returned element
     * @return WebElement found by given Locator
     * if multiple elements match the locator, the first is returned
     */
    fun element(loc: Locator, actions: WebElement.() -> Unit = {}): WebElement

    /**
     * Find all elements within the current page using the given mechanism.
     *
     * @param loc The locating mechanism to use
     * @param waiter optionally controls how long WebDriver is supposed to wait until empty List is returned
     * @return A list of all WebElements, or an empty list if nothing matches
     * @see element
     */
    fun elements(loc: Locator, waiter: WebDriverWait): List<WebElement>

    /**
     * overload of elements without dedicated wait parameter
     */
    fun elements(loc: Locator): List<WebElement>
}
