package me.sirmonkeyboy.bank.Utill;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;

public class Utills {

    static Audience console = Bukkit.getConsoleSender();

    public static void StartBanner() {
        console.sendMessage(Component.text("########################################################################################################################"));
        console.sendMessage(Component.text("#").append(Component.text("  #   #   ###   #       #    ####    #####      #####     #              #      ####      ##     #       #   #    #   ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #  #     #    # #     #   #        #    #    #     #    # #          # #      #   #    #  #    # #     #   #  #     ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  ##       #    #   #   #   #   ##   #     #  #       #   #   #      #   #      ####    ######   #   #   #   ##       ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #  #     #    #     # #   #    #   #    #    #     #    #    #   #     #      #   #   #    #   #     # #   #  #     ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #   #   ###   #       #    ####    #####      #####     #      #       #      ####    #    #   #       #   #    #   ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("########################################################################################################################"));
    }
}
