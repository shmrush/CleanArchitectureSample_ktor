package config

import com.natpryce.konfig.PropertyGroup
import com.natpryce.konfig.getValue
import com.natpryce.konfig.stringType

object database : PropertyGroup() {
    val url by stringType
    val driver by stringType
    val user by stringType
    val password by stringType
}