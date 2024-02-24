package com.ayush.linkup.presentation.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ayush.linkup.data.repository.DataStoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository
) : ViewModel() {

    private val _readOnboardingState = MutableStateFlow<Boolean>(false)
    val readOnboardingState get() = _readOnboardingState

    fun isOnboarded(completed: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveOnboardingState(completed = completed)
        }
    }

    fun readOnboardingState() {
        viewModelScope.launch {
            dataStoreRepository.readOnboardingState()
                .collect {
                    _readOnboardingState.value = it
                }
        }
    }

}