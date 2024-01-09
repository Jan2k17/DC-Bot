package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Functions.Logging;
import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CMD_settings extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("settings")) {
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            if(!SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("**Server needs a setup!**").queue();
                return;
            }
            if(SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("**You need to setup the server first!**").queue();
                return;
            }
            String option = e.getOption("option", OptionMapping::getAsString);
            if(option.equalsIgnoreCase("autorole")){
                Role r = e.getOption("autorole", OptionMapping::getAsRole);
                SQL_Handler.updateAutoRole(e.getGuild().getId(), r.getId());
                e.getHook().editOriginal("**[SETTINGS]** Autorole role have been updated to " + r.getAsMention()).queue();

                Logging.Log(e.getGuild(),"**[SETTINGS]** Autorole role have been updated to " + r.getAsMention(), e.getMember());
            }
            if(option.equalsIgnoreCase("logchannel")){
                String chID = e.getOption("logchannel", OptionMapping::getAsChannel).getId();
                SQL_Handler.updateLogChannel(e.getGuild().getId(), chID);
                e.getHook().editOriginal("**[SETTINGS]** Logchannel have been updated to " + e.getOption("logchannel", OptionMapping::getAsChannel).getAsMention()).queue();

                Logging.Log(e.getGuild(), "**[SETTINGS]** Logchannel have been updated to " + e.getOption("logchannel", OptionMapping::getAsChannel).getAsMention(), e.getMember());
            }
        }
    }
}
