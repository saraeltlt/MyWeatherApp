package com.example.myweatherapp.startPref


import androidx.lifecycle.ViewModel
import com.example.myweatherapp.datasource.RepoInterface

class StartPrefViewModel(
    private val repository: RepoInterface
) : ViewModel() {

    fun putStringInSharedPreferences(key: String, stringInput: String) {
        repository.putStringInSharedPreferences(key, stringInput)
    }

    fun putBooleanInSharedPreferences(key: String, booleanInput: Boolean) {
        repository.putBooleanInSharedPreferences(key, booleanInput)
    }

    /*suspend fun getMapsAutoCompleteResponse(textInput: CharSequence?): MapsAutoCompleteResponse {
        return repository.getMapsAutoCompleteResponse(textInput)
    }*/

    fun setIsDarkTrue() {
        repository.putBooleanInSharedPreferences("isDarkTheme" , true)
    }

}





