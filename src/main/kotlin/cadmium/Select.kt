package cadmium

import org.openqa.selenium.support.ui.Select

/**
 * Represents an Option Element like a dropdown menu or radio button group.
 *
 * Unlike WebElement, SelectOptions are evaluated eagerly, as it only serves as a base for the byX methods.
 * Eager evaluation fails faster in case of errors.
 *
 * @see org.openqa.selenium.support.ui.Select
 */
class SelectOptions(private val element: WebElement) {

    private val option
        get() = Select(element.actualElement)

    /**
     * Select the option from option by given locator
     *
     * @param loc mechanism to specify element to select.
     * @throws NoSuchElementException If no matching option elements are found
     *
     * If more than one element match the given locator, all are selected.
     */
    fun select(loc: SelectLocator) = when (loc) {
        is ByValue -> option.selectByValue(loc.value)
        is ByText -> option.selectByVisibleText(loc.text)
        is ByIndex -> option.selectByIndex(loc.index)
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
        is ByValue -> option.deselectByValue(loc.value)
        is ByText -> option.deselectByVisibleText(loc.text)
        is ByIndex -> option.deselectByIndex(loc.index)
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
    private val options
        get() = element.elements(Tag("option"));
}

sealed class SelectLocator

/**
 * Select all elements which match given values
 */
class ByValue(val value: String) : SelectLocator()

/**
 * Select all elements which have given text as visible text.
 */
class ByText(val text: String) : SelectLocator()

/**
 * Select the option at the given index. This is done by examining the "index" attribute of an
 * element, and not merely by counting.
 */
class ByIndex(val index: Int) : SelectLocator()

/**
 * Get an Option Group, a WebElement which is a SELECT tag
 *
 * @param loc Locator used to identify the Element
 * @param actions optional actions executed on option element
 * @return WebElement wrapping the SELECT found by given Locator
 * @throws org.openqa.selenium.support.ui.UnexpectedTagNameException when element is not a SELECT
 *
 * If multiple elements match the locator, the first is returned.
 */
fun SearchContext.withOption(loc: Locator, actions: SelectOptions.() -> Unit = {}) =
    SelectOptions(element(loc)).apply(actions)