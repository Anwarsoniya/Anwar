package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickNoteEntity
import com.example.data.model.SaleEntity
import com.example.data.model.StaffEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ChuriDao {

    // --- Products ---
    @Query("SELECT * FROM products ORDER BY name ASC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    @Query("SELECT * FROM products WHERE id = :id")
    suspend fun getProductById(id: Long): ProductEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    @Update
    suspend fun updateProduct(product: ProductEntity)

    @Delete
    suspend fun deleteProduct(product: ProductEntity)

    @Query("UPDATE products SET stock = stock - :qty WHERE id = :id")
    suspend fun decrementStock(id: Long, qty: Double)

    @Query("UPDATE products SET stock = stock + :qty WHERE id = :id")
    suspend fun incrementStock(id: Long, qty: Double)

    // --- Customers ---
    @Query("SELECT * FROM customers ORDER BY name ASC")
    fun getAllCustomers(): Flow<List<CustomerEntity>>

    @Query("SELECT * FROM customers WHERE id = :id")
    suspend fun getCustomerById(id: Long): CustomerEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomer(customer: CustomerEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCustomers(customers: List<CustomerEntity>)

    @Update
    suspend fun updateCustomer(customer: CustomerEntity)

    @Delete
    suspend fun deleteCustomer(customer: CustomerEntity)

    @Query("UPDATE customers SET due = due + :amount WHERE id = :id")
    suspend fun increaseDue(id: Long, amount: Double)

    @Query("UPDATE customers SET due = due - :amount WHERE id = :id")
    suspend fun decreaseDue(id: Long, amount: Double)

    // --- Sales ---
    @Query("SELECT * FROM sales ORDER BY timestamp DESC")
    fun getAllSales(): Flow<List<SaleEntity>>

    @Query("SELECT * FROM sales WHERE customerId = :custId ORDER BY timestamp DESC")
    fun getSalesByCustomer(custId: Long): Flow<List<SaleEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSale(sale: SaleEntity): Long

    // --- Transactions ---
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE customerId = :custId ORDER BY timestamp DESC")
    fun getTransactionsByCustomer(custId: Long): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    // --- Expenses ---
    @Query("SELECT * FROM expenses ORDER BY timestamp DESC")
    fun getAllExpenses(): Flow<List<ExpenseEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertExpense(expense: ExpenseEntity): Long

    @Delete
    suspend fun deleteExpense(expense: ExpenseEntity)

    // --- Quick Notes ---
    @Query("SELECT * FROM quick_notes ORDER BY timestamp DESC")
    fun getAllNotes(): Flow<List<QuickNoteEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: QuickNoteEntity): Long

    @Delete
    suspend fun deleteNote(note: QuickNoteEntity)

    // --- Staff ---
    @Query("SELECT * FROM staff ORDER BY name ASC")
    fun getAllStaff(): Flow<List<StaffEntity>>

    @Query("SELECT * FROM staff WHERE LOWER(username) = LOWER(:username) LIMIT 1")
    suspend fun getStaffByUsername(username: String): StaffEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertStaff(staff: StaffEntity): Long

    @Delete
    suspend fun deleteStaff(staff: StaffEntity)
}
