package it.rainbowbreeze.ciscolive.common;

import android.content.Context;

import com.squareup.otto.Bus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import it.rainbowbreeze.ciscolive.data.AppPrefsManager;
import it.rainbowbreeze.ciscolive.logic.CmxManager;
import it.rainbowbreeze.ciscolive.logic.action.ActionsManager;
import it.rainbowbreeze.ciscolive.logic.bus.MainThreadBus;
import it.rainbowbreeze.ciscolive.ui.MainActivity;
import it.rainbowbreeze.ciscolive.ui.LocationFragment;

/**
 * Dagger modules for classes that don't need an Application context
 * Created by alfredomorresi on 31/10/14.
 */
@Module (
        injects = {
                MyApp.class,

                AppPrefsManager.class,

                MainActivity.class,
                LocationFragment.class,
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
    private final Context mAppContext;

    public MobileModule(Context appContext) {
        mAppContext = appContext;
    }

    /**
     * Allow the application context to be injected but require that it be annotated with
     * {@link ForApplication @Annotation} to explicitly differentiate it from an activity context.
     */
    @Provides @Singleton @ForApplication public Context provideApplicationContext () {
        return mAppContext;
    }

    @Provides @Singleton public ILogFacility provideLogFacility () {
        return new LogFacility();
    }

    @Provides @Singleton
    public Bus provideBus() {
        return new MainThreadBus();
    }

    @Provides @Singleton public AppPrefsManager provideAppPrefsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility) {
        return new AppPrefsManager(appContext, logFacility);
    }

    @Provides @Singleton public CmxManager provideCmxManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            AppPrefsManager appPrefsManager,
            Bus bus) {
        return new CmxManager(appContext, logFacility, appPrefsManager, bus);
    }

    @Provides @Singleton public ActionsManager provideActionsManager(
            @ForApplication Context appContext,
            ILogFacility logFacility,
            CmxManager cmxManager,
            Bus bus) {
        return new ActionsManager(appContext, logFacility, cmxManager, bus);
    }
}
