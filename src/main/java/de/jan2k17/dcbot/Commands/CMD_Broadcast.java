package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.Channel;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CMD_Broadcast extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("broadcast")){
            Member m = e.getMember();
            Guild g = e.getGuild();
            Channel ch = e.getOption("channel", OptionMapping::getAsChannel);
            String msg = e.getOption("message", OptionMapping::getAsString);
            TextChannel tch = g.getTextChannelById(ch.getId());
            tch.sendMessage(msg).queue();
            e.reply("**Broadcast** mit der Nachricht:\r\n" +
                    "```" + msg + "```\r\n" +
                    "Wurde versendet!").queue();
        }
    }
}
