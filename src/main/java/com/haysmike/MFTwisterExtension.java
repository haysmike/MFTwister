package com.haysmike;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.DoubleValueChangedCallback;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.ControllerHost;
import com.bitwig.extension.controller.api.MidiOut;
import com.bitwig.extension.controller.api.Parameter;
import com.bitwig.extension.controller.api.UserControlBank;

import java.util.HashSet;
import java.util.Set;

public class MFTwisterExtension extends ControllerExtension {
    private static final int numControlStepsDefault = 128;
    private static final int numControlStepsShifted = 256;
    private static final int maxMidiValue = 127;
    private static final int numControls = 64;

    private static final Set<Integer> shiftedCcs = new HashSet<>();

    protected MFTwisterExtension(final MFTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }

    @Override
    public void init() {
        final ControllerHost host = getHost();

        MidiOut midiOut = host.getMidiOutPort(0);
        UserControlBank userControlBank = host.createUserControls(numControls);
        for (int i = 0; i < numControls; i++) {
            final int cc = i;
            Parameter control = userControlBank.getControl(cc);
            String label = "CC " + cc;
            control.setLabel(label);
            control.value().addValueObserver(new DoubleValueChangedCallback() {
                @Override
                public void valueChanged(double value) {
                    midiOut.sendMidi(176, cc, (int) (value * maxMidiValue));
                }
            });
        }

        host.getMidiInPort(0).setMidiCallback(new ShortMidiMessageReceivedCallback() {
            @Override
            public void midiReceived(ShortMidiMessage msg) {
                int cc = msg.getData1();
                int value = msg.getData2();
                if (msg.isControlChange() && msg.getChannel() == 1 && cc < numControls) {
                    if (value == 127) {
                        shiftedCcs.add(cc);
                    } else if (value == 0) {
                        shiftedCcs.remove(cc);
                    }
                } else if (msg.isControlChange() && msg.getChannel() == 0 && cc < numControls && isValidIncrementalValue(value)) {
                    int resolution = shiftedCcs.contains(cc) ? numControlStepsShifted : numControlStepsDefault;
                    userControlBank.getControl(cc).inc(value - 64, resolution);
                }
            }
        });
    }

    @Override
    public void exit() {}

    @Override
    public void flush() {}

    private boolean isValidIncrementalValue(int value) {
        return value == 63 || value == 65;
    }
}
