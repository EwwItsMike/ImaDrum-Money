package com.imadrummoney;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.NpcLootReceived;
import net.runelite.client.events.PlayerLootReceived;
import net.runelite.client.game.ItemManager;
import net.runelite.client.game.ItemStack;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.loottracker.LootReceived;
import net.runelite.http.api.loottracker.LootRecordType;

import java.util.Collection;

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
    public void onNpcLootReceived(NpcLootReceived npcLootReceived){
        handleReceivedLoot(npcLootReceived.getItems());
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived playerLootReceived){
        handleReceivedLoot(playerLootReceived.getItems());
    }

    @Subscribe
    public void onLootReceived(LootReceived lootReceived) {
        if (lootReceived.getType() != LootRecordType.EVENT && lootReceived.getType() != LootRecordType.PICKPOCKET) {
            return;
        }
        handleReceivedLoot(lootReceived.getItems());
    }

    private void handleReceivedLoot(Collection<ItemStack> items) {
        for (ItemStack stack : items) {
            int value = itemManager.getItemPrice(stack.getId()) * stack.getQuantity();

            if (value >= config.minValue()) {
                new Thread(() -> {
                    soundclipManager.playClip(soundclipManager.getRandomSoundclip());
                }).start();
                return;
            }
        }
    }

    @Provides
    ImadrumMoneyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ImadrumMoneyConfig.class);
    }
}
