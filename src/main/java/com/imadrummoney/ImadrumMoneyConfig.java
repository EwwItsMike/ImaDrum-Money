package com.imadrummoney;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("ImaDrum Money")
public interface ImadrumMoneyConfig extends Config
{
	@ConfigSection(
			name = "Situations",
			description = "In which situation would you like to play a soundclip?",
			position = 1
	)
	String situationSettings = "situations";

	@ConfigSection(
			name = "Functional",
			description = "Change functional settings of the plugin",
			position = 0
	)
	String functionalSettings = "functionalSettings";


	@ConfigItem(
			keyName = "value",
			name = "Minimum value",
			description = "Minimum value at which to play the sound",
			section = functionalSettings
	)
	default int minValue() {
		return 500000;
	}

	@ConfigItem(
			keyName = "soundVolume",
			name = "Sound volume",
			description = "Adjust the volume of the soundclip",
			position = 2,
			section = functionalSettings
	)
	default int soundVolume() {
		return 75;
	}

	@ConfigItem(
			keyName = "lootNotif",
			name = "Receive loot notifications",
			description = "Receive sound notifications for loot",
			section = situationSettings
	)
	default boolean receiveLootNotif() { return true; }

	@ConfigItem(
			keyName = "petNotif",
			name = "Receive pet notifications",
			description = "Receive sound notifications for pet drops",
			section = situationSettings
	)
	default boolean receivePetNotif() { return true; }

	@ConfigItem(
			keyName = "clueNotif",
			name = "Receive clue notifications",
			description = "Receive sound notifications for clue scrolls",
			section = situationSettings
	)
	default boolean receiveClueNotif() { return true; }

	@ConfigItem(
			keyName = "barrowsSadness",
			name = "Receive no-Barrows item notifications",
			description = "Receive sound notifications when you don't get a Barrows drop",
			section = situationSettings
	)
	default boolean receiveBarrowsSadnessNotif() {return true;}

	@ConfigItem(
			keyName = "REEEplacement",
			name = "Replace Ruby Bolts spec sound",
			description = "Replace the sound of Ruby Bolts special effect",
			section = situationSettings
	)
	default boolean replaceRubySpecSound() {return true;}

	@ConfigItem(
			keyName = "pleae",
			name = "Play death sound effect",
			description = "Receive sound notifications when you die",
			section = situationSettings
	)
	default boolean receiveDeathSoundNotif() { return true; }

	@ConfigItem(
			keyName = "Superior",
			name = "Receive Superior spawn notifications",
			description = "Receive sound notifications for Superior creature spawns",
			section = situationSettings
	)
	default boolean receiveSuperiorSoundNotif() { return true; }
}
