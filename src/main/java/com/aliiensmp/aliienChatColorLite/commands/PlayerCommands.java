package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorCache;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorRegistry;
import com.aliiensmp.aliienChatColorLite.service.PlayerColorService;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Handles player-facing chat color commands.
 */
public class PlayerCommands extends CommandSupport implements CommandExecutor {

    private final ChatColorRegistry colorRegistry;
    private final PlayerColorService playerColorService;
    private final AdminCommands adminCommands;

    /**
     * Creates the player command handler.
     *
     * @param chatColorManager message manager
     * @param colorRegistry configured color registry
     * @param playerColorService player color service
     * @param adminCommands admin command handler
     */
    public PlayerCommands(
            ChatColorManager chatColorManager,
            ChatColorRegistry colorRegistry,
            PlayerColorService playerColorService,
            AdminCommands adminCommands
    ) {
        super(chatColorManager);
        this.colorRegistry = colorRegistry;
        this.playerColorService = playerColorService;
        this.adminCommands = adminCommands;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 0) {
            sendIncorrectUsage(sender);
            return true;
        }

        return switch (args[0].toLowerCase()) {
            case "set" -> handleSet(sender, args);
            case "clear" -> handleClear(sender);
            case "admin", "a" -> adminCommands.handle(sender, args);
            default -> {
                sendIncorrectUsage(sender);
                yield true;
            }
        };
    }

    /**
     * Handles {@code /chatcolor set <color>}.
     *
     * @param sender command sender
     * @param args raw command arguments
     * @return always {@code true} after processing
     */
    private boolean handleSet(CommandSender sender, String[] args) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }

        if (!player.hasPermission("aliien.chatcolor.set")) {
            sendNoPermission(sender);
            return true;
        }

        if (args.length < 2) {
            sendIncorrectUsage(sender);
            return true;
        }

        ChatColorCache color = colorRegistry.getColor(args[1]);
        if (color == null) {
            sendMessage(sender, chatColorManager.getInvalidColorMsg());
            return true;
        }

        if (!colorRegistry.hasAccess(sender, color)) {
            sendNoPermission(sender);
            return true;
        }

        playerColorService.setPlayerColor(player, color.id());
        sendMessage(sender, chatColorManager.getSuccessMsg());
        return true;
    }

    /**
     * Handles {@code /chatcolor clear}.
     *
     * @param sender command sender
     * @return always {@code true} after processing
     */
    private boolean handleClear(CommandSender sender) {
        Player player = requirePlayer(sender);
        if (player == null) {
            return true;
        }

        if (!player.hasPermission("aliien.chatcolor.clear")) {
            sendNoPermission(sender);
            return true;
        }

        playerColorService.removePlayerColor(player);
        sendMessage(sender, chatColorManager.getClearColorMsg());
        return true;
    }
}
