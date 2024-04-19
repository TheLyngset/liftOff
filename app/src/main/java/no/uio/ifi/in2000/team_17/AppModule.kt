package no.uio.ifi.in2000.team_17

import android.content.Context
import no.uio.ifi.in2000.team_17.data.thresholds.ThresholdsRepository
import no.uio.ifi.in2000.team_17.data.Repository
import no.uio.ifi.in2000.team_17.data.RepositoryImplementation
import no.uio.ifi.in2000.team_17.data.settings.SettingsRepository

interface AppModule{
    val repository: Repository
    val settingsRepository: SettingsRepository
    val thresholdsRepository: ThresholdsRepository
}
class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val repository: RepositoryImplementation by lazy{
        RepositoryImplementation()
    }
    override val thresholdsRepository: ThresholdsRepository by lazy {
        ThresholdsRepository(appContext.thresholdsDataStore)
    }
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(appContext.settingsStore)
    }
}