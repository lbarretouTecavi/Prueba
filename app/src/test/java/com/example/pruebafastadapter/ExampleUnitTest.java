package com.example.pruebafastadapter;

import org.junit.Test;

import static org.junit.Assert.*;

import com.example.pruebafastadapter.data.api.HttpClient;
import com.example.pruebafastadapter.data.models.Task;

import java.io.IOException;
import java.util.Date;

import okhttp3.OkHttpClient;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

//    @Test
//    public void testCheckTaskExists() throws IOException {
//        HttpClient httpClient = new HttpClient();
//        String URL_API = "https://cam.tecavi.net/api/task/";
//
//        Task task = new Task(Long.parseLong("1"),"86051005-158f-47a1-b826-6540e4b4dfdc","Hola",new Date().toString(),new Date().toString(),false, false);
//
//        // Verificar que la tarea existente se detecte correctamente
//        assertTrue("Pasó",httpClient.existTaskInApi(URL_API, task));
//
//        // IdUnique de una tarea que no existe
//        Task task2 = new Task(Long.parseLong("2"),"86051005-158f-47a1-b826-6540e4b4dfd1","Hola",new Date().toString(),new Date().toString(),false, false);
//
//        // Verificar que la tarea que no existe se maneje correctamente
//        assertFalse("No pasó", httpClient.existTaskInApi(URL_API, task2));
//    }
}