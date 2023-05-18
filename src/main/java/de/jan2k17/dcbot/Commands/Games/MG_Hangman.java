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
    private static boolean correct = false;
    private static int error = 0;
    private static String msgID = "";
    private static String lines = "";
    private static char[] letters;
    private static EmbedBuilder eb;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if(e.getName().equalsIgnoreCase("hangman")) {
            e.deferReply(true).queue();
            Member m = e.getMember();
            Guild g = e.getGuild();
            if (m.getUser().isBot()) { return; }

            if(active != true) {
                Random rn = new Random();
                int word = rn.nextInt(words.length);
                curr_word = words[word].toLowerCase();
                active = true;
                int i = 1;

                System.out.println("[HANGMAN] " + curr_word);
                letters = curr_word.toCharArray();

                while(i <= curr_word.length()){
                    lines = lines + "-";
                    i++;
                }

                eb = new EmbedBuilder();
                eb.setTitle("Hangman");
                eb.setDescription("Which word we are searching for?\r\n" +
                        "\r\n" +
                        "Word has " + letters.length + " letters:\r\n" +
                        "\r\n" +
                        "" + lines + "\r\n" +
                        "\r\n" +
                        "Mistakes: " + error + "/9");
                eb.setImage(getErrPic(0));
                msgID = e.getChannel().sendMessageEmbeds(eb.build()).complete().getId();
                e.getHook().editOriginal("You have started the hangman game.\r\n" +
                        "Good luck!").queue();
            } else {
                int length = curr_word.length();
                int i = 0;
                String letter = e.getOption("letter", OptionMapping::getAsString);
                char[] cur_word = curr_word.toCharArray();
                while(i <= (length - 1)){
                    if(String.valueOf(letters[i]).equalsIgnoreCase(letter)){
                        correct = true;
                        char[] l = letter.toCharArray();
                        if(letters[i] == l[0]) {
                            lines = replaceChar(lines, l[0], i);
                        }
                    }
                    if(i == (length - 1)){
                        if(!lines.contains("-") || active == false){
                            active = !active;
                            e.getHook().editOriginal("You can't guess letters without an active hangman game.").queue();
                            return;
                        }
                        break;
                    }
                    i++;
                }
                if(correct == false) {
                    error = error + 1;

                    eb = new EmbedBuilder();
                    eb.setTitle("Hangman");
                    eb.setDescription("Which word we are searching for?\r\n" +
                            "\r\n" +
                            "Word has " + letters.length + " letters:\r\n" +
                            "\r\n" +
                            "" + lines + "\r\n" +
                            "\r\n" +
                            "Mistakes: " + error + "/9");
                    eb.setImage(getErrPic(error));
                    e.getChannel().editMessageEmbedsById(msgID, eb.build()).queue();
                    e.getHook().editOriginal("You guessed an incorrect letter :(").queue();
                } else {
                    eb = new EmbedBuilder();
                    eb.setTitle("Hangman");
                    eb.setDescription("Which word we are searching for?\r\n" +
                            "\r\n" +
                            "Word has " + letters.length + " letters:\r\n" +
                            "\r\n" +
                            "" + lines + "\r\n" +
                            "\r\n" +
                            "Mistakes: " + error + "/9");
                    eb.setImage(getErrPic(error));
                    e.getChannel().editMessageEmbedsById(msgID, eb.build()).queue();
                    e.getHook().editOriginal("Congratulations, you guessed a correct letter ;)").queue();
                    correct = false;
                }
            }
        }
    }

    public String replaceChar(String str, char ch, int index) {
        char[] chars = str.toCharArray();
        chars[index] = ch;
        return String.valueOf(chars);
    }

    private String getErrPic(int error){
        return "http://www.j-f-a-b.de/hangman/"+error+".jpg";
    }
}
