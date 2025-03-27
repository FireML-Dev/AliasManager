import net.minecrell.pluginyml.paper.PaperPluginDescription

plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.plugin.yml)
}

repositories {
    mavenCentral()
    gradlePluginPortal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/FireML/")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.commandapi)

    paperLibrary(libs.boostedyaml)
}

group = "uk.firedev"
version = "1.0-SNAPSHOT"
description = "A simple plugin that allows you to create command aliases"
java.sourceCompatibility = JavaVersion.VERSION_21

paper {
    name = project.name
    version = project.version.toString()
    main = "uk.firedev.aliasmanager.local.AliasManager"
    apiVersion = "1.21.4"
    author = "FireML"
    description = project.description.toString()

    loader = "uk.firedev.aliasmanager.local.LibraryLoader"
    generateLibrariesJson = true
}

publishing {
    repositories {
        maven {
            url = uri("https://repo.codemc.io/repository/FireML/")

            val mavenUsername = System.getenv("JENKINS_USERNAME")
            val mavenPassword = System.getenv("JENKINS_PASSWORD")

            if (mavenUsername != null && mavenPassword != null) {
                credentials {
                    username = mavenUsername
                    password = mavenPassword
                }
            }
        }
    }
    publications {
        create<MavenPublication>("maven") {
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()

            from(components["shadow"])
        }
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    shadowJar {
        archiveBaseName.set(project.name)
        archiveVersion.set(project.version.toString())
        archiveClassifier.set("")

        relocate("uk.firedev.daisylib.api", "uk.firedev.aliasmanager.libs.daisylib")
        relocate("dev.jorel.commandapi", "uk.firedev.aliasmanager.libs.commandapi")
    }
    withType<JavaCompile> {
        options.encoding = "UTF-8"
    }
}
