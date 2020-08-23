package me.harley.manager;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import me.harley.cropstop.CropsTop;
import me.harley.display.CropCategoryInventory;

public class CropsTopCmd implements CommandExecutor {

	private CropsTop main;
	private ManageData data = new ManageData();

	public CropsTopCmd(CropsTop main) {
		this.main = main;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (sender instanceof Player) {
			Player p = (Player) sender;
			if (cmd.getName().equalsIgnoreCase("cropstop")) {
				if (args.length == 0)
					new CropCategoryInventory(main).createInventory(p);
				if (args.length == 1) {
					if (args[0].equalsIgnoreCase("reload")) {
						if (p.hasPermission("cropstop.reload")) {
							main.saveConfig();
							main.saveData();
							p.sendMessage(main.getConfig().getString("reloaded").replace("&", "§"));
							return true;
						}
						p.sendMessage(main.getConfig().getString("no-perms").replace("&", "§"));
					}
					if (args[0].equalsIgnoreCase("update")) {
						if (p.hasPermission("cropstop.update")) {
							new ManageData().uploadPlayers();
							p.sendMessage("Updated!");
							return true;
						}
						p.sendMessage(main.getConfig().getString("no-perms").replace("&", "§"));
					}
				}
			}
		}
		return true;
	}

}
