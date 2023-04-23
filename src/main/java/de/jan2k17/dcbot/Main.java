package de.jan2k17.dcbot;

import de.jan2k17.dcbot.Commands.*;
import de.jan2k17.dcbot.Functions.EVENT_SendMessage;
import de.jan2k17.dcbot.Functions.GuildJoin;
import de.jan2k17.dcbot.Handler.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        MySQL.connect();

        if(!MySQL.isConnected()) {
            System.out.println("No database connection!");
            System.exit(0);
        } else {
            List<GatewayIntent> gateway = new ArrayList<>();
            for(GatewayIntent intent : GatewayIntent.values()){
                gateway.add(intent);
            }
            JDA jda = JDABuilder.createLight(Token.token, Collections.emptyList()).enableIntents(gateway)
                    .addEventListeners(new CMD_Help())
                    .addEventListeners(new CMD_Broadcast())
                    .addEventListeners(new CMD_Giveaway())
                    .addEventListeners(new GuildJoin())
                    .addEventListeners(new CMD_GiveRole())
                    .addEventListeners(new CMD_settings())
                    .addEventListeners(new CMD_Ping())
                    //.addEventListeners(new CMD_getWinner())

                    .addEventListeners(new EVENT_SendMessage())
                    .setActivity(Activity.playing("Type /help"))
                    .build();

            jda.updateCommands().addCommands(
                    Commands.slash("help", "list of commands")
                            .setGuildOnly(true),
                    Commands.slash("ping", "ping")
                            .setGuildOnly(true),
                    Commands.slash("giveaway", "create a giveaway")
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "channel to post the giveaway", true)
                            .addOption(OptionType.INTEGER, "duration", "duration for giveaway (days)", true)
                            .addOption(OptionType.STRING, "message", "message for the giveaway", true),
                    Commands.slash("activegw", "shows you how many giveaways are active")
                            .setGuildOnly(true),
                    Commands.slash("giverole", "gives a member a specific role")
                            .setGuildOnly(true)
                            .addOption(OptionType.ROLE, "role", "select a role to remove", true)
                            .addOption(OptionType.USER, "user", "select an user", true),
                    Commands.slash("removerole", "removes a specific role from member")
                            .setGuildOnly(true)
                            .addOption(OptionType.ROLE, "role", "select a role", true)
                            .addOption(OptionType.USER, "user", "select an user", true),
                    Commands.slash("broadcast", "To broadcast a message in the specified channel")
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "select a channel", true)
                            .addOption(OptionType.STRING, "message", "message to broadcast", true),
                    Commands.slash("settings", "change some settings")
                            .setGuildOnly(true)
                            .addOption(OptionType.STRING, "option", "select an option: autorole")
                            .addOption(OptionType.ROLE, "autorole", "*only for autorole-option*")/*,
                    Commands.slash("getwinner", "get a winner")
                            .setGuildOnly(true)
                            .addOption(OptionType.STRING, "giveaway", "giveaway-id", true)
                            .addOption(OptionType.CHANNEL, "channel", "select the giveaway channel", true)*/
            ).queue();
            //CMD_Giveaway.activeGW();
        }
    }
}
