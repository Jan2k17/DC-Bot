package de.jan2k17.dcbot.Commands;

import de.jan2k17.dcbot.Functions.Logging;
import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Component;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.interactions.components.selections.SelectMenu;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectInteraction;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;
import net.dv8tion.jda.api.interactions.modals.Modal;
import okhttp3.internal.connection.RouteSelector;

import java.util.HashMap;

public class CMD_Shop extends ListenerAdapter {
    private static Message shopMSG;
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent e) {
        if (e.getName().equalsIgnoreCase("shop")) {
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            if(SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("Setup already done!").queue();
                return;
            }
            HashMap<String, Integer> shopItems = SQL_Handler.getShopItems(e.getGuild().getId());
            int count = SQL_Handler.getCountItems(e.getGuild().getId());

            StringSelectMenu.Builder mbuilder = StringSelectMenu.create("shop:1");

            if(count >= 1) {
                shopItems.forEach((name, cost) -> {
                    mbuilder.addOption(name + " (" + cost + " Coins)", name + "_" + cost);
                });
            } else {
                mbuilder.addOption("no items in shop", "no_items");
            }

            StringSelectMenu shopMenu = mbuilder.build();
            e.getHook().editOriginalComponents(ActionRow.of(shopMenu)).queue();
        }
        if (e.getName().equalsIgnoreCase("shopadd")) {
            if(e.getMember().getUser().isBot()) { return; }
            if(SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("Setup already done!").queue();
                return;
            }
            TextInput itemname = TextInput.create("itemname", "Item name", TextInputStyle.SHORT)
                    .setRequiredRange(1, 50)
                    .build();
            TextInput cost = TextInput.create("cost", "item price (Coins)", TextInputStyle.SHORT)
                    .setMinLength(1)
                    .setPlaceholder("1")
                    .build();
            Modal modalShopAdd = Modal.create("mShopAdd", "Add shop item")
                    .addComponents(ActionRow.of(itemname), ActionRow.of(cost))
                    .build();
            e.replyModal(modalShopAdd).queue();
        }
        if (e.getName().equalsIgnoreCase("shopdel")) {
            if(e.getMember().getUser().isBot()) { return; }
            e.deferReply(true).queue();
            if(SQL_Handler.existsGuild(e.getGuild().getId())){
                e.getHook().editOriginal("Setup already done!").queue();
                return;
            }
            HashMap<String, Integer> shopItems = SQL_Handler.getShopItems(e.getGuild().getId());
            int count = SQL_Handler.getCountItems(e.getGuild().getId());

            StringSelectMenu.Builder mbuilder = StringSelectMenu.create("shop:2");

            if(count >= 1) {
                shopItems.forEach((name, cost) -> {
                    mbuilder.addOption(name + " (" + cost + " Coins)", name + "_" + cost);
                });
            } else {
                mbuilder.addOption(SQL_Handler.getTranslations("shop_noItems", SQL_Handler.getGuildLang(e.getGuild().getId())), "no_items");
            }

            StringSelectMenu shopMenu = mbuilder.build();
            e.getHook().editOriginalComponents(ActionRow.of(shopMenu)).queue();
        }
    }

    @Override
    public void onModalInteraction(ModalInteractionEvent e){
        if(e.getModalId().equals("mShopAdd")) {
            String itemname = e.getValue("itemname").getAsString();
            String cost = e.getValue("cost").getAsString();
            int count = SQL_Handler.getCountItems(e.getGuild().getId());

            if (count >= 24) {
                e.reply(SQL_Handler.getTranslations("shop_maxItems", SQL_Handler.getGuildLang(e.getGuild().getId()))).setEphemeral(true).queue();
            } else {
                SQL_Handler.addShopItem(e.getGuild().getId(), itemname, cost);
                String text = SQL_Handler.getTranslations("shop_added", SQL_Handler.getGuildLang(e.getGuild().getId())).replace("%itemname%", itemname).replace("%price%", cost);
                e.reply(text).setEphemeral(true).queue();
                Logging.Log(e.getGuild(),"Item **" + itemname + "** with price **" + cost + " Coins** added!", e.getMember());
            }
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent e){
        e.deferReply(true).queue();
        Member m = e.getMember();
        Guild g = e.getGuild();
        String[] value = e.getValues().get(0).split("_");
        String item = value[0];
        int cost = Integer.parseInt(value[1]);

        if(e.getComponentId().equals("shop:1")){
            if(SQL_Handler.getCoins(g.getId(), m.getId()) >= cost) {
                SQL_Handler.delCoin(g.getId(), m.getId(), cost);
                String text = SQL_Handler.getTranslations("shop_buy", SQL_Handler.getGuildLang(e.getGuild().getId())).replace("%itemname%", item).replace("%price%", ""+cost);
                e.getHook().editOriginal(text).queue();

                Logging.Log(e.getGuild(),"You bought successfully the item **" + item + "** for **" + cost + "Coins**", e.getMember());
            } else {

            }
        }
        if(e.getComponentId().equals("shop:2")){
            SQL_Handler.delShopItem(g.getId(), item, cost);
            String text = SQL_Handler.getTranslations("shop_delItem", SQL_Handler.getGuildLang(e.getGuild().getId())).replace("%itemname%", item);
            e.getHook().editOriginal(text).queue();
            Logging.Log(e.getGuild(),"You removed the item **" + item + "** from shop!", e.getMember());
        }
    }
}
