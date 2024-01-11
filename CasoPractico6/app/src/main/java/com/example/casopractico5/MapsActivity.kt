package com.example.casopractico5

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.casopractico5.databinding.ActivityMapsBinding
import com.example.casopractico5.entities.Autonomie
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        //Nos traemos el nombre de la comunidad en la que se ha seleccionado
        var seleccionada = intent.getStringExtra("nombre")

        var longitudSeleccionada =0.0
        var latitudSeleccionada=0.0

        // Añadimos la marca en españa
        val spain = LatLng(39.472053137885354, -3.5524011712886914)

        //Sacamos la lista de autonomias
        var autonomias = markupData()

        //Para cada autonomia ponemos una marca en el mapa, con su bandera y sus cosas
        autonomias.forEach { autonomia ->

            //Si el nombre de la comunidad seleccionada coincida con la iterada, sacamos su longitud y latitud
            if(autonomia.nombre == seleccionada){
                longitudSeleccionada=autonomia.latitud
                latitudSeleccionada=autonomia.longitud
            }

            val punto = LatLng(autonomia.longitud, autonomia.latitud)
            mMap.addMarker(MarkerOptions().position(punto).title(autonomia.nombre).icon(BitmapDescriptorFactory.fromResource(autonomia.icono)).title("${autonomia.nombre} - Capital: ${autonomia.capital}").snippet("Habitantes : ${autonomia.habitantes}"))
        }

        CameraUpdateFactory.zoomTo(5f)

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(spain,5f))


        //Hacemos la transicion a la comunidad seleccionada correctamente
        val cameraPosition = CameraPosition(LatLng(latitudSeleccionada,longitudSeleccionada),9f,
            0F, 0F)

        val cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition)

        mMap.animateCamera(cameraUpdate)



    }



    fun markupData() :MutableList<Autonomie> {
        return mutableListOf(
            Autonomie(0,"Andalucía", R.drawable.andalucia,8472407 ,"Sevilla",37.56640275933285 ,-4.7406737719892265, R.drawable.andalucia_icon),
            Autonomie(1,"Aragón",  R.drawable.aragon,1326261,"Zaragoza",41.61162981125681, -0.9738034948937436,R.drawable.aragon_icon),
            Autonomie(2,"Asturias",  R.drawable.asturias,1011792,"Oviedo",43.45998093597627, -5.864665888274809,R.drawable.asturias_icon),
            Autonomie(3,"Baleares", R.drawable.baleares,1173008,"Palma de Mallorca",39.57880491837696, 2.904506700284016,R.drawable.baleares_icon),
            Autonomie(4,"Canarias",  R.drawable.canarias,2172944,"Las Palmas de GC y SC de Tenerife",28.334567287944736, -15.913870062646897,R.drawable.canarias_icon),
            Autonomie(5,"Cantabria",  R.drawable.cantabria,584507,"Santander",43.36511077650701, -3.8398424912727958,R.drawable.cantabria_icon),
            Autonomie(6,"Castilla y León", R.drawable.castillaleon,2383139,"No tiene (Valladolid)",41.82966675375594, -4.841538702082391,R.drawable.castillaleon_icon),
            Autonomie(7,"Castilla La Mancha", R.drawable.castillamancha,2049562,"No tiene (Toledo)",39.42393852713387, -3.4784057150456764,R.drawable.castillamancha_icon),
            Autonomie(8,"Cataluña",  R.drawable.catalunya,7763362,"Barcelona",42.07542633707148, 1.5197485699265891,R.drawable.catalunya_icon),
            Autonomie(9,"Ceuta",  R.drawable.ceuta,83517,"Ceuta",35.90091766842379, -5.309980167928874,R.drawable.ceuta_icon),
            Autonomie(10,"Extremadura",  R.drawable.extremadura,1059501,"Mérida",39.05050233766541, -6.351254430283863,R.drawable.extremadura_icon),
            Autonomie(11,"Galicia",  R.drawable.galicia,2695645,"Santiago de Compostela",42.789055617025404, -7.996440102093343,R.drawable.galicia_icon),
            Autonomie(12,"La Rioja",  R.drawable.larioja,319796,"Logroño",42.568072855089895, -2.470916178908127,R.drawable.larioja_icon),
            Autonomie(13,"Madrid",  R.drawable.madrid,6751251,"Madrid",40.429642598652, -3.76167856716930,R.drawable.madrid_icon),
            Autonomie(14,"Melilla",  R.drawable.melilla,86261,"Melilla",35.34689811596408, -2.957162284523383,R.drawable.melilla_icon),
            Autonomie(15,"Murcia",  R.drawable.murcia,1518486,"Murcia",38.088904824462176, -1.4100155858243844,R.drawable.murcia_icon),
            Autonomie(16,"Navarra", R.drawable.navarra,661537,"Pamplona",42.71764719490406, -1.657559057849277,R.drawable.navarra_icon),
            Autonomie(17,"País Vasco", R.drawable.paisvasco,2213993,"Vitoria",43.11260202399828, -2.594687915428055,R.drawable.paisvasco_icon),
            Autonomie(18,"Valencia",  R.drawable.valencia,5058138, "Valencia",39.515011403926145, -0.6939076854376838,R.drawable.valencia_icon)
        )
    }
}

