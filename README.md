# MFTwister

A Bitwig Extension for the DJ TechTools MIDI Fighter Twister

## Description

This extension is intended to be very simple. It explicitly avoids navigation and Remote Control integration, opting instead for manual user control mapping. While this requires a bit more upfront setup, I find this workflow preferable because it allows me to enjoy tweaking parameters across many tracks and devices without looking at the screen.

## Features

- Endless encoder integration: Once assigned, each knob’s LED ring shows the control’s value.
- Avoids unwanted MIDI CC messages when changing banks.
- “Shifted” CCs: Pressing an encoder down makes it more precise.

## Upcoming Features

- Configurable resolution.
- Color LED integration.

## MIDI Fighter Twister Setup

- If you haven’t already, install the MIDI Fighter Utility. Go to [MIDI Fighter Setup](https://store.djtechtools.com/pages/midi-fighter-setup#MFT) to download the utility and view the manual.
- In the MIDI Fighter Utility, set all of your encoders to incremental encoders:
  - Click the “Multiple” button to edit multiple encoders at once.
  - Select all 16 encoders.
  - Change the “Encoder MIDI Type” to “Enc 3FH/41H”. Repeat this step for each bank.
  - Click “Send to Midi Fighter”.

## Extension Installation

- Install the extension by copying `MFTwister.bwextension` into your `Documents/Bitwig Studio/Extensions` folder.
- Set up the extension in Bitwig by going to Settings → Controllers. You may need to manually add the controller and set its MIDI input and output.

## Development

- Install [`asdf`](https://asdf-vm.com/) and its plugins for [Java](https://github.com/halcyon/asdf-java) and [Gradle](https://github.com/rfrancis/asdf-gradle).
- Run `asdf install` in this directory.
- Set `bitwigExtensionsDir` to your Bitwig Extensions directory (absolute path) in your `$GRADLE_USER_HOME/gradle.properties` file.
- Run the `install` task via IntelliJ’s Gradle view or `./gradlew install`.

## Inspiration and Credits

- [Jürgen Moßgraber](https://www.mossgrabers.de/), and particularly his [YouTube playlist on Bitwig extension development](https://www.youtube.com/playlist?list=PLqRWeSPiYQ66KBGONBenPv1O3luQCFQR2)
- [Keith McMillen’s articles on Bitwig scripting](https://www.keithmcmillen.com/category/blog/tutorials/bitwig-studio/)
- [Bitwig’s open source extensions repository](https://github.com/bitwig/bitwig-extensions)
- [TwisterSister](https://github.com/dozius/TwisterSister)
