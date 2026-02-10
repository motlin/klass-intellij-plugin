import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("org.jetbrains.intellij.platform") version "2.11.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.2"
    id("com.diffplug.spotless") version "8.2.1"
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
        languageVersion = JavaLanguageVersion.of(21)
    }
}

dependencies {
    intellijPlatform {
        intellijIdeaCommunity("2024.3")
        bundledPlugin("com.intellij.java")
        pluginVerifier()
        testFramework(TestFrameworkType.Platform)
    }

    implementation("org.eclipse.collections:eclipse-collections-api:13.0.0")
    implementation("org.eclipse.collections:eclipse-collections:13.0.0")

    testImplementation("junit:junit:4.13.2")
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
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
            ide(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3.5")
        }
    }
}

grammarKit {
    jflexRelease = "1.9.1"
}

spotless {
    java {
        target("src/main/java/**/*.java", "src/test/java/**/*.java")
        googleJavaFormat("1.25.2")
        formatAnnotations()
    }
}

tasks {
    wrapper {
        gradleVersion = "9.2.1"
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

    processResources {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
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
    test {
        java {
            srcDirs("src/test/java")
        }
        resources {
            srcDirs("src/test/testData")
        }
    }
}
