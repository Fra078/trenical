import com.google.protobuf.gradle.id

plugins {
    kotlin("jvm") version "2.1.10"
    id("com.google.protobuf") version("0.9.5")
}

group = "it.trenical"
version = "1.0-SNAPSHOT"


dependencies {
    implementation("io.grpc:grpc-protobuf:1.64.0")
    implementation("io.grpc:grpc-stub:1.64.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")
    implementation("io.grpc:grpc-kotlin-stub:1.4.1")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")
    implementation("com.google.protobuf:protobuf-kotlin:3.25.3")
}


protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.3"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.64.0"
        }
        id("grpckt") {
            artifact = "io.grpc:protoc-gen-grpc-kotlin:1.4.1:jdk8@jar"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
                id("grpckt")
            }
            it.builtins {
                id("kotlin")
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

kotlin {
    jvmToolchain(21)
}