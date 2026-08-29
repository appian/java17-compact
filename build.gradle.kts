plugins {
    `java-library`
}

group = "com.appiancorp.jre17.compact"
description = "Library which contains Utilities for JRE17 compatibility"
version = "1.0.0"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java.srcDir("src/main/thirdparty")
    }
}

dependencies {
    testImplementation(libs.junit.jupiter)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

tasks.named<Jar>("jar") {
    archiveBaseName.set("com.appiancorp.jre17.compact")
}

tasks.named<Test>("test") {
    useJUnitPlatform()
}
