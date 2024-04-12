package no.uio.ifi.in2000.team_17

import android.content.Context
import no.uio.ifi.in2000.team_17.data.AdvancedSettingsRepository
import no.uio.ifi.in2000.team_17.data.RepositoryImplementation

interface AppModule{
    val repository: RepositoryImplementation
    val advancedSettingsRepository: AdvancedSettingsRepository
}
class AppModuleImpl(
    private val appContext: Context
): AppModule {
    override val repository: RepositoryImplementation by lazy{
        RepositoryImplementation()
    }
    override val advancedSettingsRepository: AdvancedSettingsRepository by lazy {
        AdvancedSettingsRepository(appContext.dataStore)
    }
}