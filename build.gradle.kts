import io.gitlab.arturbosch.detekt.Detekt
import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    idea
    java
    jacoco
    groovy
    kotlin("plugin.jpa") version Versions.Plugins.kotlin
    kotlin("jvm") version Versions.Plugins.kotlin
    kotlin("plugin.spring") version Versions.Plugins.kotlin
    kotlin("kapt") version Versions.Plugins.kotlin
    id("org.jetbrains.kotlin.plugin.allopen") version Versions.Plugins.kotlin
    id("org.jetbrains.kotlin.plugin.noarg") version Versions.Plugins.kotlin
    id("org.springframework.boot") version Versions.Plugins.springboot
    id("io.spring.dependency-management") version Versions.Plugins.dependencyManagement
    id("io.gitlab.arturbosch.detekt").version(Versions.Plugins.detekt)
}

group = "${Release.group}${Release.name}"
version = Release.version

java.sourceCompatibility = JavaVersion.VERSION_1_8


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set(Release.name)
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.victor.example.webfluxcoroutine.Application"
    }
}

tasks {
    withType<Detekt> {
        this.jvmTarget = "1.8"
    }
}

detekt {
    failFast = true // fail build on any finding
    buildUponDefaultConfig = true // preconfigure defaults
    config = files("$projectDir/buildSrc/config/detekt.yml") // point to your custom config defining rules to run, overwriting default behavior
//    baseline = file("$projectDir/config/baseline.xml") // a way of suppressing issues before introducing detekt

    reports {
        html.enabled = false // observe findings in your browser with structure and code snippets
        xml.enabled = true // checkstyle like format mainly for integrations like Jenkins
        txt.enabled = true // similar to the console output, contains issue signature to manually edit baseline files
    }
}

repositories {
    maven(url = "https://repo.spring.io/release")
    mavenCentral()
    jcenter()
}

dependencies {
    implementation(Dependencies.Spring.devTools)
    implementation(Dependencies.Spring.thymeleaf)
    implementation(Dependencies.Spring.actuator)
    implementation(Dependencies.Spring.webflux)
    implementation(Dependencies.Spring.kafka)
    implementation(Dependencies.Spring.reactorAddOn)

    implementation(Dependencies.Kotlin.jackson)
    implementation(Dependencies.Kotlin.stdlibJdk8)
    implementation(Dependencies.Kotlin.reflect)
    implementation(Dependencies.Kotlin.coroutineCore)
    implementation(Dependencies.Kotlin.coroutineReactor)
    implementation(Dependencies.Kotlin.reactorExtension)

    implementation(Dependencies.Resilience4j.core)
    implementation(Dependencies.Resilience4j.reactor)
    implementation(Dependencies.Resilience4j.micrometer)

    implementation(Dependencies.micrometerPrometheus)
    implementation(Dependencies.redisson)
    implementation(Dependencies.fst)
    implementation(Dependencies.snappy)
    implementation(Dependencies.modelMapper)
    implementation(Dependencies.httpClient)
    implementation(Dependencies.guava)
    implementation(Dependencies.commonLang3)

    testImplementation(TestDependencies.Spring.test)
    testImplementation(TestDependencies.Spring.restdocMockMvc)
    testImplementation(TestDependencies.reactorTest)
    testImplementation(TestDependencies.embeddedRedis)
    testImplementation(TestDependencies.javaxInject)

    testImplementation(TestDependencies.Spock.groovy)
    testImplementation(TestDependencies.Spock.core)

    // https://mvnrepository.com/artifact/com.nhaarman/mockito-kotlin
    testImplementation(TestDependencies.mockitoKotlin)
}

configure<DependencyManagementExtension> {
    resolutionStrategy {
        cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}