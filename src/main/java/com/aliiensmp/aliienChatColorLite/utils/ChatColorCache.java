package com.aliiensmp.aliienChatColorLite.utils;

public class ChatColorCache {
    private final String id;
    private final String format;
    private final String permission;

    public ChatColorCache(String id, String format, String permission) {
        this.id = id;
        this.format = format;
        this.permission = permission;
    }

    public String getId() { return id; }
    public String getFormat() { return format; }
    public String getPermission() { return permission; }
}