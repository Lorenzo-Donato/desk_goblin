plugins {
    id("java")
    id("application")
}

val gdxVersion = "1.13.1"

dependencies {
    implementation(project(":core"))
    implementation("com.badlogicgames.gdx:gdx-backend-lwjgl3:$gdxVersion")
    implementation("com.badlogicgames.gdx:gdx-platform:$gdxVersion:natives-desktop")
    implementation("com.badlogicgames.gdx:gdx-freetype-platform:$gdxVersion:natives-desktop")
}

application {
    mainClass.set("com.deskgoblin.desktop.DesktopLauncher")
}

tasks.named<JavaExec>("run") {
    workingDir = file("../assets")
}

tasks.register<Jar>("dist") {
    archiveBaseName.set("DeskGoblin")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "com.deskgoblin.desktop.DesktopLauncher"
    }
    from(sourceSets.main.get().output)
    from(fileTree("../assets"))
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
