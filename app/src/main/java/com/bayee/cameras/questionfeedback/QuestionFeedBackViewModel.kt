package com.bayee.cameras.questionfeedback

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.api.bean.receive.QuestionFeedBackResponse
import com.bayee.cameras.api.bean.send.Question
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.model.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class QuestionFeedBackViewModel : BaseViewModel() {

    private val _question = MutableSharedFlow<QuestionFeedBackResponse>()
    val question: Flow<QuestionFeedBackResponse> = _question

    fun sendFeedback(typeIndex: Int, content: String) {
        viewModelScope.launch(coroutineExceptionHandler) {
            val question = Question(typeIndex, content)
            val result = DefaultNetworkRepository.sendFeedback(question)
            if (result.code == HttpURLConnection.HTTP_OK){
                Log.d(TAG, "sendFeedback: ${result.code}")
            }else{
                Log.d(TAG, "sendFeedback: ${result.code}")
                Log.d(TAG, "sendFeedback: ${result.message}")
            }
        }
    }

}













