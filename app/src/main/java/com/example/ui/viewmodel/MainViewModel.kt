package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CartItem
import com.example.data.model.CustomerEntity
import com.example.data.model.ExpenseEntity
import com.example.data.model.ProductEntity
import com.example.data.model.QuickNoteEntity
import com.example.data.model.SaleEntity
import com.example.data.model.StaffEntity
import com.example.data.model.TransactionEntity
import com.example.data.repository.ChuriRepository
import com.example.ui.util.BanglaUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Calendar

enum class AppDestination(val title: String) {
    DASHBOARD("ড্যাশবোর্ড"),
    POS("চুড়ি বিক্রি"),
    INVENTORY("স্টক খাতা"),
    DUE_LEDGER("বাকি খাতা"),
    DAILY_REPORT("প্রতিদিনের হিসাব"),
    HISTORY("ক্যাশ মেমো তালিকা")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ChuriRepository

    init {
        val database = AppDatabase.getInstance(application)
        repository = ChuriRepository(database.churiDao())
    }

    // --- StateFlows from DB ---
    val products: StateFlow<List<ProductEntity>> = repository.allProducts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val customers: StateFlow<List<CustomerEntity>> = repository.allCustomers
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val sales: StateFlow<List<SaleEntity>> = repository.allSales
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val expenses: StateFlow<List<ExpenseEntity>> = repository.allExpenses
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val transactions: StateFlow<List<TransactionEntity>> = repository.allTransactions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val notes: StateFlow<List<QuickNoteEntity>> = repository.allNotes
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val staffList: StateFlow<List<StaffEntity>> = repository.allStaff
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Auth State ---
    private val _isLoggedIn = MutableStateFlow(true) // Auto-login to Admin for smooth UX, allow role switch anytime
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _isAdmin = MutableStateFlow(true)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    private val _currentStaff = MutableStateFlow<StaffEntity?>(null)
    val currentStaff: StateFlow<StaffEntity?> = _currentStaff.asStateFlow()

    // --- Navigation ---
    private val _currentDestination = MutableStateFlow(AppDestination.DASHBOARD)
    val currentDestination: StateFlow<AppDestination> = _currentDestination.asStateFlow()

    // --- Dashboard Filter (today, week, month) ---
    private val _dashboardPeriod = MutableStateFlow("today")
    val dashboardPeriod: StateFlow<String> = _dashboardPeriod.asStateFlow()

    // --- POS State ---
    private val _posSearchQuery = MutableStateFlow("")
    val posSearchQuery: StateFlow<String> = _posSearchQuery.asStateFlow()

    private val _cart = MutableStateFlow<List<CartItem>>(emptyList())
    val cart: StateFlow<List<CartItem>> = _cart.asStateFlow()

    private val _cartDiscount = MutableStateFlow(0.0)
    val cartDiscount: StateFlow<Double> = _cartDiscount.asStateFlow()

    private val _selectedCustomerId = MutableStateFlow<Long?>(null)
    val selectedCustomerId: StateFlow<Long?> = _selectedCustomerId.asStateFlow()

    private val _cashGiven = MutableStateFlow(0.0)
    val cashGiven: StateFlow<Double> = _cashGiven.asStateFlow()

    // --- Dialogs & Active Details ---
    private val _activeInvoice = MutableStateFlow<SaleEntity?>(null)
    val activeInvoice: StateFlow<SaleEntity?> = _activeInvoice.asStateFlow()

    private val _activeCustomerDetails = MutableStateFlow<CustomerEntity?>(null)
    val activeCustomerDetails: StateFlow<CustomerEntity?> = _activeCustomerDetails.asStateFlow()

    private val _quantityTargetProduct = MutableStateFlow<ProductEntity?>(null)
    val quantityTargetProduct: StateFlow<ProductEntity?> = _quantityTargetProduct.asStateFlow()

    private val _showStaffDialog = MutableStateFlow(false)
    val showStaffDialog: StateFlow<Boolean> = _showStaffDialog.asStateFlow()

    private val _showExpenseDialog = MutableStateFlow(false)
    val showExpenseDialog: StateFlow<Boolean> = _showExpenseDialog.asStateFlow()

    private val _showExpenseHistoryDialog = MutableStateFlow(false)
    val showExpenseHistoryDialog: StateFlow<Boolean> = _showExpenseHistoryDialog.asStateFlow()

    private val _showNotesDialog = MutableStateFlow(false)
    val showNotesDialog: StateFlow<Boolean> = _showNotesDialog.asStateFlow()

    private val _showAddProductDialog = MutableStateFlow(false)
    val showAddProductDialog: StateFlow<Boolean> = _showAddProductDialog.asStateFlow()

    private val _editingProduct = MutableStateFlow<ProductEntity?>(null)
    val editingProduct: StateFlow<ProductEntity?> = _editingProduct.asStateFlow()

    private val _showAddCustomerDialog = MutableStateFlow(false)
    val showAddCustomerDialog: StateFlow<Boolean> = _showAddCustomerDialog.asStateFlow()

    private val _toastEvent = MutableSharedFlow<String>()
    val toastEvent: SharedFlow<String> = _toastEvent.asSharedFlow()

    fun showToast(msg: String) {
        viewModelScope.launch {
            _toastEvent.emit(msg)
        }
    }

    // --- Auth Actions ---
    fun loginAsAdmin(password: String = "1234"): Boolean {
        _isAdmin.value = true
        _currentStaff.value = null
        _isLoggedIn.value = true
        showToast("এডমিন (মালিক) হিসেবে প্রবেশ করেছেন")
        return true
    }

    fun loginAsStaff(username: String, pass: String): Boolean {
        val staff = staffList.value.find { it.username.equals(username, ignoreCase = true) }
        return if (staff != null && staff.password == pass) {
            _isAdmin.value = false
            _currentStaff.value = staff
            _isLoggedIn.value = true
            _currentDestination.value = AppDestination.POS
            showToast("স্টাফ (${staff.name}) হিসেবে প্রবেশ করেছেন")
            true
        } else {
            showToast("ভুল ইউজারনেম বা পাসওয়ার্ড!")
            false
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _cart.value = emptyList()
        _activeInvoice.value = null
        showToast("লগ আউট সম্পন্ন হয়েছে")
    }

    fun switchRoleQuick() {
        if (_isAdmin.value) {
            val firstStaff = staffList.value.firstOrNull()
            if (firstStaff != null) {
                _isAdmin.value = false
                _currentStaff.value = firstStaff
                showToast("স্টাফ মোডে পরিবর্তন করা হয়েছে: ${firstStaff.name}")
            } else {
                showToast("কোনো স্টাফ একাউন্ট নেই! আগে স্টাফ যুক্ত করুন।")
            }
        } else {
            _isAdmin.value = true
            _currentStaff.value = null
            showToast("এডমিন (মালিক) মোডে পরিবর্তন করা হয়েছে")
        }
    }

    fun navigateTo(destination: AppDestination) {
        if (destination == AppDestination.INVENTORY && !_isAdmin.value) {
            showToast("শুধুমাত্র এডমিন স্টক খাতা দেখতে পারবেন!")
            return
        }
        _currentDestination.value = destination
    }

    fun setDashboardPeriod(period: String) {
        _dashboardPeriod.value = period
    }

    // --- POS & Cart Actions ---
    fun setPosSearchQuery(q: String) {
        _posSearchQuery.value = q
    }

    fun openQuantityPicker(product: ProductEntity) {
        if (product.stock <= 0) {
            showToast("স্টক শেষ! এই চুড়ি আর মজুদ নেই")
            return
        }
        _quantityTargetProduct.value = product
    }

    fun closeQuantityPicker() {
        _quantityTargetProduct.value = null
    }

    fun addToCart(product: ProductEntity, quantity: Double) {
        if (quantity <= 0) return
        val currentList = _cart.value.toMutableList()
        val index = currentList.indexOfFirst { it.product.id == product.id }
        if (index >= 0) {
            val existing = currentList[index]
            val totalNeeded = existing.quantity + quantity
            if (totalNeeded > product.stock) {
                showToast("স্টকে মাত্র ${BanglaUtils.formatNumber(product.stock)} ${product.unit} রয়েছে!")
                return
            }
            currentList[index] = existing.copy(quantity = totalNeeded)
        } else {
            if (quantity > product.stock) {
                showToast("স্টকে মাত্র ${BanglaUtils.formatNumber(product.stock)} ${product.unit} রয়েছে!")
                return
            }
            currentList.add(CartItem(product, quantity))
        }
        _cart.value = currentList
        closeQuantityPicker()
        showToast("\"${product.name}\" কার্টে যোগ হয়েছে")
    }

    fun updateCartItemQuantity(index: Int, delta: Double) {
        val list = _cart.value.toMutableList()
        if (index !in list.indices) return
        val item = list[index]
        val newQty = Math.round((item.quantity + delta) * 10.0) / 10.0
        if (newQty <= 0) {
            list.removeAt(index)
            showToast("আইটেম সরানো হয়েছে")
        } else {
            val latestProduct = products.value.find { it.id == item.product.id } ?: item.product
            if (newQty > latestProduct.stock) {
                showToast("স্টকে সর্বোচ্চ ${BanglaUtils.formatNumber(latestProduct.stock)} ${latestProduct.unit} রয়েছে!")
                return
            }
            list[index] = item.copy(quantity = newQty)
        }
        _cart.value = list
    }

    fun removeCartItem(index: Int) {
        val list = _cart.value.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            _cart.value = list
            showToast("আইটেম সরানো হয়েছে")
        }
    }

    fun clearCart() {
        _cart.value = emptyList()
        _cartDiscount.value = 0.0
        _cashGiven.value = 0.0
        _selectedCustomerId.value = null
    }

    fun setCartDiscount(discount: Double) {
        _cartDiscount.value = discount.coerceAtLeast(0.0)
    }

    fun setSelectedCustomer(customerId: Long?) {
        _selectedCustomerId.value = customerId
    }

    fun setCashGiven(amount: Double) {
        _cashGiven.value = amount.coerceAtLeast(0.0)
    }

    fun checkout(isDueSale: Boolean) {
        val currentItems = _cart.value
        if (currentItems.isEmpty()) {
            showToast("কার্টে কোনো চুড়ি যোগ করা হয়নি!")
            return
        }

        val custId = _selectedCustomerId.value
        if (isDueSale && custId == null) {
            showToast("বাকিতে বিক্রির জন্য কাস্টমার নির্বাচন করুন")
            return
        }

        val subtotal = currentItems.sumOf { it.lineTotal }
        val discount = _cartDiscount.value
        val finalTotal = (subtotal - discount).coerceAtLeast(0.0)

        val customer = if (custId != null) customers.value.find { it.id == custId } else null
        val customerName = customer?.name ?: "সাধারণ কাস্টমার"
        val customerPhone = customer?.phone ?: ""

        val sellerName = if (_isAdmin.value) "এডমিন" else (_currentStaff.value?.name ?: "স্টাফ")

        viewModelScope.launch(Dispatchers.IO) {
            val sale = SaleEntity(
                dateStr = BanglaUtils.getCurrentDateStr(),
                timeStr = BanglaUtils.getCurrentTimeStr(),
                subtotal = subtotal,
                discount = discount,
                finalTotal = finalTotal,
                saleType = if (isDueSale) "due" else "cash",
                customerId = custId,
                customerName = customerName,
                customerPhone = customerPhone,
                soldBy = sellerName,
                itemsJson = SaleEntity.serializeItems(currentItems)
            )
            val saleId = repository.insertSale(sale)

            // Decrement product stocks
            currentItems.forEach { cartItem ->
                repository.decrementStock(cartItem.product.id, cartItem.quantity)
            }

            // Create transaction & update due if applicable
            if (isDueSale && custId != null) {
                repository.increaseCustomerDue(custId, finalTotal)
                repository.insertTransaction(
                    TransactionEntity(
                        type = "SALE_DUE",
                        customerId = custId,
                        saleId = saleId,
                        amount = finalTotal,
                        dateStr = BanglaUtils.getCurrentDateStr(),
                        note = "বাকিতে চুড়ি বিক্রি (মেমো #${saleId.toString().takeLast(4)})"
                    )
                )
            } else {
                repository.insertTransaction(
                    TransactionEntity(
                        type = "SALE_CASH",
                        saleId = saleId,
                        amount = finalTotal,
                        dateStr = BanglaUtils.getCurrentDateStr(),
                        note = "নগদ বিক্রি"
                    )
                )
            }

            // Show Invoice
            _activeInvoice.value = sale.copy(id = saleId)
            clearCart()
            showToast(if (isDueSale) "বাকিতে বিক্রি সফল হয়েছে" else "নগদে বিক্রি সফল হয়েছে")
        }
    }

    fun showInvoice(sale: SaleEntity) {
        _activeInvoice.value = sale
    }

    fun dismissInvoice() {
        _activeInvoice.value = null
    }

    // --- Inventory Actions ---
    fun openAddProductDialog() {
        _showAddProductDialog.value = true
    }

    fun closeAddProductDialog() {
        _showAddProductDialog.value = false
    }

    fun addProduct(name: String, unit: String, buyPrice: Double, sellPrice: Double, stock: Double) {
        if (name.isBlank()) {
            showToast("চুড়ির নাম ও সাইজ লিখুন")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertProduct(
                ProductEntity(
                    name = name.trim(),
                    unit = unit,
                    buyPrice = buyPrice,
                    sellPrice = sellPrice,
                    stock = stock
                )
            )
            closeAddProductDialog()
            showToast("নতুন চুড়ি যুক্ত করা হয়েছে")
        }
    }

    fun openEditProduct(product: ProductEntity) {
        _editingProduct.value = product
    }

    fun closeEditProduct() {
        _editingProduct.value = null
    }

    fun updateProduct(product: ProductEntity, addStock: Double) {
        viewModelScope.launch(Dispatchers.IO) {
            val updated = product.copy(stock = product.stock + addStock)
            repository.updateProduct(updated)
            closeEditProduct()
            showToast("চুড়ির তথ্য ও স্টক আপডেট হয়েছে")
        }
    }

    fun deleteProduct(product: ProductEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteProduct(product)
            showToast("পণ্য মুছে ফেলা হয়েছে")
        }
    }

    // --- Customer Due Ledger Actions ---
    fun openAddCustomerDialog() {
        _showAddCustomerDialog.value = true
    }

    fun closeAddCustomerDialog() {
        _showAddCustomerDialog.value = false
    }

    fun addCustomer(name: String, phone: String) {
        if (name.isBlank()) {
            showToast("কাস্টমারের নাম লিখুন")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertCustomer(
                CustomerEntity(name = name.trim(), phone = phone.trim(), due = 0.0)
            )
            closeAddCustomerDialog()
            showToast("কাস্টমার সফলভাবে যোগ হয়েছে")
        }
    }

    fun openCustomerDetails(customer: CustomerEntity) {
        _activeCustomerDetails.value = customer
    }

    fun closeCustomerDetails() {
        _activeCustomerDetails.value = null
    }

    fun takeCustomerPayment(customerId: Long, amount: Double) {
        if (amount <= 0) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.decreaseCustomerDue(customerId, amount)
            repository.insertTransaction(
                TransactionEntity(
                    type = "DUE_PAYMENT",
                    customerId = customerId,
                    amount = amount,
                    dateStr = BanglaUtils.getCurrentDateStr(),
                    note = "বকেয়া টাকা পরিশোধ"
                )
            )
            // Update active detail view
            _activeCustomerDetails.value = _activeCustomerDetails.value?.let {
                it.copy(due = (it.due - amount).coerceAtLeast(0.0))
            }
            showToast("৳${BanglaUtils.formatNumber(amount)} জমা নেওয়া হয়েছে")
        }
    }

    fun deleteCustomer(customer: CustomerEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteCustomer(customer)
            if (_activeCustomerDetails.value?.id == customer.id) {
                _activeCustomerDetails.value = null
            }
            showToast("কাস্টমার হিসাব মুছে ফেলা হয়েছে")
        }
    }

    // --- Expenses ---
    fun openExpenseDialog() {
        _showExpenseDialog.value = true
    }

    fun closeExpenseDialog() {
        _showExpenseDialog.value = false
    }

    fun openExpenseHistory() {
        _showExpenseDialog.value = false
        _showExpenseHistoryDialog.value = true
    }

    fun closeExpenseHistory() {
        _showExpenseHistoryDialog.value = false
    }

    fun addExpense(amount: Double, reason: String) {
        if (amount <= 0 || reason.isBlank()) {
            showToast("খরচের পরিমাণ ও কারণ লিখুন")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertExpense(
                ExpenseEntity(
                    amount = amount,
                    reason = reason.trim(),
                    dateStr = BanglaUtils.getCurrentDateStr()
                )
            )
            showToast("দোকান খরচ লিপিবদ্ধ হয়েছে")
        }
    }

    fun deleteExpense(expense: ExpenseEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteExpense(expense)
            showToast("খরচ মুছে ফেলা হয়েছে")
        }
    }

    // --- Quick Notes ---
    fun openNotesDialog() {
        _showNotesDialog.value = true
    }

    fun closeNotesDialog() {
        _showNotesDialog.value = false
    }

    fun addNote(text: String) {
        if (text.isBlank()) return
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertNote(
                QuickNoteEntity(
                    text = text.trim(),
                    dateStr = BanglaUtils.getCurrentDateStr()
                )
            )
            showToast("নোট সংরক্ষিত হয়েছে")
        }
    }

    fun deleteNote(note: QuickNoteEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteNote(note)
            showToast("নোট মুছে ফেলা হয়েছে")
        }
    }

    // --- Staff Management ---
    fun openStaffDialog() {
        if (!_isAdmin.value) {
            showToast("শুধুমাত্র এডমিন স্টাফ একাউন্ট তৈরি করতে পারেন")
            return
        }
        _showStaffDialog.value = true
    }

    fun closeStaffDialog() {
        _showStaffDialog.value = false
    }

    fun addStaff(name: String, username: String, pass: String) {
        if (name.isBlank() || username.isBlank() || pass.length < 3) {
            showToast("নাম, ইউজারনেম এবং কমপক্ষে ৩ অক্ষরের পাসওয়ার্ড দিন")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            val existing = repository.getStaffByUsername(username)
            if (existing != null) {
                showToast("এই ইউজারনেমটি আগে থেকেই রয়েছে!")
                return@launch
            }
            repository.insertStaff(
                StaffEntity(
                    name = name.trim(),
                    username = username.trim(),
                    password = pass.trim(),
                    createdAt = BanglaUtils.getCurrentDateStr()
                )
            )
            showToast("স্টাফ একাউন্ট সফলভাবে তৈরি হয়েছে")
        }
    }

    fun deleteStaff(staff: StaffEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteStaff(staff)
            showToast("স্টাফ মুছে ফেলা হয়েছে")
        }
    }
}
