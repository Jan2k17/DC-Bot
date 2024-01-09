package de.jan2k17.dcbot.Commands.Games;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;

import java.util.Random;

public class MG_Hangman extends ListenerAdapter {

    //private static String[] words = {"Discord", "Facebook", "YouTube", "EpicGames", "Steam", "Google"};
    private static String[] words = {"a","b","c"};
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
            if(!SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("**Server needs a setup!**").queue();
                return;
            }
            //System.out.println("active game :: " + active);

            if(active == false) {
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
                        "The Word have " + letters.length + " letters:\r\n" +
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
                        if(active == false){
                            //active = !active;
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
                    e.getChannel().editMessageEmbedsById(msgID, eb.build()).complete();
                    e.getHook().editOriginal("You guessed an incorrect letter :(").queue();
                    if(error == 9){
                        active = false;
                        curr_word = "";
                        lines = "";
                        error = 0;

                        eb = new EmbedBuilder();
                        eb.setTitle("Hangman");
                        eb.setDescription("**GAME OVER**\r\n" +
                                "You reached a count of **9** mistakes and Benny died in **Hangman**\r\n" +
                                "\r\n" +
                                "Good luck next time ;)");
                        eb.setImage(getErrPic(error));
                        e.getChannel().editMessageEmbedsById(msgID, eb.build()).complete();
                        e.getHook().editOriginal("You reached a count of **9** mistakes and Benny died in **Hangman**").queue();
                    }
                } else {
                    System.out.println("active game - 2 :: " + active);
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
                    e.getChannel().editMessageEmbedsById(msgID, eb.build()).complete();
                    e.getHook().editOriginal("Congratulations, you guessed a correct letter ;)").queue();

                    correct = false;
                }

                boolean working = letters.toString().contains("-");
                System.out.println("finished: " + working);
                if(working == false && active == true){
                    active = false;
                    curr_word = "";
                    lines = "";

                    eb = new EmbedBuilder();
                    eb.setTitle("Hangman");
                    eb.setDescription("The word have been finished with **" + error + "** mistakes!\r\n" +
                            "Congratulations " + e.getMember().getAsMention() + ", you won 50 coins!");
                    e.getChannel().editMessageEmbedsById(msgID, eb.build()).complete();
                    e.getHook().editOriginal("Congratulations, you guessed the last letter and won 50 coins ;)").queue();
                    SQL_Handler.addCoin(g.getId(), e.getMember().getUser().getId(), 50);

                    error = 0;
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
