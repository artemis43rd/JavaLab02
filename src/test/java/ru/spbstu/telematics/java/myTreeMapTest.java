package ru.spbstu.telematics.java;

import org.junit.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class myTreeMapTest
{
    @Test
    public void testMyTreeMap()
    {
        myTreeMap<Integer, String> myTreeMap = new myTreeMap<>();

        assertTrue(myTreeMap.isEmpty());

        myTreeMap.put(3, "Three");
        myTreeMap.put(1, "One");
        myTreeMap.put(2, "Two");

        // Проверка, что размер равен количеству добавленных элементов
        assertEquals(3, myTreeMap.size());

        // Проверка, что карта содержит определенные значения
        assertTrue(myTreeMap.containsValue("One"));
        assertTrue(myTreeMap.containsValue("Two"));
        assertFalse(myTreeMap.containsValue("Four"));

        // Проверка, что ключи в правильном порядке
        assertEquals(1, myTreeMap.firstKey());
        assertEquals(3, myTreeMap.lastKey());

        // Проверка subMap
        SortedMap<Integer, String> subMap = myTreeMap.subMap(1, true, 2, true);
        assertEquals(2, subMap.size());
        assertTrue(subMap.containsKey(1));
        assertTrue(subMap.containsKey(2));
        assertFalse(subMap.containsKey(3));

        // Удаление элемента и проверка уменьшения размера
        myTreeMap.remove(2);
        assertEquals(2, myTreeMap.size());
        assertFalse(myTreeMap.containsKey(2));

        // Создание списка значений и проверка
        List<String> values = myTreeMap.values();
        assertIterableEquals(Arrays.asList("One", "Three"), values);

        // Создание множество ключей и проверка
        Set<Integer> keys = myTreeMap.keySet();
        assertIterableEquals(new HashSet<>(Arrays.asList(1, 3)), keys);

        // Очистка карты и проверка
        myTreeMap.clear();
        assertTrue(myTreeMap.isEmpty());
    }
}