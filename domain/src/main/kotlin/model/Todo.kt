package model

import org.joda.time.DateTime

data class Todo(
    val id: Int,
    val title: String,
    val description: String,
    val deadLineAt: DateTime,
    val createdAt: DateTime,
    val updatedAt: DateTime
)