package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class CMD_Coins extends ListenerAdapter {
    private SQL_Handler sqlH;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("coins")) {
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            //e.getHook().editOriginal("You have **" + SQL_Handler.getCoins(e.getGuild().getId(), e.getMember().getId()) + " Coins**").queue();
            e.getHook().editOriginal(sqlH.getTranslations("cmd_coins", sqlH.getGuildLang(e.getGuild().getId())).replace("%user%", (sqlH.getCoins(e.getGuild().getId(), e.getMember().getId()) + ""))).queue();
        }
    }
}
