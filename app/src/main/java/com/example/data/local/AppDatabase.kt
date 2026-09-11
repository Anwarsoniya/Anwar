package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickNoteEntity
import com.example.data.model.SaleEntity
import com.example.data.model.StaffEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        ProductEntity::class,
        CustomerEntity::class,
        SaleEntity::class,
        TransactionEntity::class,
        ExpenseEntity::class,
        QuickNoteEntity::class,
        StaffEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun churiDao(): ChuriDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "churi_ghor_database"
                )
                    .addCallback(object : Callback() {
                        override fun onCreate(db: SupportSQLiteDatabase) {
                            super.onCreate(db)
                            CoroutineScope(Dispatchers.IO).launch {
                                populateInitialData(getInstance(context).churiDao())
                            }
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }

        private suspend fun populateInitialData(dao: ChuriDao) {
            val sampleProducts = listOf(
                ProductEntity(
                    name = "জয়পুরী কাচের চুড়ি - 2.4",
                    unit = "ডজন",
                    buyPrice = 120.0,
                    sellPrice = 180.0,
                    stock = 25.0
                ),
                ProductEntity(
                    name = "রেশমি চুড়ি - লাল 2.6",
                    unit = "ডজন",
                    buyPrice = 90.0,
                    sellPrice = 150.0,
                    stock = 18.0
                ),
                ProductEntity(
                    name = "গোল্ড প্লেটেড বাউটি চুড়ি - 2.6",
                    unit = "জোড়া",
                    buyPrice = 220.0,
                    sellPrice = 350.0,
                    stock = 12.0
                ),
                ProductEntity(
                    name = "ভেলভেট চুড়ি সেট - মেরুন",
                    unit = "সেট",
                    buyPrice = 280.0,
                    sellPrice = 420.0,
                    stock = 10.0
                ),
                ProductEntity(
                    name = "সিটি গোল্ড বালা - প্রিমিয়াম",
                    unit = "জোড়া",
                    buyPrice = 350.0,
                    sellPrice = 550.0,
                    stock = 6.0
                ),
                ProductEntity(
                    name = "মেটালিক স্টোনের চুড়ি সেট",
                    unit = "সেট",
                    buyPrice = 300.0,
                    sellPrice = 480.0,
                    stock = 3.0 // Low stock warning
                ),
                ProductEntity(
                    name = "কাচের রেশম চুড়ি - সবুজ 2.8",
                    unit = "ডজন",
                    buyPrice = 100.0,
                    sellPrice = 160.0,
                    stock = 15.0
                ),
                ProductEntity(
                    name = "কুন্দন কারুকাজ ব্রাইডাল চুড়ি",
                    unit = "বক্স",
                    buyPrice = 650.0,
                    sellPrice = 950.0,
                    stock = 2.0 // Low stock warning
                )
            )
            dao.insertProducts(sampleProducts)

            val sampleCustomers = listOf(
                CustomerEntity(name = "সালমা বেগম", phone = "01711223344", due = 450.0),
                CustomerEntity(name = "নুসরাত জাহান", phone = "01822334455", due = 280.0),
                CustomerEntity(name = "রাহেলা খাতুন", phone = "01933445566", due = 0.0)
            )
            dao.insertCustomers(sampleCustomers)

            val sampleNotes = listOf(
                QuickNoteEntity(
                    text = "চকবাজার মহাজনের কাছে আগামী সোমবার কাচের চুড়ির নতুন লট ডেলিভারি আসবে।",
                    dateStr = "আজ",
                    timestamp = System.currentTimeMillis()
                ),
                QuickNoteEntity(
                    text = "সালমা আপার স্পেশাল ব্রাইডাল লাল চুড়ি সাইজ 2.8 আলাদা রাখা আছে।",
                    dateStr = "আজ",
                    timestamp = System.currentTimeMillis() - 3600000
                )
            )
            sampleNotes.forEach { dao.insertNote(it) }

            // Sample staff
            dao.insertStaff(
                StaffEntity(
                    name = "মোঃ রহিম",
                    username = "rahim",
                    password = "123",
                    createdAt = "আজ"
                )
            )
        }
    }
}
