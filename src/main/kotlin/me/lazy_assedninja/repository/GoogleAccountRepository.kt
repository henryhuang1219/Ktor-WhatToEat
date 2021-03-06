package me.lazy_assedninja.repository

import me.lazy_assedninja.po.GoogleAccounts
import me.lazy_assedninja.po.Users
import me.lazy_assedninja.vo.GoogleAccount
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime

/**
 * Repository that handles GoogleAccount instance.
 */
class GoogleAccountRepository {

    fun bind(userID: Int, data: GoogleAccount) {
        transaction {
            val googleAccountID = GoogleAccounts.insertAndGetId {
                it[googleID] = data.googleID
                it[email] = data.email
                it[name] = data.name
                it[pictureURL] = data.pictureURL
                it[createTime] = DateTime.now()
                it[updateTime] = DateTime.now()
            }
            Users.update({ Users.id eq userID }) {
                it[Users.googleAccountID] = googleAccountID.value
            }
        }
    }

    suspend fun get(googleID: String): GoogleAccount? {
        return withContext(Dispatchers.IO) {
            transaction {
                GoogleAccounts.select { GoogleAccounts.googleID eq googleID }.firstOrNull()?.let {
                    toGoogleAccount(it)
                }
            }
        }
    }

    private fun toGoogleAccount(row: ResultRow): GoogleAccount = GoogleAccount(
        id = row[GoogleAccounts.id].value,
        googleID = row[GoogleAccounts.googleID],
        email = row[GoogleAccounts.email],
        name = row[GoogleAccounts.name],
        pictureURL = row[GoogleAccounts.pictureURL],
        createTime = row[GoogleAccounts.createTime].toString("yyyy-MM-dd HH:mm:ss"),
        updateTime = row[GoogleAccounts.updateTime].toString("yyyy-MM-dd HH:mm:ss")
    )
}