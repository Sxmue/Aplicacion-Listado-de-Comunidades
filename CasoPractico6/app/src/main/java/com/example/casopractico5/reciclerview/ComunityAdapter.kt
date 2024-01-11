package com.example.casopractico5.reciclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.casopractico5.entities.Comunity
import com.example.casopractico5.R

/**
 * Esta clase se va a encargar de implementar los 3 metodos que necesitamos para el reciclerview
 * Recibe la lista de objetos que queramos mostrar y implemente esta interfaz recyclerview
 */
class ComunityAdapter(private val comunities:List<Comunity>,
    //Recibe un OnClickListener con una lambda para que cada vez que cliques en una comunidad haga lo que sea
                      private val onClickListener:(Comunity)->Unit): RecyclerView.Adapter<ComunityViewHolder>() {


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