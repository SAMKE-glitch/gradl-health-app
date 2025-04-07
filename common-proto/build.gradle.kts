plugins {
    id("java")
    id("com.google.protobuf") version "0.9.4"
}

group = "com.codewiz"
version = "0.0.1-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.protobuf:protobuf-java:3.21.1")
    implementation("io.grpc:grpc-protobuf:1.46.0")
    implementation("io.grpc:grpc-stub:1.46.0")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.21.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.46.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.plugins {
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
