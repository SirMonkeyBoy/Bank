package me.SirMonkeyBoy.Bank.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;

public class Reload implements CommandExecutor {
	
	private Main bank;
	
    public Reload(Main bank) {
         this.bank = bank;
         bank.getCommand("bank2").setExecutor(this);
    }

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String labal, String[] args) {
			if (labal.equalsIgnoreCase("bank2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (!player.hasPermission("bank.bank")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
			if (args.length == 0) {
				// /bank
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.help")) {
					player.sendMessage(ChatColor.GREEN + "Usage: /bank {reload/balance/bal/deposit/withdraw/help}");
					return true;
				}
				if (!player.hasPermission("bank.help")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
			}
			if (args.length > 0) {
				// /bank help
				if (args[0].equalsIgnoreCase("help2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.help")) {
					player.sendMessage(ChatColor.GREEN + "Usage: /bank {reload/balance/bal/deposit/withdraw/help}");
					bank.reloadConfig();
					return true;
				}
				else {
					if (!player.hasPermission("bank.help")) {
						player.sendMessage(ChatColor.RED + "You can't run this command");
						return true;
					}
					}
				}
				}
			}
			if (args.length > 0) {
				// /bank reload
				if (args[0].equalsIgnoreCase("reload2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.reload")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("reload-message")));
					bank.reloadConfig();
					return true;
				}
				else {
					if (!player.hasPermission("bank.reload")) {
						player.sendMessage(ChatColor.RED + "You can't run this command");
						return true;
					}
					}
				}
				}
			}
			if (args.length > 0) {
				// /bank deposit
				if (args[0].equalsIgnoreCase("deposit2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.deposit")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("deposit-message")));
					return true;
				}else {
				if (!player.hasPermission("bank.deposit")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
				}
				}
			}
			if (args.length > 0) {
				// /bank withdraw
				if (args[0].equalsIgnoreCase("withdraw2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.withdraw")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("withdraw-message")));
					return true;
				}
				else {
				if (!player.hasPermission("bank.withdraw")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
				}
				}
			}
			if (args.length > 0) {
				// /bank balance
				if (args[0].equalsIgnoreCase("balance2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.balance")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("balance-message")));
					return true;
				}else {
				if (!player.hasPermission("bank.balance")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
				}
				}
			}
			if (args.length > 0) {
				// /bank bal
				if (args[0].equalsIgnoreCase("bal2")) {
				if (sender instanceof Player) {
					Player player = (Player) sender;
				if (player.hasPermission("bank.balance")) {
					player.sendMessage(ChatColor.translateAlternateColorCodes('&', bank.getConfig().getString("balance-message")));
					return true;
				}else {
				if (!player.hasPermission("bank.balance")) {
					player.sendMessage(ChatColor.RED + "You can't run this command");
					return true;
				}
				}
				}
				}
			}
		}
		return false;
	}
}
