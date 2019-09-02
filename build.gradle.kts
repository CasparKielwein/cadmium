import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    java
    kotlin("jvm") version "1.3.50"
    id ("org.jetbrains.dokka").version("0.9.18")
}

version = "0.1"

repositories {
    mavenCentral()
    jcenter()
    maven ( url = "https://jitpack.io" )
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntime("org.junit.jupiter:junit-jupiter-engine:5.4.2")

    //selenium plugin
    compile("org.seleniumhq.selenium:selenium-firefox-driver:3.141.59")
    compile("org.seleniumhq.selenium:selenium-server:3.141.59")
    compile("org.seleniumhq.selenium:selenium-java:3.141.59")

    compile("com.github.kizitonwose.time:time:1.0.3")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}
tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

tasks {
    test {
        useJUnitPlatform()
        maxHeapSize = "4g"
    }

    dokka {
        outputFormat = "html"
        outputDirectory = "$buildDir/javadoc"
        samples = listOf("src/test/kotlin/testBrowser.kt")
    }
}
