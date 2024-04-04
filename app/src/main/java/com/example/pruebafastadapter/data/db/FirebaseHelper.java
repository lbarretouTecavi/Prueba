package com.example.pruebafastadapter.data.db;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.pruebafastadapter.data.models.Task;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;

public class FirebaseHelper {
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("tareas");
    private FirebaseFirestore firestoreReference = FirebaseFirestore.getInstance();

    public DatabaseReference getDatabaseReference() {
        return databaseReference;
    }

    public FirebaseFirestore getFirestoreReference() {
        return firestoreReference;
    }

    public void saveTask(Task task) {
        databaseReference.child(task.getIdUnique()).setValue(task);
    }

    public void updateTask(Task task) {
        databaseReference.child(task.getIdUnique()).setValue(task);
    }

    public void deleteTask(Task task) {
        databaseReference.child(task.getIdUnique()).removeValue();
    }

    public void crearTask(Task task) {
        firestoreReference.collection("tasks")
                .document(task.getIdUnique())
                .set(task)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FIRESTORE", "Documento agregado");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIRESTORE", "Error al agregar documento", e);
                    }
                });
    }

    public void actualizarTask(Task task) {
        firestoreReference.collection("tasks")
                .document(task.getIdUnique())
                .set(task, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FIRESTORE", "Documento actualizado");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIRESTORE", "Error al actualizar documento", e);
                    }
                });
    }

    public void eliminarTask(Task task) {
        firestoreReference.collection("tasks")
                .document(task.getIdUnique())
                .delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("FIRESTORE", "Documento eliminado");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FIRESTORE", "Error al eliminar documento", e);
                    }
                });
    }
}
