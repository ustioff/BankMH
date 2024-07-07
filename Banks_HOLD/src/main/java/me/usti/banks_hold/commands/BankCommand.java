// 
// Decompiled by Procyon v0.5.36
// 

package me.usti.banks_hold.commands;

import me.usti.banks_hold.configs.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;

import org.bukkit.OfflinePlayer;

import org.bukkit.entity.Player;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.jetbrains.annotations.NotNull;
import org.bukkit.command.CommandSender;

import me.usti.banks_hold.RusBank;
import org.bukkit.command.TabCompleter;
import org.bukkit.command.CommandExecutor;

public class BankCommand implements CommandExecutor, TabCompleter
{
    RusBank main;

    public BankCommand(final RusBank main) {
        Objects.requireNonNull(main.getCommand("Банк")).setExecutor((CommandExecutor)this);
        this.main = main;
    }
    static Map<String, String> targetinf = new HashMap<>();
   static Map<String, Player> playerMap = new HashMap<>();

   static Map<String, OfflinePlayer> targ = new HashMap<>();
   static Map<String, Player> reg = new HashMap<>();

   public void addPlayer(java.lang.String s, org.bukkit.entity.Player player){
       playerMap.put(s, player);

   }
    public  Map<String, Player> getPlayerMap() {
        return playerMap;
    }
    public  Map<String, Player> getReg() {
        return reg;
    }

    public void addInf(String string, String s){
        targetinf.put(string, s);
    }
    public void addReg(String string, Player s){
        reg.put(string, s);
    }
    public void addTarg(String string, OfflinePlayer s){targ.put(string, s);}

    public  Map<String, String> getTargetinf() {

        return targetinf;
    }
    public Map<String, OfflinePlayer> getTarg(){
       return targ;
}



    
    public boolean onCommand(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String label, @NotNull final String[] args) {
        if (sender == null) {
            $$$reportNull$$$1(0);
        }
        if (command == null) {
            $$$reportNull$$$1(1);
        }
        if (label == null) {
            $$$reportNull$$$1(2);
        }
        if (args == null) {
            $$$reportNull$$$1(3);
        }
        if (sender == null) {
            $$$reportNull$$$0(0);
        }
        if (command == null) {
            $$$reportNull$$$0(1);
        }
        if (label == null) {
            $$$reportNull$$$0(2);
        }

       if (args.length == 0) {
           addPlayer("player", (Player) sender);
           Beebank_Menu menu = new Beebank_Menu();
           ((Player) sender).openInventory(menu.getInventory());
           Player player =(Player) sender;
           player.setn
       }
       else if (args[0].equalsIgnoreCase("регистрация")) {
           if (sender.hasPermission("bankir")) {
               if (args.length == 1) {
                   Menu_Plauereg menureg = new Menu_Plauereg();
                   Player player = (Player) sender;
                   player.openInventory(menureg.getInventory());
               } else {
                   sender.sendMessage(ChatColor.RED + "\uD83D\uDEC8 Неверно введина команда \uD83D\uDEC8");
               }
           } else {
               sender.sendMessage(ChatColor.RED + "\uD83D\uDEC8 У вас нет прав \uD83D\uDEC8");
           }

       }
        return true;
   }
    @Nullable
    public List<String> onTabComplete(@NotNull final CommandSender sender, @NotNull final Command command, @NotNull final String alias, @NotNull final String[] args) {
        if (sender == null) {
            $$$reportNull$$$1(4);
        }
        if (command == null) {
            $$$reportNull$$$1(5);
        }
        if (alias == null) {
            $$$reportNull$$$1(6);
        }
        if (args == null) {
            $$$reportNull$$$1(7);
        }
        if (sender == null) {
            $$$reportNull$$$0(4);
        }
        if (command == null) {
            $$$reportNull$$$0(5);
        }
        if (alias == null) {
            $$$reportNull$$$0(6);
        }

        final List<String> list = new ArrayList<String>();
        if (sender.hasPermission("bankir") || sender.isOp()) {
            if (args.length == 1) {
                list.add("регистрация");

                return list;
            }

            if (args.length >= 2) {
                return Collections.singletonList("Команда введена не верно");
            }
            return list;
        }
        else {
            if (args.length > 0) {

                    list.add("У вас нет прав");
                return list;
            }
            return list;
        }
    }

    private static void $$$reportNull$$$0(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "sender";
                break;
            }
            case 1:
            case 5: {
                args[0] = "command";
                break;
            }
            case 2: {
                args[0] = "label";
                break;
            }
            case 3:
            case 7: {
                args[0] = "args";
                break;
            }
            case 6: {
                args[0] = "alias";
                break;
            }
        }
        args[1] = "me/usti/banks_hold/commands/BankCommand";
        switch (n) {
            default: {
                args[2] = "onCommand";
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7: {
                args[2] = "onTabComplete";
                break;
            }
        }
        throw new IllegalArgumentException(String.format("Argument for @NotNull parameter '%s' of %s.%s must not be null", args));
    }

    static {
        BankCommand.targetinf = new HashMap<String, String>();
        BankCommand.playerMap = new HashMap<String, Player>();
    }

    private static /* synthetic */ void $$$reportNull$$$1(final int n) {
        final String format = "Argument for @NotNull parameter '%s' of %s.%s must not be null";
        final Object[] args = new Object[3];
        switch (n) {
            default: {
                args[0] = "sender";
                break;
            }
            case 1:
            case 5: {
                args[0] = "command";
                break;
            }
            case 2: {
                args[0] = "label";
                break;
            }
            case 3:
            case 7: {
                args[0] = "args";
                break;
            }
            case 6: {
                args[0] = "alias";
                break;
            }
        }
        args[1] = "me/usti/banks_hold/commands/BankCommand";
        switch (n) {
            default: {
                args[2] = "onCommand";
                break;
            }
            case 4:
            case 5:
            case 6:
            case 7: {
                args[2] = "onTabComplete";
                break;
            }
        }
        throw new IllegalArgumentException(String.format(format, args));
    }
}



