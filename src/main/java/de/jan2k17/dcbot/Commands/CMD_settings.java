package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.sql.SQLException;

public class CMD_settings extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("settings")) {
            e.deferReply(true).queue();
            String option = e.getOption("option", OptionMapping::getAsString);
            if(option.equalsIgnoreCase("autorole")){
                Role r = e.getOption("autorole", OptionMapping::getAsRole);
                SQL_Handler.updateAutoRole(e.getGuild().getId(), r.getId());
                e.getHook().editOriginal("**[SETTINGS]** Autorole role have been updated to " + r.getAsMention()).queue();

            }
        }
    }
}
