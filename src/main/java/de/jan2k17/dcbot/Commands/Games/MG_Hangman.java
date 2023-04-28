package de.jan2k17.dcbot.Commands.Games;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class MG_Hangman extends ListenerAdapter {

    private static String[] words = {"Discord", "Facebook", "YouTube", "EpicGames", "Steam", "Google"};
    private static String curr_word = "";
    private static boolean active = false;
    private static int error = 0;
    private static String msgID = "";
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if(e.getName().equalsIgnoreCase("hangman")) {
            Member m = e.getMember();
            Guild g = e.getGuild();
            if (m.getUser().isBot()) { return; }

            if(!active) {
                Random rn = new Random();
                int word = rn.nextInt(words.length) + 1;
                curr_word = words[word].toLowerCase();
                active = true;
                String lines = "";
                int i = 0;

                //curr_word.charAt(0);
                while(i <= curr_word.length()){
                    lines = lines + "_";
                    i++;
                }
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Hangman");
                eb.setDescription("Wich word we are searching for?\r\n" +
                        "\r\n" +
                        "" + lines + "");
            } else {
                int length = curr_word.length();
                int i = 0;
                String letter = e.getOption("letter", OptionMapping::getAsString);
                while(i <= length){
                    /*if(curr_word. == letter){

                    }*/
                }
            }
        }
    }
}
