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
og får vise informasjon for nye lokasjoner og beregninger for nye thresholds. <br>
Brukeren kan velge å følge med på hjemmeskjermen en valg tidspunt og lokasjon hvis f.eks. 
det er ønskelig å følge med framtidig informasjon på en lett måte.
</p>
<h3>Tekstlig beskrivelse av de viktigste use-casene: </h3>![img.png](img.png)
<p>Appen vår </p>

<h3> Use-case Diagram:</h3>
<img src = "./Modelling_pictures/UseCaseDiagram.png">
<h3> Sequence Diagrams: </h3>
<img src = "./Modelling_pictures/SequenceDiagram_UserOpensApp.png">
<img src = "./Modelling_pictures/SequenceDiagram_UserTableChangeLocation.png">
<img src = "./Modelling_pictures/SequenceDiagram_UserGraphPinToHomescreen.png">

<h2> ● (2) Klassediagram </h2>

```mermaid
classDiagram
    Repository --|> HomeScreenViewModel
    Repository --|> DataScreenViewModel

    SettingsRepository --|> Settings

    HomeScreenViewModel --|> HomeScreenUiState

    DataScreenViewModel --|> DataScreenUiState




    class Repository{
        +WeatherDataLists weatherDataLists
        +Boolean hasLocationForecastData
        +Boolean hasIsobaricData
        +load()
    }
    class SettingsRepository{
        +Settings settings
    }
    class Settings{
        +Int maxHeight
        +Double lat
        +Double lng
        +String time
    }


    class HomeScreenViewModel{
        +HomeScreenUiState uiState
    }
    class HomeScreenUiState{
        +WeatherPointInTime weatherPointinTime
        +TrafficLightColor trafficLightColor
        +String updated
        +Thresholds thresholds
    }

    class DataScreenViewModel{
        +uiState
        +setTimeIndex()
        +dontShowDialogAgain()
    }
    class DataScreenUiState{
        +WeatherDataLists weatherDataLists
        +Thresholds thresholds
        +Int selectedTimeIndex
        +Boolean hasDissmissedDialouge
    }
    
    class InputSheetViewModel{
        +uiState
        +setLat()
        +setLng()
        +setMaxHeight()
    }
    class SplashScreenViewModel{
        +uiState
    }
    class ThresholdsScreenViewModel{
        +uiState
    }
```

<h2> ● (3) Andre diagrammer </h2>
