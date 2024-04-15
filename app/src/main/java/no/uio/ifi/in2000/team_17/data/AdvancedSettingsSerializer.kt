package no.uio.ifi.in2000.team_17.data

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import no.uio.ifi.in2000.team17.AdvancedSettings
import java.io.InputStream
import java.io.OutputStream

object AdvancedSettingsSerializer : Serializer<AdvancedSettings>{
    override val defaultValue: AdvancedSettings
        get() = AdvancedSettings.getDefaultInstance().toBuilder()
            .setGroundWindSpeed(8.6)
            .setMaxWindSpeed(17.2)
            .setMaxWindShear(24.5)
            .setCloudFraction(15.0)
            .setFog(0.0)
            .setRain(0.0)
            .setHumidity(75.0)
            .setDewPoint(15.0)
            .setMargin(0.6)
            .build()
    override suspend fun readFrom(input: InputStream): AdvancedSettings {
        return try {
            AdvancedSettings.parseFrom(input)
        }catch (e: InvalidProtocolBufferException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: AdvancedSettings, output: OutputStream) = t.writeTo(output)
}
