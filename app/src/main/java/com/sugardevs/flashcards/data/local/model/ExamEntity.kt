package com.sugardevs.flashcards.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "exam",
    foreignKeys = [
        ForeignKey(
            entity = TopicEntity::class,
            parentColumns =  ["id"],
            childColumns = ["topicId"],
            onDelete = ForeignKey.CASCADE
            )
    ],
    indices = [Index("topicId")]
)
data class ExamEntity (
   @PrimaryKey(autoGenerate = true) val id: Int = 0,
   val topicId: String,
   val question: String,
   val options: List<String>,
   val answer: String,
   val createdAt: Long = System.currentTimeMillis()
)