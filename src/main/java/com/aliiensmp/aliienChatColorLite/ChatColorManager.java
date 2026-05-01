package com.aliiensmp.aliienChatColorLite;

import dev.dejvokep.boostedyaml.YamlDocument;

/**
 * Keeps message configuration values in memory for fast command and listener access.
 */
public class ChatColorManager {

    private static final String EMPTY_PREFIX = "";

    private final YamlDocument messages;

    private String invalidColorMsg;
    private String successMsg;
    private String incorrectCmdUsageMsg;
    private String noPermsMsg;
    private String reloadMsg;
    private String failReloadMsg;
    private String clearColorMsg;
    private String newVersionMsg;

    /**
     * Creates the message manager.
     *
     * @param messages loaded messages document
     */
    public ChatColorManager(YamlDocument messages) {
        this.messages = messages;
    }

    /**
     * Reloads message values from the current messages document.
     */
    public void reload() {
        invalidColorMsg = messages.getString("messages.invalid-color", "<red>Sorry, that color is invalid/doesn't exist!");
        successMsg = messages.getString("messages.success", "<green>You have successfully updated your chat color!");
        incorrectCmdUsageMsg = messages.getString("messages.incorrect-cmd-usage", "<red>This command was not used correctly!");
        noPermsMsg = messages.getString("messages.no-perms", "<red>You do not have permission to use this!");
        reloadMsg = messages.getString("messages.reload", "<green>AliienChatColorLite has been reloaded!");
        failReloadMsg = messages.getString("messages.fail-reload", "<red>There was an error while reloading (check console)");
        clearColorMsg = messages.getString("messages.clear-color", "<green>You have successfully cleared the chat color!");
        newVersionMsg = messages.getString("messages.new-version", "<green>A new AliienChatColorLite version is now available!");
    }

    /**
     * Gets the prefix passed to AliienCore message utilities.
     *
     * @return empty prefix for the lite plugin
     */
    public String getPrefix() {
        return EMPTY_PREFIX;
    }

    /**
     * Gets the invalid color message.
     *
     * @return configured invalid color message
     */
    public String getInvalidColorMsg() {
        return invalidColorMsg;
    }

    /**
     * Gets the success message.
     *
     * @return configured success message
     */
    public String getSuccessMsg() {
        return successMsg;
    }

    /**
     * Gets the incorrect command usage message.
     *
     * @return configured incorrect usage message
     */
    public String getIncorrectCmdUsageMsg() {
        return incorrectCmdUsageMsg;
    }

    /**
     * Gets the no permission message.
     *
     * @return configured no permission message
     */
    public String getNoPermsMsg() {
        return noPermsMsg;
    }

    /**
     * Gets the reload success message.
     *
     * @return configured reload message
     */
    public String getReloadMsg() {
        return reloadMsg;
    }

    /**
     * Gets the reload failure message.
     *
     * @return configured reload failure message
     */
    public String getFailReloadMsg() {
        return failReloadMsg;
    }

    /**
     * Gets the clear color message.
     *
     * @return configured clear color message
     */
    public String getClearColorMsg() {
        return clearColorMsg;
    }

    /**
     * Gets the update notification message.
     *
     * @return configured update notification message
     */
    public String getNewVersionMsg() {
        return newVersionMsg;
    }
}
