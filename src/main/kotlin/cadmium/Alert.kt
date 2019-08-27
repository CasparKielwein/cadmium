package cadmium

/**
 * Represents an Alert triggered by a page
 */
class Alert(private val browser: Browser) {
    /**
     * Accept the Alert dialog
     */
    fun accept() = browser.driver.switchTo().alert().accept()

    /**
     * Dismiss the Alert dialog
     */
    fun dismiss() = browser.driver.switchTo().alert().dismiss()

    /**
     * Text of alert dialog
     */
    val text: String
        get() {
            val currentWindow = browser.driver.windowHandle
            val alert = browser.driver.switchTo().alert()
            browser.driver.switchTo().window(currentWindow)
            return alert.text;
        }
}