package com.imadrummoney;

import com.google.inject.Provides;

import javax.inject.Inject;

import lombok.extern.slf4j.Slf4j;
import net.runelite.api.*;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.SoundEffectPlayed;
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

import java.util.*;

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

    @Inject
    Client client;

    // Pet Drops
    private static final String FOLLOW_PET = "You have a funny feeling like you're being followed";
    private static final String INVENTORY_PET = "You feel something weird sneaking into your backpack";
    private static final String DUPE_PET = "You have a funny feeling like you would have been followed";
    private static final String SUPERIOR = "A superior foe has appeared...";
    private static final String DEATH = "Oh dear, you are dead!";

    private static final ArrayList<String> BARROWS_ITEMS = new ArrayList<>(Arrays.asList("Ahrim's", "Karil's", "Guthan's", "Dharok's", "Verac's", "Torag's"));


    @Subscribe
    public void onNpcLootReceived(NpcLootReceived npcLootReceived) {
        handleReceivedLoot(npcLootReceived.getItems(), npcLootReceived.getNpc().getName());
    }

    @Subscribe
    public void onPlayerLootReceived(PlayerLootReceived playerLootReceived) {
        handleReceivedLoot(playerLootReceived.getItems(), playerLootReceived.getPlayer().getName());
    }

    @Subscribe
    public void onLootReceived(LootReceived lootReceived) {
        if (lootReceived.getType() != LootRecordType.EVENT && lootReceived.getType() != LootRecordType.PICKPOCKET) {
            return;
        }
        handleReceivedLoot(lootReceived.getItems(), lootReceived.getName());
    }

    @Subscribe
    public void onChatMessage(ChatMessage chatmessage) {

        if (chatmessage.getType() != ChatMessageType.GAMEMESSAGE) {
            return;
        }

        String message = chatmessage.getMessage();

        if ((message.contains(FOLLOW_PET) || message.contains(INVENTORY_PET)) && config.receivePetNotif()) {
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getNewPetSound());
            }).start();
        } else if (message.contains(DUPE_PET) && config.receivePetNotif()) {
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getDupePetSound());
            }).start();
        } else if (message.contains(SUPERIOR) && config.receiveSuperiorSoundNotif()) {
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getSuperiorSound());
            }).start();
        } else if (message.contains(DEATH) && config.receiveDeathSoundNotif()) {
            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getDeathSound());
            }).start();
        }
    }

    @Subscribe
    public void onSoundEffectPlayed(SoundEffectPlayed event) {
        if (!config.replaceRubySpecSound())
            return;

        if (event.getSoundId() == 2911) {
            event.consume();

            soundclipManager.playClip(soundclipManager.getRubySpecSound());
        }
    }

    private void handleReceivedLoot(Collection<ItemStack> items, String name) {
        if (name != null && name.equals("Barrows")
                && !containsBarrowsItem(items)
                && config.receiveBarrowsSadnessNotif()) {

            new Thread(() -> {
                soundclipManager.playClip(soundclipManager.getRandomSadSoundClip());
            }).start();
            return;
        }

        for (ItemStack stack : items) {
            int value = itemManager.getItemPrice(stack.getId()) * stack.getQuantity();

            if (value >= config.minValue() && config.receiveLootNotif()) {
                new Thread(() -> {
                    soundclipManager.playClip(soundclipManager.getRandomSoundclip());
                }).start();
                return;
            }

            if (isClueScroll(stack.getId()) && config.receiveClueNotif()) {
                new Thread(() -> {
                    soundclipManager.playClip(soundclipManager.getClueSound());
                }).start();
                return;
            }

        }
    }

    private Boolean isClueScroll(Integer ID) {
        ItemComposition itemComposition = itemManager.getItemComposition(ID);

        return itemComposition.getName().toLowerCase(Locale.ROOT).contains("clue scroll");
    }

    private Boolean containsBarrowsItem(Collection<ItemStack> items) {
        for (ItemStack item : items) {
            ItemComposition comp = itemManager.getItemComposition(item.getId());
            for (String barrowsName : BARROWS_ITEMS) {
                if (comp.getName().contains(barrowsName)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Provides
    ImadrumMoneyConfig provideConfig(ConfigManager configManager) {
        return configManager.getConfig(ImadrumMoneyConfig.class);
    }
}
