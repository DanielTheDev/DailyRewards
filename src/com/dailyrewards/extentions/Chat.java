package com.dailyrewards.extentions;

import com.dailyrewards.PluginClass;
import com.dailyrewards.config.PlayerData;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Chat {

    public static String toColor(String text) {
        return toColor(text, false);
    }

    public static String toColor(String text, boolean reset) {
        return ChatColor.translateAlternateColorCodes('&', (reset ? "&r" : "") + text);
    }

    public static void sendToConsole(String msg) {
        PluginClass.getPlugin().getLogger().info(ChatColor.stripColor(toColor(msg)));
    }

    public static String percentText(String symbol, int max, int current, char color_1, char color_2) {
        StringBuilder builder = new StringBuilder("&"+color_2);
        for (int x = 0; x < max; x++) {
            if(current == x) builder.append("&"+color_1);
            builder.append(symbol);
        }
        return builder.toString();
    }

    public static void sendMessage(CommandSender sender, String msg) {
        if(sender instanceof Player) {
            sender.sendMessage(toColor(msg));
        } else {
            Chat.sendToConsole(msg);
        }
    }

    public static void sendMessage(CommandSender player, List<String> lines, String... replace) {
        for(String line : lines) {
            for (int word = 0; word < replace.length; word+=2) {
                line = line.replace(replace[word], replace[word+1]);
            }
            player.sendMessage(toColor(line));
        }
    }

    public static String placeholder(Player player, String line) {
        PlayerData data = PluginClass.getPlugin().getPluginLib().getPlayerDataManager().getPlayer(player);
        return line
                .replace("%player%", player.getName())
                .replace("%claim-streak%", data.getClaimStreak()+"")
                .replace("%total-claimed%", data.getTotalClaimed()+"")
                .replace("%last-claimed%", data.getLastClaimedRelative())
                .replace("%first-claimed%", data.getFirstClaimedRelative())
                .replace("%last-claimed-date%", data.getLastClaimedFormat())
                .replace("%first-claimed-date%", data.getFirstClaimedFormat())
                .replace("%uuid%", player.getUniqueId().toString());
    }

    public static List<String> toColor(String... lines) {
        return toColor(false, lines);
    }

    public static List<String> toColor(boolean reset, String... lines) {
        return toColor(Arrays.asList(lines), reset);
    }

    public static List<String> toColor(List<String> lines) {
        return toColor(lines, false);
    }

    public static List<String> toColor(List<String> lines, boolean reset) {

        List<String> list = new ArrayList<>();
        for (int place = 0; place < lines.size(); place++) {
            list.add(toColor(lines.get(place), reset));
        }
        return list;
    }

    public static String timeTranslate(long delay) {
        int minutes = (int)Math.floor(delay/60);
        int hours = (int)Math.floor(delay/3600);
        int days = (int)Math.floor(delay/86400);
        int years = (int)Math.floor(delay/31536000);

        if(delay < 60) {
            return multipleAmount(delay, "second");
        } else if(delay < 3600) {
            return multipleAmount(minutes, "minute")+" and "+ multipleAmount(delay-(minutes*60), "second");
        } else if(delay < 86400) {
            return multipleAmount(hours, "hour")+" and " + multipleAmount((delay-(hours*3600))/60, "minute");
        } else if(delay < 31536000){
            return multipleAmount(days, "day")+" and " + multipleAmount((delay-(days*86400))/3600, "hour");
        } else {
            return multipleAmount(years, "year")+" and " + multipleAmount((days-(delay*31536000))/86400, "day");
        }
    }
    public static String multipleAmount(long amount, String type) {
        return amount+ " " +type + (amount == 1 ? "" : "s");
    }


    public static <T> List<T> toList(T... t) {
        return Arrays.asList(t);
    }

    public static <T> T[] toArray(List<T> t, Class<T> c) {
        return t.toArray((T[])Array.newInstance(c, t.size()));
    }
}
