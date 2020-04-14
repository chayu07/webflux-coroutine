import io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.springframework.boot.gradle.tasks.bundling.BootJar

val resilience4jVersion = "1.1.0"
val springKafkaVersion = "2.3.1.RELEASE"

val javaxServletApiVersion = "3.1.0"
val reactorAddonVersion = "3.2.0.RELEASE"
val redissonVersion = "3.6.5"
val modelMapperVersion = "0.7.5"

val fstVersion = "2.55"
val snappyVersion = "1.1.7.1"

val httpclientVersion = "4.3.4"

val micrometerVersion="1.0.6"
val logbackVersion="8.0.3"

val groovyVersion="2.4.13"
val spockVersion="1.1-groovy-2.4"
val mockitoKotlinVersion="1.5.0"
val embeddedRedisVersion="0.6"
val reactorKotlinExVersion="1.0.0.M1"

plugins {
    val kotlinVersion = "1.3.50"
    val springbootVersion = "2.2.0.RELEASE"
    idea
    java
    jacoco
    groovy
    kotlin("plugin.jpa") version kotlinVersion
    kotlin("jvm") version kotlinVersion
    kotlin("plugin.spring") version kotlinVersion
    kotlin("kapt") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.allopen") version kotlinVersion
    id("org.jetbrains.kotlin.plugin.noarg") version kotlinVersion

    id("org.springframework.boot") version springbootVersion
    id("io.spring.dependency-management") version "1.0.7.RELEASE"

}

group = "com.victor.example.webfluxcoroutine"
version = "0.0.1-SNAPSHOT"

java.sourceCompatibility = JavaVersion.VERSION_1_8


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

tasks.named<BootJar>("bootJar") {
    archiveBaseName.set("webflux-coroutine")
}

tasks.named<Jar>("jar") {
    manifest {
        attributes["Main-Class"] = "com.victor.example.webfluxcoroutine.Application"
    }
}

repositories {
    maven(url = "http://repo.spring.io/milestone")
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-webflux")

    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions:$reactorKotlinExVersion")

    // resilience4j []
    implementation("io.github.resilience4j:resilience4j-all:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-reactor:$resilience4jVersion")
    implementation("io.github.resilience4j:resilience4j-micrometer:$resilience4jVersion")
    // resilience4j []

    implementation("io.micrometer:micrometer-registry-prometheus:$micrometerVersion")

    implementation("org.springframework.kafka:spring-kafka:$springKafkaVersion")

    implementation("io.projectreactor.addons:reactor-extra:$reactorAddonVersion")

    implementation("org.redisson:redisson:$redissonVersion")
    implementation("de.ruedigermoeller:fst:$fstVersion")
    implementation("org.xerial.snappy:snappy-java:$snappyVersion")
    implementation("com.getsentry.raven:raven-logback:$logbackVersion")
    implementation("org.modelmapper:modelmapper:$modelMapperVersion")
    implementation("org.apache.httpcomponents:httpclient:$httpclientVersion")
    implementation("com.google.guava:guava:23.0")
    implementation("org.apache.commons:commons-lang3:3.9")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.restdocs:spring-restdocs-mockmvc")
    testImplementation("com.github.kstyrc:embedded-redis:$embeddedRedisVersion")
    testImplementation("javax.inject:javax.inject:1")
    // mandatory dependencies for using Spock[]
    testImplementation("org.codehaus.groovy:groovy-all:$groovyVersion")
    testImplementation("org.spockframework:spock-core:$spockVersion")

    // https://mvnrepository.com/artifact/com.nhaarman/mockito-kotlin
    testImplementation("com.nhaarman:mockito-kotlin:$mockitoKotlinVersion")
    // mandatory dependencies for using Spock[]
}

configure<DependencyManagementExtension> {
    resolutionStrategy {
        cacheChangingModulesFor(0, TimeUnit.SECONDS)
    }
}