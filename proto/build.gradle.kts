import com.google.protobuf.gradle.id

plugins {
    id("java")
    id("com.google.protobuf") version("0.9.5")
}

group = "it.trenical"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.grpc:grpc-protobuf:1.64.0")
    implementation("io.grpc:grpc-stub:1.64.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.64.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

sourceSets {
    main {
        proto {
            srcDir("src/main/proto")
        }
    }
}

tasks {
    withType(Copy::class.java){
        filesMatching("**/*.proto") {
            duplicatesStrategy = DuplicatesStrategy.INCLUDE
        }
    }
}