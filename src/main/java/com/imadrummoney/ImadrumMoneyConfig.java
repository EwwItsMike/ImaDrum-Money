package com.imadrummoney;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ImaDrum Money")
public interface ImadrumMoneyConfig extends Config
{
	@ConfigItem(
			keyName = "value",
			name = "Minimum value",
			description = "Minimum value at which to play the sound"
	) default int minValue() {
		return 500000;
	}

	@ConfigItem(
			keyName = "soundVolume",
			name = "Sound volume",
			description = "Adjust the volume of the soundclip",
			position = 2
	)
	default int soundVolume() {
		return 75;
	}

	@ConfigItem(
			keyName = "lootNotif",
			name = "Receive loot notifications",
			description = "Receive sound notifications for loot"
	)
	default boolean receiveLootNotif() { return true; }

	@ConfigItem(
			keyName = "petNotif",
			name = "Receive pet notifications",
			description = "Receive sound notifications for pet drops"
	)
	default boolean receivePetNotif() { return true; }

	@ConfigItem(
			keyName = "clueNotif",
			name = "Receive clue notifications",
			description = "Receive sound notifications for clue scrolls"
	)
	default boolean receiveClueNotif() { return true; }


}
