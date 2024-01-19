package com.example.casopractico5.reciclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.casopractico5.ComunityDiffUtil
import com.example.casopractico5.entities.Comunity
import com.example.casopractico5.R

/**
 * Esta clase se va a encargar de implementar los 3 metodos que necesitamos para el reciclerview
 * Recibe la lista de objetos que queramos mostrar y implemente esta interfaz recyclerview
 */
class ComunityAdapter(private var comunities:List<Comunity>,
    //Recibe un OnClickListener con una lambda para que cada vez que cliques en una comunidad haga lo que sea
                      private val onClickListener:(Comunity)->Unit): RecyclerView.Adapter<ComunityViewHolder>() {

    /**
     * Con esta funcion y la libreria DiffUtils hacemos que los cambios en la lista del recyclerView se
     * notifiquen automaticamente
     * Para ello hemos tenido que hacer variable y no constante la lista que recibe justo arriba el adapter
     */
    fun updateList(newList:List<Comunity>){
        //Creamos un objeto de la clase ComunityDiff que acabamos de crear
        val comunityDiff=ComunityDiffUtil(comunities,newList)
        //llamamos al metodo calculate de la libreria para calcular las diferencias y las metemos en result
        val result = DiffUtil.calculateDiff(comunityDiff)
        //Asignamos a la lista antigua, la lista nueva
        comunities=newList
        //Con este metodo arregla las irregularidades
        result.dispatchUpdatesTo(this)
        //De este modo ya no tenemos que llamar al notify del adapter en la Main Activity
    }



    /**
     * Este metodo se encarga de inflar el layout, la declaracion siempre es la misma
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ComunityViewHolder {
        val layoutInflater= LayoutInflater.from(parent.context)
        return ComunityViewHolder(layoutInflater.inflate(R.layout.item_comunity,parent,false))
    }


    /**
     * Este metodo llama al metodo render de cada objeto que estemos mostrando
     */
    override fun onBindViewHolder(holder: ComunityViewHolder, position: Int) {
        val item=comunities[position]
        holder.render(item,onClickListener)
    }


    /**
     * Este solo devuelve el tamaño de la lista de comunidades
     */
    override fun getItemCount(): Int {
        return comunities.size
    }

}