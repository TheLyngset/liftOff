package no.uio.ifi.in2000.team_17

import android.content.Context
import no.uio.ifi.in2000.team_17.data.AdvancedSettingsRepository
import no.uio.ifi.in2000.team_17.data.RepositoryImplementation
import no.uio.ifi.in2000.team_17.data.SettingsRepository

interface AppModule{
    val repository: RepositoryImplementation
    val settingsRepository: SettingsRepository
    val advancedSettingsRepository: AdvancedSettingsRepository
}
class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val repository: RepositoryImplementation by lazy{
        RepositoryImplementation()
    }
    override val advancedSettingsRepository: AdvancedSettingsRepository by lazy {
        AdvancedSettingsRepository(appContext.advancedSettingsStore)
    }
    override val settingsRepository: SettingsRepository by lazy {
        SettingsRepository(appContext.settingsStore)
    }
}