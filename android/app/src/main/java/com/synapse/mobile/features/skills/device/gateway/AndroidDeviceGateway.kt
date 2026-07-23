package com.synapse.mobile.features.skills.device.gateway
import android.bluetooth.BluetoothAdapter
import android.content.Context
import android.media.AudioManager
import android.net.wifi.WifiManager
import android.os.BatteryManager
import android.os.Build
import android.provider.Settings
import android.content.Intent

class AndroidDeviceGateway(
    private val context: Context
) : DeviceGateway {

    override fun setBrightness(
        percentage: Int
    ): Boolean {

        if (!Settings.System.canWrite(context)) {
            return false
        }

        return try {

            val value =
                percentage.coerceIn(0, 100) * 255 / 100

            Settings.System.putInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS,
                value
            )

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun getBrightness(): Int {

        return try {

            val brightness = Settings.System.getInt(
                context.contentResolver,
                Settings.System.SCREEN_BRIGHTNESS
            )

            brightness * 100 / 255

        } catch (e: Exception) {

            0

        }

    }

    override fun setVolume(
        percentage: Int
    ): Boolean {

        return try {

            val manager =
                context.getSystemService(
                    Context.AUDIO_SERVICE
                ) as AudioManager

            val max =
                manager.getStreamMaxVolume(
                    AudioManager.STREAM_MUSIC
                )

            val volume =
                percentage.coerceIn(0, 100) * max / 100

            manager.setStreamVolume(
                AudioManager.STREAM_MUSIC,
                volume,
                0
            )

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun getVolume(): Int {

        val manager =
            context.getSystemService(
                Context.AUDIO_SERVICE
            ) as AudioManager

        val current =
            manager.getStreamVolume(
                AudioManager.STREAM_MUSIC
            )

        val max =
            manager.getStreamMaxVolume(
                AudioManager.STREAM_MUSIC
            )

        return current * 100 / max

    }

    override fun setWifi(
        enabled: Boolean
    ): Boolean {

        return try {

            val intent = Intent(
                Settings.Panel.ACTION_WIFI
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun isWifiEnabled(): Boolean {

        val manager =
            context.applicationContext.getSystemService(
                Context.WIFI_SERVICE
            ) as WifiManager

        return manager.isWifiEnabled

    }

    override fun setBluetooth(
        enabled: Boolean
    ): Boolean {

        return try {

            val intent = Intent(
                Settings.ACTION_BLUETOOTH_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun isBluetoothEnabled(): Boolean {

        val adapter =
            BluetoothAdapter.getDefaultAdapter()

        return adapter?.isEnabled ?: false

    }

    override fun setRotation(
        enabled: Boolean
    ): Boolean {

        return try {

            val intent = Intent(
                Settings.ACTION_DISPLAY_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun setDnd(
        enabled: Boolean
    ): Boolean {

        return try {

            val intent = Intent(
                Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS
            ).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }

            context.startActivity(intent)

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun getBatteryLevel(): Int {

        val manager =
            context.getSystemService(
                Context.BATTERY_SERVICE
            ) as BatteryManager

        return manager.getIntProperty(
            BatteryManager.BATTERY_PROPERTY_CAPACITY
        )

    }

}