package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.ChatColorManager;
import com.aliiensmp.aliienChatColorLite.utils.ChatColorCache;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TabAutoCompleter implements TabCompleter {
    private final ChatColorManager colorManager;

    public TabAutoCompleter(ChatColorManager colorManager) {
        this.colorManager = colorManager;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, String @NonNull [] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (sender.hasPermission("aliien.chatcolor.set")) completions.add("set");
            if (sender.hasPermission("aliien.chatcolor.clear")) completions.add("clear");
            if (sender.hasPermission("aliien.chatcolor.admin")) completions.add("admin");

            completions.removeIf(sub -> !sub.startsWith(args[0].toLowerCase()));
            return completions;
        }

        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("set") && sender.hasPermission("aliien.chatcolor.set")) {
                for (ChatColorCache color : colorManager.getCachedColors().values()) {
                    if (sender.hasPermission(color.getPermission()) && color.getId().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(color.getId());
                    }
                }
                return completions;
            }

            if ((args[0].equalsIgnoreCase("admin") || args[0].equalsIgnoreCase("a")) && sender.hasPermission("aliien.chatcolor.admin")) {
                if (sender.hasPermission("aliien.chatcolor.admin.reload") && "reload".startsWith(args[1].toLowerCase())) {
                    completions.add("reload");
                }
                return completions;
            }
        }
        return List.of();
    }
}