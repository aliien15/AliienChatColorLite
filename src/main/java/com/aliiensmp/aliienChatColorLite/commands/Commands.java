package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.AliienChatColorLite;
import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.aliienChatColorLite.utils.ChatColorCache;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class Commands implements CommandExecutor {
    private final AliienChatColorLite plugin;
    private final ChatColorManager colorManager;
    private final MiniMessage mm = MiniMessage.miniMessage();

    public Commands(AliienChatColorLite plugin, ChatColorManager colorManager) {
        this.plugin = plugin;
        this.colorManager = colorManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(mm.deserialize(colorManager.getIncorrectCmdUsageMsg()));
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "set": {
                if (!(sender instanceof Player player)) return true;

                if (!player.hasPermission("aliien.chatcolor.set")) {
                    sender.sendMessage(mm.deserialize(colorManager.getNoPermsMsg()));
                    return true;
                }

                if (args.length < 2) {
                    sender.sendMessage(mm.deserialize(colorManager.getIncorrectCmdUsageMsg()));
                    return true;
                }

                String chosenColor = args[1].toLowerCase();
                ChatColorCache color = colorManager.getCachedColors().get(chosenColor);

                if (color == null) {
                    sender.sendMessage(mm.deserialize(colorManager.getInvalidColorMsg()));
                    return true;
                }

                if (!sender.hasPermission(color.getPermission())) {
                    sender.sendMessage(mm.deserialize(colorManager.getNoPermsMsg()));
                    return true;
                }

                colorManager.setPlayerColor(player, chosenColor);
                sender.sendMessage(mm.deserialize(colorManager.getSuccessMsg()));
                return true;
            }
            case "clear": {
                if (!(sender instanceof Player player)) return true;

                if (!player.hasPermission("aliien.chatcolor.clear")) {
                    sender.sendMessage(mm.deserialize(colorManager.getNoPermsMsg()));
                    return true;
                }

                colorManager.removePlayerColor(player);
                sender.sendMessage(mm.deserialize(colorManager.getClearColorMsg()));
                return true;
            }
            case "admin":
            case "a": {
                if (!sender.hasPermission("aliien.chatcolor.admin")) {
                    sender.sendMessage(mm.deserialize(colorManager.getNoPermsMsg()));
                    return true;
                }

                if (args.length > 1 && args[1].equalsIgnoreCase("reload")) {
                    if (!sender.hasPermission("aliien.chatcolor.admin.reload")) {
                        sender.sendMessage(mm.deserialize(colorManager.getNoPermsMsg()));
                        return true;
                    }

                    try {
                        plugin.getCustomConfig().reload();
                        plugin.getMessages().reload();
                        colorManager.loadColorsToCache();
                        sender.sendMessage(mm.deserialize(colorManager.getReloadMsg()));
                    } catch (Exception e) {
                        sender.sendMessage(mm.deserialize(colorManager.getFailReloadMsg()));
                    }
                    return true;
                }
            }
        }

        sender.sendMessage(mm.deserialize(colorManager.getIncorrectCmdUsageMsg()));
        return true;
    }
}