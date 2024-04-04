package com.example.pruebafastadapter.utils;

import android.os.Message;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;

public class NotificationHelper {
    public void enviarNotificacionATodos(String titulo, String cuerpo) {
        // Obtener los tokens de registro de todos los dispositivos suscritos al tema
        List<String> tokens = obtenerTokensDispositivosSuscritos();

        // Crear un mensaje de notificación

        // Enviar el mensaje de notificación

    }

    private List<String> obtenerTokensDispositivosSuscritos() {
        // Aquí debes implementar la lógica para obtener los tokens de registro
        // de todos los dispositivos suscritos al tema o grupo adecuado.
        // Puedes almacenar los tokens en una base de datos y recuperarlos aquí.
        // Por ahora, supongamos que tienes una lista estática de tokens de ejemplo.
        List<String> tokens = new ArrayList<>();
        tokens.add("token_dispositivo_1");
        tokens.add("token_dispositivo_2");
        // Agregar más tokens si es necesario
        return tokens;
    }
}
