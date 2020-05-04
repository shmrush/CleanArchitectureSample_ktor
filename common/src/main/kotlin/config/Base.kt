package config

import com.natpryce.konfig.ConfigurationProperties

val config = ConfigurationProperties.fromResource(
    "application.properties"
)
