package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Functions.Logging;
import de.jan2k17.dcbot.Handler.SQL_Handler;
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
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            Member m = e.getMember();
            Guild g = e.getGuild();
            if(!SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("**Server needs a setup!**").queue();
                return;
            }
            Channel ch = e.getOption("channel", OptionMapping::getAsChannel);
            String msg = e.getOption("message", OptionMapping::getAsString);
            TextChannel tch = g.getTextChannelById(ch.getId());
            tch.sendMessage(msg).queue();
            e.getHook().editOriginal("**Broadcast** mit der Nachricht:\r\n" +
                    "```" + msg + "```\r\n" +
                    "Wurde versendet!").queue();

            Logging.Log(e.getGuild(), "**Broadcast** mit der Nachricht:\r\n" +
                    "```" + msg + "```\r\n" +
                    "Wurde versendet!", e.getMember());
        }
    }
}
