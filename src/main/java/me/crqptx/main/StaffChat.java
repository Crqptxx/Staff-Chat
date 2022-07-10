
/*
 * StaffChat, Een simple staffchat plugin!
 * Copyright (C) 2022 Justin. K <Crqptx@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package me.crqptx.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


public class StaffChat extends JavaPlugin {

    private File customConfigFile;
    private FileConfiguration customConfig;

    @Override
    public void onEnable() {

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[SC] Plugin is aan het starten...");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[SC] Laden van de config...");
        createCustomConfig();
        getConfig().addDefault("Version", "1.0");
        getConfig().addDefault("Prefix", "&7[&8SC&7] ");
        getConfig().addDefault("No Permission", "&4ERROR: Je hebt hier geen permissie voor!");
        saveDefaultConfig();
        getConfig().options().copyDefaults(true);
        saveConfig();
        saveDefaultConfig();
        getLogger().info(ChatColor.translateAlternateColorCodes('&', "<text>"));
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[SC] Config is geladen...");

    }

    public FileConfiguration getCustomConfig() {
        return this.customConfig;
    }

    private void createCustomConfig() {
        customConfigFile = new File(getDataFolder(), "config.yml");
        if (!customConfigFile.exists()) {
            customConfigFile.getParentFile().mkdirs();
            saveResource("config.yml", false);
        }

        customConfig = new YamlConfiguration();
        try {
            customConfig.load(customConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onDisable() {

        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "[SC] Plugin is gestopt!");
    }




    public boolean onCommand(CommandSender sender, Command cmd, String list, String args[]) {

        // Prefix
        String nonColoredPrefix = getConfig().getString("Prefix");
        String coloredPrefix = ChatColor.translateAlternateColorCodes('&', nonColoredPrefix);

        if(cmd.getName().equalsIgnoreCase("sc")) {

            if(sender.hasPermission("SC.use")) {

                if(args.length == 0) {

                    sender.sendMessage(coloredPrefix + ChatColor.RED + "ERROR: Niet genoeg argumenten!");
                    return true;

                }

                StringBuilder x = new StringBuilder();
                for(int i = 0; i < args.length; i++) {

                    x.append(args[i] + " ");

                }

                for(Player all : Bukkit.getOnlinePlayers()){
                    if (all.hasPermission("SC.see")) {

                        String COLOR = getConfig().getString("Color");
                        String ColorBeforeName = getConfig().getString("ColorBeforeName");
                        String ColorAfterName = getConfig().getString("ColorAfterName");

                        all.sendMessage(coloredPrefix + ColorBeforeName + sender.getName() + ColorAfterName + ": " + x.toString());

                    }
                }
                return true;

            }
            else {

                String nonColoredPermError = getConfig().getString("Geen permissie");
                String coloredPermError  = ChatColor.translateAlternateColorCodes('&', nonColoredPermError);

                sender.sendMessage(coloredPrefix + " " + coloredPermError);
                return true;

            }

        }

        if(cmd.getName().equalsIgnoreCase("screload")) {

            if(sender.hasPermission("SC.reload")) {

                sender.sendMessage(coloredPrefix + ChatColor.RED + "Reloading!");
                reloadConfig();
                sender.sendMessage(coloredPrefix + ChatColor.RED + "Reloaded!");

                return true;

            }
            else {

                String nonColoredPermError = getConfig().getString("Geen permissie");
                String coloredPermError  = ChatColor.translateAlternateColorCodes('&', nonColoredPermError);

                sender.sendMessage(coloredPrefix + " " + coloredPermError);
                return true;

            }

        }

        return true;

    }

}