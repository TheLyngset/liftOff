# Arkitekturen vår
#### Vi har etter beste evne implementert [Modern App Architecture](https://developer.android.com/topic/architecture#modern-app-architecture). Definisjonen av dette fra [developer.android.com](https://developer.android.com/topic/architecture) definerer det slik:
This Modern App Architecture encourages using the following techniques, among others:

* A reactive and layered architecture.
* Unidirectional Data Flow (UDF) in all layers of the app.
* A UI layer with state holders to manage the complexity of the UI.
* Coroutines and flows.
* Dependency injection best practices.

Vi har implementert tre distinkte lag som komuniserer med hverandre:

#### UI Layer:
Her inngår alt under app/src/main/java/no/uio/ifi/in2000/team_17/ui. Det er inndelt i de forskjellige skjermene våre, der skjermer som trenger det har en viewModel som utfører logic for å kunn eksponere den dataen som trengs til skjermen gjennom ui-states. 

#### Data Layer: 
Her inngår alt i packagen [data](app/src/main/java/no/uio/ifi/in2000/team_17/data). Vi har forskjellige repositories der tanken bak inndelingen er at hvert repository har ansvar for en "type" data slik at hver view-model får så lite unødvendig data som mulig. Hoved repositoryet [Repository.kt](app/src/main/java/no/uio/ifi/in2000/team_17/data/Repository.kt)


