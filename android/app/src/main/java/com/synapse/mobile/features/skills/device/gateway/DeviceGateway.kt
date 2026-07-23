package com.synapse.mobile.features.skills.device.gateway

interface DeviceGateway {

    fun setBrightness(
        percentage: Int
    ): Boolean

    fun getBrightness(): Int

    fun setVolume(
        percentage: Int
    ): Boolean

    fun getVolume(): Int

    fun setWifi(
        enabled: Boolean
    ): Boolean

    fun isWifiEnabled(): Boolean

    fun setBluetooth(
        enabled: Boolean
    ): Boolean

    fun isBluetoothEnabled(): Boolean

    fun setRotation(
        enabled: Boolean
    ): Boolean

    fun setDnd(
        enabled: Boolean
    ): Boolean

    fun getBatteryLevel(): Int

}