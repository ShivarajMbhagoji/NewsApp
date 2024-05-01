package com.example.newsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.newsapp.domain.usecases.cases.AppEntryUseCases
import com.example.newsapp.presentation.mainActivity.MainViewModel
import com.example.newsapp.presentation.onboarding.OnboardingViewModel
import com.example.newsapp.presentation.onboarding.onBoardingScreen
import com.example.newsapp.ui.theme.NewsAppTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var appEntryUseCases: AppEntryUseCases

    private val viewModel by viewModels<MainViewModel>()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        installSplashScreen().apply {
            setKeepOnScreenCondition(
                condition = {viewModel.splashCondition.value}
            )
        }
//        lifecycleScope.launch {
//            appEntryUseCases.readAppEntry().collect{
//                Log.d("test",it.toString())
//            }
//        }

        setContent {
            NewsAppTheme (dynamicColor = false){
                Box(modifier = Modifier.background(MaterialTheme.colorScheme.background)) {
                    val viewModel: OnboardingViewModel = hiltViewModel()
                    onBoardingScreen(
                        event = viewModel::onEvent
                    )
                }
            }
        }
    }
}




