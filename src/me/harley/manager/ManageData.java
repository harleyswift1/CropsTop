package me.harley.manager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import me.harley.cropstop.CropsTop;

public class ManageData implements Listener {

	private CropsTop main;

	public ManageData(CropsTop main) {
		this.main = main;
	}

	public ManageData() {
	}

	public void registerPlayers() {
		Bukkit.getLogger().log(Level.INFO, "[CropsTop] Registering players...");
		if (main.getData().getConfigurationSection("stats") != null)
			for (String players : main.getData().getConfigurationSection("stats").getKeys(false)) {
				new CropTopPlayer(UUID.fromString(players), new HashMap<>(), getBroken(players, "sugar_cane_block"),
						getBroken(players, "cactus"), getBroken(players, "wheat"), getBroken(players, "pumpkin"),
						getBroken(players, "melon"), getBroken(players, "carrot"), getBroken(players, "potato"));
			}
	}

	public void uploadPlayers() {
		for (CropTopPlayer players : CropTopPlayer.getPlayers()) {
			players.saveToFile();
		}
	}

	@EventHandler
	public void onSetupPlayer(PlayerJoinEvent e) {
		setupPlayer(e.getPlayer());
	}

	public List<CropTopPlayer> getTop() {
		List<CropTopPlayer> ar = new ArrayList<>(CropTopPlayer.getPlayers());

		Collections.sort(ar, new Sort());
		Collections.reverse(ar);

		return ar;
	}

	public void setupPlayer(Player p) {
		String uuid = p.getUniqueId().toString();
		if (!main.getData().contains("stats." + uuid)) {
			new CropTopPlayer(p.getUniqueId(), new HashMap<>(), 0, 0, 0, 0, 0, 0, 0).saveToFile();
		}
	}

	public int getBroken(String offlinePlayer, String crop) {
		return main.getData().getInt("stats." + offlinePlayer + ".crops." + crop);
	}

	public void startAutoSave() {
		Bukkit.getScheduler().scheduleSyncRepeatingTask(main, () -> {
			uploadPlayers();
		}, 0, 20 * 60 * 5);
	}

}
