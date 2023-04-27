package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Functions.Logging;
import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.ChannelType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class CMD_Giveaway extends ListenerAdapter {
    public static HashMap<String, String> giveaways = new HashMap<>();
    private String msgID = "";
    private EmbedBuilder eb;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("giveaway")){
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            int duration = e.getOption("duration", OptionMapping::getAsInt);
            String msg = e.getOption("message", OptionMapping::getAsString);
            ChannelType ct = e.getOption("channel", OptionMapping::getChannelType);
            if(ct != ChannelType.TEXT && ct != ChannelType.NEWS) {
                e.getHook().editOriginal("**ERR::G01**").queue();
                return;
            }


            Member author = e.getMember();

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();

            String[] dateString = sdf.format(date).split("-");

            cal.set(Integer.parseInt(dateString[0]), Integer.parseInt(dateString[1]) - 1, Integer.parseInt(dateString[2]));
            cal.add(Calendar.DAY_OF_MONTH, duration);
            String dateEnd = sdf.format(cal.getTime());


            eb = new EmbedBuilder();
            eb.setTitle("GIVEAWAY", null);
            eb.setColor(new Color(52, 204, 235));

            eb.setDescription("To enter the giveaway tick the :white_check_mark:");

            eb.addField("Message", msg, false);
            eb.addField("Hoster", author.getAsMention(), false);
            eb.addBlankField(false);
            eb.addField("End", dateEnd, false);

            if(ct == ChannelType.TEXT){
                msgID = e.getOption("channel", OptionMapping::getAsChannel).asTextChannel()
                        .sendMessageEmbeds(eb.build()).complete().getId();
            } else if(ct == ChannelType.NEWS){
                msgID = e.getOption("channel", OptionMapping::getAsChannel).asNewsChannel()
                        .sendMessageEmbeds(eb.build()).complete().getId();
            }
            System.out.println("msgID: " + msgID);
            giveaways.put(msgID, dateEnd);
            SQL_Handler.addGW(e.getGuild().getId(), msg, dateEnd, author.getId(),""+msgID);


            eb.addField("Giveaway-ID:", msgID, false);
            if(ct == ChannelType.TEXT){
                e.getOption("channel", OptionMapping::getAsChannel).asTextChannel()
                        .editMessageEmbedsById(msgID, eb.build()).queue();
            } else if(ct == ChannelType.NEWS){
                e.getOption("channel", OptionMapping::getAsChannel).asNewsChannel()
                        .editMessageEmbedsById(msgID, eb.build()).queue();
            }

            e.getHook().editOriginal("Giveaway started. (End: " + dateEnd + ")").queue();
            Logging.Log(e.getGuild(), "Giveaway started. (End: " + dateEnd + " - gwID: " + msgID + ")", e.getMember());
        }
        if(e.getName().equalsIgnoreCase("activegw")){
            e.reply("There are " + giveaways.size() + " active giveaways!").queue();
        }
    }
    public static void activeGW(){
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                Date date = new Date();
                Calendar cal = Calendar.getInstance();

                String[] dateString = sdf.format(date).split("-");

                cal.set(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]), Integer.parseInt(dateString[0]));
                try{
                    cal.setTime(sdf.get2DigitYearStart());
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                while(giveaways.size() > 0){
                    giveaways.remove(cal.getTime());
                }
            }
        }, 0, 5, TimeUnit.MINUTES);
    }

    public static String getCurTime(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDateTime now = LocalDateTime.now();

        String dateTime = dtf.format(now);
        return dateTime;
    }
}
