/*
 * SPDX-FileCopyrightText: The LineageOS Project
 * SPDX-License-Identifier: Apache-2.0
 */

package org.lineageos.updater.deviceinfo

import android.os.Build
import android.os.SystemProperties
import com.android.settingslib.DeviceInfoUtils as SettingsLibDeviceInfoUtils

object DeviceInfoUtils : SettingsLibDeviceInfoUtils() {

    private const val PROP_AB_DEVICE = "ro.build.ab_update"
    private const val PROP_ALLOW_MAJOR_UPGRADES = "blaze.updater.allow_major_upgrades"
    private const val PROP_ALLOW_MAJOR_UPGRADES_FALLBACK = "lineage.updater.allow_major_upgrades"
    private const val PROP_BUILD_DATE = "ro.build.date.utc"
    private const val PROP_BUILD_VERSION = "ro.blaze.build.version"
    private const val PROP_BUILD_VERSION_FALLBACK = "ro.lineage.build.version"
    private const val PROP_BUILD_VERSION_INCREMENTAL = "ro.build.version.incremental"
    private const val PROP_DEVICE = "ro.blaze.device"
    private const val PROP_DEVICE_FALLBACK = "ro.lineage.device"
    private const val PROP_NEXT_DEVICE = "ro.updater.next_device"
    private const val PROP_RELEASE_TYPE = "ro.blaze.releasetype"
    private const val PROP_RELEASE_TYPE_FALLBACK = "ro.lineage.releasetype"
    private const val PROP_UPDATER_ALLOW_DOWNGRADING = "blaze.updater.allow_downgrading"
    private const val PROP_UPDATER_ALLOW_DOWNGRADING_FALLBACK = "lineage.updater.allow_downgrading"
    private const val PROP_UPDATER_URI = "blaze.updater.uri"
    private const val PROP_UPDATER_URI_FALLBACK = "lineage.updater.uri"
    private const val PROP_UPDATE_RECOVERY = "persist.vendor.recovery_update"

    private fun getPropWithFallback(primary: String, fallback: String): String =
        SystemProperties.get(primary, "").ifEmpty { SystemProperties.get(fallback, "") }

    private fun getBooleanWithFallback(primary: String, fallback: String): Boolean =
        if (SystemProperties.get(primary, "").isNotEmpty()) {
            SystemProperties.getBoolean(primary, false)
        } else {
            SystemProperties.getBoolean(fallback, false)
        }

    // Read-only
    val androidVersion: String = Build.VERSION.RELEASE

    val buildSecurityPatch: String = Build.VERSION.SECURITY_PATCH

    val sdkLevel: Int = Build.VERSION.SDK_INT

    @JvmStatic
    val buildDateTimestamp: Long = SystemProperties.getLong(PROP_BUILD_DATE, 0)

    @JvmStatic
    val buildVersion: String = getPropWithFallback(PROP_BUILD_VERSION, PROP_BUILD_VERSION_FALLBACK)

    @JvmStatic
    val buildVersionIncremental: String = SystemProperties.get(PROP_BUILD_VERSION_INCREMENTAL, "")

    @JvmStatic
    val device: String = SystemProperties.get(PROP_NEXT_DEVICE, "").ifEmpty {
        getPropWithFallback(PROP_DEVICE, PROP_DEVICE_FALLBACK)
    }

    @JvmStatic
    val isABDevice: Boolean = SystemProperties.getBoolean(PROP_AB_DEVICE, false)

    @JvmStatic
    val releaseType: String = getPropWithFallback(PROP_RELEASE_TYPE, PROP_RELEASE_TYPE_FALLBACK)

    // Mutable at runtime
    @JvmStatic
    val isDowngradingAllowed: Boolean
        get() = getBooleanWithFallback(
            PROP_UPDATER_ALLOW_DOWNGRADING,
            PROP_UPDATER_ALLOW_DOWNGRADING_FALLBACK,
        )

    @JvmStatic
    val isMajorUpdateAllowed: Boolean
        get() = getBooleanWithFallback(
            PROP_ALLOW_MAJOR_UPGRADES,
            PROP_ALLOW_MAJOR_UPGRADES_FALLBACK,
        )

    @JvmStatic
    var isRecoveryUpdateEnabled: Boolean
        get() = SystemProperties.getBoolean(PROP_UPDATE_RECOVERY, false)
        set(value) = SystemProperties.set(PROP_UPDATE_RECOVERY, value.toString())

    @JvmStatic
    val updaterUri: String
        get() = getPropWithFallback(PROP_UPDATER_URI, PROP_UPDATER_URI_FALLBACK)
}
