plugins {
    kotlin("jvm") version "2.0.21"
    application
    id("io.github.reyerizo.gradle.jcstress") version "0.9.0"
    id("me.champeau.jmh") version "0.7.3"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))

    jmh("org.openjdk.jmh:jmh-core:1.37")
    jmhAnnotationProcessor("org.openjdk.jmh:jmh-generator-annprocess:1.37")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("ru.itmo.siaod.lab4.AppKt")
}
