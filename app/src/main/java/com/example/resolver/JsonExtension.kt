package com.example.resolver

import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonObject

fun <T> JsonElement.find(member: String): T? {
    try {
        val keys = member.split(".")
        for (i in 0..keys.size) {
            val key = keys[i]
            if (i == (keys.size - 1)) {
                if (this is JsonObject) {
                    return get(key) as T
                } else if (this is JsonArray) {
                    return get(0).asJsonObject.get(key) as T
                }
            } else {
                if (this is JsonObject) {
                    return get(key)?.find(member.substring(key.length + 1))
                } else if (this is JsonArray) {
                    return get(0).find(member)
                }
            }
        }
    } catch (e: Exception) {
        // Do nothing - As its optional
    }
    return null
}