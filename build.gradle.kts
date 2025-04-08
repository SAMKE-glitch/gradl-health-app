plugins {
	id("java")
	id("com.google.protobuf") version "0.9.4"
}

group = "com.codewiz"
version = "0.0.1-SNAPSHOT"

// Define the versions at the top
val grpcVersion = "1.58.0"
val protobufVersion = "3.24.4"
val springBootVersion = "3.4.2" // Update this based on your Spring Boot version
val springDataJdbcVersion = "2.4.0" // Update based on the latest compatible version

java {
	toolchain {
		languageVersion.set(JavaLanguageVersion.of(21))
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// gRPC dependencies
	implementation("com.google.protobuf:protobuf-java:$protobufVersion")
	implementation("com.google.protobuf:protobuf-java-util:$protobufVersion")
	implementation("io.grpc:grpc-protobuf:$grpcVersion")
	implementation("io.grpc:grpc-stub:$grpcVersion")

	// Spring Data JDBC
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc:$springBootVersion")

	// PostgreSQL JDBC Driver
	runtimeOnly("org.postgresql:postgresql:42.6.0")

	// Lombok for reducing boilerplate code (optional)
	compileOnly("org.projectlombok:lombok:1.18.24")
	annotationProcessor("org.projectlombok:lombok:1.18.24")

	// Testing dependencies
	testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
	testImplementation("org.springframework.boot:spring-boot-starter-test:$springBootVersion")
}

protobuf {
	protoc {
		artifact = "com.google.protobuf:protoc:$protobufVersion"
	}
	plugins {
		create("grpc") {
			artifact = "io.grpc:protoc-gen-grpc-java:$grpcVersion"
		}
	}
	generateProtoTasks {
		ofSourceSet("main").forEach {
			it.plugins {
				create("grpc") {
					option("java_multiple_files=true")
				}
			}
		}
	}
}

tasks.test {
	useJUnitPlatform()
}
