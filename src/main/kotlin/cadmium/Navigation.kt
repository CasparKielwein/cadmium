package cadmium

/**
 * Allows navigation between pages.
 *
 * A thin wrapper around selenium.Navigation.
 * Not all functions are supported.
 * The refresh() function is found in class Page.
 *
 * @see org.openqa.selenium.WebDriver.Navigation
 */
class Navigation(private val browser: Browser) {

    /**
     * Move back a single "item" in the browser's history.
     *
     * @return a Page opened at that point in the history.
     */
    fun back() : Page {
        browser.driver.navigate().back()
        return Page(browser)
    }

    /**
     * Move back a single "item" in the browser's history.
     *
     * @return a Page opened at that point in the history.
     */
    fun forward() : Page {
        browser.driver.navigate().forward()
        return Page(browser)
    }
}