package com.haysmike

import com.bitwig.extension.api.util.midi.ShortMidiMessage
import com.bitwig.extension.callback.IntegerValueChangedCallback
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback
import com.bitwig.extension.controller.ControllerExtension
import com.bitwig.extension.controller.api.ControllerHost
import kotlin.collections.HashSet
import kotlin.collections.MutableSet

class MFTwisterExtension(definition: MFTwisterExtensionDefinition?, host: ControllerHost?) :
    ControllerExtension(definition, host) {
    override fun init() {
        val host = getHost()

        val midiOut = host.getMidiOutPort(0)
        val userControlBank = host.createUserControls(numControls)
        for (i in 0..<numControls) {
            val cc = i
            val control = userControlBank.getControl(cc)
            val label = "CC " + cc
            control.setLabel(label)
            control.value().addValueObserver(midiRange, object : IntegerValueChangedCallback {
                override fun valueChanged(value: Int) {
                    midiOut.sendMidi(176, cc, value)
                }
            })
        }

        host.getMidiInPort(0).setMidiCallback(object : ShortMidiMessageReceivedCallback {
            override fun midiReceived(msg: ShortMidiMessage) {
                val cc = msg.getData1()
                val value = msg.getData2()
                if (msg.isControlChange() && msg.getChannel() == 1 && cc < numControls) {
                    if (value == 127) {
                        shiftedCcs.add(cc)
                    } else if (value == 0) {
                        shiftedCcs.remove(cc)
                    }
                } else if (msg.isControlChange() && msg.getChannel() == 0 && cc < numControls && isValidIncrementalValue(
                        value
                    )
                ) {
                    val resolution: Int =
                        if (shiftedCcs.contains(cc)) numControlStepsShifted else numControlStepsDefault
                    userControlBank.getControl(cc).inc(value - 64, resolution)
                }
            }
        })
    }

    override fun exit() {}

    override fun flush() {}

    private fun isValidIncrementalValue(value: Int): Boolean {
        return value == 63 || value == 65
    }

    companion object {
        private const val numControlStepsDefault = 128
        private const val numControlStepsShifted = 256
        private const val midiRange = 128
        private const val numControls = 64

        private val shiftedCcs: MutableSet<Int?> = HashSet<Int?>()
    }
}
