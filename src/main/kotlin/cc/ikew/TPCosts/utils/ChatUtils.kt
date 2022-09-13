package cc.ikew.TPCosts.utils

import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player

object ChatUtils {
	var papi: Boolean = false;

	fun checkPAPI(){
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) papi = true;
	}

	fun send(p: Player, m: String){
		if (papi){
			p.sendMessage(PlaceholderAPI.setPlaceholders(p,m))
		}else{
			p.sendMessage(ChatColor.translateAlternateColorCodes('&', m))
		}
	}
}

