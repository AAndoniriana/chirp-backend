plugins {
    id("chirp.kotlin-common")
    id("io.spring.dependency-management")
}

dependencies {
    "implementation"(libraries.findLibrary("kotlin-reflect").get())
    "implementation"(libraries.findLibrary("kotlin-stdlib").get())
    "implementation"(libraries.findLibrary("spring-boot-starter-web").get())

    "implementation"(libraries.findLibrary("spring-boot-starter-test").get())
    "implementation"(libraries.findLibrary("kotlin-test-junit5").get())
    "implementation"(libraries.findLibrary("junit-platform-launcher").get())
}