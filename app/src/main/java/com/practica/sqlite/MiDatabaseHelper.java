package com.practica.sqlite;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;
import android.widget.Toast;

public class MiDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "miBaseDeDatos.db";
    private static final int DATABASE_VERSION = 1;
    private Context context;  // Contexto para mostrar los mensajes de Toast

    // Constructor
    public MiDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;  // Inicializamos el contexto
    }

    // Crear las tablas de la base de datos
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE usuarios (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "nombre TEXT," +
                "edad INTEGER)";
        db.execSQL(CREATE_TABLE);
    }

    // Actualizar la base de datos si la versión cambia
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS usuarios");
        onCreate(db);
    }

    // Insertar un usuario en la base de datos
    public void insertarUsuario(String nombre, int edad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nombre);
        valores.put("edad", edad);

        long resultado = db.insert("usuarios", null, valores);
        db.close();

        // Mostrar mensaje de éxito o error
        if (resultado > 0) {
            Toast.makeText(context, "Registro Exitoso", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al registrar", Toast.LENGTH_SHORT).show();
        }
    }

    // Actualizar un usuario en la base de datos
    public void actualizarUsuario(int id, String nuevoNombre, int nuevaEdad) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("nombre", nuevoNombre);
        valores.put("edad", nuevaEdad);

        // Ejecutar la actualización
        int resultado = db.update("usuarios", valores, "id = ?", new String[]{String.valueOf(id)});
        db.close();

        // Mostrar mensaje de éxito o error
        if (resultado > 0) {
            Toast.makeText(context, "Usuario Actualizado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al actualizar", Toast.LENGTH_SHORT).show();
        }
    }

    // Eliminar un usuario de la base de datos
    public void eliminarUsuario(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int resultado = db.delete("usuarios", "id = ?", new String[]{String.valueOf(id)});

        if (resultado > 0) {
            Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Error al eliminar usuario", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    // Obtener todos los usuarios de la base de datos
    public Cursor obtenerUsuarios() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM usuarios", null);
    }

    // Mostrar todos los usuarios en la consola (usualmente útil para debugging)
    public void mostrarUsuarios() {
        Cursor cursor = obtenerUsuarios();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                @SuppressLint("Range") int edad = cursor.getInt(cursor.getColumnIndex("edad"));

                // Imprimir el id, nombre y edad en la consola
                System.out.println("ID: " + id + ", Nombre: " + nombre + ", Edad: " + edad);

            } while (cursor.moveToNext());
        }

        cursor.close();
        System.out.println("Total de usuarios mostrados.");
    }
}