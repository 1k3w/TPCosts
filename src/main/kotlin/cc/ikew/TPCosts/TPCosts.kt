package cc.ikew.TPCosts

import cc.ikew.TPCosts.listener.PlayerListener
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class TPCosts: JavaPlugin() {

    override fun onEnable() {
		saveDefaultConfig()
		File("players").mkdir();
		server.pluginManager.registerEvents(PlayerListener(config), this)
    }

	override fun onDisable() {
	}
}
