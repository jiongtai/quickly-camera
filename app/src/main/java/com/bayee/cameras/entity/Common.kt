package com.bayee.cameras.entity

import android.os.Parcel
import android.os.Parcelable

open class Common : BaseId {
    /**
     * 创建时间
     */
    var createdAt: String? = null

    /**
     * 更新时间
     */
    var updatedAt: String? = null

    constructor() : super()

    constructor(parcel: Parcel) : super(parcel) {
        createdAt = parcel.readString()
        updatedAt = parcel.readString()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        super.writeToParcel(parcel, flags)
        parcel.writeString(createdAt)
        parcel.writeString(updatedAt)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Common

        if (createdAt != other.createdAt) return false
        if (updatedAt != other.updatedAt) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + (createdAt?.hashCode() ?: 0)
        result = 31 * result + (updatedAt?.hashCode() ?: 0)
        return result
    }


    companion object CREATOR : Parcelable.Creator<Common> {
        override fun createFromParcel(parcel: Parcel): Common {
            return Common(parcel)
        }

        override fun newArray(size: Int): Array<Common?> {
            return arrayOfNulls(size)
        }
    }
}