package com.haysmike;

import com.bitwig.extension.api.PlatformType;
import com.bitwig.extension.controller.AutoDetectionMidiPortNamesList;
import com.bitwig.extension.controller.ControllerExtensionDefinition;
import com.bitwig.extension.controller.api.ControllerHost;

import java.util.UUID;

public class MFTwisterExtensionDefinition extends ControllerExtensionDefinition {
    private static final UUID DRIVER_ID = UUID.fromString("dc337c96-9a72-47aa-9d60-b8f7766419bb");

    @Override
    public String getName() {
        return "MFTwister";
    }

    @Override
    public String getAuthor() {
        return "haysmike";
    }

    @Override
    public String getVersion() {
        return "0.1";
    }

    @Override
    public UUID getId() {
        return DRIVER_ID;
    }

    @Override
    public String getHardwareVendor() {
        return "DJ TechTools";
    }

    @Override
    public String getHardwareModel() {
        return "MIDI Fighter Twister";
    }

    @Override
    public int getRequiredAPIVersion() {
        return 21;
    }

    @Override
    public int getNumMidiInPorts() {
        return 1;
    }

    @Override
    public int getNumMidiOutPorts() {
        return 1;
    }

    @Override
    public void listAutoDetectionMidiPortNames(final AutoDetectionMidiPortNamesList list, final PlatformType platformType) {
        list.add(new String[]{"Midi Fighter Twister"}, new String[]{"Midi Fighter Twister"});
    }

    @Override
    public MFTwisterExtension createInstance(final ControllerHost host) {
        return new MFTwisterExtension(this, host);
    }
}
