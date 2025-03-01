/*
 * This file was generated by the Gradle 'init' task.
 *
 * This generated file contains a sample Java application project to get you started.
 * For more details on building Java & JVM projects, please refer to https://docs.gradle.org/8.8/userguide/building_java_projects.html in the Gradle documentation.
 */

plugins {
    // Apply the application plugin to add support for building a CLI application in Java.
    application
}

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

dependencies {
    // Use JUnit Jupiter for testing.
    testImplementation(libs.junit.jupiter)

    testRuntimeOnly("org.junit.platform:junit-platform-launcher")

    // This dependency is used by the application.
    implementation(libs.guava)
    
    // JavaFX dependencies for jpackage
    implementation("org.openjfx:javafx-base:23.0.2")
    implementation("org.openjfx:javafx-controls:23.0.2")
    implementation("org.openjfx:javafx-fxml:23.0.2")
    implementation("org.openjfx:javafx-graphics:23.0.2")
}

// Apply a specific Java toolchain to ease working on different environments.
java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

application {
    // Define the main class for the application.
    mainClass = "com.camt.dii.secure.App"
}

// Create a "fat JAR" with all dependencies included
tasks.register<Jar>("shadowJar") {
    archiveClassifier.set("uber")
    manifest {
        attributes["Main-Class"] = "com.camt.dii.secure.App"
        attributes["Implementation-Version"] = "23.0.2"
    }
    
    // Include compiled classes and resources
    from(sourceSets.main.get().output)
    
    // Include the contents of all runtime dependencies
    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
    
    // Ensure META-INF/MANIFEST.MF has the main class set
    manifest {
        attributes["Main-Class"] = "com.camt.dii.secure.App"
    }
    
    // Avoid duplicate files in the JAR
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

tasks.named<Test>("test") {
    // Use JUnit Platform for unit tests.
    useJUnitPlatform()
}

// Configure Javadoc generation
tasks.javadoc {
    options {
        // Set Javadoc options
        this as StandardJavadocDocletOptions
        
        // Output format settings
        windowTitle = "Access Control System API Documentation"
        header = "Access Control System"
        docTitle = "Access Control System API Documentation"
        
        // Set overview content
        overview = "src/main/javadoc/overview.html"
        
        // Enable additional features
        use(true)                 // Create class and package usage pages
        version(true)             // Include @version paragraphs
        author(true)              // Include @author paragraphs
        splitIndex(true)          // Split index into one file per letter
        linkSource(true)          // Link to source code
        
        // Group packages
        group("Core API", "com.camt.dii.secure")
        group("Access Control", "com.camt.dii.secure.access*")
        group("Card Management", "com.camt.dii.secure.card*")
        group("Audit System", "com.camt.dii.secure.audit*")
        group("Services", "com.camt.dii.secure.service*")
        
        // Add custom tags for design patterns
        addStringOption("tag", "pattern:a:Design Pattern:")
        addStringOption("tag", "principle:a:OOP Principle:")
    }
    
    // Include all source files
    source = sourceSets.main.get().allJava
    
    // Disable failing on error with missing documentation
    // (Remove this line if you want stricter documentation checking)
    (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
}

// Task to create a ZIP file containing Javadoc
tasks.register<Zip>("javadocZip") {
    dependsOn(tasks.javadoc)
    archiveBaseName.set("access-control-system")
    archiveClassifier.set("javadoc")
    from(tasks.javadoc.get().destinationDir)
}

// Task to create a standalone documentation site with Javadoc and additional resources
tasks.register("generateDocumentation") {
    description = "Generates complete documentation for the Access Control System"
    group = "documentation"
    
    dependsOn(tasks.javadoc, "javadocZip")
    
    doLast {
        println("Documentation generated successfully!")
        println("Javadoc located at: ${tasks.javadoc.get().destinationDir}")
        println("Javadoc ZIP created at: ${(tasks.named("javadocZip").get() as Zip).archiveFile.get().asFile}")
    }
}
