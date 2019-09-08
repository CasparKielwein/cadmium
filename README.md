# Cadmium
## Simple Browser Automation in Kotlin

[![Build Status](https://travis-ci.org/CasparKielwein/cadmium.svg?branch=master)](https://travis-ci.org/CasparKielwein/cadmium)

Cadmium is a browser automation library based on Selenium.
It leverages the power of the Kotlin language to use of Selenium simple and concise

Cadmium can be used to to simplify testing but unlike selenide is not a test framework itself.
Cadmium can be used independently of test and/or assert libraries.

## Why not Selenium directly?
* Selenium-java is somewhat verbose as is often the case in java
* Calling a java library directly in Kotlin causes issues with null safety.
The wrappers provided by Cadmium make it explicit, when `null` is a possibility.
* A nice and clean mini DSL

## Design Goals
* Concise and Readable, Automation and test code using Cadmium should be as concise and readable as possible
* Typesafe, use the typesystem to stop as many bugs as possible

## Cadmium DSL
Cadmium provides a very small Domain Specific Language to make writing browser automation code very simple.
The core classes of the API all provide methods which take extension functions on 
Cadmium classes as parameters and execute them. This allows users to search for or construct objects
and immediately execute their own code on them without extra indirections. 

```Kotlin
        //constructs object representing running browser
        val firefox = headlessFirefox()
        //get Page object pointing to wikipedia and use it
        firefox.browse(URL("https://en.wikipedia.org/wiki")) {
            //get element searchInput and interact with it
            element(Id("searchInput")) {
                enter("cheese")
                enter(Keys.ENTER)
            }

            waitForPageLoad()
            assertEquals("Cheese", element(Id("firstHeading")).text)
        }
```

## The name?
Cadmium selenide has a beautiful red color.  
<https://en.wikipedia.org/wiki/Cadmium_selenide>


## Dependencies
* Selenium <https://www.seleniumhq.org/>
* For tests of Cadmium itself:
    * Junit5
    * Firefox Geckodriver <https://github.com/mozilla/geckodriver/>
    * chromedriver <https://sites.google.com/a/chromium.org/chromedriver/>