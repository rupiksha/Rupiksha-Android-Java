package com.app.rupiksha.presentation.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.app.rupiksha.R
import com.app.rupiksha.presentation.navigation.Screen

@Composable
fun LoginScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "App Logo",
            modifier = Modifier.size(150.dp)
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Button(
            onClick = {
                navController.navigate(Screen.SignIn.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "LOGIN")
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedButton(
            onClick = {
                navController.navigate(Screen.SignUp.route)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "SIGN UP")
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(
            onClick = {
                // Dial action can be moved to a helper or kept here
            }
        ) {
            Text(text = "Contact Us: +91 7004128310")
        }
    }
}