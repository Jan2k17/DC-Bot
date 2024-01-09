package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CMD_Ping extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("ping")){
            if(e.getMember().getUser().isBot()){return;}
            if(!SQL_Handler.existsGuild(e.getGuild().getId())){
                e.reply("**Server needs a setup!**").queue();
                return;
            }
            long time = System.currentTimeMillis();
            e.reply("Pong!").setEphemeral(true).flatMap((v) ->
                        e.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time)
                    ).queue();
        }
    }
}
