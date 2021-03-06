package me.lazy_assedninja.po

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.jodatime.datetime

object Reports : IntIdTable(name = "report") {

    val content = text("content")
    val createTime = datetime("create_time")

    val storeID = integer("store_id")
        .references(Stores.id, onDelete = ReferenceOption.NO_ACTION).nullable()
    val userID = integer("user_id")
        .references(Users.id, onDelete = ReferenceOption.NO_ACTION)
}