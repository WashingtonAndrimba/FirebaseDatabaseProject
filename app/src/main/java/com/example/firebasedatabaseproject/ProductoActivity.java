package com.example.firebasedatabaseproject;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProductoActivity extends AppCompatActivity {

    private DatabaseReference databaseReference;
    private String productoCodigo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registroproductos);

        databaseReference = FirebaseDatabase.getInstance().getReference("productos");

        Button btnAlmacenar = findViewById(R.id.btnAlmacenar);
        btnAlmacenar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (productoCodigo == null) {
                    guardarDatosProducto();
                } else {
                    actualizarDatosProducto();
                }
            }
        });
        Button btnConstultarProducto = findViewById(R.id.btnConsultarProducto);
        btnConstultarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                obtenerDatosProducto();
            }
        });
        Button btnRegresar = findViewById(R.id.btnRegresar);
        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductoActivity.this, Login.class);
                startActivity(intent);
            }
        });
        Button btnEliminarProducto = findViewById(R.id.btnEliminarProducto);
        btnEliminarProducto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminarProducto();
            }
        });

    }

    private void guardarDatosProducto() {
        String codigo = ((EditText) findViewById(R.id.etCodigo)).getText().toString();
        String nombre = ((EditText) findViewById(R.id.etNombreProducto)).getText().toString();
        int stock = Integer.parseInt(((EditText) findViewById(R.id.etStock)).getText().toString());
        double precioCosto = Double.parseDouble(((EditText) findViewById(R.id.etPrecioCosto)).getText().toString());
        double precioVenta = Double.parseDouble(((EditText) findViewById(R.id.etPrecioVenta)).getText().toString());

        if (codigo.isEmpty()) {
            Toast.makeText(this, "El código no puede estar vacío", Toast.LENGTH_SHORT).show();
            return;
        }

        Producto producto = new Producto(codigo, nombre, stock, precioCosto, precioVenta);

        DatabaseReference productoRef = databaseReference.child(codigo);
        productoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    productoRef.setValue(producto);
                    Toast.makeText(ProductoActivity.this, "Actualizado correctamente", Toast.LENGTH_SHORT).show();
                } else {
                    productoRef.setValue(producto);
                    Toast.makeText(ProductoActivity.this, "Almacenado correctamente", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductoActivity.this, "Error al verificar producto existente", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void llenarCamposConDatos(Producto producto) {
        ((EditText) findViewById(R.id.etCodigo)).setText(producto.getCodigo());
        ((EditText) findViewById(R.id.etNombreProducto)).setText(producto.getNombre());
        ((EditText) findViewById(R.id.etStock)).setText(String.valueOf(producto.getStock()));
        ((EditText) findViewById(R.id.etPrecioCosto)).setText(String.valueOf(producto.getPrecioCosto()));
        ((EditText) findViewById(R.id.etPrecioVenta)).setText(String.valueOf(producto.getPrecioVenta()));

        Toast.makeText(ProductoActivity.this, "Producto existente cargado", Toast.LENGTH_SHORT).show();
    }


    private void actualizarDatosProducto() {
        String nombre = ((EditText) findViewById(R.id.etNombreProducto)).getText().toString();
        int stock = Integer.parseInt(((EditText) findViewById(R.id.etStock)).getText().toString());
        double precioCosto = Double.parseDouble(((EditText) findViewById(R.id.etPrecioCosto)).getText().toString());
        double precioVenta = Double.parseDouble(((EditText) findViewById(R.id.etPrecioVenta)).getText().toString());

        Producto productoActualizado = new Producto(productoCodigo, nombre, stock, precioCosto, precioVenta);
        databaseReference.child(productoCodigo).setValue(productoActualizado)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ProductoActivity.this, "Datos de Producto actualizados en Firebase", Toast.LENGTH_SHORT).show();
                        limpiarCampos();
                        productoCodigo = null;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ProductoActivity.this, "Error al actualizar datos de Producto", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void obtenerDatosProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Obtener Datos de Producto");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String codigoProducto = input.getText().toString();
                consultarDatosProducto(codigoProducto);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    private void consultarDatosProducto(String codigoProducto) {
        DatabaseReference productoRef = databaseReference.child(codigoProducto);
        productoRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Producto producto = dataSnapshot.getValue(Producto.class);
                    llenarCamposConDatos(producto);
                    productoCodigo = codigoProducto;
                } else {
                    Toast.makeText(ProductoActivity.this, "Producto no encontrado", Toast.LENGTH_SHORT).show();
                    productoCodigo = null;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ProductoActivity.this, "Error al obtener datos de Producto", Toast.LENGTH_SHORT).show();
                productoCodigo = null;
            }
        });
    }

    private void eliminarProducto() {
        if (productoCodigo != null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Eliminar Producto");
            builder.setMessage("¿Estás seguro de que deseas eliminar este producto?");

            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    DatabaseReference productoRef = databaseReference.child(productoCodigo);
                    productoRef.removeValue()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProductoActivity.this, "Producto eliminado correctamente", Toast.LENGTH_SHORT).show();
                                    limpiarCampos();
                                    productoCodigo = null;
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProductoActivity.this, "Error al eliminar producto", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            });

            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });

            builder.show();
        } else {
            Toast.makeText(this, "No hay producto seleccionado para eliminar", Toast.LENGTH_SHORT).show();
        }
    }


    private void limpiarCampos() {
        ((EditText) findViewById(R.id.etCodigo)).setText("");
        ((EditText) findViewById(R.id.etNombreProducto)).setText("");
        ((EditText) findViewById(R.id.etStock)).setText("");
        ((EditText) findViewById(R.id.etPrecioCosto)).setText("");
        ((EditText) findViewById(R.id.etPrecioVenta)).setText("");
    }
}
