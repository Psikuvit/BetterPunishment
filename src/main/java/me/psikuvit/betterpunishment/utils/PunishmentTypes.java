package me.psikuvit.betterpunishment.utils;

public enum PunishmentTypes {
    BAN(true),
    TEMP_BAN(false),
    MUTE(true),
    TEMP_MUTE(false),
    KICK(true),
    BLACKLIST(true);

    private final boolean permanent;

    PunishmentTypes(boolean permanent) {
        this.permanent = permanent;
    }

    public boolean isPermanent() {
        return permanent;
    }
}
