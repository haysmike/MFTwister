package com.haysmike;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.callback.DoubleValueChangedCallback;
import com.bitwig.extension.callback.ShortMidiMessageReceivedCallback;
import com.bitwig.extension.controller.api.*;
import com.bitwig.extension.controller.ControllerExtension;

public class MFTwisterExtension extends ControllerExtension {
    private static final int numMidiSteps = 128;
    private static final int maxMidiValue = numMidiSteps - 1;
    private static final int numControls = 16;

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
                host.showPopupNotification(msg.toString());
                int cc = msg.getData1();
                if (msg.isControlChange() && msg.getChannel() == 0 && cc < numControls) {
                    userControlBank.getControl(cc).inc(msg.getData2() - 64, numMidiSteps);
                }
            }
        });

        host.showPopupNotification("MFTwister Initialized");
    }

    @Override
    public void exit() {
        getHost().showPopupNotification("MFTwister Exited");
    }

    @Override
    public void flush() {}
}
