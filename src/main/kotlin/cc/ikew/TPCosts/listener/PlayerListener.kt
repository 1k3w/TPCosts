package cc.ikew.TPCosts.listener

import cc.ikew.TPCosts.TPplayer
import cc.ikew.TPCosts.utils.ChatUtils
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerChangedWorldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.event.player.PlayerTeleportEvent
import java.io.File
import java.io.InputStream
import kotlin.math.cos
import kotlin.math.pow

class PlayerListener(val config: FileConfiguration) : Listener {

	val players: ArrayList<TPplayer> = ArrayList();

	@EventHandler
	fun onJoin(e: PlayerJoinEvent){
		val f: File = File("players/${e.player.uniqueId}");

		if (!f.exists()) {
			f.createNewFile()
			players.add(TPplayer(e.player.uniqueId))
			return;
		}

		val stream: InputStream = f.inputStream()

		val data = stream.bufferedReader().readLine();

		players.add(Json.decodeFromString(data))
	}

	@EventHandler
	fun onLeave(e: PlayerQuitEvent){
		var p: TPplayer? = null;
		players.forEach {
			if (e.player.uniqueId.equals(it.uuid)){
				p = it;
				it.setLoc(e.player.location.world.name, e.player.location)
				it.save()
			}
		}
		players.remove(p)
	}

	@EventHandler
	fun onTP(e: PlayerTeleportEvent){
		if (e.cause == PlayerTeleportEvent.TeleportCause.END_GATEWAY
			|| e.cause == PlayerTeleportEvent.TeleportCause.END_PORTAL
			|| e.cause == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) return
		//if (e.player.hasPermission("tpCosts.bypass") || config.getStringList("whitelist").contains(e.to.world.name)) return
		var distance: Int = 0;
		var p: TPplayer? = null;
		players.forEach {
			if (e.player.uniqueId.equals(it.uuid)){
				p = it;
			}
		}

		if (e.to.world == e.from.world){

			distance = Math.sqrt(Math.pow((e.to.x - e.from.x), 2.0) + Math.pow((e.to.z - e.from.z), 2.0)).toInt()
		}else{
			if (p?.getLoc(e.to.world.name) == null) return
			val l = p?.getLoc(e.to.world.name)
			println("${l?.x} x ${l?.z}")
			distance = Math.sqrt(Math.pow((e.to.x - (l?.x ?: 0.0)), 2.0) + Math.pow((e.to.z - (l?.z ?: 0.0)), 2.0)).toInt()
		}

		val cost: Double = config.getDouble("amount") * (distance / config.getInt("per"))
		if (e.player.level < cost) {
			e.isCancelled = true;
			ChatUtils.send(e.player,
				config.getString("messages.denied")?.replace("{blocks}", "$distance")
					?.replace("{cost}", String.format("%.1f", cost))?: ""
			)
			return
		}
		e.player.level = (e.player.level - cost).toInt()
		ChatUtils.send(e.player,
			config.getString("messages.teleported")?.replace("{blocks}", "$distance")
				?.replace("{cost}", String.format("%.1f", cost))?: ""
		)
	}

	@EventHandler
	fun onteleport(e: PlayerTeleportEvent){
		//if (e.to.world == e.from.world) return
		players.forEach {
			if (e.player.uniqueId.equals(it.uuid)){
				it.setLoc(e.from.world.name, e.from)
			}
		}
	}
}
