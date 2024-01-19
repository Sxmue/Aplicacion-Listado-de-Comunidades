package com.example.casopractico5

import androidx.recyclerview.widget.DiffUtil
import com.example.casopractico5.entities.Comunity


/**
 * Clase para detectar los cambios en la lista del recicler automaticamente
 */
class ComunityDiffUtil(private val oldlist: List<Comunity>,
                       private val newlist: List<Comunity>
): DiffUtil.Callback() {
    //Heredar de DiffUtil.callback() hace que tengas que implementar estos 4 metodos facilitos
    override fun getOldListSize(): Int {
        return oldlist.size
    }

    override fun getNewListSize(): Int {
        return newlist.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldlist[oldItemPosition].name.equals(newlist[newItemPosition].name)
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldlist[oldItemPosition] == newlist[newItemPosition]
    }


}
