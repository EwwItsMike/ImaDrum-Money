package com.imadrummoney;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.ItemComposition;
import net.runelite.api.events.ChatMessage;
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

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

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

    // Pet Drops
    private static final String FOLLOW_PET = "You have a funny feeling like you're being followed";
    private static final String INVENTORY_PET = "You feel something weird sneaking into your backpack";
    private static final String DUPE_PET = "You have a funny feeling like you would have been followed";



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

    @Subscribe
    public void onChatMessage(ChatMessage chatmessage){
        if (chatmessage.getType() != ChatMessageType.GAMEMESSAGE)
            return;

        String message = chatmessage.getMessage();

        if ((message.contains(FOLLOW_PET) || message.contains(INVENTORY_PET)) && config.receivePetNotif()) {
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getNewPetSound());
            }).start();
        }
        else if (message.contains(DUPE_PET) && config.receivePetNotif()){
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getDupePetSound());
            }).start();
        }
    }

    private void handleReceivedLoot(Collection<ItemStack> items) {
        for (ItemStack stack : items) {
            int value = itemManager.getItemPrice(stack.getId()) * stack.getQuantity();

            if (value >= config.minValue() && config.receiveLootNotif()) {
                new Thread(() -> {
                    soundclipManager.playClip(soundclipManager.getRandomSoundclip());
                }).start();
                return;
            }

            if (isClueScroll(stack.getId()) && config.receiveClueNotif()){
                new Thread(() -> {
                    soundclipManager.playClip(soundclipManager.getClueSound());
                }).start();
                return;
            }

        }
    }

    private Boolean isClueScroll(Integer ID){
        ItemComposition itemComposition = itemManager.getItemComposition(ID);

        return itemComposition.getName().toLowerCase(Locale.ROOT).contains("clue scroll");
    }

    @Provides
    ImadrumMoneyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ImadrumMoneyConfig.class);
    }
}
