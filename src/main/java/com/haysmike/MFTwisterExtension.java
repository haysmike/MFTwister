package com.haysmike;

import com.bitwig.extension.api.util.midi.ShortMidiMessage;
import com.bitwig.extension.controller.ControllerExtension;
import com.bitwig.extension.controller.api.*;

public class MFTwisterExtension extends ControllerExtension {
  private static final int NUM_ENCODERS = 64;

  private static final int ENCODER_INPUT_CHANNEL = 0;
  private static final int INDICATOR_RING_OUTPUT_CHANNEL = 0;
  private static final int MAX_CC_VALUE = 127;

  private static final int SWITCH_INPUT_CHANNEL = 1;
  private static final int SWITCH_OFF_VALUE = 0;
  private static final int SWITCH_ON_VALUE = 127;

  private static final int RGB_ANIMATION_OUTPUT_CHANNEL = 2;
  private static final int RGB_OFF_VALUE = 17;
  private static final int RGB_MAX_BRIGHTNESS_VALUE = 47;

  public static final double DEFAULT_ENCODER_SENSITIVITY = 1.0;
  public static final double SHIFTED_ENCODER_SENSITIVITY = 0.5;

  private HardwareSurface hardwareSurface;
  private MidiOut midiOut;

  protected MFTwisterExtension(
      final MFTwisterExtensionDefinition definition, final ControllerHost host) {
    super(definition, host);
  }

  @Override
  public void init() {
    final ControllerHost host = getHost();

    MidiIn midiIn = host.getMidiInPort(0);
    midiOut = host.getMidiOutPort(0);

    hardwareSurface = host.createHardwareSurface();

    for (int i = 0; i < NUM_ENCODERS; i++) {
      final int cc = i;
      final int encoderNumber = i + 1;
      RelativeHardwareKnob relativeHardwareKnob =
          hardwareSurface.createRelativeHardwareKnob(String.format("Enc %02d", encoderNumber));
      relativeHardwareKnob.setAdjustValueMatcher(
          midiIn.createRelativeBinOffsetCCValueMatcher(ENCODER_INPUT_CHANNEL, cc, MAX_CC_VALUE));
      relativeHardwareKnob
          .modulatedTargetValue()
          .addValueObserver(
              value -> sendMidiCc(INDICATOR_RING_OUTPUT_CHANNEL, cc, (int) (value * MAX_CC_VALUE)));
      relativeHardwareKnob
          .hasTargetValue()
          .addValueObserver(
              value ->
                  sendMidiCc(
                          RGB_ANIMATION_OUTPUT_CHANNEL, cc, value ? RGB_MAX_BRIGHTNESS_VALUE : RGB_OFF_VALUE));

      HardwareButton hardwareButton =
          hardwareSurface.createHardwareButton(String.format("Switch %02d", encoderNumber));
      HardwareAction pressedAction = hardwareButton.pressedAction();
      pressedAction.setActionMatcher(
          midiIn.createCCActionMatcher(SWITCH_INPUT_CHANNEL, cc, SWITCH_ON_VALUE));
      pressedAction.setBinding(
          host.createCallbackAction(
              () -> relativeHardwareKnob.setSensitivity(SHIFTED_ENCODER_SENSITIVITY),
              () -> "Handle switch pressed"));
      HardwareAction releasedAction = hardwareButton.releasedAction();
      releasedAction.setActionMatcher(
          midiIn.createCCActionMatcher(SWITCH_INPUT_CHANNEL, cc, SWITCH_OFF_VALUE));
      releasedAction.setBinding(
          host.createCallbackAction(
              () -> relativeHardwareKnob.setSensitivity(DEFAULT_ENCODER_SENSITIVITY),
              () -> "Handle switch released"));
    }
  }

  @Override
  public void exit() {
    for (int cc = 0; cc < NUM_ENCODERS; cc++) {
      sendMidiCc(RGB_ANIMATION_OUTPUT_CHANNEL, cc, RGB_MAX_BRIGHTNESS_VALUE);
    }
  }

  @Override
  public void flush() {
    hardwareSurface.updateHardware();
  }

  private void sendMidiCc(int channel, int cc, int value) {
    midiOut.sendMidi(ShortMidiMessage.CONTROL_CHANGE | channel, cc, value);
  }
}
