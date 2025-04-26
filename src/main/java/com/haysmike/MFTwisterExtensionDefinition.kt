package com.haysmike

import com.bitwig.extension.api.PlatformType
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList
import com.bitwig.extension.controller.ControllerExtensionDefinition
import com.bitwig.extension.controller.api.ControllerHost
import java.util.*

class MFTwisterExtensionDefinition : ControllerExtensionDefinition() {
    override fun getName(): String {
        return "MFTwister"
    }

    override fun getAuthor(): String {
        return "haysmike"
    }

    override fun getVersion(): String {
        return "0.1"
    }

    override fun getId(): UUID {
        return DRIVER_ID
    }

    override fun getHardwareVendor(): String {
        return "DJ TechTools"
    }

    override fun getHardwareModel(): String {
        return "MIDI Fighter Twister"
    }

    override fun getRequiredAPIVersion(): Int {
        return 21
    }

    override fun getNumMidiInPorts(): Int {
        return 1
    }

    override fun getNumMidiOutPorts(): Int {
        return 1
    }

    override fun listAutoDetectionMidiPortNames(list: AutoDetectionMidiPortNamesList, platformType: PlatformType?) {
        list.add(arrayOf<String>("Midi Fighter Twister"), arrayOf<String>("Midi Fighter Twister"))
    }

    override fun createInstance(host: ControllerHost?): MFTwisterExtension {
        return MFTwisterExtension(this, host)
    }

    companion object {
        private val DRIVER_ID: UUID = UUID.fromString("dc337c96-9a72-47aa-9d60-b8f7766419bb")
    }
}
