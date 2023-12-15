package com.android.euy.data.repositories

import com.android.euy.data.model.LoginSSORequest
import com.android.euy.data.model.LoginSSOResponse
import com.android.euy.data.services.ApiService
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class UserRepository @Inject constructor(private val api: ApiService) {

    fun login(request:LoginSSORequest): Single<LoginSSOResponse> {
        return api.login(request).subscribeOn(Schedulers.io())
            .observeOn((AndroidSchedulers.mainThread()))
    }
}