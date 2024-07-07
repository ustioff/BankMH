package me.usti.banks_hold;

import com.destroystokyo.paper.profile.PlayerProfile;
import me.clip.placeholderapi.PlaceholderAPI;
import me.usti.banks_hold.commands.BankCommand;
import me.usti.banks_hold.configs.*;
import me.usti.banks_hold.utils.ChatUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.profile.PlayerTextures;

import java.net.MalformedURLException;
import java.net.URL;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class Event implements Listener {
    public  Map<String, String> targetbank = new HashMap<>();
    public  Map<String, String> bank = new HashMap<>();
    public  Map<String, Integer> banksum = new HashMap<>();
    public  Map<String, String> banktake = new HashMap<>();
    public  Map<String, String> bankadd = new HashMap<>();

    private List<String> leashed;
    private RusBank plugin;

    private List<String> strings;
    private List<String> add;
    private List<String> take;

    public Event() {
        this.leashed = new ArrayList<String>();
        this.plugin = plugin;
        this.strings = new ArrayList<String>();
        this.add = new ArrayList<String>();
        this.take = new ArrayList<String>();
    }
    private static final UUID RANDOM_UUID = UUID.fromString("92864445-51c5-4c3b-9039-517c9927d1b4"); // We reuse the same "random" UUID all the time
    private static PlayerProfile getProfile(String url) {
        PlayerProfile profile = (PlayerProfile) Bukkit.createPlayerProfile(RANDOM_UUID); // Get a new player profile
        PlayerTextures textures = profile.getTextures();
        URL urlObject;
        try {
            urlObject = new URL(url); // The URL to the skin, for example: https://textures.minecraft.net/texture/18813764b2abc94ec3c3bc67b9147c21be850cdf996679703157f4555997ea63a
        } catch (MalformedURLException exception) {
            throw new RuntimeException("Invalid URL", exception);
        }
        textures.setSkin(urlObject); // Set the skin of the player profile to the URL
        profile.setTextures(textures); // Set the textures back to the profile
        return profile;
    }

    @EventHandler
    public void MenuBank(InventoryClickEvent event)  {
        BankCommand command = new BankCommand(RusBank.inst);
        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        if (event.getClickedInventory() == null) {
            return;
        }

        if (event.getInventory().getHolder() instanceof Beebank_Menu) {
            if (event.getSlot() == 8) {
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                player.closeInventory();

            }
            event.setCancelled(true);
            if (event.getSlot() == 23) {
                    History_Menu mn = new History_Menu();
                    player.closeInventory();
                if (DataUtils.hasBank(event.getWhoClicked().getName(), "beenkoof")) {
                    player.openInventory(mn.getInventory());
                } else if (DataUtils.hasBank(event.getWhoClicked().getName(), "Sbeebank")) {
                    player.openInventory(mn.getInventory());
                } else if (DataUtils.hasBank(event.getWhoClicked().getName(), "beebusiness")) {
                    player.openInventory(mn.getInventory());
                } else {
                    player.sendMessage(ChatColor.RED + "\uD83D\uDEC8 У вас нет банка \uD83D\uDEC8");
                    player.closeInventory();
                }
            } else if (event.getSlot() == 21) {
                if (Data.getConfig().getInt("everybank") >= 1) {
                    for (int i = 1; i <= Data.getConfig().getInt("everybank"); i++) {
                        int bankx = Data.getConfig().getInt("bankx" + i);
                        int banky = Data.getConfig().getInt("banky" + i);
                        int bankz = Data.getConfig().getInt("bankz" + i);
                        int playerx = (int) ((Player) event.getWhoClicked()).getLocation().getX();
                        int playery = (int) ((Player) event.getWhoClicked()).getLocation().getY();
                        int playerz = (int) ((Player) event.getWhoClicked()).getLocation().getZ();
                        int bankxr = Data.getConfig().getInt("bankrx" + i);
                        int bankyr = Data.getConfig().getInt("bankrx" + i);
                        int bankzr = Data.getConfig().getInt("bankrx" + i);
                        if (playerx > (bankx - bankxr) && playerx < (bankx + bankxr) && playery > (banky - bankyr) && playery < (banky + bankyr) && playerz > (bankz - bankzr) && playerz < (bankz + bankzr)) {
                            MenuTake mn = new MenuTake();
                            player.closeInventory();
                            if (DataUtils.hasBank(event.getWhoClicked().getName(), "beenkoof")) {
                                player.openInventory(mn.getInventory());
                                i = Data.getConfig().getInt("everybank") + 1;
                            } else if (DataUtils.hasBank(event.getWhoClicked().getName(), "Sbeebank")) {
                                player.openInventory(mn.getInventory());
                                i = Data.getConfig().getInt("everybank") + 1;
                            } else if (DataUtils.hasBank(event.getWhoClicked().getName(), "beebusiness")) {
                                player.openInventory(mn.getInventory());
                                i = Data.getConfig().getInt("everybank") + 1;
                            } else {
                                player.sendMessage(ChatColor.RED + "\uD83D\uDEC8 У вас нет банка \uD83D\uDEC8");
                                i = Data.getConfig().getInt("everybank") + 1;
                                player.closeInventory();
                            }
                        } else if (i == Data.getConfig().getInt("everybank")) {
                            event.getWhoClicked().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Вы не находитесь в банке \uD83D\uDEC8");
                            player.closeInventory();
                        }
                    }
                } else {
                    event.getWhoClicked().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Банков ещё нет \uD83D\uDEC8");
                    player.closeInventory();
                }
            }


        } else if (event.getInventory().getHolder() instanceof Menu_Plauer) {
            event.setCancelled(true);
            int i = event.getSlot();

            if (i != 52 && i != 53) {
                if (event.getClickedInventory().getItem(i).getType() == Material.PLAYER_HEAD) {
                    String string = event.getClickedInventory().getItem(i).getItemMeta().getDisplayName();
                    command.addInf(player.getUniqueId().toString(), string);
                    command.addTarg("targ", Bukkit.getOfflinePlayer(string));
                    command.addTarg(player.getUniqueId().toString(), Bukkit.getOfflinePlayer(string));
                    player.closeInventory();
                    Bank_Menu menu = new Bank_Menu();
                    player.openInventory(menu.getInventory());
                }
            } else {
                if (i == 53) {
                    if (event.getClickedInventory().getItem(51).getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        Inventory inventory = event.getClickedInventory();
                        int a = event.getClickedInventory().getItem(51).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER);
                        int ip = 0;
                        int ipt = 0;
                        for (int s = 0; s < 52; s++) {
                            inventory.setItem(s, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                        }
                        for (OfflinePlayer playert : Bukkit.getOfflinePlayers()) {
                            ipt = ipt + 1;
                            if (ipt>a &&  ipt < (a + 53)){
                                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                                meta.setDisplayName(playert.getName());
                                List list = new ArrayList<>();
                                meta.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, ipt);
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Был в сети:");
                                list.add(ChatColor.RESET + ""+ ChatColor.YELLOW + PlaceholderAPI.setPlaceholders(playert, "%player_last_played_formatted%"));
                                meta.setLore(list);
                                meta.setOwner(playert.getName());
                                itemStack.setItemMeta(meta);

                                if (ip < 52) {
                                    if (DataUtils.hasBank(playert.getName(), "beenkoof")) {
                                    inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    } else if (DataUtils.hasBank(playert.getName(), "Sbeebank")) {
                                    inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    } else if (DataUtils.hasBank(playert.getName(), "beebusiness")) {
                                    inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    }
                                }

                            }else {

                                ip = 0;
                            }
                        }

                        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
                        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
                        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
                        down.setItemMeta(downItemMeta);
                        inventory.setItem(52, down);
                        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
                        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
                        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
                        up.setItemMeta(upItemMeta);
                        inventory.setItem(53, up);
                    }


                }
                if (i == 52) {
                    if (event.getClickedInventory().getItem(0).getType() != Material.BLACK_STAINED_GLASS_PANE) {
                        Inventory inventory = event.getClickedInventory();
                        int a = event.getClickedInventory().getItem(0).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) - 53;
                        if (a >= 0){
                        int ip = 0;
                        int ipt = 0;
                        for (int s = 0; s < 52; s++) {
                            inventory.setItem(s, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                        }
                        for (OfflinePlayer playert : Bukkit.getOfflinePlayers()) {
                            ipt = ipt + 1;
                            if (ipt > a) {
                                ItemStack itemStack = new ItemStack(Material.PLAYER_HEAD);
                                SkullMeta meta = (SkullMeta) itemStack.getItemMeta();
                                meta.setDisplayName(playert.getName());
                                List list = new ArrayList<>();
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Был в сети:");
                                list.add(ChatColor.RESET + "" + ChatColor.YELLOW + PlaceholderAPI.setPlaceholders(playert, "%player_last_played_formatted%"));
                                meta.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, ipt);
                                meta.setLore(list);
                                meta.setOwner(playert.getName());
                                itemStack.setItemMeta(meta);
                                if (ip <= 52) {
                                    if (DataUtils.hasBank(playert.getName(), "beenkoof")) {
                                        inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    } else if (DataUtils.hasBank(playert.getName(), "Sbeebank")) {
                                        inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    } else if (DataUtils.hasBank(playert.getName(), "beebusiness")) {
                                        inventory.setItem(ip, itemStack);
                                        ip = ip + 1;
                                    }
                                }
                            } else {
                                ip = 0;
                            }
                        }
                        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
                        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
                        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
                        down.setItemMeta(downItemMeta);
                        inventory.setItem(52, down);
                        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
                        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
                        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
                        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
                        up.setItemMeta(upItemMeta);
                        inventory.setItem(53, up);

                    }
                    }
                }
            }
        }else if(event.getInventory().getHolder() instanceof Menu_Plauereg) {
            event.setCancelled(true);
            int i = event.getSlot();

            if (event.getClickedInventory().getItem(i).getType() != Material.BLACK_STAINED_GLASS_PANE) {
                String string = event.getClickedInventory().getItem(i).getItemMeta().getDisplayName();
                command.addReg("reg", Bukkit.getPlayer(string));
                command.addReg(player.getUniqueId().toString(), Bukkit.getPlayer(string));
                player.closeInventory();
                Bank_Menureg menu = new Bank_Menureg();
                player.openInventory(menu.getInventory());
            }
        }else if (event.getInventory().getHolder() instanceof Bank_Menureg){
            event.setCancelled(true);
            String playert = command.getReg().get(player.getUniqueId().toString()).getName();
            Player player1 = Bukkit.getPlayer(playert);
            if (DataUtils.hasBank(playert, "beenkoof")) {
                if (DataUtils.hasBank(playert, "Sbeebank")) {
                    if (DataUtils.hasBank(playert, "beebusiness")) {
                        player.closeInventory();
                    } else {
                        if (event.getSlot() == 13) {
                            DataUtils.registerPlayer(playert, "beebusiness");
                            player.closeInventory();
                            player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                            player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                            player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Би-Бизнес");
                        }
                    }
                } else if (DataUtils.hasBank(playert, "beebusiness")) {
                    if (event.getSlot() == 13) {
                        DataUtils.registerPlayer(playert, "Sbeebank");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Сби-банк");
                    }

                } else {
                    if (event.getSlot() == 12) {
                        DataUtils.registerPlayer(playert, "Sbeebank");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Сби-банк");
                    } else if (event.getSlot() == 14){
                        DataUtils.registerPlayer(playert, "beebusiness");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Би-Бизнес");
                    }

                }
            } else if (DataUtils.hasBank(playert, "Sbeebank")) {
                if (DataUtils.hasBank(playert, "beebusiness")) {
                    if (event.getSlot() == 13) {

                        DataUtils.registerPlayer(playert, "beenkoof");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Бинькофф");
                    }
                } else {
                    if (event.getSlot() == 12) {
                        DataUtils.registerPlayer(playert, "beenkoof");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Бинькофф");

                    } else if (event.getSlot() == 14) {
                        DataUtils.registerPlayer(playert, "beebusiness");
                        player.closeInventory();
                        player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                        player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                        player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Би-Бизнес");

                    }
                }

            }
            else if (DataUtils.hasBank(playert, "beebusiness")) {
                if (event.getSlot() == 12) {
                    DataUtils.registerPlayer(playert, "beenkoof");
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                    player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                    player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Бинькофф");
                }else if (event.getSlot() == 14) {

                    DataUtils.registerPlayer(playert, "Sbeebank");
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                    player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                    player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Сби-банк");
                }

            }else {
                if (event.getSlot() == 11) {
                    DataUtils.registerPlayer(playert, "beenkoof");
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                    player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                    player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Бинькофф");
                }else if (event.getSlot() == 13) {
                    DataUtils.registerPlayer(playert, "Sbeebank");
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                    player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                    player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Сби-банк");
                }else if (event.getSlot() == 15){
                    DataUtils.registerPlayer(playert, "beebusiness");
                    player.closeInventory();
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Игрок зарегистрирован");
                    player1.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "Вы зарегистрировны");
                    player1.sendMessage(ChatColor.WHITE + "Банк: " + ChatColor.YELLOW + "Би-Бизнес");
                }

            }
            if (command.getReg().get("reg") != null) {
                command.getReg().remove("reg");
            }
            if (command.getReg().get(player.getUniqueId().toString()) != null) {
                command.getReg().remove(player.getUniqueId().toString());
            }
             if (event.getSlot() == 8){
                player.closeInventory();
                 if (command.getReg().get("reg") != null) {
                     command.getReg().remove("reg");
                 }
                 if (command.getReg().get(player.getUniqueId().toString()) != null) {
                     command.getReg().remove(player.getUniqueId().toString());
                 }
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                 player.closeInventory();
                 Beebank_Menu beebankMenu = new Beebank_Menu();
                 player.openInventory(beebankMenu.getInventory());
            }
        }else if (event.getInventory().getHolder() instanceof History_Menu) {
            event.setCancelled(true);
            if (event.getSlot() == 8) {
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                if (command.getReg().get("reg") != null) {
                    command.getReg().remove("reg");
                }
                if (command.getReg().get(player.getUniqueId().toString()) != null) {
                    command.getReg().remove(player.getUniqueId().toString());
                }
                player.closeInventory();
                Beebank_Menu beebankMenu = new Beebank_Menu();
                player.openInventory(beebankMenu.getInventory());
            }
            else if (event.getSlot() == 3) {
                Menu_Plauer mn = new Menu_Plauer();
                player.closeInventory();
                player.openInventory(mn.getInventory());
            }
           else if (event.getSlot() == 5){
                Historyadd_Menu mn = new Historyadd_Menu();
                player.closeInventory();
                player.openInventory(mn.getInventory());
            }
            else if (event.getSlot() == 1) {
                if (event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) != null) {
                    if (event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) > 1) {
                        int i = event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) - 1;
                        for (int a = 9; a < 27; a++) {
                            Database database = RusBank.inst.getDatabase();
                            Statement countstatement = null;
                            int sum = 0;
                            String target = null;
                            String bank = null;
                            String messeg = null;
                            String targetbank = null;
                            int num = 0;
                            try {
                                Statement statement = database.getConnection().createStatement();
                                String sql = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'take' AND num = '" + i + "'";
                                ResultSet resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    sum = resultSet.getInt("sum");
                                    target = resultSet.getString("target");
                                    bank = resultSet.getString("bank");
                                    messeg = resultSet.getString("messeg");
                                    targetbank = resultSet.getString("targetbank");
                                    num = resultSet.getInt("num");
                                    statement.close();
                                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                                    List list = new ArrayList<>();
                                    hist.setOwner(target);
                                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                                    hist.setDisplayName(target);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + targetbank);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + bank);
                                    hist.setLore(list);
                                    histor.setItemMeta(hist);
                                    event.getClickedInventory().setItem(a, histor);

                                } else {
                                    event.getClickedInventory().setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                                    statement.close();
                                }
                                statement.close();
                            } catch (SQLException e) {
                            }
                            i--;


                        }
                    }
                }
            } else if (event.getSlot() == 0) {
                if (event.getInventory().getItem(9).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) != null ) {

                        int i = event.getClickedInventory().getItem(9).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) + 1;
                        for (int a = 26; a > 8; a = a - 1) {

                            Database database = RusBank.inst.getDatabase();
                            Statement countstatement = null;
                            int sum = 0;
                            String target = null;
                            String bank = null;
                            String messeg = null;
                            String targetbank = null;
                            int num = 0;
                            try {
                                Statement statement = database.getConnection().createStatement();
                                String sql = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'take' AND num = '" + i + "'";
                                ResultSet resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    sum = resultSet.getInt("sum");
                                    target = resultSet.getString("target");
                                    bank = resultSet.getString("bank");
                                    messeg = resultSet.getString("messeg");
                                    targetbank = resultSet.getString("targetbank");
                                    num = resultSet.getInt("num");
                                    statement.close();
                                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                                    List list = new ArrayList<>();
                                    hist.setOwner(target);
                                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                                    hist.setDisplayName(target);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + targetbank);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + bank);
                                    hist.setLore(list);
                                    histor.setItemMeta(hist);
                                    event.getClickedInventory().setItem(a, histor);
                                } else {
                                    int count = 0;
                                    try {
                                        statement = database.getConnection().createStatement();
                                        String sqls = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'take'";
                                        ResultSet countresultSet = statement.executeQuery(sqls);
                                        count = database.resultSetCount(countresultSet);
                                    } catch (SQLException e) {
                                        count = 0;
                                    }
                                    if(i < count) {
                                        event.getClickedInventory().setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                                        statement.close();
                                    }
                                }
                                statement.close();
                            } catch (SQLException e) {
                            }
                            i = i + 1;



                    }
                }
            }
        }else if (event.getInventory().getHolder() instanceof Historyadd_Menu) {
            event.setCancelled(true);
            if (event.getSlot() == 8) {
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                if (command.getReg().get("reg") != null) {
                    command.getReg().remove("reg");
                }
                if (command.getReg().get(player.getUniqueId().toString()) != null) {
                    command.getReg().remove(player.getUniqueId().toString());
                }
                player.closeInventory();
                Beebank_Menu beebankMenu = new Beebank_Menu();
                player.openInventory(beebankMenu.getInventory());
            }
            else if (event.getSlot() == 3) {
                Menu_Plauer mn = new Menu_Plauer();
                player.closeInventory();
                player.openInventory(mn.getInventory());
            }
            else if (event.getSlot() == 5){
                History_Menu mn = new History_Menu();
                player.closeInventory();
                player.openInventory(mn.getInventory());
            }
            else if (event.getSlot() == 1) {
                if (event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) != null) {
                    if (event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) > 1) {
                        int i = event.getClickedInventory().getItem(26).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) - 1;

                        for (int a = 9; a < 27; a++) {

                            Database database = RusBank.inst.getDatabase();
                            Statement countstatement = null;
                            int sum = 0;
                            String target = null;
                            String bank = null;
                            String messeg = null;
                            String targetbank = null;
                            int num = 0;
                            try {
                                Statement statement = database.getConnection().createStatement();
                                String sql = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'add' AND num = '" + i + "'";
                                ResultSet resultSet = statement.executeQuery(sql);
                                if (resultSet.next()) {
                                    sum = resultSet.getInt("sum");
                                    target = resultSet.getString("target");
                                    bank = resultSet.getString("bank");
                                    messeg = resultSet.getString("messeg");
                                    targetbank = resultSet.getString("targetbank");
                                    num = resultSet.getInt("num");
                                    statement.close();
                                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                                    List list = new ArrayList<>();
                                    hist.setOwner(target);
                                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                                    hist.setDisplayName(target);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + bank);
                                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + targetbank);
                                    hist.setLore(list);
                                    histor.setItemMeta(hist);
                                    event.getClickedInventory().setItem(a, histor);

                                } else {
                                    event.getClickedInventory().setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                                    statement.close();
                                }
                                statement.close();
                            } catch (SQLException e) {
                            }
                            i--;


                        }
                    }
                }
            } else if (event.getSlot() == 0) {
                if (event.getClickedInventory().getItem(9).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) != null ) {

                    int i = event.getClickedInventory().getItem(9).getItemMeta().getPersistentDataContainer().get(NamespacedKey.fromString("num"), PersistentDataType.INTEGER) + 1;
                    for (int a = 26; a > 8; a = a - 1) {

                        Database database = RusBank.inst.getDatabase();
                        Statement countstatement = null;
                        int sum = 0;
                        String target = null;
                        String bank = null;
                        String messeg = null;
                        String targetbank = null;
                        int num = 0;
                        try {
                            Statement statement = database.getConnection().createStatement();
                            String sql = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'add' AND num = '" + i + "'";
                            ResultSet resultSet = statement.executeQuery(sql);
                            if (resultSet.next()) {
                                sum = resultSet.getInt("sum");
                                target = resultSet.getString("target");
                                bank = resultSet.getString("bank");
                                messeg = resultSet.getString("messeg");
                                targetbank = resultSet.getString("targetbank");
                                num = resultSet.getInt("num");
                                statement.close();
                                ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                                SkullMeta hist = (SkullMeta) histor.getItemMeta();
                                List list = new ArrayList<>();
                                hist.setOwner(target);
                                hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                                hist.setDisplayName(target);
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + targetbank);
                                list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + bank);
                                hist.setLore(list);
                                histor.setItemMeta(hist);
                                event.getClickedInventory().setItem(a, histor);
                            } else {
                                int count = 0;
                                try {
                                    statement = database.getConnection().createStatement();
                                    String sqls = "SELECT * FROM pay WHERE Users = '" + event.getWhoClicked().getName() + "' AND action = 'add'";
                                    ResultSet countresultSet = statement.executeQuery(sqls);
                                    count = database.resultSetCount(countresultSet);
                                } catch (SQLException e) {
                                    count = 0;
                                }
                                if(i < count) {
                                    event.getClickedInventory().setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                                    statement.close();
                                }
                            }
                            statement.close();
                        } catch (SQLException e) {
                        }
                        i = i + 1;



                    }
                }
            }
        }
    }



    @EventHandler
    public void MenuSender(InventoryClickEvent event)  {
        BankCommand command = new BankCommand(RusBank.inst);
        Player player = (Player) event.getWhoClicked(); // The player that clicked the item
        Sender_Menu menu = new Sender_Menu();

        if (event.getClickedInventory() == null) {
            return;
        }
        if (event.getInventory().getHolder() instanceof Sender_Menu) {
            event.setCancelled(true);
            String playert = command.getTargetinf().get(event.getWhoClicked().getUniqueId().toString());
            if (DataUtils.hasBank(playert, "beenkoof")) {
                if (DataUtils.hasBank(playert, "Sbeebank")) {
                    if (DataUtils.hasBank(playert, "beebusiness")) {
                        if (event.getSlot() == 11) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        } else if (event.getSlot() == 13) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        } else if (event.getSlot() == 15) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        }
                    } else {
                        if (event.getSlot() == 12) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        } else if (event.getSlot() == 14) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        }
                    }
                } else if (DataUtils.hasBank(playert, "beebusiness")) {
                    if (event.getSlot() == 12) {

                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    } else if (event.getSlot() == 14) {

                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    }
                } else {
                    if (event.getSlot() == 13) {

                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    }
                }
            } else if (DataUtils.hasBank(playert, "Sbeebank")) {
                if (DataUtils.hasBank(playert, "beebusiness")) {
                    if (event.getSlot() == 12) {

                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    } else if (event.getSlot() == 14) {


                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    }
                } else {
                    if (event.getSlot() == 13) {
                        if (DataUtils.hasBank(playert, "Sbeebank")) {

                            targetbank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            this.leashed.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            player.closeInventory();
                        }
                    }
                }
            }
            else if (DataUtils.hasBank(playert, "beebusiness")) {
                    if (event.getSlot() == 13) {

                        targetbank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        this.leashed.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        player.closeInventory();
                    }
            }

            if (event.getSlot() == 8){
                player.closeInventory();
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                if (command.getReg().get("reg") != null) {
                    command.getReg().remove("reg");
                }
                if (command.getReg().get(player.getUniqueId().toString()) != null) {
                    command.getReg().remove(player.getUniqueId().toString());
                }
                player.closeInventory();
                Beebank_Menu beebankMenu = new Beebank_Menu();
                player.openInventory(beebankMenu.getInventory());
            }
        } else if (event.getInventory().getHolder() instanceof Bank_Menu) {
            event.setCancelled(true);
            if (DataUtils.hasBank(player.getName(), "beenkoof")) {
                if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                    if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        if (event.getSlot() == 11) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        } else if (event.getSlot() == 13) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        } else if (event.getSlot() == 15) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        }
                    } else {
                        if (event.getSlot() == 12) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        } else if (event.getSlot() == 14) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        }
                    }
                } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    if (event.getSlot() == 12) {
                        bank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                        player.closeInventory();
                        player.openInventory(menu.getInventory());

                    } else if (event.getSlot() == 14) {
                        bank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        player.closeInventory();
                        player.openInventory(menu.getInventory());

                    }
                } else {
                    if (event.getSlot() == 13) {
                        bank.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                        player.closeInventory();
                        player.openInventory(menu.getInventory());

                    }
                }
            } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    if (event.getSlot() == 12) {
                        bank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                        player.closeInventory();
                        player.openInventory(menu.getInventory());


                    } else if (event.getSlot() == 14) {
                        bank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        player.closeInventory();
                        player.openInventory(menu.getInventory());

                    }
                } else {
                    if (event.getSlot() == 13) {
                        if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                            bank.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            player.closeInventory();
                            player.openInventory(menu.getInventory());

                        }
                    }
                }
            }
            else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                if (event.getSlot() == 13) {
                    bank.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                    player.closeInventory();
                    player.openInventory(menu.getInventory());
                }
            }
            if (event.getSlot() == 8) {
                player.closeInventory();
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                if (command.getReg().get("reg") != null) {
                    command.getReg().remove("reg");
                }
                if (command.getReg().get(player.getUniqueId().toString()) != null) {
                    command.getReg().remove(player.getUniqueId().toString());
                }
                player.closeInventory();
                Beebank_Menu beebankMenu = new Beebank_Menu();
                player.openInventory(beebankMenu.getInventory());
            }
        }
    }
        @EventHandler
    public void Take(InventoryClickEvent event) throws SQLException {
        Player player = (Player) event.getWhoClicked();
        if (event.getInventory().getHolder() instanceof MenuTake){
            event.setCancelled(true);
            if (event.getSlot() == 8){
                player.closeInventory();
                BankCommand command = new BankCommand(RusBank.inst);
                if (getAdd().contains(player.getUniqueId().toString())) {
                    unadd(player);
                }
                if (getTake().contains(player.getUniqueId().toString())) {
                    untake(player);
                }
                if (getStrings().contains(player.getUniqueId().toString())) {
                    unstrig(player);
                }
                if (getLeashed().contains(player.getUniqueId().toString())) {
                    unleashPlayer(player);
                }
                if (command.getPlayerMap().get(player.getUniqueId().toString()) != null) {
                    command.getPlayerMap().remove(player.getUniqueId().toString());
                }
                if (command.getTargetinf().get(player.getUniqueId().toString()) != null) {
                    command.getTargetinf().remove(player.getUniqueId().toString());
                }
                if (command.getTarg().get(player.getUniqueId().toString()) != null) {
                    command.getTarg().remove(player.getUniqueId().toString());
                }
                if (banksum.get(player.getUniqueId().toString()) != null) {
                    banksum.remove(player.getUniqueId().toString());
                }
                if (banktake.get(player.getUniqueId().toString()) != null) {
                    banktake.remove(player.getUniqueId().toString());
                }
                if (bankadd.get(player.getUniqueId().toString()) != null) {
                    bankadd.remove(player.getUniqueId().toString());
                }
                if (bank.get(player.getUniqueId().toString()) != null) {
                    bank.remove(player.getUniqueId().toString());
                }
                if (targetbank.get(player.getUniqueId().toString()) != null) {
                    targetbank.remove(player.getUniqueId().toString());
                }
                if (command.getReg().get("reg") != null) {
                    command.getReg().remove("reg");
                }
                if (command.getReg().get(player.getUniqueId().toString()) != null) {
                    command.getReg().remove(player.getUniqueId().toString());
                }
                player.closeInventory();
                Beebank_Menu beebankMenu = new Beebank_Menu();
                player.openInventory(beebankMenu.getInventory());
            }
            if (event.getClick().isRightClick()) {
                if (DataUtils.hasBank(player.getName(), "beenkoof")) {
                    if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                        if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                            if (event.getSlot() == 11) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                                player.closeInventory();
                            } else if (event.getSlot() == 13) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            } else if (event.getSlot() == 15) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                                player.closeInventory();
                            }
                        } else {
                            if (event.getSlot() == 12) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                                player.closeInventory();
                            } else if (event.getSlot() == 14) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            }
                        }
                    } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        if (event.getSlot() == 12) {
                            this.take.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            banktake.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                        } else if (event.getSlot() == 14) {
                            this.take.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            banktake.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            player.closeInventory();
                        }
                    } else {
                        if (event.getSlot() == 13) {
                            this.take.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            banktake.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                        }
                    }
                } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                    if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        if (event.getSlot() == 12) {
                            this.take.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            banktake.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            player.closeInventory();
                        } else if (event.getSlot() == 14) {
                            this.take.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            banktake.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            player.closeInventory();
                        }
                    } else {
                        if (event.getSlot() == 13) {
                            if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                                this.take.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                banktake.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            }
                        }
                    }
                }
                else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    if (event.getSlot() == 13) {
                        this.take.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        banktake.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        player.closeInventory();
                    }
                }
            }
            if (event.getClick().isLeftClick()) {
                if (DataUtils.hasBank(player.getName(), "beenkoof")) {
                    if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                        if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                            if (event.getSlot() == 11) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                                player.closeInventory();
                            } else if (event.getSlot() == 13) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            } else if (event.getSlot() == 15) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                                player.closeInventory();
                            }
                        } else {
                            if (event.getSlot() == 12) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                                player.closeInventory();
                            } else if (event.getSlot() == 14) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            }
                        }
                    } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        if (event.getSlot() == 12) {
                            this.add.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                        } else if (event.getSlot() == 14) {
                            this.add.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            player.closeInventory();
                        }
                    } else {
                        if (event.getSlot() == 13) {
                            this.add.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beenkoof");
                            player.closeInventory();
                        }
                    }
                } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                    if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        if (event.getSlot() == 12) {
                            this.add.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            bankadd.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                            player.closeInventory();
                        } else if (event.getSlot() == 14) {
                            this.add.add(player.getUniqueId().toString());
                            player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                            bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                            player.closeInventory();
                        }
                    } else {
                        if (event.getSlot() == 13) {
                            if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                                this.add.add(player.getUniqueId().toString());
                                player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                                bankadd.put(event.getWhoClicked().getUniqueId().toString(), "Sbeebank");
                                player.closeInventory();
                            }
                        }
                    }
                }
                else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    if (event.getSlot() == 13) {
                        this.add.add(player.getUniqueId().toString());
                        player.sendMessage(ChatColor.GOLD + "\u270e \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0441\u0443\u043c\u043c\u0443 \u270e");
                        bankadd.put(event.getWhoClicked().getUniqueId().toString(), "beebusiness");
                        player.closeInventory();
                    }
                }
            }
        }
    }
@EventHandler
public void open(InventoryOpenEvent event)  {
    if (event.getInventory().getHolder() instanceof Bank_Menu) {
        Inventory inventory = event.getInventory();
        ItemStack tin = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metatin = (SkullMeta) tin.getItemMeta();
        PlayerProfile profile = getProfile("http://textures.minecraft.net/texture/8433b7964a05576ea1fd665e52edffd74311ae7f7aedaae6403bced329f4fdca");
        metatin.setOwnerProfile(profile);
        metatin.setDisplayName("Бинькофф");
        tin.setItemMeta(metatin);
        ItemStack sbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metasbe = (SkullMeta) sbe.getItemMeta();
        PlayerProfile profilesbe = getProfile("http://textures.minecraft.net/texture/5fb4b94f1a3f6f207632da797eddb11ddb3c0dffea8d67a6f2e420f1094cf198");
        metasbe.setOwnerProfile(profilesbe);
        metasbe.setDisplayName("Сби-банк");
        sbe.setItemMeta(metasbe);
        ItemStack besbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metabesbe = (SkullMeta) besbe.getItemMeta();
        PlayerProfile profilebe = getProfile("http://textures.minecraft.net/texture/4a7e0d55ea8051569955da138697174a0bd76190b2d58b997509737dce5fb61f");
        metabesbe.setOwnerProfile(profilebe);
        metabesbe.setDisplayName("Би-бизнес");
        besbe.setItemMeta(metabesbe);
        BankCommand command = new BankCommand(RusBank.inst);
        Player player = (Player) event.getPlayer();
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) exit.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem(8, exit);
        if (DataUtils.hasBank(player.getName(), "beenkoof")) {
            if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    inventory.setItem(11, tin);
                    inventory.setItem(13, sbe);
                    inventory.setItem(15, besbe);
                } else {
                    inventory.setItem(12, tin);
                    inventory.setItem(14, sbe);
                }
            } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {

                inventory.setItem(12, tin);
                inventory.setItem(14, besbe);
            } else {
                inventory.setItem(13, tin);
            }
        } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
            if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                inventory.setItem(12, sbe);
                inventory.setItem(14, besbe);
            } else {
                inventory.setItem(13, sbe);
            }
        } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
            inventory.setItem(13, besbe);
        }
    } else if (event.getInventory().getHolder() instanceof Bank_Menureg) {
        Inventory inventory = event.getInventory();
        ItemStack tin = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metatin = (SkullMeta) tin.getItemMeta();
        PlayerProfile profile = getProfile("http://textures.minecraft.net/texture/8433b7964a05576ea1fd665e52edffd74311ae7f7aedaae6403bced329f4fdca");
        metatin.setOwnerProfile(profile);
        metatin.setDisplayName("Бинькофф");
        tin.setItemMeta(metatin);
        ItemStack sbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metasbe = (SkullMeta) sbe.getItemMeta();
        PlayerProfile profilesbe = getProfile("http://textures.minecraft.net/texture/5fb4b94f1a3f6f207632da797eddb11ddb3c0dffea8d67a6f2e420f1094cf198");
        metasbe.setOwnerProfile(profilesbe);
        metasbe.setDisplayName("Сби-банк");
        sbe.setItemMeta(metasbe);
        ItemStack besbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metabesbe = (SkullMeta) besbe.getItemMeta();
        PlayerProfile profilebe = getProfile("http://textures.minecraft.net/texture/4a7e0d55ea8051569955da138697174a0bd76190b2d58b997509737dce5fb61f");
        metabesbe.setOwnerProfile(profilebe);
        metabesbe.setDisplayName("Би-бизнес");
        besbe.setItemMeta(metabesbe);
        BankCommand command = new BankCommand(RusBank.inst);
        Player player = command.getReg().get("reg");
        for (int i = 0; i < 27; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) exit.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem(8, exit);
        if (DataUtils.hasBank(player.getName(), "beenkoof")) {
            if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                if (DataUtils.hasBank(player.getName(), "beebusiness")) {

                } else {
                    inventory.setItem(13, besbe);
                }
            } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {

                inventory.setItem(13, sbe);

            } else {
                inventory.setItem(12, sbe);
                inventory.setItem(14, besbe);
            }
        } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
            if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                inventory.setItem(13, tin);
            } else {
                inventory.setItem(12, tin);
                inventory.setItem(14, besbe);
            }
        } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
            inventory.setItem(12, tin);
            inventory.setItem(14, sbe);
        } else {
            inventory.setItem(11, tin);
            inventory.setItem(13, sbe);
            inventory.setItem(15, besbe);
        }
    }
    else if (event.getInventory().getHolder() instanceof Beebank_Menu) {
        Inventory inventory = event.getInventory();
            for (int i = 0; i < 27; i++) {
                inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
            BankCommand command = new BankCommand(RusBank.inst);
            ItemStack player = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta meta = (SkullMeta) player.getItemMeta();
            meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Личный кабинет");
            List list = new ArrayList<>();
            list.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE + "Имя: " + ChatColor.GREEN + event.getPlayer().getName());
            list.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE + "Наиграно: " + ChatColor.GREEN + PlaceholderAPI.setPlaceholders((Player) event.getPlayer(), "%statistic_time_played:days%д %statistic_time_played:hours%ч %statistic_time_played:minutes%м"));
            meta.setOwner(event.getPlayer().getName());
            meta.setLore(list);
            player.setItemMeta(meta);
            inventory.setItem(4, player);
            ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta mexit = (SkullMeta) exit.getItemMeta();
            mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
            mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
            mexit.setLore(null);
            exit.setItemMeta(mexit);
            inventory.setItem(8, exit);
            ItemStack pay = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta mpay = (SkullMeta) pay.getItemMeta();
            mpay.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Баланс");
            mpay.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/95fd67d56ffc53fb360a17879d9b5338d7332d8f129491a5e17e8d6e8aea6c3a"));
            mpay.setLore(null);
            pay.setItemMeta(mpay);
            inventory.setItem(21, pay);
            ItemStack shtr = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta mshtr = (SkullMeta) shtr.getItemMeta();
            mshtr.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Баланс");
            mshtr.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/95fd67d56ffc53fb360a17879d9b5338d7332d8f129491a5e17e8d6e8aea6c3a"));
            mshtr.setLore(null);
            shtr.setItemMeta(mshtr);
            ItemStack take = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta mtake = (SkullMeta) take.getItemMeta();
            mtake.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Переводы");
            mtake.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/c4c01642bf488b1c747f8f09139cb6d069cc85fb0becabe0429c3b7b8cb8fbc9"));
            mtake.setLore(null);
            take.setItemMeta(mtake);
            inventory.setItem(23, take);

    }else if (event.getInventory().getHolder() instanceof History_Menu){
        Inventory inventory = event.getInventory();
        for (int i = 0; i < 9; i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        BankCommand command = new BankCommand(RusBank.inst);
        ItemStack add = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta addItemMeta = (SkullMeta) add.getItemMeta();
        addItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/69ea1d86247f4af351ed1866bca6a3040a06c68177c78e42316a1098e60fb7d3"));
        addItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Вам перевели");
        add.setItemMeta(addItemMeta);
        inventory.setItem(5, add);
        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
        down.setItemMeta(downItemMeta);
        inventory.setItem(0, down);
        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
        up.setItemMeta(upItemMeta);
        inventory.setItem(1, up);

        ItemStack player = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) player.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Перевести");
        meta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8ae7bf4522b03dfcc866513363eaa9046fddfd4aa6f1f0889f03c1e6216e0ea0"));
        player.setItemMeta(meta);
        inventory.setItem(3, player);
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) player.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED +   "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem(8,exit);
        Database database = RusBank.inst.getDatabase();
        Statement countstatement = null;
        int count = 0;
        try {
            countstatement = database.getConnection().createStatement();
            String sql = "SELECT * FROM pay WHERE Users = '" + command.getPlayerMap().get("player").getName() + "' AND action = 'take'";
            ResultSet resultSet = countstatement.executeQuery(sql);
            count = database.resultSetCount(resultSet);
        } catch (SQLException e) {
            count = 0;
        }
        int i = count;
        for (int a = 9; a < 27; a++) {
            int sum = 0;
            String target = null;
            String bank = null;
            String messeg = null;
            String targetbank = null;
            int num = 0;
            try {
                Statement statement = database.getConnection().createStatement();
                String sql = "SELECT * FROM pay WHERE Users = '" + event.getPlayer().getName() + "' AND action = 'take' AND num = '" + i + "'";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    sum = resultSet.getInt("sum");
                    target = resultSet.getString("target");
                    bank = resultSet.getString("bank");
                    messeg = resultSet.getString("messeg");
                    targetbank = resultSet.getString("targetbank");
                    num = resultSet.getInt("num");
                    statement.close();
                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                    List list = new ArrayList<>();
                    hist.setOwner(target);
                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                    hist.setDisplayName(target);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + targetbank);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + bank);
                    hist.setLore(list);
                    histor.setItemMeta(hist);
                    inventory.setItem(a, histor);

                } else {
                    inventory.setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    statement.close();
                }
                statement.close();
            } catch (SQLException e) {
            }

            i--;

        }

    }
    else if (event.getInventory().getHolder() instanceof Historyadd_Menu ) {
        Inventory inventory = event.getInventory();
        for (int i = 0; i < 9; i++) {
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        BankCommand command = new BankCommand(RusBank.inst);
        ItemStack add = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta addItemMeta = (SkullMeta) add.getItemMeta();
        addItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8271a47104495e357c3e8e80f511a9f102b0700ca9b88e88b795d33ff20105eb"));
        addItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Ваши переводы");
        add.setItemMeta(addItemMeta);
        inventory.setItem(5, add);
        ItemStack down = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta downItemMeta = (SkullMeta) down.getItemMeta();
        downItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Предыдущая страница");
        downItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/b72d10e410df8d515abf35b76666f11b74639500cf0eeb6e70d45f38bd4bba3a"));
        down.setItemMeta(downItemMeta);
        inventory.setItem(0, down);
        ItemStack up = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta upItemMeta = (SkullMeta) up.getItemMeta();
        upItemMeta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Следущая страница");
        upItemMeta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8003f2c9f01d2ed58b900314d72fb25156ca9bf13b7fae3093104ce8fe964e9f"));
        up.setItemMeta(upItemMeta);
        inventory.setItem(1, up);

        ItemStack player = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) player.getItemMeta();
        meta.setDisplayName(ChatColor.DARK_BLUE + "" + ChatColor.RESET + "Перевести");

        meta.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/8ae7bf4522b03dfcc866513363eaa9046fddfd4aa6f1f0889f03c1e6216e0ea0"));
        player.setItemMeta(meta);
        inventory.setItem(3, player);
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) player.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem(8, exit);
        Database database = RusBank.inst.getDatabase();
        Statement countstatement = null;
        int count = 0;
        try {
            countstatement = database.getConnection().createStatement();
            String sql = "SELECT * FROM pay WHERE Users = '" + command.getPlayerMap().get("player").getName() + "' AND action = 'add'";
            ResultSet resultSet = countstatement.executeQuery(sql);
            count = database.resultSetCount(resultSet);
        } catch (SQLException e) {
            count = 0;
        }
        int i = count;
        for (int a = 9; a < 27; a++) {
            int sum = 0;
            String target = null;
            String bank = null;
            String messeg = null;
            String targetbank = null;
            int num = 0;
            try {
                Statement statement = database.getConnection().createStatement();
                String sql = "SELECT * FROM pay WHERE Users = '" + event.getPlayer().getName() + "' AND action = 'add' AND num = '" + i + "'";
                ResultSet resultSet = statement.executeQuery(sql);

                if (resultSet.next()) {
                    sum = resultSet.getInt("sum");
                    target = resultSet.getString("target");
                    bank = resultSet.getString("bank");
                    messeg = resultSet.getString("messeg");
                    targetbank = resultSet.getString("targetbank");
                    num = resultSet.getInt("num");
                    statement.close();
                    ItemStack histor = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta hist = (SkullMeta) histor.getItemMeta();
                    List list = new ArrayList<>();
                    hist.setOwner(target);
                    hist.getPersistentDataContainer().set(NamespacedKey.fromString("num"), PersistentDataType.INTEGER, num);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сумма: " + ChatColor.YELLOW + sum);
                    hist.setDisplayName(target);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Сообщение: " + ChatColor.YELLOW + messeg);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "В банк: " + ChatColor.YELLOW + bank);
                    list.add(ChatColor.RESET + "" + ChatColor.WHITE + "Из банка: " + ChatColor.YELLOW + targetbank);
                    hist.setLore(list);
                    histor.setItemMeta(hist);
                    inventory.setItem(a, histor);

                } else {
                    inventory.setItem(a, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
                    statement.close();
                }
                statement.close();
            } catch (SQLException e) {
            }

            i--;

        }

    }
    else if (event.getInventory().getHolder() instanceof MenuTake){
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getPlayer();
        BankCommand command = new BankCommand(RusBank.inst);
        ItemStack tin = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metatin = (SkullMeta) tin.getItemMeta();
        PlayerProfile profile = getProfile("http://textures.minecraft.net/texture/8433b7964a05576ea1fd665e52edffd74311ae7f7aedaae6403bced329f4fdca");
        metatin.setOwnerProfile(profile);
        metatin.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Счёт 'Бинькофф' ");
        List list = new ArrayList<>();
        list.add(ChatColor.WHITE + "" + ChatColor.RESET +ChatColor.WHITE +  "Баланс: " +  ChatColor.YELLOW  + String.valueOf(DataUtils.getSum(player.getName(), "beenkoof") + "АР"));
        list.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ЛКМ " + ChatColor.WHITE + "- Положить на счёт");
        list.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ПКМ " + ChatColor.WHITE + "- Снять со счёт");
        metatin.setLore(list);
        tin.setItemMeta(metatin);
        ItemStack sbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metasbe = (SkullMeta) sbe.getItemMeta();
        PlayerProfile profilesbe = getProfile("http://textures.minecraft.net/texture/5fb4b94f1a3f6f207632da797eddb11ddb3c0dffea8d67a6f2e420f1094cf198");
        metasbe.setOwnerProfile(profilesbe);
        metasbe.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET + "Счёт 'Сби-банк'");
        List listbe = new ArrayList<>();
        listbe.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE + "Баланс: " +  ChatColor.YELLOW  +String.valueOf(DataUtils.getSum(player.getName(), "Sbeebank") + "АР"));
        listbe.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ЛКМ " + ChatColor.WHITE + "- Положить на счёт");
        listbe.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ПКМ " + ChatColor.WHITE + "- Снять со счёт");
        metasbe.setLore(listbe);

        sbe.setItemMeta(metasbe);
        ItemStack besbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metabesbe = (SkullMeta) besbe.getItemMeta();
        PlayerProfile profilebe = getProfile("http://textures.minecraft.net/texture/4a7e0d55ea8051569955da138697174a0bd76190b2d58b997509737dce5fb61f");
        metabesbe.setOwnerProfile(profilebe);
        metabesbe.setDisplayName(ChatColor.YELLOW + "" + ChatColor.RESET +  "Счёт 'Би-бизнес'");
        List listbebi = new ArrayList<>();
        listbebi.add(ChatColor.WHITE + "" + ChatColor.RESET + ChatColor.WHITE + "Баланс: " +  ChatColor.YELLOW  + String.valueOf(DataUtils.getSum(player.getName(), "beebusiness") + "АР"));
        listbebi.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ЛКМ " + ChatColor.WHITE + "- Положить на счёт");
        listbebi.add(ChatColor.YELLOW+ "" + ChatColor.RESET + ChatColor.YELLOW+ "ПКМ " + ChatColor.WHITE + "- Снять со счёт");
        metabesbe.setLore(listbebi);
        besbe.setItemMeta(metabesbe);
        for (int i = 0; i < 27; i++){
            inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
        }
        ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta mexit = (SkullMeta) exit.getItemMeta();
        mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED +   "Закрыть");
        mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
        exit.setItemMeta(mexit);
        inventory.setItem( 8,exit);
        if (DataUtils.hasBank(player.getName(), "beenkoof")){
            if (DataUtils.hasBank(player.getName(), "Sbeebank")){
                if (DataUtils.hasBank(player.getName(), "beebusiness")){
                    inventory.setItem(11,tin);
                    inventory.setItem(13, sbe);
                    inventory.setItem(15, besbe);
                }else {
                    inventory.setItem(12, tin);
                    inventory.setItem(14, sbe);
                }
            }
            else if (DataUtils.hasBank(player.getName(), "beebusiness")){

                inventory.setItem(12, tin);
                inventory.setItem(14, besbe);
            }
            else {
                inventory.setItem(13, tin);
            }
        }else if (DataUtils.hasBank(player.getName(), "Sbeebank")){
            if (DataUtils.hasBank(player.getName(), "beebusiness")){
                inventory.setItem(12, sbe);
                inventory.setItem(14, besbe);
            }
            else {
                inventory.setItem(13, sbe);
            }
        }
        else if (DataUtils.hasBank(player.getName(), "beebusiness")){
            inventory.setItem(13, besbe);
        }
    }
    else if (event.getInventory().getHolder() instanceof Sender_Menu){
        Inventory inventory = event.getInventory();
        ItemStack tin = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metatin = (SkullMeta) tin.getItemMeta();
        PlayerProfile profile = getProfile("http://textures.minecraft.net/texture/8433b7964a05576ea1fd665e52edffd74311ae7f7aedaae6403bced329f4fdca");
        metatin.setOwnerProfile(profile);
        metatin.setDisplayName("Бинькофф");
        tin.setItemMeta(metatin);


        ItemStack sbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metasbe = (SkullMeta) sbe.getItemMeta();
        PlayerProfile profilesbe = getProfile("http://textures.minecraft.net/texture/5fb4b94f1a3f6f207632da797eddb11ddb3c0dffea8d67a6f2e420f1094cf198");
        metasbe.setOwnerProfile(profilesbe);
        metasbe.setDisplayName("Сби-банк");
        sbe.setItemMeta(metasbe);


        ItemStack besbe = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta metabesbe = (SkullMeta) besbe.getItemMeta();
        PlayerProfile profilebe = getProfile("http://textures.minecraft.net/texture/4a7e0d55ea8051569955da138697174a0bd76190b2d58b997509737dce5fb61f");
        metabesbe.setOwnerProfile(profilebe);
        metabesbe.setDisplayName("Би-бизнес");
        besbe.setItemMeta(metabesbe);

        BankCommand command = new BankCommand(RusBank.inst);
        if (command.getTarg().get(event.getPlayer().getUniqueId().toString()) != null) {
            OfflinePlayer player = command.getTarg().get(event.getPlayer().getUniqueId().toString());
            for (int i = 0; i < 27; i++) {
                inventory.setItem(i, new ItemStack(Material.BLACK_STAINED_GLASS_PANE));
            }
            ItemStack exit = new ItemStack(Material.PLAYER_HEAD);
            SkullMeta mexit = (SkullMeta) exit.getItemMeta();
            mexit.setDisplayName(ChatColor.RED + "" + ChatColor.RESET + ChatColor.RED + "Закрыть");
            mexit.setOwnerProfile(getProfile("http://textures.minecraft.net/texture/548d7d1e03e1af145b0125ab841285672b421265da2ab915015f9058438ba2d8"));
            exit.setItemMeta(mexit);
            inventory.setItem(8, exit);
            if (DataUtils.hasBank(player.getName(), "beenkoof")) {
                if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                    if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                        inventory.setItem(11, tin);
                        inventory.setItem(13, sbe);
                        inventory.setItem(15, besbe);
                    } else {
                        inventory.setItem(12, tin);
                        inventory.setItem(14, sbe);
                    }
                } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {

                    inventory.setItem(12, tin);
                    inventory.setItem(14, besbe);
                } else {
                    inventory.setItem(13, tin);
                }
            } else if (DataUtils.hasBank(player.getName(), "Sbeebank")) {
                if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                    inventory.setItem(12, sbe);
                    inventory.setItem(14, besbe);
                } else {
                    inventory.setItem(13, sbe);
                }
            } else if (DataUtils.hasBank(player.getName(), "beebusiness")) {
                inventory.setItem(13, besbe);
            }
        }
    }
}


    @EventHandler
    public void Chat(PlayerChatEvent event){
        BankCommand command = new BankCommand(RusBank.inst);
        if (getLeashed().contains(event.getPlayer().getUniqueId().toString())){
            try {
            int sum = Integer.parseInt(event.getMessage());
            }
            catch (NumberFormatException e2) {
                ChatUtils.sendMsg(event.getPlayer(),  ChatColor.RED + "\uD83D\uDEC8 Введено неверное число, попробуйте ещё раз\uD83D\uDEC8 ");
                event.setCancelled(true);
                return;
            }
            int sum = Integer.parseInt(event.getMessage());
            unleashPlayer(event.getPlayer());
            event.setCancelled(true);
            if (sum <= DataUtils.getSum(event.getPlayer().getName(), bank.get(event.getPlayer().getUniqueId().toString()))) {
                if (sum > 0) {
                    event.getPlayer().sendMessage("Сумма: " + ChatColor.GOLD + sum);
                    event.getPlayer().sendMessage(ChatColor.GOLD + "✎ Введите сообщение ✎");
                    banksum.put(event.getPlayer().getUniqueId().toString(), sum);
                    this.strings.add(event.getPlayer().getUniqueId().toString());
                }else {
                    event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Невозможная сумма, попробуйте ещё раз \uD83D\uDEC8");
event.setCancelled(true);
                }

            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Недостаточно средств, попробуйте ещё раз \uD83D\uDEC8");
                event.setCancelled(true);
            }
        }
        else if (getStrings().contains(event.getPlayer().getUniqueId().toString())){
            String messeg = event.getMessage();
            if (messeg.length() <= 100) {
                String targetname = command.getTargetinf().get(event.getPlayer().getUniqueId().toString());
                OfflinePlayer target = Bukkit.getOfflinePlayer(targetname);
                DataUtils.addSum(targetname, targetbank.get(event.getPlayer().getUniqueId().toString()), banksum.get(event.getPlayer().getUniqueId().toString()));
                DataUtils.takeSum(event.getPlayer().getName(), bank.get(event.getPlayer().getUniqueId().toString()), banksum.get(event.getPlayer().getUniqueId().toString()));
                event.getPlayer().sendMessage(ChatColor.BOLD + " ");
                event.getPlayer().sendMessage(ChatColor.GOLD + " " + ChatColor.BOLD + "Перевод выполнен");
                event.getPlayer().sendMessage("Вы перевели игроку: " + ChatColor.GOLD + targetname);
                event.getPlayer().sendMessage("Сумму: " + ChatColor.GOLD + banksum.get(event.getPlayer().getUniqueId().toString()) + " АР");
                event.getPlayer().sendMessage("С комментарием: " + ChatColor.GOLD + messeg);
                event.getPlayer().sendMessage(ChatColor.BOLD + " ");
                event.setCancelled(true);
                if (target.isOnline()) {
                    Player target1 = Bukkit.getPlayer(targetname);
                    target1.sendMessage(ChatColor.BOLD + " ");
                    target1.sendMessage(ChatColor.GOLD + " " + ChatColor.BOLD + "Новый перевод");
                    target1.sendMessage("Вам перевёл: " + ChatColor.GOLD + event.getPlayer().getName());
                    target1.sendMessage("Сумму: " + ChatColor.GOLD + banksum.get(event.getPlayer().getUniqueId().toString()) + " АР");
                    target1.sendMessage("С комментарием: " + ChatColor.GOLD + messeg);
                    target1.sendMessage(ChatColor.BOLD + " ");
                }
                String bankt = null;
                String targbankt = null;
                if (bank.get(event.getPlayer().getUniqueId().toString()) == "Sbeebank") {
                    bankt = "Сби-банк";
                }
                if (bank.get(event.getPlayer().getUniqueId().toString()) == "beenkoof") {
                    bankt = "Бинькофф";
                }
                if (bank.get(event.getPlayer().getUniqueId().toString()) == "beebusiness") {
                    bankt = "Би-Бизнес";
                }
                if (targetbank.get(event.getPlayer().getUniqueId().toString()) == "Sbeebank") {
                    targbankt = "Сби-банк";
                }
                if (targetbank.get(event.getPlayer().getUniqueId().toString()) == "beenkoof") {
                    targbankt = "Бинькофф";
                }
                if (targetbank.get(event.getPlayer().getUniqueId().toString()) == "beebusiness") {
                    targbankt = "Би-Бизнес";
                }
                Database database = RusBank.inst.getDatabase();
                for (int a = 1; a != -1; a++) {
                    Statement statement = null;
                    int count = 0;
                    try {
                        statement = database.getConnection().createStatement();
                        String sql = "SELECT * FROM pay WHERE Users = '" + event.getPlayer().getName() + "' AND action = 'take'";
                        ResultSet resultSet = statement.executeQuery(sql);
                        count = database.resultSetCount(resultSet);
                    } catch (SQLException e) {
                        count = 0;
                    }
                    if (a > count) {

                        try {
                            PreparedStatement preparedstatement = database.getConnection().prepareStatement("INSERT INTO  pay (Users, action, num,sum,target,bank,targetbank,messeg) VALUES (?,?,?,?,?,?,?,?)");
                            preparedstatement.setInt(4, banksum.get(event.getPlayer().getUniqueId().toString()));
                            preparedstatement.setString(1, event.getPlayer().getName());
                            preparedstatement.setString(5, targetname);
                            preparedstatement.setString(7, targbankt);
                            preparedstatement.setString(6, bankt);
                            preparedstatement.setString(8, messeg);
                            preparedstatement.setInt(3, a);
                            preparedstatement.setString(2, "take");
                            preparedstatement.executeUpdate();
                            preparedstatement.close();
                            a = -2;
                        } catch (SQLException e) {
                            event.setCancelled(true);
                        }

                    }
                }
                for (int a = 1; a != -1; a++) {
                    Statement statement = null;
                    int count = 0;
                    try {
                        statement = database.getConnection().createStatement();
                        String sql = "SELECT * FROM pay WHERE Users = '" + targetname + "' AND action = 'add'";
                        ResultSet resultSet = statement.executeQuery(sql);
                        count = database.resultSetCount(resultSet);
                    } catch (SQLException e) {
                        count = 0;
                    }
                    if (a > count) {
                        try {
                            PreparedStatement preparedstatement = database.getConnection().prepareStatement("INSERT INTO  pay (Users, action, num,sum,target,bank,targetbank,messeg) VALUES (?,?,?,?,?,?,?,?)");
                            preparedstatement.setInt(4, banksum.get(event.getPlayer().getUniqueId().toString()));
                            preparedstatement.setString(1, targetname);
                            preparedstatement.setString(5, event.getPlayer().getName());
                            preparedstatement.setString(7, bankt);
                            preparedstatement.setString(6, targbankt);
                            preparedstatement.setString(8, messeg);
                            preparedstatement.setInt(3, a);
                            preparedstatement.setString(2, "add");
                            preparedstatement.executeUpdate();

                            a = -2;
                        } catch (SQLException e) {

                            event.setCancelled(true);
                        }
                    }
                }
                command.getTargetinf().remove(event.getPlayer().getUniqueId().toString());
                banksum.remove(event.getPlayer().getUniqueId().toString());
                bank.remove(event.getPlayer().getUniqueId().toString());
                targetbank.remove(event.getPlayer().getUniqueId().toString());
                event.setCancelled(true);
                unstrig(event.getPlayer());
            }else {
                event.getPlayer().sendMessage(ChatColor.RED + "🛈 Слишком длинное сообщение 🛈");
                event.setCancelled(true);
            }
        } else  if (getTake().contains(event.getPlayer().getUniqueId().toString())){
            if (bankadd.get(event.getPlayer().getUniqueId().toString()) != null) {
                bankadd.remove(event.getPlayer().getUniqueId().toString());
            }
            if (getAdd().contains(event.getPlayer().getUniqueId().toString())) {
                unadd(event.getPlayer());
            }
            try {
            int sum = Integer.parseInt(event.getMessage());
        }
            catch (NumberFormatException e2) {
            ChatUtils.sendMsg(event.getPlayer(), ChatColor.RED + "\uD83D\uDEC8 \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0432\u0435\u0440\u043d\u043e\u0435 \u0447\u0438\u0441\u043b\u043e, попробуйте ещё раз \uD83D\uDEC8 ");
event.setCancelled(true);
            return;
        }
        int sum = Integer.parseInt(event.getMessage());
            unleashPlayer(event.getPlayer());
            event.setCancelled(true);
            if (sum <= DataUtils.getSum(event.getPlayer().getName(), banktake.get(event.getPlayer().getUniqueId().toString()))) {
                if (sum > 0) {
                    int air = 0;
                    for (int i = 0; i < 36; i++){
                        if (event.getPlayer().getInventory().getItem(i) == null||event.getPlayer().getInventory().getItem(i) == null){
                            air = air + 64;
                        }
                        else if (event.getPlayer().getInventory().getItem(i).getType() == Material.DEEPSLATE_DIAMOND_ORE){
                            air = air + 64 - event.getPlayer().getInventory().getItem(i).getAmount();
                        }
                    }
                    if (sum <= air) {
                        DataUtils.takeSum(event.getPlayer().getName(), banktake.get(event.getPlayer().getUniqueId().toString()), sum);
                        event.getPlayer().getInventory().addItem(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, sum));
                        event.getPlayer().sendMessage(ChatColor.GOLD + " " + ChatColor.BOLD + "Новое Cнятие");
                        event.getPlayer().sendMessage("Сумма: " + ChatColor.GOLD+ sum + " АР");
                        banktake.remove(event.getPlayer().getUniqueId().toString());
                        event.setCancelled(true);
                        untake(event.getPlayer());
                    }else {
                        event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Инвентарь полен, попробуйте ещё раз \uD83D\uDEC8");
                        event.setCancelled(true);
                    }
                }else {
                    event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Невозможная сумма, попробуйте ещё раз \uD83D\uDEC8");
                    event.setCancelled(true);
                }
            } else {
                event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Недостаточно средств, попробуйте ещё раз \uD83D\uDEC8");
                event.setCancelled(true);
            }
        }
        else if (getAdd().contains(event.getPlayer().getUniqueId().toString())){
            if (banktake.get(event.getPlayer().getUniqueId().toString()) != null) {
                banktake.remove(event.getPlayer().getUniqueId().toString());
            }
            if (getTake().contains(event.getPlayer().getUniqueId().toString())) {
                untake(event.getPlayer());
            }
            try {
            int sum = Integer.parseInt(event.getMessage());
        }
            catch (NumberFormatException e2) {
            ChatUtils.sendMsg(event.getPlayer(), ChatColor.RED + "\uD83D\uDEC8 \u0412\u0432\u0435\u0434\u0438\u0442\u0435 \u0432\u0435\u0440\u043d\u043e\u0435 \u0447\u0438\u0441\u043b\u043e, попробуйте ещё раз \uD83D\uDEC8 ");
            event.setCancelled(true);
            return;
        }

            int sum = Integer.parseInt(event.getMessage());
            int money = DataUtils.getSum(event.getPlayer().getName(), bankadd.get(event.getPlayer().getUniqueId().toString()));
            int maxBal2 = Banks.getConfig().getInt(bankadd.get(event.getPlayer().getUniqueId().toString()) + ".max-balance");
            Player player = event.getPlayer();
            unleashPlayer(event.getPlayer());
            event.setCancelled(true);
            if (sum + money < maxBal2) {
                if (sum > 0) {
                    int a =0;
                    if (!player.getInventory().contains(Material.DEEPSLATE_DIAMOND_ORE, sum)) {
                        player.sendMessage(ChatColor.RED + "\uD83D\uDEC8 Недостаточно средств, попробуйте ещё раз \uD83D\uDEC8");
                        event.setCancelled(true);
                    } else {
                        player.getInventory().removeItem(new ItemStack(Material.DEEPSLATE_DIAMOND_ORE, sum));
                        DataUtils.addSum(player.getName(), bankadd.get(player.getUniqueId().toString()), sum);
                        event.getPlayer().sendMessage(ChatColor.GOLD + " " + ChatColor.BOLD + "Пополнение выполнено");
                        event.getPlayer().sendMessage("Сумма: " + ChatColor.GOLD + sum + " АР");
                        bankadd.remove(event.getPlayer().getUniqueId().toString());
                        unadd(event.getPlayer());
                        event.setCancelled(true);
                    }
                }else {
                    event.getPlayer().sendMessage(ChatColor.RED + "\uD83D\uDEC8 Невозможная сумма, попробуйте ещё раз \uD83D\uDEC8");

                    event.setCancelled(true);
                }
            }else {
                ChatUtils.sendMsg(player, ChatColor.RED + "\uD83D\uDEC8 Превышен лимит баланса, попробуйте ещё раз \uD83D\uDEC8 ");
                ChatUtils.sendMsg(player, ChatColor.RED +"\uD83D\uDEC8 Максимальный баланс: "  + maxBal2 + "АР &c\uD83D\uDEC8");
                event.setCancelled(true);

            }


        }

    }

    private void unleashPlayer(final Player leashedPlayer) {this.getLeashed().remove(leashedPlayer.getUniqueId().toString());}
    private void unstrig(final Player strinplayer){
        this.getStrings().remove(strinplayer.getUniqueId().toString());
    }
    private void unadd(final Player strinplayer){
        this.getAdd().remove(strinplayer.getUniqueId().toString());
    }
    private void untake(final Player strinplayer){
        this.getTake().remove(strinplayer.getUniqueId().toString());
    }

    public List<String> getLeashed() {return this.leashed;}
    public List<String> getStrings(){
        return this.strings;
    }
    public List<String> getAdd(){
        return this.add;
    }
    public List<String> getTake(){
        return this.take;
    }
}
