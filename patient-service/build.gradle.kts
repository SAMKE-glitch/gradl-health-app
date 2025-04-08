plugins {
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
    id("java")
}

group = "com.codewiz"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

repositories {
    mavenCentral()
}

val grpcVersion = "1.58.0"  // Define the gRPC version
val protobufVersion = "3.24.4"

dependencies {
    implementation(project(":common-proto"))

    // Spring Boot dependencies
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-data-jdbc")
    implementation("net.devh:grpc-server-spring-boot-starter:2.15.0.RELEASE")

    // Upgrade PostgreSQL JDBC driver to a compatible version
    implementation("org.postgresql:postgresql:42.6.0")
    // ✅ Add PostgreSQL Driver
    //runtimeOnly("org.postgresql:postgresql:42.3.6")

    // gRPC dependencies for the patient-service module
    implementation("io.grpc:grpc-stub:$grpcVersion")
    implementation("io.grpc:grpc-netty:$grpcVersion")
    implementation("com.google.protobuf:protobuf-java:$protobufVersion")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
