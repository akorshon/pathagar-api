import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

group = "com.marufh"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_21

plugins {
	id("org.springframework.boot") version "3.2.2"
	id("io.spring.dependency-management") version "1.1.4"
	id ("jacoco")
	id ("org.sonarqube") version "4.2.1.3168"
	kotlin("jvm") version "1.7.22"
	kotlin("plugin.spring") version "1.7.22"
	kotlin("plugin.jpa") version "1.7.22"
}

sonar {
	properties {
		property("sonar.projectKey", "akorshon_pathagar-api")
		property("sonar.organization", "akorshon")
		property("sonar.host.url", "https://sonarcloud.io")
	}
}
repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-oauth2-resource-server")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.apache.pdfbox:pdfbox:2.0.28")
	implementation("org.apache.pdfbox:jbig2-imageio:3.0.2")
	implementation("com.github.jai-imageio:jai-imageio-core:1.4.0")
	implementation("com.github.jai-imageio:jai-imageio-jpeg2000:1.4.0")

	runtimeOnly("com.mysql:mysql-connector-j")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.security:spring-security-test")
	testImplementation("org.testcontainers:junit-jupiter:1.17.6")
	testImplementation("org.testcontainers:mysql:1.17.6")

	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
}


tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

tasks.bootRun {
	args("--spring.profiles.active=dev")
}

tasks.jacocoTestReport {
	reports {
		xml.required = true
	}
}