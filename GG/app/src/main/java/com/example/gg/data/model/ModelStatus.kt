package com.example.gg.data.model

enum class ModelStatus {
    CREATED,
    UPDATED,
    DELETED,
    NO_CHANGE;

    companion object {
        private val map = ModelStatus.values().associateBy(ModelStatus::ordinal)
        fun fromInt(type: Int) = map[type]
    }
}