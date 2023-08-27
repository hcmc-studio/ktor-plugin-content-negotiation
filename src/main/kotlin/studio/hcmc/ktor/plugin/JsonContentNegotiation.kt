package studio.hcmc.ktor.plugin

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder

private val defaultJsonKey = AttributeKey<Json>("defaultJson")

val Application.defaultJson: Json get() = attributes[defaultJsonKey]

class JsonContentNegotiationConfiguration(
    var jsonBuilder: JsonBuilder.() -> Unit = {},
    var contentNegotiationConfiguration: ContentNegotiationConfig.() -> Unit = {}
)

val JsonContentNegotiation = createApplicationPlugin("JsonContentNegotiation", ::JsonContentNegotiationConfiguration) {
    val json = Json {
        pluginConfig.jsonBuilder(this)
    }

    application.attributes.put(defaultJsonKey, json)
    application.install(ContentNegotiation) {
        json(json)
        this@createApplicationPlugin.pluginConfig.contentNegotiationConfiguration(this)
    }
}