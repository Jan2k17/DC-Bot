package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CMD_Help extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("help")){
            Member m = e.getMember();
            e.reply("There are the following commands available:\r\n" +
                    "**/help** - command list\r\n" +
                    "**/broadcast <channelid> <msg>** - To broadcast a message in the specified channel\r\n" +
                    "**/giverole <role> <user>** - add a specific role to member\r\n" +
                    "**/removerole <role> <user>** - remove a specific role from member\r\n" +
                    "**/activegw** - shows you how many giveaways are active").queue();
        }
    }
}
