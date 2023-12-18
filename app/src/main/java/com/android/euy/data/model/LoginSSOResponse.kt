package com.android.euy.data.model

data class LoginSSOResponse(
    val accessToken:String,
    val data: SSOObject
)

data class SSOObject(val sso : SSO)