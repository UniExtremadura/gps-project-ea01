package es.unex.giiis.asee.snapmap_ea01

import android.app.Application
import es.unex.giiis.asee.snapmap_ea01.utils.AppContainer

class SnapMapApplication: Application() {
    lateinit var appContainer: AppContainer
    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}