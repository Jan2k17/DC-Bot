package de.jan2k17.dcbot;

import de.jan2k17.dcbot.Commands.CMD_Broadcast;
import de.jan2k17.dcbot.Commands.CMD_Giveaway;
import de.jan2k17.dcbot.Commands.CMD_Help;
import de.jan2k17.dcbot.Functions.EnterGiveaway;
import de.jan2k17.dcbot.Handler.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;

import java.util.Collections;

public class Main {
    public static void main(String[] args) {
        //MySQL.connect();

        //if(MySQL.isConnected()) {
            String token = "MTA0MjczMTY1OTEwOTM1MTQ1NA.GbPC1h.Ez4ZC_STQxFsC8rU4MTcXKzBRVhQ_BeimFPf4o";
            JDA jda = JDABuilder.createLight(token, Collections.emptyList())
                    .addEventListeners(new CMD_Help())
                    .addEventListeners(new CMD_Broadcast())
                    .addEventListeners(new CMD_Giveaway())
                    //
                    .addEventListeners(new EnterGiveaway())
                    //.addEventListeners(new CMD_GiveRole())
                    .setActivity(Activity.playing("Type /help"))
                    .build();

            jda.updateCommands().addCommands(
                    Commands.slash("help", "list of commands")
                            .setGuildOnly(true),
                    Commands.slash("giveaway", "start a giveaway")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "channel to post the giveaway")
                            .addOption(OptionType.INTEGER, "duration", "duration for giveaway (days)")
                            .addOption(OptionType.STRING, "message", "message for the giveaway"),
                    Commands.slash("activegw", "shows you how many giveaways are active")
                            .setGuildOnly(true),
                    /*Commands.slash("giverole", "gives a member a specific role")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_ROLES))
                            .setGuildOnly(true)
                            .addOption(OptionType.ROLE, "role", "select a role")
                            .addOption(OptionType.USER, "user", "select an user"),*/
                    Commands.slash("broadcast", "To broadcast a message in the specified channel")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.MANAGE_CHANNEL))
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "select a channel")
                            .addOption(OptionType.STRING, "message", "message to broadcast")
            ).queue();
            //CMD_Giveaway.activeGW();
        /*} else {
            System.out.println("No database connection!");
            System.exit(0);
        }*/
    }
}
