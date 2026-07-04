package com.example;

import com.google.inject.Provides;
import java.util.Arrays;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.gameval.InterfaceID;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

@PluginDescriptor(
        name = "No Wield In Bank",
        description = "Removes the Wield and/or Wear options from items in your inventory while the bank is open, to prevent accidental equips when depositing all",
        tags = {"bank", "wield", "wear", "equip", "menu", "qol"}
)
public class NoWieldInBankPlugin extends Plugin
{
    @Inject
    private Client client;

    @Inject
    private NoWieldInBankConfig config;

    @Provides
    NoWieldInBankConfig provideConfig(ConfigManager configManager)
    {
        return configManager.getConfig(NoWieldInBankConfig.class);
    }

    @Subscribe
    public void onMenuEntryAdded(MenuEntryAdded event)
    {
        // Only act while the bank is open — same reasoning as No-Drink-In-Bank:
        // this targets accidental clicks while your inventory is docked next to
        // the bank and you're right-clicking to deposit items.
        if (!isBankOpen())
        {
            return;
        }

        String option = event.getOption();
        if (option == null)
        {
            return;
        }

        String lowerOption = option.toLowerCase();

        // "Wield" covers weapons, shields, and ammo (e.g. arrows).
        // "Wear" covers armor and other wearable equipment.
        // Each is independently toggleable via the plugin's config panel.
        boolean shouldRemove =
                (config.hideWield() && lowerOption.startsWith("wield"))
                        || (config.hideWear() && lowerOption.startsWith("wear"));

        if (!shouldRemove)
        {
            return;
        }

        removeCurrentEntry();
    }

    /**
     * Removes the most recently added menu entry (the one that triggered onMenuEntryAdded)
     * by rebuilding the client's menu entry array without it.
     */
    private void removeCurrentEntry()
    {
        MenuEntry[] entries = client.getMenu().getMenuEntries();

        if (entries.length == 0)
        {
            return;
        }

        // The entry that was just added is always the last one in the array
        MenuEntry[] newEntries = Arrays.copyOf(entries, entries.length - 1);
        client.getMenu().setMenuEntries(newEntries);
    }

    private boolean isBankOpen()
    {
        return client.getWidget(InterfaceID.BANKMAIN, 0) != null;
    }
}