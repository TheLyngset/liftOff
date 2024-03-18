# weatherDTO documentation

## LocationforecastDTO

>
> Infor contained in this API:
> "air_pressure_at_sea_level": "hPa",
"air_temperature": "celsius",
"air_temperature_max": "celsius",
"air_temperature_min": "celsius",
"air_temperature_percentile_10": "celsius",
"air_temperature_percentile_90": "celsius",
"cloud_area_fraction": "%",
"cloud_area_fraction_high": "%",
"cloud_area_fraction_low": "%",
"cloud_area_fraction_medium": "%",
"dew_point_temperature": "celsius",
"fog_area_fraction": "%",
"precipitation_amount": "mm",
"precipitation_amount_max": "mm",
"precipitation_amount_min": "mm",
"probability_of_precipitation": "%",
"probability_of_thunder": "%",
"relative_humidity": "%",
"ultraviolet_index_clear_sky": "1",
"wind_from_direction": "degrees",
"wind_speed": "m/s",
"wind_speed_of_gust": "m/s",
"wind_speed_percentile_10": "m/s",
"wind_speed_percentile_90": "m/s"
> Not all information is used to create the groundWeatherDataPoint.
>
>
> ### This is the main object which contains all the information from the API-call.
> #### LocationforecastDTO contains:
> - `geometry: Geometry`
> - `type: String`
> - `properties: Properties`
>
> Of these we only really care about **properties** and maybe **geometry** if needed.
>
> **Geometry** contains the given coordinates plus a variable **type: String**
> "geometry": {
"type": "Point",
"coordinates": [
9.58,
60.1,
496
]
> } - the third coordinate is the alltitude (meters above sea level) for the given coordinates.

## properties

> #### Properties contains:
> - `meta: Meta`
> - `timeseries: List<Timesery>`
>
> **Meta** is only used for getting the units used for measuring, and the updated time of our
> information.
>
> For simple displaying of information we only need **timeseries**. Which is a list of Timesery data
> classes

## Timesery

> #### Timesery contains:
> - `data: Data`
> - `time: String`
>
> Time is a string value that contains time for Timesery data in format: `YYYY-MM-DDT00:00:00Z`
>
> Time resolution for a timeseries can vary. The first half of the timeseries could have time values
> with one hour intervals, while the last can have time values with six hour intervals

## Data

> #### Data contains:
> - `instant: Instant`
> - `next_12_hours: Next12Hours`
> - `next_1_hours: Next1Hours,`
> - `next_6_hours: Next6Hours`
>
> Most paramaters (e.g. `air_temperature`) have a value that describes the state at exapt timestamp.
> These are found under instant object under details.
>
> Periods of time are found in next_x_hours these are aggregated values. E.g `preciptation_amount`
> They also contain a `summary` variable which contains a symbol code.

## Details

> ### This it the endpoint for gathering the data.
> All instance variables will be found here.
>
> All period variables will be found in `DetailsXX`
>
> [**All variables available at end of page
**](https://docs.api.met.no/doc/locationforecast/datamodel)