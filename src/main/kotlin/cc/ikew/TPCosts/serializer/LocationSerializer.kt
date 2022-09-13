package cc.ikew.TPCosts.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import org.bukkit.Bukkit
import org.bukkit.Location

object LocationSerializer : KSerializer<Location>{
	override val descriptor = PrimitiveSerialDescriptor("UUID", PrimitiveKind.STRING)

	override fun deserialize(decoder: Decoder): Location {
		val dataArray = decoder.decodeString().split(":")
		return Location(
			Bukkit.getWorld(dataArray[0]),
			dataArray[1].toDouble(),
			dataArray[2].toDouble(),
			dataArray[3].toDouble(),
			dataArray[4].toFloat(),
			dataArray[5].toFloat(),
		)
	}

	override fun serialize(encoder: Encoder, value: Location) {
		encoder.encodeString("${value.world.name}:${value.x}:${value.y}:${value.z}:${value.yaw}:${value.pitch}")
	}
}
