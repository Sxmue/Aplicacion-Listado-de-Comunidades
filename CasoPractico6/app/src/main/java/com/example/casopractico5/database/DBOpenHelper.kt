package com.example.casopractico5.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.casopractico5.R
import com.example.casopractico5.entities.Comunity

class DBOpenHelper private constructor(context: Context?) : SQLiteOpenHelper(context,ComunityContract.NOMBRE_BD,null,ComunityContract.VERSION) {


    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        try {

            // Crear la tabla PRINCIPAL
            val consulta = ("CREATE TABLE ${ComunityContract.Companion.Entrada.NOMBRE_TABLA} "
                    + "( ${ComunityContract.Companion.Entrada.COLUMNA_ID} INTEGER PRIMARY KEY" //Importante asi para que el id se autoincremente
                    + " , ${ComunityContract.Companion.Entrada.COLUMNA_FLAG} INTEGER NOT NULL"
                    + " , ${ComunityContract.Companion.Entrada.COLUMNA_NAME} NVARCHAR(20) NOT NULL"
                    + " , ${ComunityContract.Companion.Entrada.COLUMNA_STATE} TEXT CHECK(${ComunityContract.Companion.Entrada.COLUMNA_STATE} IN ('activo', 'eliminado')) DEFAULT 'activo');")

            sqLiteDatabase.execSQL(consulta)

            inicializarBBDD(sqLiteDatabase,ComunityContract.Companion.Entrada.NOMBRE_TABLA)


            // Crear la tabla BACKUP
            sqLiteDatabase.execSQL("CREATE TABLE ${ComunityContract.Companion.Backup.NOMBRE_TABLA} "
                    + "( ${ComunityContract.Companion.Backup.COLUMNA_ID} INTEGER PRIMARY KEY"
                    + " , ${ComunityContract.Companion.Backup.COLUMNA_FLAG} INTEGER NOT NULL"
                    + " , ${ComunityContract.Companion.Backup.COLUMNA_NAME} NVARCHAR(20) NOT NULL"
                    + " , ${ComunityContract.Companion.Backup.COLUMNA_STATE} TEXT CHECK(${ComunityContract.Companion.Backup.COLUMNA_STATE} IN ('activo', 'eliminado')) DEFAULT 'activo');")

            inicializarBBDD(sqLiteDatabase,ComunityContract.Companion.Backup.NOMBRE_TABLA)



        } catch (e: Exception) {
            // Manejar la excepción
            e.printStackTrace()
        }
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase?, p1: Int, p2: Int) {

        sqLiteDatabase!!.execSQL("DROP TABLE IF EXISTS ${ComunityContract.Companion.Entrada.NOMBRE_TABLA};")

        onCreate(sqLiteDatabase)


    }

     fun inicializarBBDD(db: SQLiteDatabase,tablename:String) {
        try {
            val lista = loadComunities()
            for (c in lista) {
                val initializer = "INSERT INTO $tablename ( " +
                        "${ComunityContract.Companion.Entrada.COLUMNA_FLAG}, " +
                        "${ComunityContract.Companion.Entrada.COLUMNA_NAME} ) VALUES ( ${c.flag} , '${c.name}' );"
                db.execSQL(initializer)
            }
        } catch (e: Exception) {
            // Manejar la excepción
            e.printStackTrace()
        }
    }


   fun loadComunities(): MutableList<Comunity> {
        return mutableListOf(
            Comunity(R.drawable.andalucia,"Andalucía"),
            Comunity(R.drawable.asturias,"Asturias"),
            Comunity(R.drawable.baleares,"Baleares"),
            Comunity(R.drawable.canarias,"Canarias"),
            Comunity(R.drawable.cantabria,"Cantabria"),
            Comunity(R.drawable.castillaleon,"Castilla León"),
            Comunity(R.drawable.castillamancha,"Castilla la Mancha"),
            Comunity(R.drawable.catalunya,"Cataluña"),
            Comunity(R.drawable.ceuta,"Ceuta"),
            Comunity(R.drawable.extremadura,"Extremadura"),
            Comunity(R.drawable.galicia,"Galícia"),
            Comunity(R.drawable.larioja,"La Rioja"),
            Comunity(R.drawable.madrid,"Madrid"),
            Comunity(R.drawable.melilla,"Melilla"),
            Comunity(R.drawable.murcia,"Murcia"),
            Comunity(R.drawable.navarra,"Navarra"),
            Comunity(R.drawable.paisvasco,"País Vasco"),
            Comunity(R.drawable.valencia,"Valencia")
        )

    }


    companion object {
        private var dbOpen: DBOpenHelper? = null
        fun getInstance(context: Context?): DBOpenHelper? {
            if (dbOpen == null) dbOpen = DBOpenHelper(context)
            return dbOpen
        }
    }






}