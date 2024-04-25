package no.uio.ifi.in2000.team_17.data.thresholds

import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import no.uio.ifi.in2000.team17.Thresholds
import java.io.InputStream
import java.io.OutputStream

/**
 * A serializer for the generated java class Thresholds from proto DataStore
 */
object ThresholdsSerializer : Serializer<Thresholds> {
    override val defaultValue: Thresholds
        get() = Thresholds.getDefaultInstance().toBuilder()
            .setGroundWindSpeed(8.6)
            .setMaxWindSpeed(17.2)
            .setMaxWindShear(24.5)
            .setCloudFraction(15.0)
            .setFog(0.001)
            .setRain(0.001)
            .setHumidity(75.0)
            .setDewPoint(15.0)
            .setMargin(0.6)
            .build()

    override suspend fun readFrom(input: InputStream): Thresholds {
        return try {
            Thresholds.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: Thresholds, output: OutputStream) = t.writeTo(output)
}