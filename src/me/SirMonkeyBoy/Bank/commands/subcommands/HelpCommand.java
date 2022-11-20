package me.SirMonkeyBoy.Bank.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;
import me.SirMonkeyBoy.Bank.commands.SubCommand;

public class HelpCommand extends SubCommand{
	
private Main bank;
	
    public HelpCommand(Main bank) {
         this.bank = bank;
    }
	
	public String getName() {
	return "help";
	
	}
	
	public String getDescription() {
		return "Help list";
		
	}
	
	public String getSyntax() {
		return "/bank help";
		
	}
	
	public void perform(Player player, String args[]) {
		
		if (args.length > 0) {
			// /bank help
			if (player.hasPermission("bank.help")) {
				player.sendMessage(ChatColor.GREEN + "Usage: /bank {reload/balance/bal/deposit/withdraw/help}");
				bank.reloadConfig();
		}
		else {
			if (!player.hasPermission("bank.help")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
			}
		}
		}
	}
}