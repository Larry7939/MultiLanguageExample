package com.multilanguageexample.multilanguageexample

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multilanguageexample.multilanguageexample.MainActivity.Companion.LocalAppLanguage
import com.multilanguageexample.multilanguageexample.MainActivity.Companion.LocalAppLanguageSetter
import com.multilanguageexample.multilanguageexample.ui.theme.MultiLanguageExampleTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            var currentLanguage by remember { mutableStateOf(getSystemLanguage(context)) }

            val updateLanguage: (String) -> Unit = { newLanguage ->
                setLanguage(context = context, language = newLanguage, onLanguageChanged = { // 새로운 언어 데이터 로컬 저장
                        })
                currentLanguage = newLanguage // 최신 언어 반영
            }

            CompositionLocalProvider(
                LocalAppLanguage provides currentLanguage,
                LocalAppLanguageSetter provides updateLanguage
            ) {
                MultiLanguageExampleTheme {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->

                        Column(
                            Modifier
                                .fillMaxSize()
                                .padding(innerPadding)
                                .padding(top = 100.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {

                            Text(
                                text = "currentLanguage: ${
                                    currentLanguage.ifEmpty { "Not Set" }
                                }",
                                color = Color.Blue,
                                modifier = Modifier.padding(top = 30.dp)
                            )

                            LanguageSelectionBox()
                            Text(
                                text = stringResource(R.string.greeting),
                                fontSize = 60.sp,
                                modifier = Modifier.padding(top = 30.dp)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun setLanguage(
        context: Context,
        onLanguageChanged: (String) -> Unit,
        language: String
    ) {
        LanguageContextWrapper.wrap(context, language)
        onLanguageChanged(language)
    }

    private fun getSystemLanguage(context: Context): String {
        val locale = context.resources.configuration.locales[0]

        return locale.toLanguageTag()
    }


    companion object {
        val LocalAppLanguage = compositionLocalOf { "" }
        val LocalAppLanguageSetter = compositionLocalOf<(String) -> Unit> {
            error("LocalAppLanguageSetter is not provided")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LanguageSelectionBox() {
    var expanded by remember { mutableStateOf(false) }
    val supportedLocales = listOf("en", "ko", "ja")
    val language = LocalAppLanguage.current
    val setLanguage = LocalAppLanguageSetter.current

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        TextField(
            modifier = Modifier.menuAnchor(),
            readOnly = true,
            value = language,
            onValueChange = { },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            supportedLocales.forEach { selectionOption ->
                DropdownMenuItem(
                    onClick = {
                        setLanguage(selectionOption)
                        expanded = false
                    }, text = { Text(text = selectionOption) }
                )
            }
        }
    }
}