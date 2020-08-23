package me.harley.cropstop;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import me.harley.display.CropCategoryInventory;
import me.harley.display.TopPlayersInventory;
import me.harley.manager.CropsTopCmd;
import me.harley.manager.ManageData;

public class CropsTop extends JavaPlugin {

	private ManageData manageData;

	@Override
	public void onEnable() {
		manageData = new ManageData(this);
		saveDefaultConfig();
		reloadConfig();

		getCommand("cropstop").setExecutor(new CropsTopCmd(this));

		getServer().getPluginManager().registerEvents(new CropCategoryInventory(this), this);
		getServer().getPluginManager().registerEvents(new TopPlayersInventory(), this);
		getServer().getPluginManager().registerEvents(new Events(), this);
		getServer().getPluginManager().registerEvents(new ManageData(this), this);

		setup();

		for (Player online : Bukkit.getOnlinePlayers()) {
			manageData.setupPlayer(online);
		}

		manageData.registerPlayers();
		manageData.startAutoSave();
	}

	@Override
	public void onDisable() {
		manageData.uploadPlayers();
	}

	private FileConfiguration data;
	private File dfile;

	private void setup() {
		dfile = new File(this.getDataFolder(), "data.yml");
		if (!dfile.exists())
			try {
				dfile.createNewFile();
			} catch (IOException e) {
				Bukkit.getServer().getLogger().severe("Error while creating data.yml!");
			}
		data = YamlConfiguration.loadConfiguration(dfile);
	}

	public void saveData() {
		try {
			data.save(dfile);
		} catch (IOException e) {
			Bukkit.getServer().getLogger().log(Level.SEVERE, "Error while saving data!");
		}
	}

	public FileConfiguration getData() {
		return data;
	}

}
