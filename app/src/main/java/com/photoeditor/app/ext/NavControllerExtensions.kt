package com.photoeditor.app.ext

import android.os.Bundle
import android.util.Log
import androidx.annotation.IdRes
import androidx.navigation.NavController

fun NavController.safelyNavigate(@IdRes resId: Int, args: Bundle? = null) =
    try {
        navigate(resId, args)
    } catch (e: Exception) {
        Log.e("safelyNavigate", e.localizedMessage.toString())
    }
