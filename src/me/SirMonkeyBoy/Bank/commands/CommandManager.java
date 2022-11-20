package me.SirMonkeyBoy.Bank.commands;

import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.SirMonkeyBoy.Bank.Main;
import me.SirMonkeyBoy.Bank.commands.subcommands.BalanceCommand;
import me.SirMonkeyBoy.Bank.commands.subcommands.DepositCommand;
import me.SirMonkeyBoy.Bank.commands.subcommands.HelpCommand;
import me.SirMonkeyBoy.Bank.commands.subcommands.ReloadCommand;
import me.SirMonkeyBoy.Bank.commands.subcommands.WithdrawCommand;

public class CommandManager implements CommandExecutor {
	
	@SuppressWarnings("unused")
	private Main bank;
	
	private ArrayList<SubCommand> subcommands = new ArrayList<>();
	
	public CommandManager(Main bank) {
		this.bank = bank;
		subcommands.add(new ReloadCommand(bank));
		subcommands.add(new DepositCommand(bank));
		subcommands.add(new WithdrawCommand(bank));
		subcommands.add(new HelpCommand(bank));
		subcommands.add(new BalanceCommand(bank));
	}
	
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String labal, String[] args) {
    	
    	if (sender instanceof Player) {
			Player player = (Player) sender;
			
			if (args.length > 0) {
				
				for (int i = 0; i < getSubcommands().size(); i++) {
					if (args[0].equalsIgnoreCase(getSubcommands().get(i).getName())) {
						getSubcommands().get(i).perform(player, args);;
					}
				}
				
			}
			else if(args.length == 0) {
	    		player.sendMessage(ChatColor.GREEN + "Usage: /bank {reload/balance/bal/deposit/withdraw/help}");
				return true;
	    	}
    	}
    	
		return true;
    }
    
    public ArrayList<SubCommand> getSubcommands() {
    	return subcommands;
    	
    }
    
    
}
