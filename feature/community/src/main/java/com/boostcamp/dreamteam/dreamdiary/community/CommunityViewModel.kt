package com.boostcamp.dreamteam.dreamdiary.community

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.boostcamp.dreamteam.dreamdiary.core.data.repository.FunctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class CommunityViewModel @Inject constructor(
    private val functionRepository: FunctionRepository
) : ViewModel() {

    fun helloWorld() {
        viewModelScope.launch {
            try {
                val result = functionRepository.helloWorld()
                Timber.d("Result: $result")
            } catch (e: Exception) {
                Timber.d("Error: ${e.message}")
            }
        }
    }


    fun calculateSum(firstNumber: Int, secondNumber: Int) {
    }
}
