# weatherDTO documentation

## LocationforecastDTO

> ### This is the main object which contains all the information from the API-call.
> #### LocationforecastDTO contains:
> - `geometry: Geometry`
> - `type: String`
> - `properties: Properties`
>
> Of these we only really care about **properties** and maybe **geometry** if needed.
>
> **Geometry** contains the given coordinates plus a variable **type: String**

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