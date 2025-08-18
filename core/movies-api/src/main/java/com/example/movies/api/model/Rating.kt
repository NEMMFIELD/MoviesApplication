package com.example.movies.api.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.encodeStructure
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.double
import kotlinx.serialization.json.jsonPrimitive


// Для авторизации
data class RequestTokenResponse(
    val success: Boolean,
    val expires_at: String,
    val request_token: String
)

data class CreateSessionRequest(
    val request_token: String
)

data class CreateSessionResponse(
    val success: Boolean,
    val session_id: String
)

// Для рейтинга
data class RatingRequest(
    val value: Double
)


data class RatingResponse(
    val status_code: Int,
    val status_message: String
)

@Suppress("SERIALIZER_TYPE_INCOMPATIBLE")
@Serializable
data class RatingStatus(
    val id: Int,
    val favorite: Boolean,
    val watchlist: Boolean,
    @kotlinx.serialization.Serializable(with = RatedSerializer::class)
    val rated: Any?
)

@Serializable
data class Rated(
    val value: Double
)

object RatedSerializer : KSerializer<Rated?> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("Rated") {
            element<Double>("value")
        }

    override fun deserialize(decoder: Decoder): Rated? {
        val input = decoder as? JsonDecoder ?: error("Can be deserialized only by JSON")
        val element = input.decodeJsonElement()

        return when {
            element is JsonObject && "value" in element -> {
                val value = element["value"]!!.jsonPrimitive.double
                Rated(value)
            }
            element is JsonPrimitive && element.booleanOrNull == false -> null
            else -> null
        }
    }

    override fun serialize(encoder: Encoder, value: Rated?) {
        if (value != null) {
            encoder.encodeStructure(descriptor) {
                encodeDoubleElement(descriptor, 0, value.value)
            }
        } else {
            encoder.encodeNull()
        }
    }
}
