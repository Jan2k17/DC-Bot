package de.jan2k17.dcbot.Functions;

import de.jan2k17.dcbot.Handler.SQL_Handler;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoin extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(GuildMemberJoinEvent e)
    {
        String AutoRole = SQL_Handler.getAutoRole(e.getGuild().getId());
        e.getGuild().addRoleToMember(e.getUser(), e.getGuild().getRoleById(AutoRole)).queue();
    }
}
