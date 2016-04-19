package com.radiofreederp.nodebbintegration.commands;

/**
 * Created by Yari on 4/17/2016.
 */
public interface IMinecraftCommand {
    boolean doCommand(Object sender, String action, String option, String value);
}
