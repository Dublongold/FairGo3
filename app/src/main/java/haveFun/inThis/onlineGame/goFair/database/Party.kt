package haveFun.inThis.onlineGame.goFair.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "parties")
data class Party (
    @PrimaryKey(autoGenerate = false) val partyNumber: Int,
    @ColumnInfo(defaultValue = "0") val unlocked: Int
)