package com.castmart.folio.api

import io.kotest.extensions.testcontainers.JdbcDatabaseContainerExtension
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.testcontainers.containers.PostgreSQLContainer
import javax.sql.DataSource

@Configuration
class TestContainerConfBean {
    @Bean
    fun testContainersPostgresExtension(): JdbcDatabaseContainerExtension {
        val dbContainer =
            PostgreSQLContainer<Nothing>("postgres:15.5-alpine").apply {
                withDatabaseName("folio")
                withInitScript("schema.sql")
                startupAttempts = 1
                withUrlParam("stringtype", "unspecified")
            }
        return JdbcDatabaseContainerExtension(dbContainer)
    }

    @Bean
    fun dataSource(extension: JdbcDatabaseContainerExtension): DataSource {
        return extension.mount {
            minimumIdle = 1
            maximumPoolSize = 2
        }
    }
}
