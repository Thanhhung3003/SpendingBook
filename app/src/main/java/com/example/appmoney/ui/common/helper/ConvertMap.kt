package com.example.appmoney.ui.common.helper

import com.example.appmoney.data.model.Category
import com.example.appmoney.data.model.CategoryColor
import com.example.appmoney.data.model.CategoryImage
import com.example.appmoney.data.model.Transaction
import com.google.firebase.Timestamp

object ConvertMap {

    fun toMap(category: Category):Map<String,Any>{
        return hashMapOf(
            "image" to category.image.name,
            "color" to category.color.name,
            "desCat" to category.desCat,
            "timeCreate" to category.timeCreate
        )
    }

    fun toCategory(categoryMap: Map<String,Any>): Category{
        return Category(
            idCat = (categoryMap["id"] as? String) ?: "",
            image = CategoryImage.valueOf((categoryMap["image"] as? String) ?: ""),
            color = CategoryColor.valueOf((categoryMap["color"] as? String) ?: ""),
            desCat = (categoryMap["desCat"] as? String) ?: "",
            timeCreate = (categoryMap["timeCreate"] as? Long) ?: 0
        )
    }
    fun toMapTrans(transaction: Transaction): Map<String,Any>{
        val map = mutableMapOf<String,Any>(
            "amount" to transaction.amount,
            "note" to transaction.note,
            "categoryId" to transaction.categoryId,
            "typeTrans" to transaction.typeTrans
        )
        TimeHelper.stringToTimestamp(transaction.date)?.let {
            map["date"] = it
        }
        return map
    }
    fun toStringTrans( transMap: Map<String,Any>): Transaction{
        val timestamp = transMap["date"] as? Timestamp
        val date = timestamp?.let { TimeHelper.timestampToString(it) } ?: ""
        return Transaction(
            amount = (transMap["amount"] as? Long) ?: 0,
            note = (transMap["note"] as? String) ?: "",
            categoryId = (transMap["categoryId"] as? String) ?: "",
            typeTrans = (transMap["typeTrans"] as? String) ?: "",
            date = date
        )
    }
}