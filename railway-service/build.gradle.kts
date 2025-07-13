plugins {
    id("java")
}

group = "it.trenical"
version = "1.0-SNAPSHOT"

dependencies {
    implementation(project(":proto"))
    implementation(project(":common-backend"))

    implementation("com.h2database:h2:2.2.224")

    implementation("io.grpc:grpc-netty-shaded:1.64.0")
    implementation("io.grpc:grpc-stub:1.64.0")
    implementation("io.grpc:grpc-protobuf:1.64.0")

    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}