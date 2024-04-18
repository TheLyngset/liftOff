package no.uio.ifi.in2000.team_17.data.settings

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import no.uio.ifi.in2000.team17.Settings
import java.io.InputStream
import java.io.OutputStream
import java.time.LocalDateTime


object SettingsSerializer: Serializer<Settings>{
    override val defaultValue: Settings
        get() = Settings.getDefaultInstance().toBuilder()
            .setMaxHeight(3)
            .setLat(59.96)
            .setLng(10.71)
            .setTime(LocalDateTime.now().toString())
            .build()

    override suspend fun readFrom(input: InputStream): Settings {
        return try {
            Settings.parseFrom(input)
        }catch (e: InvalidProtocolBufferException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Settings, output: OutputStream) = t.writeTo(output)
}
