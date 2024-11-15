package com.altaf.plantanist

import android.app.Application
import com.altaf.plantanist.data.PlantDatabase

class PlantApplication : Application() {
    val database: PlantDatabase by lazy { PlantDatabase.getDatabase(this) }
}
