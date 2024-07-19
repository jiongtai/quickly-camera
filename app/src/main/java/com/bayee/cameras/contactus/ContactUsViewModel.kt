package com.bayee.cameras.contactus

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.bayee.cameras.api.repository.DefaultNetworkRepository
import com.bayee.cameras.api.bean.OnlineSupportLink
import com.bayee.cameras.api.bean.response.DetailResponse
import com.bayee.cameras.api.bean.response.onSuccess
import com.bayee.cameras.model.BaseViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import java.net.HttpURLConnection

class ContactUsViewModel : BaseViewModel() {

    private val _link = MutableSharedFlow<OnlineSupportLink>()
    val link: Flow<OnlineSupportLink> = _link
//    val _link = MutableSharedFlow<DetailResponse<OnlineSupportLink>>()
//    val link: Flow<DetailResponse<OnlineSupportLink>> = _link

    fun loadOnlineSupportLink() {
        viewModelScope.launch(coroutineExceptionHandler) {
            val onlineSupportLink = DefaultNetworkRepository.getOnlineSupportLink("1001")
            Log.d(TAG, "loadOnlineSupportLink: ${onlineSupportLink.data}")
            if (onlineSupportLink.code == HttpURLConnection.HTTP_OK){
                _link.emit(onlineSupportLink)
            }else{
                _exception.value = Exception(onlineSupportLink.message)
            }
        }
    }


}








