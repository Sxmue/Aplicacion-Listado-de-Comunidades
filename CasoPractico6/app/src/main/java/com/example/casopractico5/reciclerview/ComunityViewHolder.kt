package com.example.casopractico5.reciclerview

import android.view.ContextMenu
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.casopractico5.entities.Comunity
import com.example.casopractico5.R
import com.example.casopractico5.databinding.ItemComunityBinding

/**
 * ViewHolder de comunidades, lo que usamos para decir de este item (Comunidad) Coge la imagen y el texto
 */
class ComunityViewHolder(view: View) : ViewHolder(view), View.OnCreateContextMenuListener {

    //De esta manera haces referencia a las vistas de un layout concreto
    val binding = ItemComunityBinding.bind(view)

    //Esta es la manera alternativa
    private val name: TextView = view.findViewById(R.id.txtComunidad)
    private val img: ImageView = view.findViewById(R.id.flagImg)

    //Variable comunidad que vamos a usar para hacer referencia a los campos del objeto comunidad
    private lateinit var comunity: Comunity

    /**
     * Con el metodo render indicamos que objetos son los que se tienen que mostrar
     * le pasamos un Listener para decirle que tiene que hacer algo para cada comunidad o item
     */
    fun render(item: Comunity, onClickListener:(Comunity)->Unit) {

        //Decimos que el item es una comunidad (ES IMPORTANTE SINO NO FUNCIONA EL MENU CONTEXTUAL)
        comunity=item

        //Aqui indicamos el nombre
        name.text = item.name

        //Aqui indicamos la bandera
        img.setImageResource(item.flag)

        //Listener para que cuando clikes salga el toast, como es cuando clickes es la celda, lo lleva el render
        itemView.setOnClickListener {
        onClickListener(item)
        }


        //Le pasamos el menu a la vista para que aparezca
        itemView.setOnCreateContextMenuListener(this)

    }

    /**
     * En este metodo desarollamos el menu contextual (dejar pulsado), como queremos que sea cuando se pulse una comunidad concretamente lo hacemos en el ViewHolder
     */
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        //Le añadimos el titulo al menu
        menu!!.setHeaderTitle(comunity.name)

        //Añadimos dos opciones al menu, dos menu items
        menu.add(this.adapterPosition,0,0,"Eliminar")
        menu.add(this.adapterPosition,1,1,"Editar")
    }


}