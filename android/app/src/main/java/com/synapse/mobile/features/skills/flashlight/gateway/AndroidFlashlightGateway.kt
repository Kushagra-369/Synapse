package com.synapse.mobile.features.skills.flashlight.gateway

import android.content.Context
import android.hardware.camera2.CameraManager

class AndroidFlashlightGateway(
    context: Context
) : FlashlightGateway {

    private val cameraManager =
        context.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    private val cameraId =
        cameraManager.cameraIdList.first()

    private var isOn = false

    override fun turnOn(): Boolean {

        return try {

            cameraManager.setTorchMode(cameraId, true)

            isOn = true

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun turnOff(): Boolean {

        return try {

            cameraManager.setTorchMode(cameraId, false)

            isOn = false

            true

        } catch (e: Exception) {

            false

        }

    }

    override fun toggle(): Boolean {

        return if (isOn) {

            turnOff()

        } else {

            turnOn()

        }

    }
}