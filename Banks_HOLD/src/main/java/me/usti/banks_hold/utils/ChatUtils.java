// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.utils;

import java.util.Iterator;
import me.usti.banks_hold.RusBank;
import org.bukkit.command.CommandSender;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ChatUtils
{
    public static void sendMsg(final Player player, final String msg) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public static void sendMsg(final CommandSender sender, final String msg) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
    }
    
    public static void helpMsg(final Player player, final RusBank main) {
        for (final String s : main.getConfig().getStringList("commands.help-msg")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    
    public static void helpAdmin(final Player player, final RusBank main) {
        for (final String s : main.getConfig().getStringList("commands.help-admin")) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    
    public static void helpMsg(final CommandSender sender, final RusBank main) {
        for (final String s : main.getConfig().getStringList("commands.help-msg")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
    
    public static void helpAdmin(final CommandSender sender, final RusBank main) {
        for (final String s : main.getConfig().getStringList("commands.help-admin")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', s));
        }
    }
}
