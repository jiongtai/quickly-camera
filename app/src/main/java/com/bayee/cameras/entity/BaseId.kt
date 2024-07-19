package com.bayee.cameras.entity

import android.os.Parcel
import android.os.Parcelable

/**
 * 所有模型Id父类
 */
open class BaseId() : Base(), Parcelable {
    /**
     * Id
     */
    lateinit var id: String

    constructor(parcel: Parcel) : this() {
        id = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BaseId

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

    companion object CREATOR : Parcelable.Creator<BaseId> {
        override fun createFromParcel(parcel: Parcel): BaseId {
            return BaseId(parcel)
        }

        override fun newArray(size: Int): Array<BaseId?> {
            return arrayOfNulls(size)
        }
    }


}