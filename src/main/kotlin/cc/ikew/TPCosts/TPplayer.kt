package cc.ikew.TPCosts

import cc.ikew.TPCosts.serializer.LocationSerializer
import cc.ikew.TPCosts.serializer.UUIDSerializer
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.bukkit.Location
import java.io.File
import java.util.UUID

@Serializable
public class TPplayer(
	@Serializable(with = UUIDSerializer::class) val uuid: UUID,
	var map: HashMap<String, @Serializable(with = LocationSerializer::class) Location> = HashMap()){

	fun setLoc(name: String, loc: Location){
		map.put(name, loc)
	}

	fun getLoc(name: String): Location?{
		return map[name]
	}

	fun save(){
		val file: File = File("players/${uuid}")
		if(file.exists())file.createNewFile()
		val json = Json.encodeToString(this)
		println(json)
		file.writeText(json)

	}

}
