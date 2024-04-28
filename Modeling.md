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