package cadmium

import org.openqa.selenium.By

/**
 * Provides Hooks for Callbacks which are executed on browser actions.
 *
 * These can be used to add debug output or slow actions down to make them more observable.
 * @property onClick executed before every click action
 * @property onEnter executed before entering text
 * @property onFail executed if an action fails with an exception
 */
data class InteractionHooks(
    val onClick: (locator: By) -> Unit = {},
    val onEnter: (locator: By, text: CharSequence) -> Unit = { _: By, _: CharSequence -> },
    val onFail: (ex: Exception) -> Unit = {}
)

/**
 * Hooks that do nothing
 */
fun noHooks() = InteractionHooks()

/**
 * Prints used WebElement on every action
 */
fun talky(): InteractionHooks {
    return InteractionHooks(onClick = {
        println("Will click on $it")
    }, onEnter = { locator: By, text: CharSequence ->
        println("Will enter '$text' into $locator")
    }, onFail = { ex: Exception ->
        println("Action failed with error $ex")
    })
}

/**
 * Sleep for 250ms to slow actions down for a human observer
 */
fun slowDown(): InteractionHooks {
    return InteractionHooks(onClick = {
        Thread.sleep(250)
    }, onEnter = { _: By, _: CharSequence ->
        Thread.sleep(250)
    })
}

