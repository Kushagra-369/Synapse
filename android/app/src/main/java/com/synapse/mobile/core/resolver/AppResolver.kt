package com.synapse.mobile.core.resolver

import android.content.Context
import android.content.pm.ApplicationInfo
import android.content.pm.PackageManager
import java.util.Locale

class AppResolver(
    private val context: Context
) {

    private val packageManager: PackageManager =
        context.packageManager

    private fun debugApps() {
        val apps = getInstalledApps()

        android.util.Log.d(
            "APP_DEBUG",
            "Total launchable apps = ${apps.size}"
        )

        apps.forEach {
            android.util.Log.d(
                "APP_DEBUG",
                "APP = '${it.appName}' | PACKAGE = '${it.packageName}'"
            )
        }
    }

    /**
     * Returns all launchable installed apps.
     */
    fun getInstalledApps(): List<ResolvedApp> {


        val launchIntent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
            addCategory(android.content.Intent.CATEGORY_LAUNCHER)
        }

        return packageManager.queryIntentActivities(
            launchIntent,
            PackageManager.MATCH_ALL
        ).map { resolveInfo ->

            val appInfo = resolveInfo.activityInfo.applicationInfo

            ResolvedApp(
                packageName = appInfo.packageName,
                appName = resolveInfo.loadLabel(packageManager).toString(),
                icon = resolveInfo.loadIcon(packageManager),
                isSystemApp = (appInfo.flags and ApplicationInfo.FLAG_SYSTEM) != 0
            )

        }.distinctBy {
            it.packageName
        }.sortedBy {
            it.appName.lowercase(Locale.getDefault())
        }
    }

    /**
     * Finds an app by exact or partial name.
     */
    fun resolve(appName: String): ResolvedApp? {

        val query = appName.trim().lowercase(Locale.getDefault())

        debugApps()

        val apps = getInstalledApps()

        apps.firstOrNull {
            it.appName.lowercase(Locale.getDefault()) == query
        }?.let {
            return it
        }

        apps.firstOrNull {
            it.appName.lowercase(Locale.getDefault()).startsWith(query)
        }?.let {
            return it
        }

        apps.firstOrNull {
            it.appName.lowercase(Locale.getDefault()).contains(query)
        }?.let {
            return it
        }

        apps.firstOrNull {

            it.packageName
                .lowercase(Locale.getDefault())
                .contains(query)

        }?.let {

            return it

        }

        return null
    }

    /**
     * Returns whether an app exists.
     */
    fun isInstalled(appName: String): Boolean {
        return resolve(appName) != null
    }

    /**
     * Returns launch intent for an app.
     */
    fun getLaunchIntent(appName: String) =
        resolve(appName)?.let {
            packageManager.getLaunchIntentForPackage(it.packageName)
        }

    /**
     * Returns package name if installed.
     */
    fun getPackageName(appName: String): String? {
        return resolve(appName)?.packageName
    }
}