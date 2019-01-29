
buildscript {
    repositories {
        jcenter()
    }

    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.20")
        classpath("org.jooq:jooq-codegen:3.7.1")
        classpath("mysql:mysql-connector-java:5.1.35")
    }
}

subprojects {
    group = "org.swordess.somekotlin"
    version = "1.0-SNAPSHOT"

    val kotlinVersion: String by project
    val junitVersion: String by project

    apply(plugin = "kotlin")

    repositories {
        jcenter()
    }
    dependencies {
        "implementation"("org.jetbrains.kotlin:kotlin-stdlib:$kotlinVersion")
        "testImplementation"("junit:junit:$junitVersion")
    }
}
