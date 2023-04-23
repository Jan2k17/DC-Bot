package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CMD_Ping extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("ping")){
            long time = System.currentTimeMillis();
            e.reply("Pong!").setEphemeral(true).flatMap((v) ->
                        e.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                    ).queue();
        }
    }
}
