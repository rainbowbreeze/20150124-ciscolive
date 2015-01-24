package it.rainbowbreeze.ciscolive.common;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.ciscolive.data.AppPrefsManager;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = {
                MyApp.class,

                AppPrefsManager.class,
        },
        // True because it declares @Provides not used inside the class, but outside.
        // Once the code is finished, it should be possible to set to false and have
        //  all the consuming classes in the injects statement
        library = true,
        // Forces validates modules and injections at compile time.
        // If true, includes also additional modules that will complete the dependency graph
        //  using the includes statement for the class included in the injects statement
        complete = true
)
public class MobileModule {
    private final Context mAppContent;

    public MobileModule(Context appContent) {
        mAppContent = appContent;
    }

    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }

    @Provides @Singleton public AppPrefsManager proviceAppPrefsManager(ILogFacility logFacility) {
        return new AppPrefsManager(mAppContent, logFacility);
    }
}
