package de.jan2k17.dcbot.Functions;

import de.jan2k17.dcbot.Commands.CMD_Giveaway;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.HashMap;

public class EnterGiveaway extends ListenerAdapter {

    public static HashMap<Long,Long> entered = new HashMap<>();
    public void onreactionAdded(MessageReactionAddEvent e) {

        MessageReaction reaction = e.getReaction();
        MessageChannel channel = e.getChannel();
        Member m = e.getMember();
        if(CMD_Giveaway.giveaways.containsValue(reaction.getMessageId())){

            entered.put(reaction.getMessageIdLong(), m.getIdLong());
        }
        //reaction.getMessageId()
        System.out.println("user " + m.getId() + " entered gw (" + reaction.getMessageId() + ")");
        //channel.sendMessage("Commands:\n !notify - notifies users who have subscribed to mailing list").queue();
    }

}
