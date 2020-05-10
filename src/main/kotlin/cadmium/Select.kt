package cadmium

import org.openqa.selenium.By
import org.openqa.selenium.support.ui.Select

/**
 * Represents an Option Element like a dropdown menu or radio button group.
 *
 * Unlike WebElement, SelectOptions are evaluated eagerly, as it only serves as a base for the byX methods.
 * Eager evaluation fails faster in case of errors.
 *
 * @property element WebElement representing the full select.
 * @see org.openqa.selenium.support.ui.Select
 */
class SelectOptions(private val element: WebElement) {

    private val option
        get() = Select(element.rawElement)

    /**
     * Select the option from option by given locator
     *
     * @param loc mechanism to specify element to select.
     * @throws NoSuchElementException If no matching option elements are found
     *
     * If more than one element match the given locator, all are selected.
     */
    fun select(loc: SelectLocator) = when (loc) {
        is Value -> option.selectByValue(loc.value)
        is Text -> option.selectByVisibleText(loc.text)
        is Index -> option.selectByIndex(loc.index)
    }

    /**
     * Deselect the option from option by given locator
     *
     * @param loc mechanism to specify element to deselect.
     * @throws NoSuchElementException If no matching option elements are found
     *
     * If more than one element match the given locator, all are deselected.
     */
    fun deselect(loc: SelectLocator) = when (loc) {
        is Value -> option.deselectByValue(loc.value)
        is Text -> option.deselectByVisibleText(loc.text)
        is Index -> option.deselectByIndex(loc.index)
    }

    /***
     * Select all entries of option.  This is only valid when the SELECT supports multiple selections.
     *
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    fun selectAll() {
        if (!option.isMultiple)
            throw UnsupportedOperationException("You may only call selectAll on multi-select options!")

        for (o in option.options)
            if (!o.isSelected)
                o.click()
    }

    /**
     * Clear all selected entries. This is only valid when the SELECT supports multiple selections.
     *
     * @throws UnsupportedOperationException If the SELECT does not support multiple selections
     */
    fun deselectAll() = option.deselectAll()

    /**
     * List of all available options in this element
     */
    val allOptions
        get() = element.elements(Tag("option"))
}

sealed class SelectLocator

/**
 * Select all option elements which match given values
 */
class Value(val value: String) : SelectLocator(), Locator {
    override val by: By
        get() = By.xpath("//option[@value=\"$value\"]")
}

/**
 * Select all option elements which have given text as visible text.
 */
class Text(val text: String) : SelectLocator(), Locator {
    override val by: By
        get() = By.xpath("//option[contains(text(),\"$text\")]")
}

/**
 * Select the option at the given index. This is done by examining the "index" attribute of an
 * element, and not merely by counting.
 *
 * @property index index of element so select, must be zero or larger.
 */
class Index(val index: Int) : SelectLocator() {
    init {
        require(index >= 0)
    }
}

/**
 * Get an Option Group, a WebElement which is a SELECT tag
 *
 * @param loc Locator used to identify the Element
 * @param actions optional actions executed on option element
 * @return SelectOptions wrapping the SELECT found by given Locator
 * @throws org.openqa.selenium.support.ui.UnexpectedTagNameException when element is not a SELECT
 * @sample cadmium_test.TestSelect.testSelectByValue
 *
 * If multiple elements match the locator, the first is returned.
 */
fun SearchContext.option(loc: Locator, actions: SelectOptions.() -> Unit = {}) =
    SelectOptions(element(loc)).apply(actions)