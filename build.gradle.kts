plugins {
	java
	id("org.springframework.boot") version "3.1.4"
	id("io.spring.dependency-management") version "1.1.3"
	id("org.openapi.generator") version "7.0.1"
}

group = "com.junglesoftware"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	mavenLocal()
	maven { url = uri("https://repo.spring.io/milestone") }
	maven { url = uri("https://repo.spring.io/snapshot") }
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.projectlombok:lombok:1.18.26")
	implementation("io.swagger.core.v3:swagger-core:2.2.8")
	implementation("io.swagger.parser.v3:swagger-parser:2.1.14")
	implementation("io.swagger.core.v3:swagger-annotations:2.2.16")
	implementation("io.swagger.core.v3:swagger-core-jakarta:2.2.16")
	implementation("org.springdoc:springdoc-openapi-ui:1.7.0")
	implementation("org.openapitools:jackson-databind-nullable:0.2.6")
	implementation("com.fasterxml.jackson.core:jackson-databind:2.15.2")
	implementation("javax.validation:validation-api:2.0.1.Final")
	implementation("javax.annotation:javax.annotation-api:1.3.2")
	implementation("org.apache.commons:org.apache.commons.lang:3.2.0")

	compileOnly("jakarta.servlet:jakarta.servlet-api:6.0.0")
	compileOnly("org.projectlombok:lombok")
	compileOnly("javax.servlet:javax.servlet-api:4.0.1")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")

}

tasks.withType<Test> {
	useJUnitPlatform()
}

tasks.openApiGenerate() {
	generatorName.set("spring")
	inputSpec.set("$projectDir/src/main/specifications/query-marketplace.yaml")
	outputDir.set("$buildDir/generated")
	configFile.set("$projectDir/src/main/specifications/query-marketplace-config.json")
	//templateDir.set("$projectDir/src/main/specifications/templates")
}

tasks.compileJava {
	dependsOn(tasks.clean)
	dependsOn(tasks.openApiGenerate)
}

sourceSets {
	main {
		java {
			srcDir("$buildDir/generated/src/main/java")
		}
	}
}