package de.jan2k17.dcbot;

import de.jan2k17.dcbot.Commands.*;
import de.jan2k17.dcbot.Commands.Games.*;
import de.jan2k17.dcbot.Functions.EVENT_SendMessage;
import de.jan2k17.dcbot.Functions.GuildJoin;
import de.jan2k17.dcbot.Handler.MySQL;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.*;

public class Main {
    private static final String[] activities = {"Type /help", "counting members", "counting coins", "Minecraft"};
    private static int cIndex = 1;
    public static void main(String[] args) {
        MySQL.connect();

        if(!MySQL.isConnected()) {
            System.out.println("No database connection!");
            System.exit(0);
        } else {
            List<GatewayIntent> gateway = new ArrayList<>();
            for(GatewayIntent intent : GatewayIntent.values()) {
                gateway.add(intent);
            }
            JDA jda = JDABuilder.createLight(Token.token, Collections.emptyList()).enableIntents(gateway)
                    .addEventListeners(new CMD_Setup())
                    .addEventListeners(new CMD_Broadcast())
                    .addEventListeners(new CMD_Giveaway())
                    .addEventListeners(new CMD_settings())
                    .addEventListeners(new CMD_Ping())
                    .addEventListeners(new CMD_Coins())
                    .addEventListeners(new CMD_Shop())
                    .addEventListeners(new MG_Hangman())
                    .addEventListeners(new GuildJoin())
                    .addEventListeners(new EVENT_SendMessage())
                    .setActivity(Activity.playing("typing ..."))
                    .build();

            jda.updateCommands().addCommands(
                    Commands.slash("setup", "setup the bot for this discord")
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "logch", "select a channel to log bot actions", true)
                            .addOption(OptionType.ROLE, "autorole", "select a standard server-role")
                            .addOptions(new OptionData(OptionType.STRING, "language", "select a server language")
                                    .addChoice("german", "de")
                                    .addChoice("english", "en")),
                    Commands.slash("ping", "ping")
                            .setGuildOnly(true),
                    Commands.slash("hangman", "play hangman ;)")
                            .setGuildOnly(true)
                            .addOption(OptionType.STRING, "letter", "guess a letter"),
                    Commands.slash("giveaway", "create a giveaway")
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "channel to post the giveaway", true)
                            .addOption(OptionType.INTEGER, "duration", "duration for giveaway (days)", true)
                            .addOption(OptionType.STRING, "message", "message for the giveaway", true),
                    Commands.slash("activegw", "shows you how many giveaways are active")
                            .setGuildOnly(true),
                    Commands.slash("broadcast", "To broadcast a message in the specified channel")
                            .setGuildOnly(true)
                            .addOption(OptionType.CHANNEL, "channel", "select a channel", true)
                            .addOption(OptionType.STRING, "message", "message to broadcast", true),
                    Commands.slash("settings", "change some settings")
                            .setGuildOnly(true)
                            .addOptions(new OptionData(OptionType.STRING, "option", "select an option", true)
                                    .addChoice("autorole", "autorole")
                                    .addChoice("log channel", "logchannel")
                            )
                            .addOptions(new OptionData(OptionType.STRING, "lang", "select the server language")
                                    .addChoice("english", "en")
                                    .addChoice("german", "de")
                            )
                            .addOption(OptionType.ROLE, "autorole", "*only for autorole-option*")
                            .addOption(OptionType.CHANNEL, "logchannel", "*only for log-option*"),
                    Commands.slash("coins", "how many coins you have?")
                            .setGuildOnly(true),
                    Commands.slash("shop", "open coin-shop")
                            .setGuildOnly(true),
                    Commands.slash("shopadd", "add item to coin-shop")
                            .setGuildOnly(true),
                    Commands.slash("shopdel", "remove an item from coin-shop")
                            .setGuildOnly(true)
            ).queue();

            new Timer().schedule(new TimerTask(){
                public void run(){
                    jda.getPresence().setActivity(Activity.playing(activities[cIndex]));
                    cIndex=(cIndex+1)% activities.length;
                }
            }, 0, 30_000);
        }
    }
}
