package com.example.plugins

import com.example.models.*
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.server.http.content.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import io.ktor.server.util.*

fun Application.configureRouting() {


    routing {
        get("/") {
            call.respondRedirect("articles")
        }
        route("articles") {
            get {
                // Show a list of articles
            }
            get("new") {
                // Show a page with fields for creating a new article
                call.respond(FreeMarkerContent("new.ftl", model = null))
            }
            post {
                // Save an article
                val formParameters = call.receiveParameters()
                val title = formParameters.getOrFail("title")
                val body = formParameters.getOrFail("body")
                val newEntry = Article.newEntry(title, body)
                articles.add(newEntry)
                call.respondRedirect("/articles/${newEntry.id}")
            }
            get("{id}") {
                // Show an article with a specific id
                val id = call.parameters.getOrFail<Int>("id").toInt()
                call.respond(FreeMarkerContent("show.ftl", mapOf("article" to articles.find { it.id == id })))
            }
            get("{id}/edit") {
                // Show a page with fields for editing an article
            }
            post("{id}") {
                // Update or delete an article
            }
        }

             get {
                call.respond(FreeMarkerContent("index.ftl", mapOf("articles" to articles)))
            }

        // Static plugin. Try to access `/static/index.html`
        static("/static") {
            resources("files")
        }
    }
}
