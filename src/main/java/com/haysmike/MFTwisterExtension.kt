package com.haysmike

import com.bitwig.extension.api.util.midi.ShortMidiMessage
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback
import com.bitwig.extension.controller.ControllerExtension
import com.bitwig.extension.controller.api.ControllerHost


private const val numControlStepsDefault = 128
private const val numControlStepsShifted = 256
private const val midiRange = 128
private const val numControls = 64

private val shiftedCcs: MutableSet<Int> = mutableSetOf()

class MFTwisterExtension(definition: MFTwisterExtensionDefinition, host: ControllerHost) :
    ControllerExtension(definition, host) {
    override fun init() {
        val midiOut = host.getMidiOutPort(0)
        val userControlBank = host.createUserControls(numControls)
        for (cc in 0..<numControls) {
            val control = userControlBank.getControl(cc)
            val label = "CC $cc"
            control.setLabel(label)
            control.value().addValueObserver(midiRange) { value ->
                midiOut.sendMidi(176, cc, value)
            }
        }

        host.getMidiInPort(0).setMidiCallback(object : ShortMidiMessageReceivedCallback {
            override fun midiReceived(msg: ShortMidiMessage) {
                val cc = msg.data1
                val value = msg.data2
                if (msg.isControlChange && cc < numControls) {
                    if (msg.channel == 0 && isValidIncrementalValue(value)) {
                        val resolution =
                            if (shiftedCcs.contains(cc)) numControlStepsShifted else numControlStepsDefault
                        userControlBank.getControl(cc).inc(value - 64, resolution)
                    } else if (msg.channel == 1) {
                        if (value == 127) {
                            shiftedCcs.add(cc)
                        } else if (value == 0) {
                            shiftedCcs.remove(cc)
                        }
                    }
                }
            }
        })
    }

    override fun exit() {}

    override fun flush() {}

    private fun isValidIncrementalValue(value: Int) = value == 63 || value == 65
}
