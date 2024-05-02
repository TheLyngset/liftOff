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
<h3> Use-cases Diagram:</h3>
<img src = "./Modelling_pictures/UseCaseDiagram.png">
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
