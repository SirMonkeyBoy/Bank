package me.SirMonkeyBoy.Bank.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;
import me.SirMonkeyBoy.Bank.commands.SubCommand;

public class ReloadCommand extends SubCommand { // /bank reload
	
	private Main bank;
	
    public ReloadCommand(Main bank) {
         this.bank = bank;
    }
	
	public String getName() {
	return "reload";
	
	}
	
	public String getDescription() {
		return "Reload Config File";
		
	}
	
	public String getSyntax() {
		return "/bank reload";
		
	}
	
	public void perform(Player player, String args[]) {
		if (args.length > 0) {
			// /bank reload
			if (player.hasPermission("bank.reload")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("reload-message")));
				bank.reloadConfig();
			}
			else {
				if (!player.hasPermission("bank.reload")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
				}
			}
			}
		}
	}