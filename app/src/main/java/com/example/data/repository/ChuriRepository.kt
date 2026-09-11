package com.example.data.repository

import com.example.data.local.ChuriDao
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickNoteEntity
import com.example.data.model.SaleEntity
import com.example.data.model.StaffEntity
import com.example.data.model.TransactionEntity
import kotlinx.coroutines.flow.Flow

class ChuriRepository(private val dao: ChuriDao) {

    val allProducts: Flow<List<ProductEntity>> = dao.getAllProducts()
    val allCustomers: Flow<List<CustomerEntity>> = dao.getAllCustomers()
    val allSales: Flow<List<SaleEntity>> = dao.getAllSales()
    val allExpenses: Flow<List<ExpenseEntity>> = dao.getAllExpenses()
    val allTransactions: Flow<List<TransactionEntity>> = dao.getAllTransactions()
    val allNotes: Flow<List<QuickNoteEntity>> = dao.getAllNotes()
    val allStaff: Flow<List<StaffEntity>> = dao.getAllStaff()

    fun getSalesByCustomer(custId: Long): Flow<List<SaleEntity>> = dao.getSalesByCustomer(custId)
    fun getTransactionsByCustomer(custId: Long): Flow<List<TransactionEntity>> = dao.getTransactionsByCustomer(custId)

    suspend fun insertProduct(product: ProductEntity) = dao.insertProduct(product)
    suspend fun updateProduct(product: ProductEntity) = dao.updateProduct(product)
    suspend fun deleteProduct(product: ProductEntity) = dao.deleteProduct(product)
    suspend fun incrementStock(productId: Long, qty: Double) = dao.incrementStock(productId, qty)
    suspend fun decrementStock(productId: Long, qty: Double) = dao.decrementStock(productId, qty)

    suspend fun insertCustomer(customer: CustomerEntity) = dao.insertCustomer(customer)
    suspend fun updateCustomer(customer: CustomerEntity) = dao.updateCustomer(customer)
    suspend fun deleteCustomer(customer: CustomerEntity) = dao.deleteCustomer(customer)
    suspend fun increaseCustomerDue(custId: Long, amount: Double) = dao.increaseDue(custId, amount)
    suspend fun decreaseCustomerDue(custId: Long, amount: Double) = dao.decreaseDue(custId, amount)

    suspend fun insertSale(sale: SaleEntity): Long = dao.insertSale(sale)
    suspend fun insertTransaction(transaction: TransactionEntity): Long = dao.insertTransaction(transaction)

    suspend fun insertExpense(expense: ExpenseEntity) = dao.insertExpense(expense)
    suspend fun deleteExpense(expense: ExpenseEntity) = dao.deleteExpense(expense)

    suspend fun insertNote(note: QuickNoteEntity) = dao.insertNote(note)
    suspend fun deleteNote(note: QuickNoteEntity) = dao.deleteNote(note)

    suspend fun insertStaff(staff: StaffEntity) = dao.insertStaff(staff)
    suspend fun deleteStaff(staff: StaffEntity) = dao.deleteStaff(staff)
    suspend fun getStaffByUsername(username: String) = dao.getStaffByUsername(username)
}
