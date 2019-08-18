package cadmium

import org.openqa.selenium.By

/**
 * Provides Hooks for Callbacks which are executed on browser actions.
 *
 * These can be used to add debug output or slow actions down to make them more observable.
 * @property onClick executed before every click action
 * @property onEnter executed before entering text
 */
data class InteractionHooks(
    val onClick: (locator: By) -> Unit = {},
    val onEnter: (locator: By, text: CharSequence) -> Unit = { _: By, _: CharSequence -> }
)

/**
 * Prints used WebWelement on every action
 */
fun talky(): InteractionHooks {
    return InteractionHooks(onClick = {
        println("Will click on $it")
    }, onEnter = { locator: By, text: CharSequence ->
        println("Will enter '$text' into $locator")
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

