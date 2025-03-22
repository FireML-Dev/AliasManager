 rootProject.name = "AliasManager"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            // Paper API
            library("paper-api", "io.papermc.paper:paper-api:1.21.4-R0.1-SNAPSHOT")

            // compileOnly dependencies

            // implementation dependencies
            library("daisylib", "uk.firedev:DaisyLib-API:2.3.1-SNAPSHOT")
            library("commandapi", "dev.jorel:commandapi-bukkit-shade-mojang-mapped:9.7.0")

            // paperLibrary dependencies

            // Gradle plugins
            plugin("shadow", "com.gradleup.shadow").version("8.3.5")
            plugin("plugin-yml", "de.eldoria.plugin-yml.paper").version("0.7.1")
        }
    }
}
