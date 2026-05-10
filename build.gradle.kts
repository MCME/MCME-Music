plugins {
    java
    id("xyz.jpenilla.run-paper") version "3.0.2"
    id("com.diffplug.spotless") version "8.4.0"
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://maven.enginehub.org/repo/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldedit:worldedit-bukkit:7.4.0")
    compileOnly("com.sk89q.worldedit:worldedit-core:7.4.0")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.13") {
        isTransitive = false
    }
    compileOnly("com.sk89q.worldguard:worldguard-core:7.0.13") {
        isTransitive = false
    }
    // compileOnly() -> for server-provided libs (Paper bundles, depended plugins like WorldEdit)
    // implementation() -> only when the class isn't on the server's runtime classpath at all. In that case you
    //                     MUST also shade + relocate to get an uber jar and avoid conflicts.
}

group = "com.mcmiddleearth"
version = "1.3"
description = "MCME-Music"

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}

tasks {
    runServer {
        minecraftVersion("1.21.4")
    }

    processResources {
        val props = mapOf("version" to version, "name" to project.name)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}

spotless {
    java {
        palantirJavaFormat()
    }
}