plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.2.1"
    id("org.jetbrains.grammarkit") version "2022.3.2.2"
}

group = "com.klass"
version = "1.0.0"

repositories {
    mavenCentral()
    intellijPlatform {
        defaultRepositories()
    }
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        bundledPlugin("com.intellij.java")
        instrumentationTools()
        pluginVerifier()
    }

    implementation("org.eclipse.collections:eclipse-collections-api:11.0.0")
    implementation("org.eclipse.collections:eclipse-collections:11.0.0")
}

intellijPlatform {
    pluginConfiguration {
        id = "com.klass"
        name = "Klass"
        version = project.version.toString()
        ideaVersion {
            sinceBuild = "243"
            untilBuild = provider { null }
        }
        vendor {
            name = "Klass"
        }
        description = """
            Klass language support for IntelliJ IDEA.
            Provides syntax highlighting, code completion, navigation, and refactoring for Klass files.
        """.trimIndent()
    }

    pluginVerification {
        ides {
            recommended()
        }
    }
}

grammarKit {
    jflexRelease = "1.9.1"
}

tasks {
    wrapper {
        gradleVersion = "8.10"
    }

    generateLexer {
        sourceFile = file("src/main/java/com/klass/intellij/lexer/KlassLexer.flex")
        targetOutputDir = file("src/main/gen/com/klass/intellij/lexer")
        purgeOldFiles = true
    }

    generateParser {
        sourceFile = file("src/main/java/com/klass/intellij/Klass.bnf")
        targetRootOutputDir = file("src/main/gen")
        pathToParser = "com/klass/intellij/parser/KlassParser.java"
        pathToPsiRoot = "com/klass/intellij/psi"
        purgeOldFiles = true
    }

    compileJava {
        dependsOn(generateLexer, generateParser)
    }
}

sourceSets {
    main {
        java {
            srcDirs("src/main/java", "src/main/gen")
        }
        resources {
            srcDirs("src/main/resources")
        }
    }
}
