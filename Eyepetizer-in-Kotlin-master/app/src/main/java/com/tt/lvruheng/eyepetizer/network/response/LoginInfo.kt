package com.tt.lvruheng.eyepetizer.network.response

import java.io.Serializable

/**
 * Created by huashigen on 2018-01-18.
 */
data class LoginInfo(var login: String, var userName: String, var idNo: String, var id: String, var mobileNo: String, var idNoStatus: String, var logonTokenKey: String) : Serializable {
    constructor() : this("", "", "", "", "", "", "")
}