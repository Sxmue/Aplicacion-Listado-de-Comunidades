package com.example.casopractico5.database

import android.provider.BaseColumns

/**
 * Contrato entre la aplicacion y la base de datos, el mapeo de las tablas
 */
class ComunityContract {

    companion object {

        val NOMBRE_BD = "Comunidades"
        val VERSION = 1

        class Entrada : BaseColumns {

            companion object {

               const val NOMBRE_TABLA = "comunities"

                const val COLUMNA_ID = "id"

                const val COLUMNA_FLAG = "flag"

                const val COLUMNA_NAME = "name"

                const val COLUMNA_STATE = "state"


            }

        }

        class Usuarios : BaseColumns {

            companion object {

                const val NOMBRE_TABLA = "usuarios"

                const val COLUMNA_ID = "id"

                const val COLUMNA_EMAIL = "email"

                const val COLUMNA_PASSWORD = "password"


            }

        }

        class Backup : BaseColumns {

            companion object {

                const val NOMBRE_TABLA = "backupComunities"

                const val COLUMNA_ID = "id"

                const val COLUMNA_FLAG = "flag"

                const val COLUMNA_NAME = "name"

                const val COLUMNA_STATE = "state"


            }

        }



    }


}