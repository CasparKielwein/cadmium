package cadmium

import org.openqa.selenium.WebDriver

/**
 * Switch to frame by index and execute action in it, then switch back to default main frame.
 *
 * @param index Index of frame in list of frames in current DOM.
 *
 * Selecting a frame by index is equivalent to the
 * JS expression window.frames[index] where "window" is the DOM window represented by the
 * current context
 */
fun Page.inFrame(index : Index, action: Page.() -> Unit) = actOnFrame({it.frame(index.index)}, action)

/**
 * Switch to frame by its WebElement and execute action in it, then switch back to default main frame.
 *
 * @param loc locator to retrieve WebElement identifying the frame
 */
fun Page.inFrame(loc: Locator, action: Page.() -> Unit) = actOnFrame({it.frame(element(loc).rawElement)}, action)

/**
 * Helper function to get frame and execute action on it.
 */
private inline fun Page.actOnFrame(frameGeter : (WebDriver.TargetLocator) -> Unit, action: Page.() -> Unit) {
    frameGeter(driver.switchTo())
    action()
    driver.switchTo().defaultContent()
}

