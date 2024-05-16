![LiftOff Logo](Readme_pictures/logo.png)

## Table of Contents
- [Background](#background)
- [Screenshots](#screenshots)
- [Documentation](#documentation)
- [Install](#install)
- [Usage](#usage)
- [API](#api)
- [Contributing](#contributing)

## About
LiftOff is an application that provides a weather forecast for both ground and atmospheric pressure points. The app can store and use thresholds set by the user to calculate "safe" rocket launch time-periods. The application is developed by a team of 6 people.
The team consists of students from the University of Oslo taking the course IN2000 - Software Engineering.

## Background
We were approached with multiple cases, and we chose to work on the case "Case 1. Rocket Launch". 
We have worked closely with the student organization [Portal Space](https://www.portalspace.no/), catering features, design, and functionality to their needs.
## Screenshots
![Home Screen](Readme_pictures/HomeScreen_shot.png)
![Table Screen](Readme_pictures/Table_shot.png)
## Documentation
Information on the applications architecture and further maintaining, will be found here. [ARCHITECTURE.md](ARCHITECTURE.md)

Graphs and system design will be found here. [MODELING.md](MODELING.md)

## Install
#### Requirements
- Emulator or physical device running Android 8.0 (API level 26) or higher
- Either a clone of this repository or the APK file
- Api-key for Locationforecast

#### Apk file
To install on a physical device, we recommend using Android Debug Bridge [(adb)](https://developer.android.com/tools/adb)
```sh
$ adb install ~/file/path/release.apk
```

#### Project Files
We recommend android studio for emulating the application through the project folders.
Clone the repository, and run trough android studio.
```sh
$ git clone https://github.uio.no/IN2000-V24/team-17.git
```
You will need permission to clone all the files.

Locationforecast API is also reliant on an API-key, which is not included.
## Usage
Run the application on an emulator or physical device. The application will guide you on usage. There are multiple features to explore, such as setting thresholds, viewing weather data, and viewing the forecast.

## API
We have used the following API's in our application:
- [Locationforecast](https://api.met.no/weatherapi/locationforecast/2.0/documentation)
  - This API is used to get the weather forecast data for the rocket launch.
- [IsobaricGRIB](https://api.met.no/weatherapi/isobaricgrib/1.0/documentation)
  - This API is used to get the atmospheric pressure data for the rocket launch.
  - Fetching, parsing and formatting is done by a secondary api we are hosting at NREC. The original api is created by MET. [EDR-Isobaric](https://github.com/metno/edrisobaric)
  - We have modified EDR-Isobaric to better suite our problems and to make it easier to use in our application. Although this is never ran through the application we have decided to add the folder to our project folders for documentation. This is found in the [isobaricgrib](app/src/main/java/no/uio/ifi/in2000/team_17/data/isobaricgrib) folder.

## Libraries
These are the libraries we have used in our application:
- [Ktor](https://ktor.io/docs/welcome.html)
  - Used for networking
- [Gson](https://github.com/google/gson)
  - Used for parsing JSON
- [Ycharts](https://github.com/codeandtheory/YCharts)
  - Used for creating graphs
- [LottieFiles](https://developers.lottiefiles.com/docs/)
  - Used for animations
- [ProtoDataStore](https://developer.android.com/topic/libraries/architecture/datastore)
  - Used for storing data locally

## Security
Since everything is stored locally, and the application does not ask for any personal information, 
information theft should not be an issue.
However the locally hosted API is not secure, and should not be used for any sensitive information. 
It has no security against DDos attacks, and is not encrypted. Beyond that, the application should be safe to use.
## Contributing
- Amandus Guldahl - [amandug](https://github.uio.no/amandug)
- Hedda Johnsen - [heddjo](https://github.uio.no/heddjo)
- Lelia-Marcela Marcau - [leliamm](https://github.uio.no/leliamm)
- Malin Kristine Bjørnstadjordet - [malinkbj](https://github.uio.no/malinkbj)
- Sander Norheim - [sandern](https://github.uio.no/sandern)
- Samuel Valland Lyngset - [samuelvl](https://github.uio.no/samuelvl)
