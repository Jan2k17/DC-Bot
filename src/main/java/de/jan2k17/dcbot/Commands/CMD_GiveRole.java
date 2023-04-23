package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

public class CMD_GiveRole extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("giverole")){
            Member m = e.getOption("user", OptionMapping::getAsMember);
            Role r = e.getOption("role", OptionMapping::getAsRole);
            e.getGuild().addRoleToMember(m, r).queue();
            e.reply("Added role " + r.getAsMention() + " to User " + m.getAsMention()).queue();
        }
        if(e.getName().equalsIgnoreCase("removerole")){
            Member m = e.getOption("user", OptionMapping::getAsMember);
            Role r = e.getOption("role", OptionMapping::getAsRole);
            e.getGuild().removeRoleFromMember(m, r).queue();
            e.reply("Removed role " + r.getAsMention() + " from User " + m.getAsMention()).queue();
        }
    }
}
