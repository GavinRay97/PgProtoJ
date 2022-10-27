plugins {
    java
}

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    implementation("io.netty:netty5-all:5.0.0.Alpha2")

    compileOnly("io.soabase.record-builder:record-builder-core:33")
    annotationProcessor("io.soabase.record-builder:record-builder-processor:33")

    implementation("org.apache.logging.log4j:log4j-api:2.17.2")
    implementation("org.apache.logging.log4j:log4j-core:2.17.2")

    implementation("com.github.jsqlparser:jsqlparser:4.4")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
}

group = "com.github.gavinray97"
version = "1.0.0-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(19))
    }
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.compilerArgs.add("-parameters")
        options.compilerArgs.add("--enable-preview")
    }

    withType<JavaExec>().configureEach {
        systemProperty("file.encoding", "UTF-8")
        jvmArgs("--enable-preview")
    }

    withType<Test> {
        useJUnitPlatform()
        systemProperty("file.encoding", "UTF-8")
        jvmArgs("--enable-preview")
    }
}
