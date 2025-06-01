package com.sugardevs.flashcards.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "cards",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns = ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("topicId")]
)
data class CardEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val topicId: String, // Foreign key to TopicEntity.id
    val content: String,
    val createdAt: Long = System.currentTimeMillis() // timestamp
)
