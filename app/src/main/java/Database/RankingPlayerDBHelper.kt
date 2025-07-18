package Database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.stuart.palma.cazarpatos.Player

class RankingPlayerDBHelper(context: Context)
    : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "CazarPatos.db"
        private const val TABLE_NAME = "TBL_RANKING"
        private const val COLUMN_NAME_PLAYER = "PLAYER_NAME"
        private const val COLUMN_NAME_DUCKS = "DUCKS_HUNTED"
        private const val SQL_CREATE_ENTRIES =
            "CREATE TABLE $TABLE_NAME (" +
                    "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                    "$COLUMN_NAME_PLAYER TEXT," +
                    "$COLUMN_NAME_DUCKS INTEGER)"
        private const val SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS $TABLE_NAME"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(SQL_CREATE_ENTRIES)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    fun insertRanking(player: Player) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_PLAYER, player.username)
            put(COLUMN_NAME_DUCKS, player.huntedDucks)
        }
        db.insert(TABLE_NAME, null, values)
        db.close()
    }

    fun readAllRanking(): ArrayList<Player> {
        val db = readableDatabase
        val projection = arrayOf(BaseColumns._ID, COLUMN_NAME_PLAYER, COLUMN_NAME_DUCKS)
        val sortOrder = "$COLUMN_NAME_DUCKS DESC"
        val cursor = db.query(TABLE_NAME, projection,
            null, null, null, null, sortOrder)
        val players = arrayListOf<Player>()
        while (cursor.moveToNext()) {
            val name = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_PLAYER))
            val ducks = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_DUCKS))
            players.add(Player(name, ducks))
        }
        cursor.close(); db.close()
        return players
    }

    fun readDucksHuntedByPlayer(playerName: String): Int {
        val db = readableDatabase
        val cursor = db.query(TABLE_NAME,
            arrayOf(COLUMN_NAME_DUCKS),
            "$COLUMN_NAME_PLAYER = ?",
            arrayOf(playerName), null, null, null)
        var hunted = 0
        if (cursor.moveToFirst()) {
            hunted = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_NAME_DUCKS))
        }
        cursor.close(); db.close()
        return hunted
    }
    fun updateRanking(player: Player) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_NAME_DUCKS, player.huntedDucks)
        }
        db.update(TABLE_NAME, values,
            "$COLUMN_NAME_PLAYER = ?", arrayOf(player.username))
        db.close()
    }
    fun deleteRanking(playerName: String) {
        val db = writableDatabase
        db.delete(TABLE_NAME,
            "$COLUMN_NAME_PLAYER = ?", arrayOf(playerName))
        db.close()
    }
    fun deleteAllRanking() {
        val db = writableDatabase
        db.delete(TABLE_NAME, null, null)
        db.close()
    }


}

