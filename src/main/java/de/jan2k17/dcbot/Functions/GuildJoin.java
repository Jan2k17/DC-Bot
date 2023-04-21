package de.jan2k17.dcbot.Functions;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.GuildJoinEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class GuildJoin extends ListenerAdapter {
    /*private User user;

    public GuildJoin(User user)
    {
        this.user = user;
    }
    private User getUser()
    {
        // Acquire a reference to the User instance through the id
        User newUser = JDA.getUserById(this.user.getIdLong());
        if (newUser != null)
            this.user = newUser;
        return this.user;
    }
    @Override
    public void onGuildJoin(GuildJoinEvent event)
    {
        JDA api = event.getJDA();
        User user = getUser(); // use getter to refresh user automatically on access
        user.openPrivateChannel().queue((channel) ->
        {
            // Send a private message to the user
            channel.sendMessageFormat("I have joined a new guild: **%s**", event.getGuild().getName()).queue();
        });
    }*/
}
