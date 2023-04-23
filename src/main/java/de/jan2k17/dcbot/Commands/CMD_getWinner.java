package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageReaction;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.List;
import java.util.Random;

public class CMD_getWinner extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("getwinner")){
            e.deferReply(true).queue();
            ChannelType ct = e.getOption("channel", OptionMapping::getChannelType);
            String gwID = e.getOption("giveaway", OptionMapping::getAsString);
            Guild guild = e.getGuild();

            System.out.println("gwID: " + gwID);

            if(ct == ChannelType.TEXT){
                TextChannel tch = e.getOption("channel", OptionMapping::getAsChannel).asTextChannel();
                List<Message> msgs = tch.getIterableHistory().stream().toList();

                msgs.forEach(msg -> {
                    if(msg.getId() == gwID){
                        List<MessageReaction> entered = msg.getReactions();
                        System.out.println(entered);
                        entered.forEach(mr -> {
                            if(mr.getEmoji().getName().equals(":white_check_mark:")){
                                List<User> eUsers = mr.retrieveUsers().stream().toList();
                                Random r = new Random();
                                int index = r.nextInt(eUsers.size());
                                User u = eUsers.get(index);

                                System.out.println("User: " + u.getName());

                                return;
                            } else {
                                System.out.println("Emote: " + mr.getEmoji().getName());
                            }
                        });
                    }
                    //System.out.println("[" + msgs.indexOf(msg) + "] " + msg.getId());
                });
                return;
            }


        }
    }
}
