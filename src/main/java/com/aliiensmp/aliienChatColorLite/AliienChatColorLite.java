package com.aliiensmp.aliienChatColorLite;

import com.aliiensmp.aliienChatColorLite.commands.Commands;
import com.aliiensmp.aliienChatColorLite.commands.TabAutoCompleter;
import com.aliiensmp.aliienChatColorLite.hooks.PlaceholderAPIExpansion;
import com.aliiensmp.aliienChatColorLite.listeners.ChatListener;
import com.aliiensmp.aliienChatColorLite.listeners.UpdateNotifyListener;
import dev.dejvokep.boostedyaml.YamlDocument;
import dev.dejvokep.boostedyaml.dvs.versioning.BasicVersioning;
import dev.dejvokep.boostedyaml.settings.dumper.DumperSettings;
import dev.dejvokep.boostedyaml.settings.general.GeneralSettings;
import dev.dejvokep.boostedyaml.settings.loader.LoaderSettings;
import dev.dejvokep.boostedyaml.settings.updater.UpdaterSettings;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public final class AliienChatColorLite extends JavaPlugin {

    private YamlDocument config;
    private YamlDocument messages;
    private ChatColorManager colorManager;
    private PlaceholderAPIExpansion expansion;

    @Override
    public void onEnable() {
        try {
            config = YamlDocument.create(new File(getDataFolder(), "config.yml"), getResource("config.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("config-version")).build());
            messages = YamlDocument.create(new File(getDataFolder(), "messages.yml"), getResource("messages.yml"), GeneralSettings.builder().setUseDefaults(false).build(), LoaderSettings.builder().setAutoUpdate(true).build(), DumperSettings.DEFAULT, UpdaterSettings.builder().setKeepAll(true).setVersioning(new BasicVersioning("config-version")).build());
        } catch (IOException e) {
            getLogger().log(java.util.logging.Level.SEVERE, "Failed to load files!", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        colorManager = new ChatColorManager(this, config, messages);
        colorManager.loadColorsToCache();

        Objects.requireNonNull(getCommand("chatcolor")).setExecutor(new Commands(this, colorManager));
        Objects.requireNonNull(getCommand("chatcolor")).setTabCompleter(new TabAutoCompleter(colorManager));

        String gistUrl = "https://gist.githubusercontent.com/aliien15/2e93ffeea8b4cce3366498da2a50b4f2/raw/AliienChatColorLite-version";
        updateChecker(gistUrl);

        getServer().getPluginManager().registerEvents(new ChatListener(colorManager), this);
        getServer().getPluginManager().registerEvents(new UpdateNotifyListener(this, colorManager, gistUrl), this);

        placeholderApiHook();
        getLogger().info("AliienChatColorLite Lite enabled!");
    }

    @Override
    public void onDisable() {
        getLogger().info("AliienChatColorLite Lite disabled!");
    }

    private void placeholderApiHook() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.expansion = new PlaceholderAPIExpansion(this, colorManager);
            if (this.expansion.register()) getLogger().info("Successfully hooked into PlaceholderAPI!");
        } else {
            getLogger().warning("Could not hook into PlaceholderAPI!");
        }
    }

    private void updateChecker(String gistUrl) {
        new com.aliiensmp.aliienChatColorLite.utils.UpdateChecker(this, gistUrl).getVersion(version -> {
            if (this.getPluginMeta().getVersion().equals(version)) {
                getLogger().info("AliienChatColorLite is up to date!");
            } else {
                getLogger().warning("A new update is available for AliienChatColorLite!");
            }
        });
    }

    public YamlDocument getCustomConfig() { return config; }
    public YamlDocument getMessages() { return messages; }
}