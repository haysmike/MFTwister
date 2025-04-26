package com.haysmike

import com.bitwig.extension.api.PlatformType
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList
import com.bitwig.extension.controller.ControllerExtensionDefinition
import com.bitwig.extension.controller.api.ControllerHost
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid
import kotlin.uuid.toJavaUuid


class MFTwisterExtensionDefinition : ControllerExtensionDefinition() {
    override fun getName() = "MFTwister"

    override fun getAuthor() = "haysmike"

    override fun getVersion() = "0.1"

    @OptIn(ExperimentalUuidApi::class)
    override fun getId() = Uuid.parse("dc337c96-9a72-47aa-9d60-b8f7766419bb").toJavaUuid()

    override fun getHardwareVendor() = "DJ TechTools"

    override fun getHardwareModel() = "MIDI Fighter Twister"

    override fun getRequiredAPIVersion() = 21

    override fun getNumMidiInPorts() = 1

    override fun getNumMidiOutPorts() = 1

    override fun listAutoDetectionMidiPortNames(list: AutoDetectionMidiPortNamesList, platformType: PlatformType) {
        list.add(arrayOf("Midi Fighter Twister"), arrayOf("Midi Fighter Twister"))
    }

    override fun createInstance(host: ControllerHost) = MFTwisterExtension(this, host)
}
