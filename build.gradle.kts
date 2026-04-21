import net.ltgt.gradle.errorprone.errorprone
import org.jetbrains.intellij.platform.gradle.IntelliJPlatformType
import org.jetbrains.intellij.platform.gradle.TestFrameworkType

plugins {
    id("java")
    id("checkstyle")
    id("org.jetbrains.intellij.platform") version "2.14.0"
    id("org.jetbrains.grammarkit") version "2023.3.0.3"
    id("com.diffplug.spotless") version "8.4.0"
    id("net.ltgt.errorprone") version "5.1.0"
    id("org.openrewrite.rewrite") version "7.30.0"
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

    errorprone("com.google.errorprone:error_prone_core:2.36.0")

    rewrite("io.liftwizard:liftwizard-rewrite:2.1.43")
    rewrite("org.openrewrite.recipe:rewrite-static-analysis:2.32.0")
    rewrite("org.openrewrite.recipe:rewrite-migrate-java:3.35.0")
    rewrite("org.openrewrite.recipe:rewrite-testing-frameworks:3.35.0")
    rewrite("org.openrewrite.recipe:rewrite-logging-frameworks:3.27.0")
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
            create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3")
            create(IntelliJPlatformType.IntellijIdeaCommunity, "2024.3.5")
        }
    }
}

rewrite {
    activeRecipe(
        "io.liftwizard.staticanalysis.CommonStaticAnalysis",
        "io.liftwizard.staticanalysis.CodeCleanup",
        "io.liftwizard.rewrite.BestPractices",
        "io.liftwizard.testing.junit.JupiterBestPractices",
        "org.openrewrite.java.migrate.UpgradeToJava21",
    )
    activeStyle("io.liftwizard.NoStarImports")
    exclusion(
        "src/main/gen/**",
        "build.gradle.kts",
        "settings.gradle.kts",
    )
}

checkstyle {
    toolVersion = "10.23.1"
    configDirectory = file("config/checkstyle")
}

tasks.withType<Checkstyle> {
    exclude("**/gen/**")
}

grammarKit {
    jflexRelease = "1.9.1"
}

spotless {
    format("misc") {
        target("**/*.md", "**/*.yaml", "**/*.yml", "**/*.sh", "**/.gitignore")
        targetExclude("build/**", ".gradle/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
    java {
        target("src/main/java/**/*.java", "src/test/java/**/*.java")
        toggleOffOn("@formatter:off", "@formatter:on")
        importOrder("java", "javax", "", "\\#java|\\#javax", "\\#")
        removeUnusedImports()
        cleanthat()
        prettier(mapOf("prettier" to "3.3.2", "prettier-plugin-java" to "2.7.4"))
            .configFile(".prettierrc.json5")
            .config(mapOf("parser" to "java", "plugins" to listOf("prettier-plugin-java")))
        formatAnnotations()
        trimTrailingWhitespace()
        endWithNewline()
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.errorprone {
        // Suppressed checks (OFF)
        // EqualsGetClass:OFF due to https://github.com/google/error-prone/issues/1144
        disable("EqualsGetClass", "UnnecessaryParentheses")
        // Promoted checks (ERROR)
        error("UnusedNestedClass")
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

    checkstyleMain {
        source = fileTree("src/main/java")
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
