package de.jan2k17.dcbot.Commands;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.text.DateFormat;
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

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e)
    {
        if(e.getName().equalsIgnoreCase("giveaway")){
            int duration = e.getOption("duration", OptionMapping::getAsInt);
            String msg = e.getOption("message", OptionMapping::getAsString);
            TextChannel tch = e.getOption("channel", OptionMapping::getAsChannel).asTextChannel();
            Member author = e.getMember();

            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date date = new Date();
            Calendar cal = Calendar.getInstance();

            String[] dateString = sdf.format(date).split("-");

            cal.set(Integer.parseInt(dateString[2]), Integer.parseInt(dateString[1]) - 1, Integer.parseInt(dateString[0]));
            cal.add(Calendar.DAY_OF_MONTH, duration);
            String dateEnd = sdf.format(cal.getTime());

            tch.sendMessage("**GIVEAWAY**\r\n" +
                    "*Message:*\r\n" +
                    "```" + msg + "```\r\n" +
                    "\r\n" +
                    "To enter the giveaway tick the :white_check_mark:" +
                    "\r\n" +
                    "*Hoster:* " + author.getAsMention() + "\r\n" +
                    "*End:* ```" + dateEnd + "```").queue((message -> {
                        long msgID = message.getIdLong();
                        System.out.println("msgID: " + msgID);
                        giveaways.put(dateEnd, "" + msgID);
            }));
            e.reply("Giveaway started. (End: " + dateEnd + ")").queue();
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
