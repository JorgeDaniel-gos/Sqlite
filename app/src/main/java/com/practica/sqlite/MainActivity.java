package com.practica.sqlite;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private MiDatabaseHelper conexion;
    private EditText inputNombre, inputEdad, inputId;
    private TextView outputResultado, outputUsuarios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializamos las vistas
        Button botonGuardar = findViewById(R.id.btn_guardar);
        Button botonActualizar = findViewById(R.id.btn_actualizar);
        Button botonEliminar = findViewById(R.id.btn_eliminar);
        Button botonMostrarUsuarios = findViewById(R.id.btn_mostrar_usuarios);

        inputNombre = findViewById(R.id.input_nombre);
        inputEdad = findViewById(R.id.input_edad);  // Agregamos el campo para la edad
        inputId = findViewById(R.id.input_id);
        outputResultado = findViewById(R.id.output_resultado);
        outputUsuarios = findViewById(R.id.output_usuarios);

        // Creamos la conexión con la base de datos
        conexion = new MiDatabaseHelper(this);

        // Configuración del botón de guardar
        botonGuardar.setOnClickListener(view -> {
            String nombre = inputNombre.getText().toString().trim();
            String edadText = inputEdad.getText().toString().trim();

            // Validamos si los campos de nombre y edad no están vacíos
            if (nombre.isEmpty() || edadText.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un nombre y una edad", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int edad = Integer.parseInt(edadText);  // Convertimos la edad a un entero
                    conexion.insertarUsuario(nombre, edad); // Insertamos el usuario con el nombre y la edad proporcionada
                    Toast.makeText(this, "Usuario guardado exitosamente", Toast.LENGTH_SHORT).show();
                    inputNombre.setText("");  // Limpiar campo nombre
                    inputEdad.setText("");   // Limpiar campo edad
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor ingresa una edad válida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuración del botón de actualizar
        botonActualizar.setOnClickListener(view -> {
            String idText = inputId.getText().toString().trim();
            String nombre = inputNombre.getText().toString().trim();
            String edadText = inputEdad.getText().toString().trim();

            // Validamos si los campos de ID, nombre y edad no están vacíos
            if (idText.isEmpty() || nombre.isEmpty() || edadText.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un ID, nombre y edad", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    int id = Integer.parseInt(idText);
                    int edad = Integer.parseInt(edadText);
                    conexion.actualizarUsuario(id, nombre, edad); // Actualizamos el usuario con la edad proporcionada
                    Toast.makeText(this, "Usuario actualizado", Toast.LENGTH_SHORT).show();
                    inputId.setText(""); // Limpiar campo ID
                    inputNombre.setText(""); // Limpiar campo nombre
                    inputEdad.setText("");  // Limpiar campo edad
                } catch (NumberFormatException e) {
                    Toast.makeText(this, "Por favor ingresa una edad válida", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Configuración del botón de eliminar
        botonEliminar.setOnClickListener(view -> {
            String idText = inputId.getText().toString().trim();
            if (idText.isEmpty()) {
                Toast.makeText(this, "Por favor ingresa un ID para eliminar", Toast.LENGTH_SHORT).show();
            } else {
                int id = Integer.parseInt(idText);
                conexion.eliminarUsuario(id); // Eliminar usuario con el ID proporcionado
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                inputId.setText(""); // Limpiar campo ID
            }
        });

        // Configuración del botón de mostrar usuarios
        botonMostrarUsuarios.setOnClickListener(view -> {
            Cursor cursor = conexion.obtenerUsuarios();
            StringBuilder usuarios = new StringBuilder();
            if (cursor.moveToFirst()) {
                do {
                    // Recuperar el ID, nombre y edad de cada usuario
                    @SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex("id"));
                    @SuppressLint("Range") String nombre = cursor.getString(cursor.getColumnIndex("nombre"));
                    @SuppressLint("Range") int edad = cursor.getInt(cursor.getColumnIndex("edad"));

                    // Agregar la información a la cadena para mostrar
                    usuarios.append("ID: ").append(id).append(", Nombre: ").append(nombre)
                            .append(", Edad: ").append(edad).append("\n");
                } while (cursor.moveToNext());
            }
            cursor.close();
            outputUsuarios.setText(usuarios.toString()); // Mostrar usuarios en la interfaz
        });
    }
}