package com.aliiensmp.aliienChatColorLite.commands;

import com.aliiensmp.aliienChatColorLite.cache.ChatColorCache;
import com.aliiensmp.aliienChatColorLite.cache.ChatColorRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.jetbrains.annotations.NotNull;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

public class TabAutoCompleter implements TabCompleter {

    private final ChatColorRegistry colorRegistry;

    /**
     * Creates the tab completer.
     *
     * @param colorRegistry configured color registry
     */
    public TabAutoCompleter(ChatColorRegistry colorRegistry) {
        this.colorRegistry = colorRegistry;
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
                for (ChatColorCache color : colorRegistry.getCachedColors().values()) {
                    if (colorRegistry.hasAccess(sender, color) && color.id().toLowerCase().startsWith(args[1].toLowerCase())) {
                        completions.add(color.id());
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
