package me.lazy_assedninja.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.lazy_assedninja.dto.Favorite
import me.lazy_assedninja.dto.Response
import me.lazy_assedninja.dto.request.FavoriteRequest
import me.lazy_assedninja.repository.FavoriteRepository

fun Route.favoriteRoute(favoriteRepository: FavoriteRepository = FavoriteRepository()) {
    route("/Favorite") {
        post("AddToFavorites") {
            val data = call.receive<Favorite>()
            favoriteRepository.insert(data)
            call.respond(mapOf("result" to "1"))
        }

        post("GetFavoriteList") {
            val data = call.receive<FavoriteRequest>()
            val userID = data.userID
            if (userID != null) {
                val stores = favoriteRepository.getAll(userID)
                call.respond(Response(result = 1, body = stores))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }

        post("DeleteFavorite") {
            val data = call.receive<FavoriteRequest>()
            val storeID = data.storeID
            val userID = data.userID
            if (storeID != null && userID != null) {
                favoriteRepository.delete(storeID, userID)
                call.respond(mapOf("result" to "1"))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }
    }
}