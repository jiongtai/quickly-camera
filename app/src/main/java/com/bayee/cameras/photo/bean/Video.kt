package com.bayee.cameras.photo.bean

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bayee.cameras.util.Constant.VIDEOS_TABLE_NAME

@Entity(tableName = VIDEOS_TABLE_NAME)
data class Video(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val path: String,
    var isSelected: Boolean = false
): Parcelable {
    // Parcelable implementation similar to Photo
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readByte() != 0.toByte()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(path)
        parcel.writeByte(if (isSelected) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Video> {
        override fun createFromParcel(parcel: Parcel): Video {
            return Video(parcel)
        }

        override fun newArray(size: Int): Array<Video?> {
            return arrayOfNulls(size)
        }
    }
}
