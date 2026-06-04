package com.aliiensmp.aliienChatColorLite;

import com.aliiensmp.aliienChatColorLite.cache.ChatColorRegistry;
import com.aliiensmp.aliienChatColorLite.cache.PlayerColorCache;
import com.aliiensmp.aliienChatColorLite.commands.AdminCommands;
import com.aliiensmp.aliienChatColorLite.commands.PlayerCommands;
import com.aliiensmp.aliienChatColorLite.commands.TabAutoCompleter;
import com.aliiensmp.aliienChatColorLite.hooks.PlaceholderAPIExpansion;
import com.aliiensmp.aliienChatColorLite.listeners.ChatListener;
import com.aliiensmp.aliienChatColorLite.listeners.PlayerConnectionListener;
import com.aliiensmp.aliienChatColorLite.service.PlayerColorService;
import com.aliiensmp.core.AliienCore;
import com.aliiensmp.core.config.ConfigManager;
import com.aliiensmp.core.lib.boostedyaml.YamlDocument;
import com.aliiensmp.core.utils.ColorUtils;
import com.aliiensmp.core.utils.updatechecker.UpdateChecker;
import com.aliiensmp.core.utils.updatechecker.UpdateNotifyListener;
import org.bstats.bukkit.Metrics;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.logging.Level;

public final class AliienChatColorLite extends JavaPlugin {

    private static final String UPDATE_URL = "https://gist.githubusercontent.com/aliien15/2e93ffeea8b4cce3366498da2a50b4f2/raw/AliienChatColorLite-version";

    private YamlDocument config;
    private YamlDocument messages;
    private ChatColorManager chatColorManager;
    private ChatColorRegistry colorRegistry;
    private PlayerColorCache playerColorCache;
    private PlayerColorService playerColorService;
    private PlaceholderAPIExpansion expansion;

    @Override
    public void onEnable() {
        AliienCore.init(this);

        try {
            loadFiles();
        } catch (IOException e) {
            getLogger().log(Level.SEVERE, "Failed to load files!", e);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        colorRegistry = new ChatColorRegistry();
        chatColorManager = new ChatColorManager(messages);
        playerColorCache = new PlayerColorCache();
        playerColorService = new PlayerColorService(this, colorRegistry, playerColorCache);
        reloadRuntimeState();

        registerCommands();
        registerListeners();
        checkForUpdates();
        placeholderApiHook();
        Metrics metrics = new Metrics(this, 31026);

        getLogger().info("AliienChatColorLite Lite enabled!");
    }

    @Override
    public void onDisable() {
        if (playerColorCache != null) {
            playerColorCache.clear();
        }

        getLogger().info("AliienChatColorLite Lite disabled!");
    }

    /**
     * Reloads runtime caches from the already loaded YAML documents.
     */
    public void reloadRuntimeState() {
        int loadedColors = colorRegistry.reload(config);
        chatColorManager.reload();
        if (playerColorService != null) {
            playerColorService.refreshOnlinePlayers();
        }
        getLogger().info("Successfully loaded " + loadedColors + " chat colors into cache!");
    }

    /**
     * Gets the loaded config document.
     *
     * @return plugin config document
     */
    public YamlDocument getCustomConfig() {
        return config;
    }

    /**
     * Gets the loaded messages document.
     *
     * @return plugin messages document
     */
    public YamlDocument getMessages() {
        return messages;
    }

    /**
     * Loads all versioned YAML files through AliienCore.
     *
     * @throws IOException when a file cannot be loaded
     */
    private void loadFiles() throws IOException {
        config = ConfigManager.loadConfig(this, "config.yml");
        messages = ConfigManager.loadConfig(this, "messages.yml");
    }

    /**
     * Registers command executors and tab completion.
     */
    private void registerCommands() {
        AdminCommands adminCommands = new AdminCommands(this, chatColorManager);
        PluginCommand chatColorCommand = Objects.requireNonNull(getCommand("chatcolor"), "chatcolor command is missing from plugin.yml");
        chatColorCommand.setExecutor(new PlayerCommands(chatColorManager, colorRegistry, playerColorService, adminCommands));
        chatColorCommand.setTabCompleter(new TabAutoCompleter(colorRegistry));
    }

    /**
     * Registers plugin event listeners.
     */
    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new ChatListener(playerColorService), this);
        getServer().getPluginManager().registerEvents(new PlayerConnectionListener(playerColorService), this);
        getServer().getPluginManager().registerEvents(new UpdateNotifyListener(
                this,
                UPDATE_URL,
                "aliien.chatcolor.version-notify",
                () -> ColorUtils.color(chatColorManager.getNewVersionMsg())
        ), this);
    }

    /**
     * Registers the PlaceholderAPI expansion when PlaceholderAPI is present.
     */
    private void placeholderApiHook() {
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.expansion = new PlaceholderAPIExpansion(this, colorRegistry, playerColorService);
            if (this.expansion.register()) getLogger().info("Successfully hooked into PlaceholderAPI!");
        } else {
            getLogger().warning("Could not hook into PlaceholderAPI!");
        }
    }

    /**
     * Checks the remote version once during startup.
     */
    private void checkForUpdates() {
        new UpdateChecker(this, UPDATE_URL).getVersion(version -> {
            if (this.getPluginMeta().getVersion().equals(version)) {
                getLogger().info("AliienChatColorLite is up to date!");
            } else {
                getLogger().warning("A new update is available for AliienChatColorLite!");
            }
        });
    }
}
