package cadmium

import com.kizitonwose.time.*
import org.openqa.selenium.Alert
import org.openqa.selenium.By
import org.openqa.selenium.NoAlertPresentException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.ExpectedConditions
import org.openqa.selenium.support.ui.WebDriverWait


fun waitFor(time: Interval<TimeUnit>) {
    Thread.sleep(time.inMilliseconds.longValue)
}

fun waitFor(v: () -> Boolean) {
    TODO(v.toString())
}

