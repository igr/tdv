plugins {
    id("dev.oblac.tdv.kotlin-library-conventions")
}

dependencies {
    api(project(":domain"))
    api(project(":analyzer"))
    implementation("io.pebbletemplates:pebble:3.2.2")
    implementation("com.github.ProjectMapK:jackson-module-kogera:2.16.1-beta11")
    implementation("com.github.ajalt.mordant:mordant:2.3.0")
}
