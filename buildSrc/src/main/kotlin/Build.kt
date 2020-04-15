object Release {
    const val group = "com.victor.example."
    const val name = "webfluxcoroutine"
    const val version = "0.0.1-SNAPSHOT"
}

object Versions {
    object Plugins {
        const val kotlin = "1.3.71"
        const val springboot = "2.2.0.RELEASE"
        const val dependencyManagement = "1.0.7.RELEASE"
        const val detekt = "1.7.4"
    }

    const val resilience4j = "1.1.0"
    const val springKafka = "2.3.1.RELEASE"

    const val reactorAddon = "3.2.0.RELEASE"
    const val reactorKotlinEx = "1.0.2.RELEASE"
    const val redisson = "3.6.5"
    const val modelMapper = "0.7.5"

    const val guava = "29.0-jre"
    const val commonsLang3 = "3.9"

    const val fst = "2.55"
    const val snappy = "1.1.7.1"

    const val httpClient = "4.3.4"

    const val micrometer = "1.0.6"

    const val groovy = "2.4.13"
    const val spock = "1.1-groovy-2.4"
    const val mockitoKotlin = "1.5.0"
    const val embeddedRedis = "0.6"
}

object TestDependencies {
    object Spring {
        const val test = "org.springframework.boot:spring-boot-starter-test"
        const val restdocMockMvc = "org.springframework.restdocs:spring-restdocs-mockmvc"
    }

    object Spock {
        const val groovy = "org.codehaus.groovy:groovy-all:${Versions.groovy}"
        const val core = "org.spockframework:spock-core:${Versions.spock}"
    }
    const val mockitoKotlin = "com.nhaarman:mockito-kotlin:${Versions.mockitoKotlin}"
    const val reactorTest = "io.projectreactor:reactor-test"
    const val embeddedRedis = ("com.github.kstyrc:embedded-redis:${Versions.embeddedRedis}")
    const val javaxInject = "javax.inject:javax.inject:1"
}

object Dependencies {
    object Spring {
        const val devTools = "org.springframework.boot:spring-boot-devtools:${Versions.Plugins.springboot}"
        const val actuator = "org.springframework.boot:spring-boot-starter-actuator:${Versions.Plugins.springboot}"
        const val webflux = "org.springframework.boot:spring-boot-starter-webflux:${Versions.Plugins.springboot}"
        const val kafka = "org.springframework.kafka:spring-kafka:${Versions.springKafka}"
        const val thymeleaf = "org.springframework.boot:spring-boot-starter-thymeleaf:${Versions.Plugins.springboot}"
        const val reactorAddOn = "io.projectreactor.addons:reactor-extra:${Versions.reactorAddon}"
    }

    object Kotlin {
        const val jackson = "com.fasterxml.jackson.module:jackson-module-kotlin"
        const val stdlibJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8"
        const val reflect = "org.jetbrains.kotlin:kotlin-reflect"
        const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core"
        const val coroutineReactor = "org.jetbrains.kotlinx:kotlinx-coroutines-reactor"
        const val reactorExtension = "io.projectreactor.kotlin:reactor-kotlin-extensions:${Versions.reactorKotlinEx}"
    }

    object Resilience4j {
        const val core = "io.github.resilience4j:resilience4j-all:${Versions.resilience4j}"
        const val reactor = "io.github.resilience4j:resilience4j-reactor:${Versions.resilience4j}"
        const val micrometer = "io.github.resilience4j:resilience4j-micrometer:${Versions.resilience4j}"
    }


    const val micrometerPrometheus = "io.micrometer:micrometer-registry-prometheus:${Versions.micrometer}"
    const val redisson = "org.redisson:redisson:${Versions.redisson}"
    const val fst = "de.ruedigermoeller:fst:${Versions.fst}"
    const val snappy = "org.xerial.snappy:snappy-java:${Versions.snappy}"
    const val modelMapper = "org.modelmapper:modelmapper:${Versions.modelMapper}"
    const val httpClient = "org.apache.httpcomponents:httpclient:${Versions.httpClient}"
    const val guava = "com.google.guava:guava:${Versions.guava}"
    const val commonLang3 = "org.apache.commons:commons-lang3:${Versions.commonsLang3}"
}