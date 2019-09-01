package cadmium

/*


import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler
import org.openqa.selenium.OutputType

/**
 * Junit5 Extension, which takes a screenshot after failed tests.
 */
open class ScreenshotOnFailure : TestExecutionExceptionHandler {

    override fun handleTestExecutionException(context: ExtensionContext, throwable: Throwable) {

        if (throwable is IOException) {
            return
        }

        val screenshot = AppTest.selenium?.driver?.getScreenshotAs<File>(OutputType.FILE)
        val time = ZonedDateTime.now(ZoneOffset.UTC).format(DateTimeFormatter.ISO_INSTANT)
        if (screenshot != null)
            Files.copy(screenshot.toPath(), File("$time.${context.displayName}.png").toPath())

        throw throwable
    }
}

 */