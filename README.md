# Cadmium
## Simple Browser Automation in Kotlin

Cadmium is a browser automation library based on Selenium.
It leverages the power of the Kotlin language to use of Selenium simple and concise

Cadmium can be used to to simplify testing but unlike selenide is not a test framework itself.
Cadmium can be used independently of test and/or assert libraries.

## Why not Selenium directly?
* Selenium-java is somewhat verbose as is often the case in java
* Calling a java library directly in Kotlin causes issues with null safety.
The wrappers provided by Cadmium make it explicit, when `null` is a possibility.

## Design Goals
* Concise and Readable, Automation and test code using Cadmium should be as concise and readable as possible
* Typesafe, use the typesystem to stop as many bugs as possible

Cadmium selenide has a beautiful red color.  
<https://en.wikipedia.org/wiki/Cadmium_selenide>

## Dependencies
* Selenium <https://www.seleniumhq.org/>
* For tests of Cadmium itself:
    * Junit5
    * Firefox Geckodriver <https://github.com/mozilla/geckodriver/>
    * chromedriver <https://sites.google.com/a/chromium.org/chromedriver/>