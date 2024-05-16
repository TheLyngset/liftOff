# Architecture

## Arkitekturen vår

 Vi har etter beste evne implementert [Modern App Architecture](https://developer.android.com/topic/architecture#modern-app-architecture). Definisjonen av dette fra [developer.android.com](https://developer.android.com/topic/architecture) definerer det slik:

"This Modern App Architecture encourages using the following techniques, among others:

* A reactive and layered architecture.
* Unidirectional Data Flow (UDF) in all layers of the app.
* A UI layer with state holders to manage the complexity of the UI.
* Coroutines and flows.
* Dependency injection best practices. " (Guide to App Architecture, 2023)

Vi har implementert tre distinkte lag som kommuniserer med hverandre:

### UI Layer

Her inngår alt i packagen [ui](app/src/main/java/no/uio/ifi/in2000/team_17/ui). Applikasjonen er inndelt i de tre forskjellige skjermer, der skjermene som trenger det, har én viewModel hver. ViewModel-ene holder på ui-stater og utfører logikk for å kunne eksponere den dataen som trengs.

### Data Layer

Her inngår alt i packagen [data](app/src/main/java/no/uio/ifi/in2000/team_17/data). Vi har forskjellige repositories der tanken bak inndelingen er at hvert repository har ansvar for en "type" data slik at hver view-model får så lite unødvendig data som mulig. Hoved repositoryet [Repository.kt](app/src/main/java/no/uio/ifi/in2000/team_17/data/Repository.kt) henter data fra de to data sourcene våre og eksponerer dataen ut til viewModel-ene. Den har også en funksjon load som laster data fra en gitt posisjon. De to andre repositoryene bruker proto DataStore til å lagre instillinger lokalt på telefonen og eksponerer de lagrede instillingene ut som en flow. Når vi henter data bruker vi de to data-source-klassene [IsoBaricDataSource](app/src/main/java/no/uio/ifi/in2000/team_17/data/isobaricgrib/IsobaricDataSource.kt) og [LocationForecastDataSource](app/src/main/java/no/uio/ifi/in2000/team_17/data/locationforecast/LocationForecastDataSource.kt)

### Domain Layer

Vi har også et lite domain layer som består av [use cases](app/src/main/java/no/uio/ifi/in2000/team_17/usecases). Her har vi klasser med metoder som utfører mer avansert logikk for å få et ekstra abstraksjonsnivå.

## Viktige objektorienterte prinsipper

Vi opprettholder viktige objektorienterte prinsipper ved å bruke MVVM, UDF og lav kobling og høy kohesjon.

### MVVM

Her kommer hvordan vi har implementert de ulike delene [Model](#model), [View](#view) og [View Model](#view-model)

#### Model

Klassen WeatherDataLists er vår hovedmodell som eksponeres ut fra Repository og inn til view model-ene som skal vise værdata. Dette er en dataklasse som er skreddersydd til å vise data i en tabell og graf enklest mulig. Vi har en egen dataklasse WeatherPointInTime som kan hentes ut fra WeatherDataList. Dette er all data vi har for et gitt tidspunkt som blir brukt på hjemskjermen.
  
#### View

Hver skjerm som ikke bare viser statisk innhold tar inn en viewModel og collecter en ui-state fra denne. Om noe skal endres har viewModel-en en metode for dette som er det eneste vi forholder oss til i skjermene. Dette gjør at vi ikke trenger å tenke på states i skjermene og rekomponering vil skje automatisk når ui-staten blir oppdatert.

#### View Model
  
Vi har flere view models som henter relevant data til sin tilhørende skjerm og eksponerer den ut via en ui-state. Om noe skal endres på, som for eksempel posisjonen, har viewModel-en en metode for dette som kaller på riktig metode i riktig repository.

#### UDF

UDF blir opprettholdt via vår implementering av MVVM. Alle skjermene har en flyt med data inn og en eller flere "tuneller" tilbake til viewModel-en sin om noe skal endres. Hver viewModel får data fra nødvendige repositories og bruker combine hvis den må høre på flere flows. Dette gjør at Skjermene blir oppdatert uansett hvor en instilling eller lignende blir oppdatert fra.

#### Lav kobling og høy kohesjon

Vi har gjort flere ting for å opprettholde lav kobling og høy kohesjon. Ved å ha en view-model for hver enkelt skjerm har hver view-model ganske lite data å holde styr på. Videre er dataen i repositoriene representert som state-flows. Hvis en view-model endrer på noe i et repository vil alle view-modelsene som hører på den state-flowen få beskjed om oppdateringen, uten at de trenger å kobles sammen seg i mellom. Vi valgte å dele opp repositoriene våre i tre for å få høy kohesjon, selv om dette fører til høyere kobling. Hadde vi hatt all dataen i samme repository ville det vært unaturlig å kombinere all dataen i én eksponert klasse. Derfor veier det tyngre å ha oversiktlige repositories enn å lage ett stort. Her er en arkitekturskisse som viser hvordan de ulike klassene er koblet sammen. Merk at statiske skjermer ikke er tatt med. 

<img src="./architecture_pictures/AppArkitektur.png" width="800" alt="App arkitektur">

### Videre Drift

Vi har valgt å bruke API-nivå 26 da biblioteket vi bruker til plotting av grafer krever dette. For målgruppen vår er nok ikke dette et problem, men om vi ville nådd et enda bredere publikum kunne vi lagd en versjon uten grafen. Her er en oversikt over teknologiene vi bruker:

#### [proto DataStore](https://developer.android.com/topic/libraries/architecture/datastore)

Vi valgte å implementere det nye lagrings biblioteket til Android Jetpack, som lar oss lagre typesikre verdier. Det krever at vi lager en protobuffer [data.proto](app/src/main/proto/data.proto) hvor vi har to "messages" som blir til genererte java objekter. Dette bruker vi gjennom de to repositoriene. Man trenger også en "serializer" som setter standard verdier og brukes av biblioteket. Legges det til en ny parameter, gjøres dette først i [data.proto](app/src/main/proto/data.proto), deretter kan man oppdatere den tilhørende "Serializeren", før man til slutt lager en oppdateringsmetode i det tilhørende repositoriet. Parameteren vil da eksponeres gjennom flowen som repositoriet eksponerer. Det følger en god del "boiler plate" kode med denne løsningen, men at den er typesikker og enkel å utvide gjorde at vi valgte denne over andre løsninger.

#### [ktor](https://ktor.io) og [gson](https://github.com/google/gson)

Til serialisering av data bruker vi Ktor og Gson. Hvert api har sin egen datakilde som blir opprettet i [MainActivity](app/src/main/java/no/uio/ifi/in2000/team_17/MainActivity.kt). Modellene er autogenerert. Vi valgte å lage modellene ikke-nullbare ved å sette standardverdier for alle parametre.

##### Dependency injection

Vi gjør manuell dependency injection ved å opprette repositoriene våre i en egendefinert applikasjonsklasse. Den har vi kalt [App](app/src/main/java/no/uio/ifi/in2000/team_17/App.kt) og inneholder en [AppModule](app/src/main/java/no/uio/ifi/in2000/team_17/AppModule.kt), som oppretter alle repositoriene. [ViewModelFactoryHelper](app/src/main/java/no/uio/ifi/in2000/team_17/ViewModelFactoryHelper.kt) er en hjelpeklasse som vi bruker til å opprette view-models. Vi trenger kun appmodulen, men ved å ha begge deler kan viewModel-ene enkelt utvides om man ønsker å ta inn andre parametre.

Referanser:
Guide to app architecture. (2023, December 12). Android Developers. Hentet 06.05.24 fra      https://developer.android.com/topic/architecture
‌
