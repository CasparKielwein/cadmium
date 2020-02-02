package cadmium.util

import org.openqa.selenium.Keys

/**
 * Contains abstractions for platform specific code.
 */

/**
 * Returns the appropriate modifier key depending on used OS.
 *
 * Linux, Windows -> CONTROL
 * MAC -> COMMAND
 */
fun modifierKey(): Keys {
    val os = System.getProperty("os.name")
    return if (os == null || os.contains("mac", ignoreCase = true)) Keys.META else Keys.CONTROL
}