package com.radiofreederp.nodebbintegration.commands;

public interface IMinecraftCommand {
    boolean doCommand(Object sender, String action, String option, String value);
}
