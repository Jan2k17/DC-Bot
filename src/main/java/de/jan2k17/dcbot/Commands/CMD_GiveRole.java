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
            //000
            m.getRoles().add(r);
            e.reply("Added role ```" + r + "``` to User " + m.getAsMention()).queue();
        }
    }
}
