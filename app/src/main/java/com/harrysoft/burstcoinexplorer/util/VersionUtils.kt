package com.harrysoft.burstcoinexplorer.util

import android.content.pm.PackageManager
import android.content.Context

object VersionUtils {

    @JvmStatic
    fun getVersionName(context: Context): String {
        var version = ""
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            version = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        return version
    }

    @JvmStatic
    fun getVersionCode(context: Context): Int {
        var versionCode = -1
        try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            versionCode = pInfo.versionCode
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }

        return versionCode
    }
}