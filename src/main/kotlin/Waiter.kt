import com.kizitonwose.time.*
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait



fun waitFor(time : Interval<TimeUnit>) { Thread.sleep(time.inMilliseconds.longValue) }

fun Browser.waitFor(p : PageLoad) { p.wait(driver)}

fun waitFor(v : () -> Boolean) { TODO(v.toString())}

/**
 * https://blog.codeship.com/get-selenium-to-wait-for-page-load/
 */
class PageLoad(private val timeOut: Interval<TimeUnit> = 10.seconds) {
    fun wait(drv: WebDriver) {
        val oldPage = drv.findElement(By.tagName("html"))
        WebDriverWait(drv, timeOut.longValue).until(ExpectedConditions.stalenessOf(oldPage))
    }
}

fun waitForAlert(driver: WebDriver, timeOut: Interval<TimeUnit>): Alert {
    assert(timeOut.inSeconds.longValue > 0)
    for (i in 0..timeOut.inSeconds.longValue) try {
        return driver.switchTo().alert()
    } catch (e: NoAlertPresentException) {
        Thread.sleep(1000)
    }
    throw NoAlertPresentException()
}
