package com.example.casopractico5.entities

import android.content.Context
import android.database.Cursor
import com.example.casopractico5.database.ComunityContract
import com.example.casopractico5.database.DBOpenHelper

class ComunityDAO {

    fun cargarLista(context:Context?):MutableList<Comunity>{

       lateinit var result:MutableList<Comunity>
        lateinit var c : Cursor
        try {
                val db = DBOpenHelper.getInstance(context)!!.readableDatabase

                val sql = "SELECT * FROM comunities WHERE state='activo'"

                c = db.rawQuery(sql, null)

                result = mutableListOf()

                while (c.moveToNext()) {
                    val nueva = Comunity(c.getInt(1), c.getString(2))
                    result.add(nueva)

                }
            }finally {

                c?.close()

            }

            return result

    }

    fun cargarBackup(context:Context?):MutableList<Comunity>{

        lateinit var result:MutableList<Comunity>
        lateinit var c : Cursor
        try {
            val db = DBOpenHelper.getInstance(context)!!.readableDatabase

            val sql = "SELECT * FROM backupComunities"

            c = db.rawQuery(sql, null)

            result = mutableListOf()

            while (c.moveToNext()) {
                val nueva = Comunity(c.getInt(1), c.getString(2))
                result.add(nueva)

            }
        }finally {

            c?.close()

        }

        return result

    }


    fun actualizarComunidad(context: Context?, c: Comunity,index:Int) {
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        db.execSQL("UPDATE comunities " +
                "SET name='${c.name}', flag=${c.flag} " +
                "WHERE id=$index;")
    }


    fun deleteComunidad(context: Context?,name:String){
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        db.execSQL("UPDATE comunities " +
                "SET state='eliminado' " +
                "WHERE name='$name';")

    }


    /**
     * Metodo que a la hora de restaurar por defecto las tablas, las empareja
     */
    fun matchTables(context: Context?) {
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        db.execSQL("DELETE FROM comunities;")

        lateinit var c: Cursor

        var r = cargarBackup(context)

        for (i in r.indices) {
            addComunity(context, r[i])

        }
    }


    /**
     * Metodo que añade una comunidad a la base de datos
     */
        fun addComunity(context: Context?,com:Comunity){
            val db = DBOpenHelper.getInstance(context)!!.writableDatabase
            db.execSQL("INSERT INTO comunities ( " +
                    "${ComunityContract.Companion.Entrada.COLUMNA_FLAG}, " +
                    "${ComunityContract.Companion.Entrada.COLUMNA_NAME} ) VALUES ( ${com.flag} , '${com.name}' );")

        }

    /**
     * Metodo que añade un usuario a la base de datos
     */
    fun addUser(context: Context?,email:String,password:String){
        val db = DBOpenHelper.getInstance(context)!!.writableDatabase
        db.execSQL("INSERT INTO usuarios ( " +
                "${ComunityContract.Companion.Usuarios.COLUMNA_EMAIL}, " +
                "${ComunityContract.Companion.Usuarios.COLUMNA_PASSWORD} ) VALUES ( '$email', '$password' );")
    }

    /**
     * Metodo para obtener un Usuario de la base de datos
     */
    fun getUser(context: Context?,userEmail:String): MutableList<String> {
        lateinit var result:MutableList<String>
        lateinit var c : Cursor
        try {
            val db = DBOpenHelper.getInstance(context)!!.readableDatabase

            val sql = "SELECT * FROM usuarios WHERE email='$userEmail'"

            c = db.rawQuery(sql, null)


            result = mutableListOf()

            while (c.moveToNext()) {
                result.add(c.getString(1))
                result.add(c.getString(2))
            }
        }finally {

                c?.close()

        }

        return result
    }

    /**
     * Metodo para pasarle la tabla vacia a la base de datos
     */
    fun loadEmpty(context: Context?):MutableList<Comunity>{
        var result= mutableListOf<Comunity>()

        return result
    }


    }








