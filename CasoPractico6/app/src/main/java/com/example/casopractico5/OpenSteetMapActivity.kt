package com.example.casopractico5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import com.example.casopractico5.databinding.ActivityOpenSteetMapBinding
import com.example.casopractico5.entities.AutonomieProvider
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem
import java.util.ArrayList

class OpenSteetMapActivity : AppCompatActivity() {

    private lateinit var map:MapView
    private lateinit var binding : ActivityOpenSteetMapBinding
    private var latitud:Double = 0.0
    private var longitud:Double = 0.0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOpenSteetMapBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var nombreComunidad = intent.getStringExtra("nombre")

        map = binding.mapa
        map.setTileSource(TileSourceFactory.MAPNIK)
        map.setMultiTouchControls(true)
        Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        Configuration.getInstance().setUserAgentValue("com.example.casopractico5"); // Reemplaza "tu_app_name" con el nombre de tu aplicación


        val mapController = map.controller
        mapController.setZoom(9.5)
        val autonomieItems: ArrayList<OverlayItem> = ArrayList<OverlayItem>()
        for (autonomia in AutonomieProvider.autonomies) {

            if(autonomia.nombre==nombreComunidad) {
                latitud=autonomia.latitud
                longitud=autonomia.longitud
            }

            // Crear un OverlayItem para cada elemento en la lista
            val overlayItem = OverlayItem(autonomia.nombre,autonomia.habitantes.toString(), GeoPoint(autonomia.longitud, autonomia.latitud))

            // Agregar el OverlayItem a la lista
            autonomieItems.add(overlayItem)
        }


        val mOverlay: ItemizedOverlayWithFocus<OverlayItem> =
            ItemizedOverlayWithFocus<OverlayItem>(
            autonomieItems,
                object : ItemizedIconOverlay.OnItemGestureListener<OverlayItem> {
                    override fun onItemSingleTapUp(index: Int, item: OverlayItem?): Boolean {
                        return true
                    }

                    override fun onItemLongPress(index: Int, item: OverlayItem?): Boolean {
                        return false
                    }

                },applicationContext
            )
        mOverlay.setFocusItemsOnTap(true)
        map.getOverlays().add(mOverlay)
        mapController.setCenter(GeoPoint(longitud, latitud))

    }

    public override fun onResume() {
        super.onResume()
        map.onResume()
    }


    public override fun onPause(){
        super.onPause()
        map.onPause()
    }




}