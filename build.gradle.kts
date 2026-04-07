import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "2.3.20"
    kotlin("plugin.spring") version "2.3.20"
    `maven-publish`
}

group = "com.binance.api"
version = "1.0.0"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
    withSourcesJar()
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework:spring-web:7.0.6")
    implementation("org.springframework:spring-websocket:7.0.6")
    implementation("org.glassfish.tyrus.bundles:tyrus-standalone-client:2.2.2")
    implementation("tools.jackson.core:jackson-databind:3.1.1")
    implementation("tools.jackson.module:jackson-module-kotlin")
    implementation("org.apache.commons:commons-lang3:3.20.0")
    implementation("commons-codec:commons-codec:1.21.0")
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlin:kotlin-reflect:2.3.20")
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:6.0.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:6.0.3")
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

