package no.uio.ifi.in2000.team_17.data.isobaricgrib.model
import com.google.gson.annotations.SerializedName


data class IsoBaricModel(
    @SerializedName("domain")
    val domain: Domain = Domain(),
    @SerializedName("id")
    val id: String = "", // isobaric
    @SerializedName("parameters")
    val parameters: Parameters = Parameters(),
    @SerializedName("ranges")
    val ranges: Ranges = Ranges(),
    @SerializedName("type")
    val type: String = "" // Coverage
) {
    data class Domain(
        @SerializedName("axes")
        val axes: Axes = Axes(),
        @SerializedName("domainType")
        val domainType: String = "", // VerticalProfile
        @SerializedName("referencing")
        val referencing: List<Referencing> = listOf(),
        @SerializedName("type")
        val type: String = "" // Domain
    ) {
        data class Axes(
            @SerializedName("t")
            val t: T = T(),
            @SerializedName("x")
            val x: X = X(),
            @SerializedName("y")
            val y: Y = Y(),
            @SerializedName("z")
            val z: Z = Z()
        ) {
            data class T(
                @SerializedName("values")
                val values: List<String> = listOf()
            )

            data class X(
                @SerializedName("values")
                val values: List<Double> = listOf()
            )

            data class Y(
                @SerializedName("values")
                val values: List<Double> = listOf()
            )

            data class Z(
                @SerializedName("values")
                val values: List<Double> = listOf()
            )
        }

        data class Referencing(
            @SerializedName("coordinates")
            val coordinates: List<String> = listOf(),
            @SerializedName("system")
            val system: System = System()
        ) {
            data class System(
                @SerializedName("calendar")
                val calendar: String = "", // Gregorian
                @SerializedName("cs")
                val cs: Cs = Cs(),
                @SerializedName("id")
                val id: String = "", // http://www.opengis.net/def/crs/OGC/1.3/CRS84
                @SerializedName("type")
                val type: String = "" // GeographicCRS
            ) {
                data class Cs(
                    @SerializedName("csAxes")
                    val csAxes: List<CsAxe> = listOf()
                ) {
                    data class CsAxe(
                        @SerializedName("direction")
                        val direction: String = "", // down
                        @SerializedName("name")
                        val name: Name = Name(),
                        @SerializedName("unit")
                        val unit: Unit = Unit()
                    ) {
                        data class Name(
                            @SerializedName("en")
                            val en: String = "" // Pressure
                        )

                        data class Unit(
                            @SerializedName("symbol")
                            val symbol: String = "" // Pa
                        )
                    }
                }
            }
        }
    }

    data class Parameters(
        @SerializedName("temperature")
        val temperature: Temperature = Temperature(),
        @SerializedName("wind_from_direction")
        val windFromDirection: WindFromDirection = WindFromDirection(),
        @SerializedName("wind_speed")
        val windSpeed: WindSpeed = WindSpeed()
    ) {
        data class Temperature(
            @SerializedName("id")
            val id: String = "", // temperature
            @SerializedName("label")
            val label: Label = Label(),
            @SerializedName("observedProperty")
            val observedProperty: ObservedProperty = ObservedProperty(),
            @SerializedName("type")
            val type: String = "", // Parameter
            @SerializedName("unit")
            val unit: Unit = Unit()
        ) {
            data class Label(
                @SerializedName("en")
                val en: String = "" // Air temperature
            )

            data class ObservedProperty(
                @SerializedName("id")
                val id: String = "", // http://vocab.met.no/CFSTDN/en/page/air_temperature
                @SerializedName("label")
                val label: Label = Label()
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // Air temperature
                )
            }

            data class Unit(
                @SerializedName("id")
                val id: String = "", // https://codes.wmo.int/common/unit/_Cel
                @SerializedName("label")
                val label: Label = Label(),
                @SerializedName("symbol")
                val symbol: String = "" // ËšC
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // degree Celsius
                )
            }
        }

        data class WindFromDirection(
            @SerializedName("id")
            val id: String = "", // wind_from_direction
            @SerializedName("label")
            val label: Label = Label(),
            @SerializedName("observedProperty")
            val observedProperty: ObservedProperty = ObservedProperty(),
            @SerializedName("type")
            val type: String = "", // Parameter
            @SerializedName("unit")
            val unit: Unit = Unit()
        ) {
            data class Label(
                @SerializedName("en")
                val en: String = "" // Wind from direction
            )

            data class ObservedProperty(
                @SerializedName("id")
                val id: String = "", // http://vocab.met.no/CFSTDN/en/page/wind_from_direction
                @SerializedName("label")
                val label: Label = Label()
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // wind_from_direction
                )
            }

            data class Unit(
                @SerializedName("id")
                val id: String = "", // https://codes.wmo.int/common/unit/_degree_(angle)
                @SerializedName("label")
                val label: Label = Label(),
                @SerializedName("symbol")
                val symbol: String = "" // Ëš
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // degree
                )
            }
        }

        data class WindSpeed(
            @SerializedName("id")
            val id: String = "", // wind_speed
            @SerializedName("label")
            val label: Label = Label(),
            @SerializedName("observedProperty")
            val observedProperty: ObservedProperty = ObservedProperty(),
            @SerializedName("type")
            val type: String = "", // Parameter
            @SerializedName("unit")
            val unit: Unit = Unit()
        ) {
            data class Label(
                @SerializedName("en")
                val en: String = "" // Wind speed
            )

            data class ObservedProperty(
                @SerializedName("id")
                val id: String = "", // http://vocab.met.no/CFSTDN/en/page/wind_speed
                @SerializedName("label")
                val label: Label = Label()
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // Wind speed
                )
            }

            data class Unit(
                @SerializedName("id")
                val id: String = "", // https://codes.wmo.int/common/unit/_m_s-1
                @SerializedName("label")
                val label: Label = Label(),
                @SerializedName("symbol")
                val symbol: String = "" // m/s
            ) {
                data class Label(
                    @SerializedName("en")
                    val en: String = "" // metres per second
                )
            }
        }
    }

    data class Ranges(
        @SerializedName("temperature")
        val temperature: Temperature = Temperature(),
        @SerializedName("wind_from_direction")
        val windFromDirection: WindFromDirection = WindFromDirection(),
        @SerializedName("wind_speed")
        val windSpeed: WindSpeed = WindSpeed()
    ) {
        data class Temperature(
            @SerializedName("axisNames")
            val axisNames: List<String> = listOf(),
            @SerializedName("dataType")
            val dataType: String = "", // float
            @SerializedName("shape")
            val shape: List<Int> = listOf(),
            @SerializedName("type")
            val type: String = "", // NdArray
            @SerializedName("values")
            val values: List<Double> = listOf()
        )

        data class WindFromDirection(
            @SerializedName("axisNames")
            val axisNames: List<String> = listOf(),
            @SerializedName("dataType")
            val dataType: String = "", // float
            @SerializedName("shape")
            val shape: List<Int> = listOf(),
            @SerializedName("type")
            val type: String = "", // NdArray
            @SerializedName("values")
            val values: List<Double> = listOf()
        )

        data class WindSpeed(
            @SerializedName("axisNames")
            val axisNames: List<String> = listOf(),
            @SerializedName("dataType")
            val dataType: String = "", // float
            @SerializedName("shape")
            val shape: List<Int> = listOf(),
            @SerializedName("type")
            val type: String = "", // NdArray
            @SerializedName("values")
            val values: List<Double> = listOf()
        )
    }
}