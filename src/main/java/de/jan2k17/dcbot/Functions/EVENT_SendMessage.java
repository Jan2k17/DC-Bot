package de.jan2k17.dcbot.Functions;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class EVENT_SendMessage extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent e){
        Member m = e.getMember();
        Guild g = e.getGuild();
        if(m.getUser().isBot()){ return; }
        //send msg = 4 coins
        SQL_Handler.addStats(g.getId(), m.getId(), "messages", 4);
        SQL_Handler.addCoin(g.getId(), m.getId(), 4);
    }
    @Override
    public void onMessageReactionAdd(MessageReactionAddEvent e){
        Member m = e.getMember();
        Guild g = e.getGuild();
        if(m.getUser().isBot()){ return; }
        //add reaction = 2 coin
        SQL_Handler.addStats(g.getId(), m.getId(), "reactions", 2);
        SQL_Handler.addCoin(g.getId(), m.getId(), 2);
    }
}
