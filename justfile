# Use mise shim for all recipes
set shell := ["mise", "x", "--", "bash", "-euo", "pipefail", "-c"]

# `just --list --unsorted`
default:
    @just --list --unsorted

# Run build and auto-formatters
precommit: mise gradle-build

# `mise install`
mise:
    mise install --quiet
    mise current

# Run Gradle build
gradle-build:
    ./gradlew build

# Build the IntelliJ plugin
build-plugin:
    ./gradlew buildPlugin

# Run Gradle tests
test:
    ./gradlew test

# Clean build output
clean:
    ./gradlew clean

# Generate lexer and parser
generate:
    ./gradlew generateLexer generateParser

# Verify the IntelliJ plugin
verify:
    ./gradlew verifyPlugin

# Override this with a command called `woof` which notifies you in whatever ways you prefer.
# My `woof` command uses `echo`, `say`, and sends a Pushover notification.
echo_command := env('ECHO_COMMAND', "echo")
