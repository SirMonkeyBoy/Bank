package me.sirmonkeyboy.bank.Utils;

import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;

import org.bukkit.Bukkit;

public class Utils {

    static Audience console = Bukkit.getConsoleSender();

    public static void getStartBanner() {
        console.sendMessage(Component.text("########################################################################################################################"));
        console.sendMessage(Component.text("#").append(Component.text("  #   #   ###   #       #    ####    #####      #####     #              #      ####      ##     #       #   #    #   ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #  #     #    # #     #   #        #    #    #     #    # #          # #      #   #    #  #    # #     #   #  #     ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  ##       #    #   #   #   #   ##   #     #  #       #   #   #      #   #      ####    ######   #   #   #   ##       ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #  #     #    #     # #   #    #   #    #    #     #    #    #   #     #      #   #   #    #   #     # #   #  #     ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("#").append(Component.text("  #   #   ###   #       #    ####    #####      #####     #      #       #      ####    #    #   #       #   #    #   ").color(NamedTextColor.GOLD)).append(Component.text("#")));
        console.sendMessage(Component.text("########################################################################################################################"));
    }

    public static void getErrorLogger(String message) {
        console.sendMessage(Component.text("[").color(NamedTextColor.GRAY)
            .append(Component.text("KingdomBank").color(NamedTextColor.GOLD))
                .append(Component.text("] ").color(NamedTextColor.GRAY))
                    .append(Component.text(message).color(NamedTextColor.DARK_RED)));
    }

    public static void logger(Component message) {
        console.sendMessage(Component.text("[").color(NamedTextColor.GRAY)
            .append(Component.text("KingdomBank").color(NamedTextColor.GOLD))
                .append(Component.text("] ").color(NamedTextColor.GRAY))
                    .append(message));
    }
}
