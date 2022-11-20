package me.SirMonkeyBoy.Bank.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;
import me.SirMonkeyBoy.Bank.commands.SubCommand;

public class BalCommand extends SubCommand {
	
	private Main bank;
	
    public BalCommand(Main bank) {
         this.bank = bank;
    }
    
	@Override
	public String getName() {
		return "bal";
	}

	@Override
	public String getDescription() {
		return "Bank Bal";
	}

	@Override
	public String getSyntax() {
		return "/bank bal";
	}

	@Override
	public void perform(Player player, String[] args) {
		if (args.length > 0) {
			// /bank balance
			if (player.hasPermission("bank.balance")) {
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("balance-message")));
			}
			else {
				if (!player.hasPermission("bank.balance")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
			}
			}
		}
		
	}
		
}