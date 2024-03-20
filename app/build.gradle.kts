import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("dev.oblac.tdv.kotlin-application-conventions")
    id("com.github.johnrengelman.shadow") version "8.1.1"
}

dependencies {
    implementation(project(":parser"))
    implementation(project(":reporter"))
    implementation(project(":analyzer"))
    implementation("com.github.ajalt.clikt:clikt:4.2.2")
    runtimeOnly("org.slf4j:slf4j-simple:2.0.12")
}

application {
    mainClass.set("dev.oblac.tdv.app.AppKt")
}

tasks {
    named<ShadowJar>("shadowJar") {
        archiveBaseName.set("tdv")
        archiveVersion.set("0.4.0")
    }
}
