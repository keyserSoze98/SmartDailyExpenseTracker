import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.keysersoze.smartdailyexpensetracker.ui.navigation.BottomNavBar
import com.keysersoze.smartdailyexpensetracker.ui.navigation.Screen
import com.keysersoze.smartdailyexpensetracker.ui.screens.ExpenseEntryScreen
import com.keysersoze.smartdailyexpensetracker.ui.screens.ExpenseListScreen
import com.keysersoze.smartdailyexpensetracker.ui.screens.ExpenseReportScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppNavHost(
    navController: NavHostController
) {
    Scaffold(
        bottomBar = { BottomNavBar(navController) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.ExpenseEntry.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.ExpenseEntry.route) {
                ExpenseEntryScreen()
            }
            composable(Screen.ExpenseList.route) {
                ExpenseListScreen()
            }
            composable(Screen.ExpenseReport.route) {
                ExpenseReportScreen()
            }
        }
    }
}