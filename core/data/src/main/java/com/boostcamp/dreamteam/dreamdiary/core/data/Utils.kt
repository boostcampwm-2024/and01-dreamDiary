package com.boostcamp.dreamteam.dreamdiary.core.data

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive

fun JsonElement.convertToFirebaseData(): Any? {
    return when (this) {
        is JsonArray -> {
            map { it.convertToFirebaseData() }
        }

        is JsonObject -> {
            toMap()
        }

        is JsonNull -> null

        is JsonPrimitive -> {
            if (this.isString) {
                content
            } else {
                content.toLong()
            }
        }

    }
}

private fun JsonObject.toMap(): Map<String, Any?> {
    return entries.associate { (key, value) ->
        key to value.convertToFirebaseData()
    }
}
