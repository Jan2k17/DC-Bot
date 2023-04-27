package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;

public class CMD_Setup extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("setup")) {
            e.deferReply().queue();
            if (e.getMember().getUser().isBot()) { return; }
            e.getHook().editOriginal("").queue();
        }
    }
}
