package com.bayee.cameras.category

import com.bayee.cameras.entity.Common


/**
 * 分类
 */
class Category : Common() {
    lateinit var title: String
    var icon: String? = null
    var data: List<Category>? = null

    companion object {
        fun create(id: String, title: String): Category {
            val r = Category()
            r.id = id
            r.title = title
            return r
        }
    }
}