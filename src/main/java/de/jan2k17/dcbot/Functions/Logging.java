package de.jan2k17.dcbot.Functions;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;

import java.awt.*;

public class Logging {
    public static void Log(Guild g, String msg, Member m){
        if(SQL_Handler.getLogChannel(g.getId()) == null) { return; }
        EmbedBuilder eb = new EmbedBuilder();

        eb.setTitle("LOG", null);
        eb.setColor(Color.RED);
        eb.setDescription(msg);
        eb.setAuthor(m.getUser().getName(), null, m.getUser().getAvatarUrl());
        eb.setFooter("coded with ❤️ by j-f-a-b.de | 2023");

        g.getTextChannelById(SQL_Handler.getLogChannel(g.getId())).sendMessageEmbeds(eb.build()).queue();
    }
}
