package me.SirMonkeyBoy.Bank.commands.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;
import me.SirMonkeyBoy.Bank.commands.SubCommand;

public class BalanceCommand extends SubCommand{
	
	private Main bank;
	
    public BalanceCommand(Main bank) {
         this.bank = bank;
    }
	

	@Override
	public String getName() {
		return "balance";
	}

	@Override
	public String getDescription() {
		return "Bank Balance";
	}

	@Override
	public String getSyntax() {
		return "/bank balance";
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
