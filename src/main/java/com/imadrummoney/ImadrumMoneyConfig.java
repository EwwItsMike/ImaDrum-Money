package com.imadrummoney;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("ImaDrum Money")
public interface ImadrumMoneyConfig extends Config
{
//	@ConfigItem(
//		keyName = "greeting",
//		name = "Welcome Greeting",
//		description = "The message to show to the user when they login"
//	)
//	default String greeting()
//	{
//		return "Hello";
//	}

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

}
