package com.example.resolver

import com.google.gson.*

object Constants {
    const val MODULE_KEY_DEFINITION = "definition"
    const val KEY_REF = "\$ref"
}

class JsonGenerator {
    private lateinit var inputJsonObject: JsonObject
    fun convertToJson(input: String): JsonObject {
        try {
            inputJsonObject = JsonParser.parseString(input) as JsonObject
            return parseJsonObject(inputJsonObject) as JsonObject
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return JsonObject()
    }

    private fun <T> parseJsonObject(jsonObj: JsonObject): T {
        val outputJsonObject = JsonObject()
        jsonObj.entrySet().forEach {
            val key = it.key as String
            when {
                jsonObj[key] is JsonObject -> {
                    if (key != Constants.MODULE_KEY_DEFINITION) {
                        outputJsonObject.add(
                            key,
                            parseJsonObject(jsonObj.getAsJsonObject(key))
                        )
                    }
                }
                jsonObj[key] is JsonArray -> {
                    val array = jsonObj.getAsJsonArray(key)
                    val outputArray = JsonArray()
                    array.forEach { item ->
                        outputArray.add(parseJsonObject(item as JsonObject) as JsonObject)
                    }
                    outputJsonObject.add(key, outputArray as JsonElement)
                }
                jsonObj[key] is JsonPrimitive -> {
                    if (key == Constants.KEY_REF) {
                        val value = jsonObj.getAsJsonPrimitive(key).toString()
                        val newValue = value.replace("#/", "").replace("/", ".").replace("\"", "")
                        return inputJsonObject.find(newValue) ?: JsonObject() as T
                    } else {
                        outputJsonObject.add(key, jsonObj.get(key))
                    }
                }
            }
        }
        return outputJsonObject as T
    }

    fun getCompiledConfig(catalystString: String) = convertToJson(catalystString).toString()
}
