package com.multilanguageexample.multilanguageexample

import android.app.LocaleManager
import android.content.Context
import android.os.Bundle
import android.os.LocaleList
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.multilanguageexample.multilanguageexample.ui.theme.MultiLanguageExampleTheme
import java.util.Locale

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val localeManager = getSystemService(Context.LOCALE_SERVICE) as LocaleManager
        val currentLocale = localeManager.applicationLocales.toLanguageTags()

        setContent {
            MultiLanguageExampleTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val supportedLocales = listOf("en", "ko", "ja")
                    var expanded by remember { mutableStateOf(false) }
                    var selectedLocale by remember {
                        mutableStateOf(currentLocale.ifEmpty { "Not Set" })
                    }

                    Column(
                        Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(top = 100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = "Current Locale: ${
                                if (currentLocale.isEmpty()) "Not Set" else currentLocale
                            }",
                            color = Color.Blue,
                            modifier = Modifier.padding(top = 30.dp)
                        )

                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = {
                                expanded = !expanded
                            }
                        ) {
                            TextField(
                                readOnly = true,
                                value = selectedLocale,
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
                                        selectedLocale = selectionOption
                                        localeManager.applicationLocales =
                                            LocaleList(Locale.forLanguageTag(selectedLocale))
                                        expanded = false
                                    }, text = { Text(text = selectionOption) }
                                    )
                                }
                            }
                        }
                        Text(
                            // getString is a function that will return the
                            // translated String using the "greeting" key
                            text = getString(R.string.greeting),
                            fontSize = 60.sp,
                            modifier = Modifier.padding(top = 30.dp)
                        )
                        Button(onClick = {
                            localeManager.applicationLocales = LocaleList.getEmptyLocaleList()
                        }, modifier = Modifier.padding(top = 200.dp)) {
                            Text(text = "Reset")
                        }
                    }
                }
            }
        }
    }
}