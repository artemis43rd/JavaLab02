package ru.spbstu.telematics.java;

import org.junit.Test;
import static org.junit.Assert.*;

public class AppTest
{

    @Test
    public void testTreeMap()
    {
        App<String, Integer> myTreeMap = new App<>();
        java.util.TreeMap<String, Integer> javaTreeMap = new java.util.TreeMap<>();

        myTreeMap.add("one", 1);
        javaTreeMap.put("one", 1);

        myTreeMap.add("two", 2);
        javaTreeMap.put("two", 2);

        myTreeMap.add("three", 3);
        javaTreeMap.put("three", 3);

        assertEquals(javaTreeMap.size(), myTreeMap.size());

        // Проверка наличия элементов
        assertTrue(myTreeMap.contains("one"));
        assertTrue(myTreeMap.contains("two"));
        assertTrue(myTreeMap.contains("three"));

        // Проверка значения элементов
        assertEquals(javaTreeMap.get("one"), myTreeMap.get("one"));
        assertEquals(javaTreeMap.get("two"), myTreeMap.get("two"));
        assertEquals(javaTreeMap.get("three"), myTreeMap.get("three"));

        // Удаление элемента
        myTreeMap.remove("two");
        javaTreeMap.remove("two");

        // Проверка размера после удаления
        assertEquals(javaTreeMap.size(), myTreeMap.size());

        // Проверка отсутствия удаленного элемента
        assertFalse(myTreeMap.contains("two"));
    }
}
