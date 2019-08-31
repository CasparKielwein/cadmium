package cadmium

/**
 * Represents an Alert triggered by a page
 */
class Alert(private val alert: org.openqa.selenium.Alert) {
    /**
     * Accept the Alert dialog
     */
    fun accept() = alert.accept()

    /**
     * Dismiss the Alert dialog
     */
    fun dismiss() = alert.dismiss()

    /**
     * Text of alert dialog
     */
    val text: String
        get() {
            return alert.text;
        }
}