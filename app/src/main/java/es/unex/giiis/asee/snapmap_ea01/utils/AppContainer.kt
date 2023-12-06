package es.unex.giiis.asee.snapmap_ea01.utils

import android.content.Context
import es.unex.giiis.asee.snapmap_ea01.api.getNetworkService
import es.unex.giiis.asee.snapmap_ea01.data.Repository
import es.unex.giiis.asee.snapmap_ea01.database.SnapMapDatabase

class AppContainer (context: Context?) {
    private val networkService = getNetworkService()
    private val db = SnapMapDatabase.getInstance(context!!)
    val repository = Repository(db!!.userDao(),db.photoURIDao(),getNetworkService())
}