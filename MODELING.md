<h1> Modellering og systemdesign </h1>
<h3>● (1) De viktigste funksjonelle kravene til applikasjonen bør beskrives – bruk gjerne use case
diagram,samt sekvensdiagram og tekstlig beskrivelse av de viktigste use-casene.</h3>
<h3>● (2) Modelleringen bør også inneholde klassediagram som reflekterer use-case og sekvensdiagrammene.</h3>
<h3>● (3) Andre diagrammer bør også være inkludert for å få frem andre perspektiver, for eksempel
aktivitetsdiagram (flytdiagram) eller tilstandsdiagram.</h3>
<p>
</p>

<h2> ● (1) De viktigste funksjonelle kravene til applikasjonen </h2>
<p>De viktigste funksjonelle kravene til appen vår er følgende:<br>
Brukeren kan se på tre forskjellige måter om det går an eller ikke å skyte opp en rakett. <br>
Brukeren kan endre instillinger som lokasjon og thresholds for hvor data er hentet fra 
og får vist informasjon for nye lokasjoner og beregninger for nye thresholds. <br>
Brukeren kan velge å følge med på hjemmeskjermen en valg tidspunt og lokasjon hvis f.eks. 
det er ønskelig å følge med framtidig informasjon på en lett måte.
</p>
<h3>Tekstlig beskrivelse av de viktigste use-casene: </h3>
<p>Viktige usecase er å åpne appen og se data på hjemmeskjermen, endre lokasjon/thresholds 
og pinne ønskelig lokasjon og tidspunkt til hjemmeskjermen. </p>
<p>Use-case navn: Åpne appen og se data på hjemmeskjermen: <br>
Aktør: PortalSpace user<br>
Prebetingelser: Ingen.<br>
Potbestingelser: Brukeren ser om det er mulig, ikke mulig eller innimelom å skyte opp 
PortalSpace sin rakett.<br>
Hovedflyt: <br>
1. Brukeren åpner appen.<br>
2. Brukeren har internett tilgang og ser skjermen med relevant data.<br>
Alternativ flyt<br>
1.1 Brukeren har ikke internett.<br>
1.2 Brukeren ser splashscreen og trykker på "Retry".<br>
1.3 Brukeren returnerer til steg 1 i hovedflytten.<br>
</p>

<p>Use-case navn: User navigates to table screen and changes location <br>
Aktør: PortalSpace user<br>
Prebetingelser: Brukeren er på hjemmeskjermen i appen og har internet.<br>
Potbestingelser: Brukeren ser data frem i tid for om det er mulig, ikke mulig eller innimelom å skyte opp 
PortalSpace sin rakett.<br>
Hovedflyt: <br>
1. Brukeren trykker på Data.<br>
2. Brukeren ser tabellen med data og scroller til høyre for å se på data<br>
3. Brukeren trykker på settings.<br>
4. Brukeren endrer lokasjon.<br>
5. Brukeren går tilbake til data-skjermen og ser på data for ny lokasjon.<br>
</p>
<p>Use-case navn: Brukeren pins tidspunkt og lokasjon til hjemmeskjerm <br>
Aktør: PortalSpace user<br>
Prebetingelser: Brukeren er på dataskjermen og ser på graf.<br>
Potbestingelser: Brukeren har pinned tidspunkt og lokasjon til hjemmeskjerm.<br>
Hovedflyt: <br>
1. Brukeren ser på graf og scroller til høyre fram til den finner tidspunt man kan skyte opp raketten.<br>
2. Brukeren trykker på grafen og ser hvilen dato og tid den er. <br>
3. Brukeren er spurt om det er ønskelig å pinne tidspunkt til hjemmeskjermen.<br>
4. Brukeren trykker på knappen for å pinne tidspunkt.<br>
</p>

<h3> Use-cases Diagram:</h3>
<img src = "./Modelling_pictures/UseCaseDiagram.png">

<h3> Sequence Diagrams: </h3>
<img src = "./Modelling_pictures/SequenceDiagram_UserOpensApp2.png">
<img src = "./Modelling_pictures/SequenceDiagram_UserTableChangeLocation.png">
<img src = "./Modelling_pictures/SequenceDiagram_UserGraphPinToHomescreen.png">

<h2> ● (2) Klassediagram </h2>

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

```mermaid
classDiagram
        class WeatherDataList{
        +time
        +date
        +groundWind
        +maxWind
        +maxWindShear
        +cloudFraction
        +fog
        +rain
        +humidity
        +dewPoint
    }
    class WeatherPointInTime{
        +time
        +date
        +groundWind
        +maxWind
        +maxWindShear
        +cloudFraction
        +fog
        +rain
        +humidity
        +dewPoint
    }
```

<h2> ● (3) Andre diagrammer </h2>