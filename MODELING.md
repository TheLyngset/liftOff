# Modellering og systemdesign

## ● (1) De viktigste funksjonelle kravene til applikasjonen bør beskrives – bruk gjerne use case diagram,samt sekvensdiagram og tekstlig beskrivelse av de viktigste use-casene

## ● (2) Modelleringen bør også inneholde klassediagram som reflekterer use-case og sekvensdiagrammene

## ● (3) Andre diagrammer bør også være inkludert for å få frem andre perspektiver, for eksempel aktivitetsdiagram (flytdiagram) eller tilstandsdiagram

## ● (1) De viktigste funksjonelle kravene til applikasjonen

De viktigste funksjonelle kravene til appen vår er følgende:

Brukeren kan se på tre forskjellige måter om det er forsvarlig å skyte opp en rakett.

Brukeren kan endre instillinger som lokasjon og thresholds for hvor data er hentet fra. I tillegg får brukeren vist informasjon for nye lokasjoner og beregninger for nye thresholds.

Brukeren kan velge å følge med på et valgt tidspunkt og lokasjon på hjemskjermen, hvis f.eks.
det er ønskelig å følge med på framtidig informasjon på en lett måte.

### Tekstlig beskrivelse av de viktigste use-casene

Viktige usecaser er å åpne appen og se data på hjemskjermen, endre lokasjon/threshold. I tillegg til å feste ønskelig lokasjon og tidspunkt til hjemskjermen.

Use-case navn: Åpne appen og se data på hjemskjermen

Aktør: Bruker

Prebetingelser: Ingen

Postbetingelser: Brukeren ser om et oppskytningsvindu er godkjent, ikke godkjent eller rundt grensen for å skyte opp raketten.

Hovedflyt:

1. Brukeren åpner appen.
2. Brukeren har internett tilgang og ser skjermen med relevant data.

Alternativ flyt

1.1 Brukeren har ikke internett.

1.2 Brukeren ser splashscreen og trykker på "Retry".

1.3 Brukeren returnerer til steg 1 i hovedflytten.

Use-case navn: Bruker navigerer til tabellscreen og endrer lokasjon

Aktør: Bruker

Prebetingelser: Brukeren er på hjemskjermen i appen og har internet.

Postbetingelser: Brukeren ser om et oppskytningsvindu er godkjent, ikke godkjent eller rundt grensen for å skyte opp raketten på den nye lokasjonen.


Hovedflyt:

1. Brukeren trykker på Data
2. Brukeren ser tabellen med data og scroller til høyre for å se på data
3. Brukeren trykker på settings.
4. Brukeren endrer lokasjon.
5. Brukeren går tilbake til data-skjermen og ser på data for ny lokasjon.

Use-case navn: Brukeren fester tidspunkt til hjemskjermen

Aktør: Bruker

Prebetingelser: Brukeren er på dataskjermen og ser på graf.

Postbetingelser: Brukeren har festet tidspunkt til hjemskjermen.

Hovedflyt:

1. Brukeren ser på graf og blar til høyre fram til de finner tidspunkt man kan skyte opp raketten.
2. Brukeren trykker på grafen og ser hvilken dato og tid det er.
3. Brukeren er spurt om det er ønskelig å festw tidspunkt til hjemskjermen.
4. Brukeren trykker på knappen for å feste tidspunkt.

### Use-cases Diagram

<img src = "./Modelling_pictures/UseCaseDiagram2.png" alt="Bilde som viser use case diagram.">

## Sequence Diagrams

<img src = "./Modelling_pictures/SequenceDiagram_UserOpensApp2.png" alt="Bilde som viser sekvensdiagram for use case bruker åpner app.">
<img src = "./Modelling_pictures/SequenceDiagram_UserTableChangeLocation.png" alt="Bilde som viser sekvensdiagram for use case bruker endrer lokasjon.">
<img src = "./Modelling_pictures/SequenceDiagram_UserGraphPinToHomescreen.png" alt="Bilde som viser sekvensdiagram for use case bruker legger til tidspunkt og lokasjon til hjemmeskjerm.">

## ● (2) Klassediagram

I dette klassediagrammet har vi valgt å fokusere mest på dataflyten. Disse forenklingene er gjort for å få diagrammet mer oversiktlig:

* Skjermene vises som klasser selv om de egentlig er Composable funksjoner
* typen til variablene og returverdiene til metodene vises ikke
* Vi har utelatt de detaljerte forholdene mellom klassene da det varierer lite mellom Jetpack Compose prosjekter

```mermaid
classDiagram 

    SplashScreen ..> SplashScreenViewModel: user input
    InputSheet ..> InputSheetViewModel: user input
    HomeScreen ..> HomeScreenViewModel: user input
    DataScreen ..> DataScreenViewModel: user input
    ThresholdsScreen ..> ThresholdsScreenViewModel: user input

    SplashScreen <.. SplashScreenViewModel: uiState
    InputSheet <.. InputSheetViewModel: uiState
    HomeScreen <.. HomeScreenViewModel: uiState
    DataScreen <.. DataScreenViewModel: uiState
    ThresholdsScreen <.. ThresholdsScreenViewModel: uiState

    SplashScreen --* SplashScreenViewModel
    InputSheet --* InputSheetViewModel
    HomeScreen --* HomeScreenViewModel
    DataScreen --* DataScreenViewModel
    ThresholdsScreen --* ThresholdsScreenViewModel

    SplashScreenViewModel <.. SettingsRepository: settingsFlow
    InputSheetViewModel <.. SettingsRepository: settingsFlow
    HomeScreenViewModel <.. SettingsRepository: settingsFlow
    DataScreenViewModel <.. SettingsRepository: settingsFlow

    SplashScreenViewModel --o SettingsRepository
    InputSheetViewModel --o SettingsRepository
    HomeScreenViewModel --o SettingsRepository
    DataScreenViewModel --o SettingsRepository

    SplashScreenViewModel <.. Repository: hasData
    InputSheetViewModel <.. Repository: failedToUpdate
    HomeScreenViewModel <.. Repository: weatherDataList
    DataScreenViewModel <.. Repository: weatherDataList

    SplashScreenViewModel --o Repository
    InputSheetViewModel --o Repository
    HomeScreenViewModel --o Repository
    DataScreenViewModel --o Repository

    HomeScreenViewModel <.. ThresholdsRepository: thresholdsFlow
    DataScreenViewModel <.. ThresholdsRepository: thresholdsFlow
    ThresholdsScreenViewModel <.. ThresholdsRepository: thresholdsFlow

    HomeScreenViewModel --o ThresholdsRepository
    DataScreenViewModel --o ThresholdsRepository
    ThresholdsScreenViewModel --o ThresholdsRepository

    SettingsRepository <.. SettingsDataStore: settingsFlow
    SettingsRepository --o SettingsDataStore

    Repository --o IsobaricDataSource
    Repository --o LocationForecastDataSource

    ThresholdsRepository --o ThresholdsDataStore

    


    class Repository{
        +weatherDataList
        +hasLocationForecastData
        +hasIsoBaricData
        +failedToUpdate
        +load()
    }
    class SettingsRepository{
        +settingsFlow
        +setMaxHeight()
        +setLat()
        +setLng()
        +setTime()
        +setGraphShowTutorial()
        +setTableShowTutorial()
    }
    class ThresholdsRepository{
        +ThresholdsFlow
        +setGroundWind()
        +setMaxWind()
        +setMaxWindShear()
        +setCloudFraction()
        +setFog()
        +setRain()
        +setHumidity()
        +setDewPoint()
        +setMargin()
        +reset()
        +resetAll()
    }

    class SettingsDataStore{
        +settingsFlow
        +maxHeight
        +lat
        +lng
        +time
        +graphShowTutorial
        +tableShowTutorial
    }

    class ThresholdsDataStore{
        +groundWind
        +maxWind
        +maxWindShear
        +cloudFraction
        +fog
        +rain
        +humidity
        +dewPoint
        +margin
    }

    class LocationForecastDataSource{
        +fetchLocationforecast()
    }

    class IsobaricDataSource{
        +getData()
    }

    class InputSheetViewModel{
        +repository
        +settingsRepository
        +failedToUpdate
        +inputSheetUiState
        +setMaxHeight()
        +setLat()
        +setLng()
        +rollback()
    }
    class HomeScreenViewModel{
        +repository
        +settingsRepository
        +thresholdsRepository
        +homeScreenUiState
    }

    class DataScreenViewModel{
        +repository
        +settingsRepository
        +thresholdsRepository
        +dataScreenUiState
        +setTimeIndex()
        +dontShowTableTutorialAgain()
        +dontShowGraphTutorialAgain()
    }

    

    class SplashScreenViewModel{
        +repository
        +settingsRepository
        +splashScreenUiState
    }

    class ThresholdsScreenViewModel{
        +thresholdsRepository
        +thresholdsUiState
        +setGroundWind()
        +setMaxWind()
        +setMaxWindShear()
        +setCloudFraction()
        +setFog()
        +setRain()
        +setHumidity()
        +setDewPoint()
        +setMargin()
        +reset()
        +resetAll()
    }
    

```

ui-state dataklassene ser slik ut for de forskjellige klassene:

```mermaid
classDiagram
    class InputSheetUiState{
        +maxHeight
        +latLng
    }
        class HomeScreenUiState{
        +weatherPointInTime
        +canLaunch
        +updated
        +thresholds
    }

    class DataScreenUiState{
        +weatherDataLists
        +thresholds
        +selectedTimeIndex
        +showGraphTutorial
        +showTableTutorial
    }

    class SplashScreenUiState{
        +isLoading
        +hasData
        +hasIsobaricData
        +hasLocationForecastData
    }

        class ThresholdsScreenUiState{
        +groundWind
        +maxWind
        +maxWindShear
        +cloudFraction
        +fog
        +rain
        +humidity
        +dewPoint
        +margin
    } 
```

Modellen vår består i hovedsak av disse klassene. Vi har noen flere som brukes til mellomregning for å finne maxWind og maxWindShear i tillegg til enum klassen WeatherParameter som vi bruker til å hente ut en parameter fra modellen:

```mermaid
classDiagram
        class WeatherDataLists{
        +List~String~ time
        +List~String~ date
        +List~WindLayer~ groundWind
        +List~WindLayer~ maxWind
        +List~WindShear~ maxWindShear
        +List~Double~ cloudFraction
        +List~Double~ fog
        +List~Rain~ rain
        +List~Double~ humidity
        +List~Double~ dewPoint
        +iterator()
        +get(index) WeatherPointInTime
    }
    class WeatherPointInTime{
        +String time
        +String date
        +WindLayer groundWind
        +WindLayer maxWind
        +WindShear maxWindShear
        +Double cloudFraction
        +Double fog
        +Rain rain
        +Double humidity
        +Double dewPoint
        +Available available
        +get(weatherParameter) Any
        +iterator()
    }
    class Available{
        +Boolean time
        +Boolean date
        +Boolean groundWind
        +Boolean maxWind
        +Boolean maxWindShear
        +Boolean cloudFraction
        +Boolean fog
        +Boolean rain
        +Boolean humidity
        +Boolean dewPoint
        +get(weatherParameter) Boolean
    }
```

Syntaksen for bruk av Availableklassen er slik:

weatherDataLists.get(index).available.get(weatherParameter)

Iteratoren til WeatherDataList returnerer en liste med Pair < WeatherParameter, List < Any > >

Parametrene rain, groundWind, maxWind og maxWindShear er egne dataklasser da de har flere verdier som vi vil bruke. Et eksempel er WindLayer klassen og Rain klassen:

```mermaid
classDiagram
class Rain{
    +Double min
    +Double median
    +Double max
    +Double probability

    +toString() String probability
}

class WindLayer{
    +Double speed
    +Double direction
    +Double height
    +toString() String speed
}
```

Vi har overskrevet toString metodene til å returnere den verdien vi vil vise i brukergrensesnittet. Dette senker kompleksiteten når vi henter ut verdiene.

## ● (3) Andre diagrammer

### Aktivitetsdiagrammer

<img src = "./Modelling_pictures/AktivitetDiagram_UserOpensApp.png" alt="Bilde som viser aktivitetsdiagram for use case bruker åpner app">
<img src = "./Modelling_pictures/AktivittetDiagram_UserTableChangeLocation.png" alt="Bilde som viser aktivitetsdiagram for use case bruker endrer lokasjon.">
<img src = "./Modelling_pictures/AktivitetDiagram_UserGraphPinToHomescreen.png" alt="Bilde som viser aktivitetsdiagram for use case bruker legger til tidspunkt og lokasjon til hjemmeskjerm.">>
