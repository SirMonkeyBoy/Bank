package me.SirMonkeyBoy.Bank;

import org.bukkit.ChatColor;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import me.SirMonkeyBoy.Bank.commands.CommandManager;
import net.milkbowl.vault.economy.Economy;

public class Main extends JavaPlugin {
	
	public Economy eco;
	
	@Override
	public void onEnable() {
		
		this.saveDefaultConfig();
		
		getCommand("bank").setExecutor(new CommandManager(this));
		
		
		if (!setupEconomy()) {
			System.out.println(ChatColor.RED + "For Bank you need Vault and an Economy Plugin Installed");
			getServer().getPluginManager().disablePlugin(this);
			return;
		}
	}
	
	@Override
	public void onDisable() {
		
	}

	private boolean setupEconomy() {
		RegisteredServiceProvider<Economy> economy = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
		if (economy != null)
			eco = economy.getProvider();
		return (eco != null);
	}
}
