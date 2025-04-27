package com.haysmike;

import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;

public class MFTwisterExtension extends ControllerExtension {
    private static final int maxMidiCcValue = 127;
    private static final int numControls = 64;

    private HardwareSurface hardwareSurface;

    protected MFTwisterExtension(final MFTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }

    @Override
    public void init() {
        final ControllerHost host = getHost();

        MidiIn midiIn = host.getMidiInPort(0);
        MidiOut midiOut = host.getMidiOutPort(0);

        hardwareSurface = host.createHardwareSurface();

        for (int i = 0; i < numControls; i++) {
            final int cc = i;
            RelativeHardwareKnob relativeHardwareKnob = hardwareSurface.createRelativeHardwareKnob("Knob " + cc);
            relativeHardwareKnob.setAdjustValueMatcher(midiIn.createRelativeBinOffsetCCValueMatcher(0, cc, maxMidiCcValue));
            // TODO: Look into `HardwareLight`
            relativeHardwareKnob.targetValue().addValueObserver(value -> midiOut.sendMidi(176, cc, (int) (value * maxMidiCcValue)));

            HardwareButton hardwareButton = hardwareSurface.createHardwareButton("Switch " + cc);
            HardwareAction pressedAction = hardwareButton.pressedAction();
            pressedAction.setActionMatcher(midiIn.createCCActionMatcher(1, cc, maxMidiCcValue));
            pressedAction.setBinding(host.createCallbackAction(() -> relativeHardwareKnob.setSensitivity(0.5), () -> "Handle switch pressed"));
            HardwareAction releasedAction = hardwareButton.releasedAction();
            releasedAction.setActionMatcher(midiIn.createCCActionMatcher(1, cc, 0));
            releasedAction.setBinding(host.createCallbackAction(() -> relativeHardwareKnob.setSensitivity(1.0), () -> "Handle switch released"));
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void flush() {
        hardwareSurface.updateHardware();
    }
}
