plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("com.formdev:flatlaf:3.1.1")
    implementation("com.fazecast:jSerialComm:[2.0.0,3.0.0)")
}

tasks.register<Copy>("copyExternalToInstall") {
    from(layout.projectDirectory.dir("external"))
    include("**/*")
    into(layout.buildDirectory.dir("installFolder"))
}

tasks.register<Copy>("copyJarToInstall") {
    dependsOn(":jar")
    from(layout.buildDirectory.dir("libs"))
    include("${rootProject.name}-${version}.jar")
    into(layout.buildDirectory.dir("installFolder"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<Jar> {
    // Otherwise you'll get a "No main manifest attribute" error
    manifest {
        attributes["Main-Class"] = "org.example.Main"
    }

    // To avoid the duplicate handling strategy error
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // To add all the dependencies otherwise a "NoClassDefFoundError" error
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}