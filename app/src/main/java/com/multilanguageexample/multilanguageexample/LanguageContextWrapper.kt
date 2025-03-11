package com.multilanguageexample.multilanguageexample

import android.content.Context
import java.util.Locale

class LanguageContextWrapper {
    companion object {
        fun wrap(context: Context, language: String){
            val configuration = context.resources.configuration
            val locale = Locale.forLanguageTag(language)
            configuration.setLocale(locale)
            val resources = context.resources
            resources.updateConfiguration(configuration, context.resources.displayMetrics)
        }
    }
}

