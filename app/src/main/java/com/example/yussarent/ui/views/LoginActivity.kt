package com.example.yussarent.ui.views

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.yussarent.R
import com.example.yussarent.data.models.Screen
import com.example.yussarent.data.repositories.impl.UserRepositoryImpl
import com.example.yussarent.ui.theme.RentTheme
import com.example.yussarent.util.ApiServiceSingleton
import com.example.yussarent.util.LoginResult
import com.example.yussarent.util.LoginResultSingleton
import com.example.yussarent.viewModels.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity:  ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val apiService = ApiServiceSingleton.createApiService()
        val userRepository = UserRepositoryImpl(apiService)
        val loginViewModel = LoginViewModel(userRepository)
        setContent {
            RentTheme {
                Scaffold(
                    content = {padding ->
                        Column(
                            modifier = Modifier
                                .padding(padding)){
                            Box(modifier = Modifier.fillMaxSize()) {
                                val navController = rememberNavController()
                                NavHost(navController = navController, startDestination = "login") {
                                    composable("login") {
                                        Box(modifier = Modifier.fillMaxSize()) {

                                            LoginScreen(loginViewModel, navController = navController)
                                        }
                                    }
                                    composable(Screen.Home.route) {
                                        // Your main screen composable that uses the user object
                                        val screens = listOf(
                                            Screen.Home,
                                            Screen.AvailableRooms,
                                            Screen.OccupiedRooms,
                                            Screen.Invoice,
                                            Screen.Payments
                                        )
                                        Surface(
                                            modifier = Modifier.fillMaxSize(),
                                            color = MaterialTheme.colorScheme.background
                                        ) {
                                            MainScreen(screens = screens, loginViewModel)
                                        }
                                    }

                                }
                            }
                        }

                    }

                )
            }

        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginResult by loginViewModel.loginResult.observeAsState()
    var loggingIn by remember { mutableStateOf(false) }
    var usernameError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var isPasswordVisible by remember{
        mutableStateOf(false)
    }
    fun submitLoginForm() {
        if (username.isNotEmpty() && password.isNotEmpty() && !loggingIn) {
            usernameError = ""
            passwordError = ""
            loggingIn = true
            loginViewModel.login(username, password)
        } else {
            usernameError = if (username.isEmpty()) "Username cannot be empty" else ""
            passwordError = if (password.isEmpty()) "Password cannot be empty" else ""
            loggingIn = false
        }
    }
    val textFieldColors = TextFieldDefaults.outlinedTextFieldColors(
        textColor = Color.Black,
        disabledTextColor = Color.Black.copy(alpha = 0.5f),
        containerColor = Color.Transparent,
        cursorColor = Color.Black,
        errorCursorColor = Color.Red,
        focusedBorderColor = Color.Black,
        unfocusedBorderColor = Color.Black.copy(alpha = 0.5f),
        disabledBorderColor = Color.Black.copy(alpha = 0.5f),
        errorBorderColor = Color.Red,
        focusedLabelColor = Color.Black,
        unfocusedLabelColor = Color.Black.copy(alpha = 0.5f),
        disabledLabelColor = Color.Black.copy(alpha = 0.5f),
        errorLabelColor = Color.Red,
        placeholderColor = Color.Black.copy(alpha = 0.5f),
        disabledPlaceholderColor = Color.Black.copy(alpha = 0.5f),
        focusedLeadingIconColor = Color.Black.copy(alpha = 0.5f),
        unfocusedLeadingIconColor = Color.Black.copy(alpha = 0.5f),
        disabledLeadingIconColor = Color.Black.copy(alpha = 0.5f),
        errorLeadingIconColor = Color.Red.copy(alpha = 0.5f),
        focusedTrailingIconColor = Color.Black.copy(alpha = 0.5f),
        unfocusedTrailingIconColor = Color.Black.copy(alpha = 0.5f),
        disabledTrailingIconColor = Color.Black.copy(alpha = 0.5f),
        errorTrailingIconColor = Color.Red.copy(alpha = 0.5f),
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Spacer(modifier = Modifier.padding(bottom = 16.dp) )
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.padding(16.dp),
            placeholder = { Text("Enter Username") },
            isError = usernameError.isNotEmpty(),
            colors = textFieldColors,
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
        )

        if (usernameError.isNotEmpty()) {
            Text(
                text = usernameError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Box(modifier = Modifier.height(0.dp))
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next,
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    submitLoginForm()
                },
                onNext = {
                    submitLoginForm()
                }

            ),
            placeholder = { Text("Enter password") },
            isError = passwordError.isNotEmpty(),
            colors = textFieldColors,
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                IconButton(onClick = {
                    isPasswordVisible = !isPasswordVisible
                }) {

                    val visibleIconAndText = Pair(
                        first = Icons.Outlined.Visibility,
                        second = stringResource(id = R.string.icon_password_visible)
                    )

                    val hiddenIconAndText = Pair(
                        first = Icons.Default.VisibilityOff,
                        second = stringResource(id = R.string.icon_password_hidden)
                    )

                    val passwordVisibilityIconAndText =
                        if (isPasswordVisible) visibleIconAndText else hiddenIconAndText

                    // Render Icon
                    Icon(
                        imageVector = passwordVisibilityIconAndText.first,
                        contentDescription = passwordVisibilityIconAndText.second
                    )
                }
            }
        )

        if (passwordError.isNotEmpty()) {
            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        } else {
            Box(modifier = Modifier.height(0.dp))
        }

        Button(
            enabled = !loggingIn,
            onClick = {
                submitLoginForm()
            },
            modifier = Modifier.padding(16.dp)
        ) {
            if (loggingIn) {

                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.background,
                    strokeWidth = 4.dp,
                    modifier = Modifier.size(48.dp)
                )

            } else {
                Text(text = "Log In")
            }

        }

        loginResult?.let {
            loggingIn = false
            when (it) {
                is LoginResult.Success -> {

                    println("Result $it")

//                    Toast.makeText(
//                        LocalContext.current,
//                        "Login successful!",
//                        Toast.LENGTH_SHORT
//                    ).show()
                    LoginResultSingleton.setLoginResult(it)
                    LaunchedEffect(true) {
                        navController.navigate(Screen.Home.route){
                            launchSingleTop = true
                            popUpTo("login") { inclusive = true }
                        }
                    }
                }
                is LoginResult.Error -> {
                    password = ""
                    Toast.makeText(
                        LocalContext.current,
                        it.message,
                        Toast.LENGTH_SHORT
                    ).show()
                    if (it.message == "Invalid credentials") {
                        username = "" // reset username only if login attempt failed due to invalid credentials
                    }
                    loginViewModel.resetLoginResult()
                }
                LoginResult.NetworkError -> {
                    // handle network error
                    loginViewModel.resetLoginResult()
                }
            }
        }

    }

}



