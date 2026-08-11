package com.synapse.mobile.features.skills.device

import com.synapse.mobile.core.actions.Skill
import com.synapse.mobile.core.models.Command
import com.synapse.mobile.core.models.CommandResult
import com.synapse.mobile.features.skills.device.actions.GetBatteryAction
import com.synapse.mobile.features.skills.device.actions.SetBluetoothAction
import com.synapse.mobile.features.skills.device.actions.SetBrightnessAction
import com.synapse.mobile.features.skills.device.actions.SetDndAction
import com.synapse.mobile.features.skills.device.actions.SetHotspotAction
import com.synapse.mobile.features.skills.device.actions.SetNfcAction
import com.synapse.mobile.features.skills.device.actions.SetRotationAction
import com.synapse.mobile.features.skills.device.actions.SetVolumeAction
import com.synapse.mobile.features.skills.device.actions.SetWifiAction
import com.synapse.mobile.features.skills.device.gateway.DeviceGateway

class DeviceSkill(
    gateway: DeviceGateway
) : Skill {

    override val name = "device"

    private val actions = mapOf(

        "setBrightness" to SetBrightnessAction(gateway),

        "setVolume" to SetVolumeAction(gateway),

        "set_wifi" to SetWifiAction(gateway),

        "set_bluetooth" to SetBluetoothAction(gateway),

        "set_rotation" to SetRotationAction(gateway),

        "set_dnd" to SetDndAction(gateway),

        "set_hotspot" to SetHotspotAction(),

        "set_nfc" to SetNfcAction(),

        "get_battery" to GetBatteryAction(gateway),


    )

    override suspend fun execute(
        command: Command
    ): CommandResult {

        val action =
            actions[command.action]

        return action?.execute(command)
            ?: CommandResult(
                false,
                "Unknown action: ${command.action}"
            )

    }

}