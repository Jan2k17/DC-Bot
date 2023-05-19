package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CMD_Setup extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("setup")){
            if(e.getMember().getUser().isBot()){return;}
            if(SQL_Handler.existsGuild(e.getGuild().getId())){
                e.reply("Setup alreeady done!").queue();
                return;
            }
            String autorole;
            if(e.getOption("autorole", OptionMapping::getAsRole).getId().toString() == null){
                autorole = null;
            } else {
                autorole = e.getOption("autorole", OptionMapping::getAsRole).getId().toString();
            }
            String logch = e.getOption("logch", OptionMapping::getAsChannel).getId().toString();
            String lang = e.getOption("language", OptionMapping::getAsString);
            SQL_Handler.setupGuild(e.getGuild().getId(), autorole, logch, lang);
            e.reply("**Setup successfully finished!**").setEphemeral(true).queue();
        }
    }
}
