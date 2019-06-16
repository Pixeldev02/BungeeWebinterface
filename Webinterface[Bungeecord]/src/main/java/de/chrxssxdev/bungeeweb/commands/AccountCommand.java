package de.chrxssxdev.bungeeweb.commands;

import de.chrxssxdev.bungeeweb.BungeeWeb;
import de.chrxssxdev.bungeeweb.Data.Strings;
import de.chrxssxdev.bungeeweb.Enums.AccountType;
import de.chrxssxdev.bungeeweb.mysql.manager.AccountManager;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class AccountCommand extends Command {

    public AccountCommand() {
        super("account");
    }

    @Override
    public void execute(CommandSender cs, String[] args) {
        if(cs instanceof ProxiedPlayer) {
            ProxiedPlayer p = (ProxiedPlayer) cs;
            BungeeWeb.getInstance().getProxy().broadcast(p.getName());
            if (p.hasPermission("accountmanager.use")) {
                if (args.length != 4 && args.length != 2) {
                    p.sendMessage("§7§m--------------------------------------------------");
                    p.sendMessage(" §7/account create <username> <password> <group>");
                    p.sendMessage(" §7/account delete <username>");
                    p.sendMessage(" §7/account edit <username> <oldPassword> <newPassword>");
                    p.sendMessage("§7§m--------------------------------------------------");
                }

                if (args.length == 4) {
                    AccountType type = null;
                    if (args[0].equalsIgnoreCase("create")) {
                        if (!(args[1].length() > 5)) {
                            p.sendMessage(Strings.getPrefix() + "Der Nutzername muss länger als 5 Zeichen sein.");
                        }
                        if (!(args[2].length() > 8)) {
                            p.sendMessage(Strings.getPrefix() + "Das Passwort muss länger als 8 Zeichen sein.");
                        }
                        if (!(args[3].equalsIgnoreCase("admin") || args[3].equalsIgnoreCase("team"))) {
                            p.sendMessage(Strings.getPrefix() + "Mögliche Gruppen sind: §cAdmin§7, §bTeam");
                        }

                        if (AccountManager.accountExists(args[1])) {
                            p.sendMessage(Strings.getPrefix() + "§cEs existiert bereits ein Nutzer mit diesem Namen!");
                        }

                        if (args[3].equalsIgnoreCase("admin")) {
                            type = AccountType.ADMIN;
                        } else if (args[3].equalsIgnoreCase("team")) {
                            type = AccountType.TEAM;
                        }

                        AccountManager.createAccount(args[1], args[2], type);
                        p.sendMessage(Strings.getPrefix() + "Der Account §a" + args[1] + " mit der Gruppe §c" + type.getName() + " §7wurde erfolgreich erstellt.");
                    }
                }
            } else p.sendMessage(Strings.getNoPerm());
        }
    }
}
