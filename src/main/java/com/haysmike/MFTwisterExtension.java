package com.haysmike;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;

public class MFTwisterExtension extends ControllerExtension {
    private static final int numControls = 64;
    private static final int maxMidiCcValue = 127;
    public static final double defaultSensitivity = 1.0;
    public static final double shiftedSensitivity = 0.5;

    private HardwareSurface hardwareSurface;
    private MidiOut midiOut;

    protected MFTwisterExtension(final MFTwisterExtensionDefinition definition, final ControllerHost host) {
        super(definition, host);
    }

    @Override
    public void init() {
        final ControllerHost host = getHost();

        MidiIn midiIn = host.getMidiInPort(0);
        midiOut = host.getMidiOutPort(0);

        hardwareSurface = host.createHardwareSurface();

        for (int i = 0; i < numControls; i++) {
            final int cc = i;
            final int encoderNumber = i + 1;
            RelativeHardwareKnob relativeHardwareKnob = hardwareSurface.createRelativeHardwareKnob("Encoder " + encoderNumber);
            relativeHardwareKnob.setAdjustValueMatcher(midiIn.createRelativeBinOffsetCCValueMatcher(0, cc, maxMidiCcValue));
            relativeHardwareKnob.modulatedTargetValue().addValueObserver(value -> sendMidiCc(0, cc, (int) (value * maxMidiCcValue)));
            relativeHardwareKnob.hasTargetValue().addValueObserver(value -> sendMidiCc(2, cc, value ? 47 : 17));

            HardwareButton hardwareButton = hardwareSurface.createHardwareButton("Switch " + encoderNumber);
            HardwareAction pressedAction = hardwareButton.pressedAction();
            pressedAction.setActionMatcher(midiIn.createCCActionMatcher(1, cc, maxMidiCcValue));
            pressedAction.setBinding(host.createCallbackAction(() -> relativeHardwareKnob.setSensitivity(shiftedSensitivity), () -> "Handle switch pressed"));
            HardwareAction releasedAction = hardwareButton.releasedAction();
            releasedAction.setActionMatcher(midiIn.createCCActionMatcher(1, cc, 0));
            releasedAction.setBinding(host.createCallbackAction(() -> relativeHardwareKnob.setSensitivity(defaultSensitivity), () -> "Handle switch released"));
        }
    }

    @Override
    public void exit() {
    }

    @Override
    public void flush() {
        hardwareSurface.updateHardware();
    }

    private void sendMidiCc(int channel, int cc, int value) {
        midiOut.sendMidi(ShortMidiMessage.CONTROL_CHANGE | channel, cc, value);
    }
}
