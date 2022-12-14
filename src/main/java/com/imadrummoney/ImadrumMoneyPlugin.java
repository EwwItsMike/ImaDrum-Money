package com.imadrummoney;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.TileItem;
import net.runelite.api.events.ItemSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.game.ItemManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@Slf4j
@PluginDescriptor(
        name = "ImaDrum Money",
        description = "Plays a sound clip by ImaDrum whenever you receive a good drop"
)
public class ImadrumMoneyPlugin extends Plugin {
    @Inject
    private ImadrumMoneyConfig config;

    @Inject
	private ItemManager itemManager;

    @Inject
	private SoundclipManager soundclipManager;

    @Subscribe
    public void onItemSpawned(ItemSpawned event) {
		TileItem spawned = event.getItem();
    	if (spawned == null)
    		return;

		int stackValue = itemManager.getItemPrice(spawned.getId()) * spawned.getQuantity();

		//Load and play the sound clip in a different thread to prevent lag
        Thread t = new Thread(() -> {
            if (stackValue >= config.minValue()){
                soundclipManager.playClip(soundclipManager.getRandomSoundclip());
            }
        });
        t.start();
    }

    @Provides
    ImadrumMoneyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ImadrumMoneyConfig.class);
    }
}
