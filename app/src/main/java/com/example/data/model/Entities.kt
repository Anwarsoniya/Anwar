package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import org.json.JSONArray
import org.json.JSONObject

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val unit: String = "ডজন", // ডজন, জোড়া, সেট, পিস, বক্স, মুঠা
    val buyPrice: Double,
    val sellPrice: Double,
    val stock: Double,
    val imageUrl: String = "",
    val category: String = "চুড়ি"
)

@Entity(tableName = "customers")
data class CustomerEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val phone: String = "",
    val due: Double = 0.0
)

data class CartItem(
    val product: ProductEntity,
    val quantity: Double
) {
    val lineTotal: Double get() = product.sellPrice * quantity
    val buyTotal: Double get() = product.buyPrice * quantity
}

data class SoldItem(
    val productId: Long,
    val name: String,
    val unit: String,
    val quantity: Double,
    val unitPrice: Double,
    val buyPrice: Double
) {
    val lineTotal: Double get() = unitPrice * quantity
}

@Entity(tableName = "sales")
data class SaleEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val dateStr: String,
    val timeStr: String,
    val subtotal: Double,
    val discount: Double,
    val finalTotal: Double,
    val saleType: String, // "cash" or "due"
    val customerId: Long? = null,
    val customerName: String = "সাধারণ কাস্টমার",
    val customerPhone: String = "",
    val soldBy: String = "এডমিন",
    val itemsJson: String = "[]"
) {
    fun parseItems(): List<SoldItem> {
        val list = mutableListOf<SoldItem>()
        try {
            val arr = JSONArray(itemsJson)
            for (i in 0 until arr.length()) {
                val obj = arr.getJSONObject(i)
                list.add(
                    SoldItem(
                        productId = obj.optLong("productId", 0),
                        name = obj.optString("name", ""),
                        unit = obj.optString("unit", "ডজন"),
                        quantity = obj.optDouble("quantity", 1.0),
                        unitPrice = obj.optDouble("unitPrice", 0.0),
                        buyPrice = obj.optDouble("buyPrice", 0.0)
                    )
                )
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return list
    }

    companion object {
        fun serializeItems(items: List<CartItem>): String {
            val arr = JSONArray()
            items.forEach { item ->
                val obj = JSONObject().apply {
                    put("productId", item.product.id)
                    put("name", item.product.name)
                    put("unit", item.product.unit)
                    put("quantity", item.quantity)
                    put("unitPrice", item.product.sellPrice)
                    put("buyPrice", item.product.buyPrice)
                }
                arr.put(obj)
            }
            return arr.toString()
        }
    }
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val type: String, // "SALE_CASH", "SALE_DUE", "DUE_PAYMENT"
    val customerId: Long? = null,
    val saleId: Long? = null,
    val amount: Double,
    val dateStr: String,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)

@Entity(tableName = "expenses")
data class ExpenseEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amount: Double,
    val reason: String,
    val dateStr: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "quick_notes")
data class QuickNoteEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val text: String,
    val dateStr: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "staff")
data class StaffEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val username: String,
    val password: String,
    val photoUri: String = "",
    val createdAt: String
)
