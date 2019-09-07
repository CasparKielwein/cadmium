package test

import cadmium.Locator
import cadmium.SearchContext
import cadmium.WebElement

/**
 * Check functions to assist testing with cadmium
 * these are written to make use with testing frameworks like Junit
 * and assert libraries like assertk as simple as possible
 *
 * These utils do not carry dependencies on frameworks themselves.
 * If asserts are used themselves, they are the general Kotlin assert
 */

/**
 * @param loc mechanism used to search for elements
 * @return true if page has an element found with given locator, false if not
 */
fun SearchContext.hasElement(loc: Locator) = this.elements(loc).isNotEmpty()

/**
 * @param attribute Name of the attribute to search for
 * @return true if the attribute exists for the Element, false if not
 */
fun WebElement.has(attribute: String) = this.getAttribute(attribute) != null
