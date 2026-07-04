package com.example;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("nowieldinbank")
public interface NoWieldInBankConfig extends Config
{
    @ConfigItem(
            keyName = "hideWield",
            name = "Hide Wield option",
            description = "Removes the Wield option (used for weapons, shields, and ammo like arrows) while the bank is open"
    )
    default boolean hideWield()
    {
        return true;
    }

    @ConfigItem(
            keyName = "hideWear",
            name = "Hide Wear option",
            description = "Removes the Wear option (used for armor and other equipment) while the bank is open"
    )
    default boolean hideWear()
    {
        return true;
    }
}