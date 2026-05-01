package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

/**
 * Handles administrative chat color commands.
 */
public class AdminCommands extends CommandSupport {

    private final AliienChatColorLite plugin;

    /**
     * Creates the admin command handler.
     *
     * @param plugin owning plugin instance
     * @param chatColorManager message manager
     */
    public AdminCommands(AliienChatColorLite plugin, ChatColorManager chatColorManager) {
        super(chatColorManager);
        this.plugin = plugin;
    }

    /**
     * Handles {@code /chatcolor admin} subcommands.
     *
     * @param sender command sender
     * @param args raw command arguments
     * @return always {@code true} after processing
     */
    public boolean handle(CommandSender sender, String[] args) {
        if (!sender.hasPermission("aliien.chatcolor.admin")) {
            sendNoPermission(sender);
            return true;
        }

        if (args.length > 1 && args[1].equalsIgnoreCase("reload")) {
            return handleReload(sender);
        }

        sendIncorrectUsage(sender);
        return true;
    }

    /**
     * Reloads plugin files and runtime caches.
     *
     * @param sender command sender
     * @return always {@code true} after processing
     */
    private boolean handleReload(CommandSender sender) {
        if (!sender.hasPermission("aliien.chatcolor.admin.reload")) {
            sendNoPermission(sender);
            return true;
        }

        try {
            plugin.getCustomConfig().reload();
            plugin.getMessages().reload();
            plugin.reloadRuntimeState();
            sendMessage(sender, chatColorManager.getReloadMsg());
        } catch (Exception exception) {
            sendMessage(sender, chatColorManager.getFailReloadMsg());
            plugin.getLogger().log(Level.SEVERE, "Failed to reload AliienChatColorLite.", exception);
        }

        return true;
    }
}
