package com.example

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.local.AppDatabase
import com.example.data.repository.ChuriRepository
import com.example.ui.components.AppBottomBar
import com.example.ui.components.AppDrawerContent
import com.example.ui.components.AppHeader
import com.example.ui.dialogs.AddCustomerDialog
import com.example.ui.dialogs.AddProductDialog
import com.example.ui.dialogs.CustomerDetailsDialog
import com.example.ui.dialogs.EditProductDialog
import com.example.ui.dialogs.ExpenseDialog
import com.example.ui.dialogs.ExpenseHistoryDialog
import com.example.ui.dialogs.InvoiceDialog
import com.example.ui.dialogs.QuantityPickerDialog
import com.example.ui.dialogs.QuickNotesDialog
import com.example.ui.dialogs.RoleSwitchDialog
import com.example.ui.dialogs.StaffManagementDialog
import com.example.ui.dialogs.StockAlertDialog
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.DailyReportScreen
import com.example.ui.screens.DashboardScreen
import com.example.ui.screens.DueLedgerScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.InventoryScreen
import com.example.ui.screens.PosScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                val vm: MainViewModel = viewModel()
                MainAppContent(viewModel = vm)
            }
        }
    }
}

@Composable
fun MainAppContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    // State collections
    val currentDestination by viewModel.currentDestination.collectAsStateWithLifecycle()
    val isAdmin by viewModel.isAdmin.collectAsStateWithLifecycle()
    val currentStaff by viewModel.currentStaff.collectAsStateWithLifecycle()
    val isLoggedIn by viewModel.isLoggedIn.collectAsStateWithLifecycle()

    val products by viewModel.products.collectAsStateWithLifecycle()
    val customers by viewModel.customers.collectAsStateWithLifecycle()
    val sales by viewModel.sales.collectAsStateWithLifecycle()
    val expenses by viewModel.expenses.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val quickNotes by viewModel.notes.collectAsStateWithLifecycle()
    val staffList by viewModel.staffList.collectAsStateWithLifecycle()

    val cart by viewModel.cart.collectAsStateWithLifecycle()
    val cartDiscount by viewModel.cartDiscount.collectAsStateWithLifecycle()
    val selectedCustomerId by viewModel.selectedCustomerId.collectAsStateWithLifecycle()
    val cashGiven by viewModel.cashGiven.collectAsStateWithLifecycle()
    val posSearchQuery by viewModel.posSearchQuery.collectAsStateWithLifecycle()

    val dashboardPeriod by viewModel.dashboardPeriod.collectAsStateWithLifecycle()

    // Dialog states
    val activeInvoice by viewModel.activeInvoice.collectAsStateWithLifecycle()
    val activeCustomerDetails by viewModel.activeCustomerDetails.collectAsStateWithLifecycle()
    val quantityTargetProduct by viewModel.quantityTargetProduct.collectAsStateWithLifecycle()
    val showStaffDialog by viewModel.showStaffDialog.collectAsStateWithLifecycle()
    val showExpenseDialog by viewModel.showExpenseDialog.collectAsStateWithLifecycle()
    val showExpenseHistoryDialog by viewModel.showExpenseHistoryDialog.collectAsStateWithLifecycle()
    val showNotesDialog by viewModel.showNotesDialog.collectAsStateWithLifecycle()
    val showAddProductDialog by viewModel.showAddProductDialog.collectAsStateWithLifecycle()
    val editingProduct by viewModel.editingProduct.collectAsStateWithLifecycle()
    val showAddCustomerDialog by viewModel.showAddCustomerDialog.collectAsStateWithLifecycle()

    var showRoleSwitchDialog by remember { mutableStateOf(false) }
    var showStockAlertDialog by remember { mutableStateOf(false) }

    // Toast event observer
    LaunchedEffect(Unit) {
        viewModel.toastEvent.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Handle back button when drawer is open or in sub screens
    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    BackHandler(enabled = !drawerState.isOpen && currentDestination != AppDestination.DASHBOARD && isLoggedIn) {
        viewModel.navigateTo(AppDestination.DASHBOARD)
    }

    if (!isLoggedIn) {
        AuthScreen(
            onAdminLogin = { viewModel.loginAsAdmin() },
            onStaffLogin = { u, p -> viewModel.loginAsStaff(u, p) }
        )
        return
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = true,
        drawerContent = {
            ModalDrawerSheet {
                AppDrawerContent(
                    currentDestination = currentDestination,
                    isAdmin = isAdmin,
                    currentStaffName = currentStaff?.name,
                    onNavigate = { dest ->
                        viewModel.navigateTo(dest)
                        scope.launch { drawerState.close() }
                    },
                    onOpenStaffDialog = {
                        scope.launch { drawerState.close() }
                        viewModel.openStaffDialog()
                    },
                    onOpenNotesDialog = {
                        scope.launch { drawerState.close() }
                        viewModel.openNotesDialog()
                    },
                    onOpenExpenseDialog = {
                        scope.launch { drawerState.close() }
                        viewModel.openExpenseDialog()
                    },
                    onSwitchRole = {
                        scope.launch { drawerState.close() }
                        showRoleSwitchDialog = true
                    },
                    onLogout = {
                        scope.launch { drawerState.close() }
                        viewModel.logout()
                    },
                    onCloseDrawer = {
                        scope.launch { drawerState.close() }
                    }
                )
            }
        }
    ) {
        val lowStockCount = remember(products) { products.count { it.stock <= 5.0 } }

        Scaffold(
            topBar = {
                AppHeader(
                    isAdmin = isAdmin,
                    currentStaffName = currentStaff?.name,
                    onOpenDrawer = { scope.launch { drawerState.open() } },
                    onRoleSwitchClick = { showRoleSwitchDialog = true },
                    onStockAlertClick = { showStockAlertDialog = true },
                    lowStockCount = lowStockCount
                )
            },
            bottomBar = {
                AppBottomBar(
                    currentDestination = currentDestination,
                    isAdmin = isAdmin,
                    onNavigate = { dest -> viewModel.navigateTo(dest) },
                    onQuickNotesClick = { viewModel.openNotesDialog() }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFDF8F8))
                    .padding(innerPadding)
            ) {
                when (currentDestination) {
                    AppDestination.DASHBOARD -> {
                        DashboardScreen(
                            period = dashboardPeriod,
                            onPeriodChange = { viewModel.setDashboardPeriod(it) },
                            isAdmin = isAdmin,
                            sales = sales,
                            expenses = expenses,
                            customers = customers,
                            transactions = transactions,
                            products = products,
                            onNewSaleClick = { viewModel.navigateTo(AppDestination.POS) },
                            onAddExpenseClick = { viewModel.openExpenseDialog() }
                        )
                    }

                    AppDestination.POS -> {
                        PosScreen(
                            searchQuery = posSearchQuery,
                            onSearchQueryChange = { viewModel.setPosSearchQuery(it) },
                            products = products,
                            cart = cart,
                            discount = cartDiscount,
                            onDiscountChange = { viewModel.setCartDiscount(it) },
                            cashGiven = cashGiven,
                            onCashGivenChange = { viewModel.setCashGiven(it) },
                            customers = customers,
                            selectedCustomerId = selectedCustomerId,
                            onCustomerSelect = { viewModel.setSelectedCustomer(it) },
                            onProductClick = { viewModel.openQuantityPicker(it) },
                            onUpdateQuantity = { idx, delta -> viewModel.updateCartItemQuantity(idx, delta) },
                            onRemoveCartItem = { idx -> viewModel.removeCartItem(idx) },
                            onClearCart = { viewModel.clearCart() },
                            onCheckout = { isDue -> viewModel.checkout(isDue) }
                        )
                    }

                    AppDestination.INVENTORY -> {
                        InventoryScreen(
                            products = products,
                            onAddProductClick = { viewModel.openAddProductDialog() },
                            onEditProductClick = { viewModel.openEditProduct(it) },
                            onDeleteProductClick = { viewModel.deleteProduct(it) }
                        )
                    }

                    AppDestination.DUE_LEDGER -> {
                        DueLedgerScreen(
                            customers = customers,
                            isAdmin = isAdmin,
                            onAddCustomerClick = { viewModel.openAddCustomerDialog() },
                            onCustomerClick = { viewModel.openCustomerDetails(it) },
                            onDeleteCustomerClick = { viewModel.deleteCustomer(it) }
                        )
                    }

                    AppDestination.DAILY_REPORT -> {
                        DailyReportScreen(
                            sales = sales,
                            expenses = expenses,
                            transactions = transactions
                        )
                    }

                    AppDestination.HISTORY -> {
                        HistoryScreen(
                            sales = sales,
                            onSaleClick = { viewModel.showInvoice(it) }
                        )
                    }
                }
            }
        }
    }

    // --- Active Dialogs ---

    // Invoice Cash Memo Dialog
    activeInvoice?.let { invoice ->
        InvoiceDialog(
            sale = invoice,
            onDismiss = { viewModel.dismissInvoice() }
        )
    }

    // Quantity Picker Dialog
    quantityTargetProduct?.let { product ->
        QuantityPickerDialog(
            product = product,
            onDismiss = { viewModel.closeQuantityPicker() },
            onConfirm = { qty -> viewModel.addToCart(product, qty) }
        )
    }

    // Customer Details Dialog
    activeCustomerDetails?.let { customer ->
        CustomerDetailsDialog(
            customer = customer,
            sales = sales,
            transactions = transactions,
            onDismiss = { viewModel.closeCustomerDetails() },
            onTakePayment = { amt -> viewModel.takeCustomerPayment(customer.id, amt) },
            onViewSaleInvoice = { sale -> viewModel.showInvoice(sale) }
        )
    }

    // Add Product Dialog
    if (showAddProductDialog) {
        AddProductDialog(
            onDismiss = { viewModel.closeAddProductDialog() },
            onSave = { name, unit, buy, sell, stock ->
                viewModel.addProduct(name, unit, buy, sell, stock)
            }
        )
    }

    // Edit Product Dialog
    editingProduct?.let { product ->
        EditProductDialog(
            product = product,
            onDismiss = { viewModel.closeEditProduct() },
            onUpdate = { updated, addStock ->
                viewModel.updateProduct(updated, addStock)
            }
        )
    }

    // Add Customer Dialog
    if (showAddCustomerDialog) {
        AddCustomerDialog(
            onDismiss = { viewModel.closeAddCustomerDialog() },
            onSave = { name, phone ->
                viewModel.addCustomer(name, phone)
            }
        )
    }

    // Expense Dialog
    if (showExpenseDialog) {
        ExpenseDialog(
            expenses = expenses,
            onDismiss = { viewModel.closeExpenseDialog() },
            onAddExpense = { amt, reason -> viewModel.addExpense(amt, reason) },
            onOpenHistory = { viewModel.openExpenseHistory() }
        )
    }

    // Expense History Dialog
    if (showExpenseHistoryDialog) {
        ExpenseHistoryDialog(
            expenses = expenses,
            isAdmin = isAdmin,
            onDismiss = { viewModel.closeExpenseHistory() },
            onDeleteExpense = { viewModel.deleteExpense(it) }
        )
    }

    // Quick Notes Dialog
    if (showNotesDialog) {
        QuickNotesDialog(
            notes = quickNotes,
            onDismiss = { viewModel.closeNotesDialog() },
            onSaveNote = { viewModel.addNote(it) },
            onDeleteNote = { viewModel.deleteNote(it) }
        )
    }

    // Staff Management Dialog
    if (showStaffDialog) {
        StaffManagementDialog(
            staffList = staffList,
            onDismiss = { viewModel.closeStaffDialog() },
            onSaveStaff = { name, user, pass -> viewModel.addStaff(name, user, pass) },
            onDeleteStaff = { viewModel.deleteStaff(it) }
        )
    }

    // Role Switch Dialog
    if (showRoleSwitchDialog) {
        RoleSwitchDialog(
            isAdmin = isAdmin,
            currentStaff = currentStaff,
            staffList = staffList,
            onDismiss = { showRoleSwitchDialog = false },
            onSwitchToAdmin = { viewModel.loginAsAdmin() },
            onSelectStaff = { staff ->
                viewModel.loginAsStaff(staff.username, staff.password)
            }
        )
    }

    // Stock Alert Dialog
    if (showStockAlertDialog) {
        StockAlertDialog(
            products = products,
            onDismiss = { showStockAlertDialog = false }
        )
    }
}
