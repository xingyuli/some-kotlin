plugins {
    id("org.springframework.boot") version "2.1.1.RELEASE"
    id("io.spring.dependency-management") version "1.0.6.RELEASE"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")

    // jdbc via HikariCP
    implementation("org.springframework.boot:spring-boot-starter-jdbc")
    implementation("mysql:mysql-connector-java:5.1.35")

    // mybatis
    implementation("org.mybatis.spring.boot:mybatis-spring-boot-starter:2.0.0")
    implementation("com.github.pagehelper:pagehelper-spring-boot-starter:1.2.10")
    // implementation("org.mybatis.dynamic-sql:mybatis-dynamic-sql:1.1.0")
}
