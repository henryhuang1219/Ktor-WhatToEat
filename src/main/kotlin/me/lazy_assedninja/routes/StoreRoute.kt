package me.lazy_assedninja.routes

import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import me.lazy_assedninja.dto.Comment
import me.lazy_assedninja.dto.Post
import me.lazy_assedninja.dto.Response
import me.lazy_assedninja.dto.request.CommentRequest
import me.lazy_assedninja.dto.request.PostRequest
import me.lazy_assedninja.dto.request.StoreRequest
import me.lazy_assedninja.repository.CommentRepository
import me.lazy_assedninja.repository.PostRepository
import me.lazy_assedninja.repository.StoreRepository

@Suppress("SENSELESS_COMPARISON")
fun Route.storeRoute(
    storeRepository: StoreRepository = StoreRepository(),
    commentRepository: CommentRepository = CommentRepository(),
    postRepository: PostRepository = PostRepository()
) {
    route("/Store") {
        post("GetStoreList") {
            val data = call.receive<StoreRequest>()
            val userID = data.userID
            val tagID = data.tagID
            if (userID != null && tagID != null) {
                val stores = storeRepository.getAll(userID, tagID)
                call.respond(Response(result = 1, body = stores))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }

        post("Search") {
            val data = call.receive<StoreRequest>()
            val userID = data.userID
            val keyword = data.keyword
            if (userID != null && keyword != null) {
                val stores = storeRepository.search(userID, "%${keyword}%")
                call.respond(Response(result = 1, body = stores))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }

        post("CreateComment") {
            val data = call.receive<Comment>()
            commentRepository.insert(data)
            call.respond(mapOf("result" to "1"))
        }

        post("GetCommentList") {
            val data = call.receive<CommentRequest>()
            val storeID = data.storeID
            if (storeID != null) {
                val comments = commentRepository.getAll(storeID)
                call.respond(Response(result = 1, body = comments))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }

        post("CreatePost") {
            val data = call.receive<Post>()
            postRepository.insert(data)
            call.respond(mapOf("result" to "1"))
        }

        post("GetPostList") {
            val data = call.receive<PostRequest>()
            val storeID = data.storeID
            if (storeID != null) {
                val posts = postRepository.getAll(storeID)
                call.respond(Response(result = 1, body = posts))
            } else {
                call.respond(mapOf("result" to "0", "errorMessage" to "Data can't be empty."))
            }
        }
    }
}