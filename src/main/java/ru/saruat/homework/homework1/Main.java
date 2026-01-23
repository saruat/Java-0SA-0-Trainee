package ru.saruat.homework.homework1;

public class Main {
    public static void main(String[] args) {
        MyHashMap<Integer, Character> myHashMap = new MyHashMap<>();

        int dataLength = 26;
        int key = 65;
        int [] keys = new int[dataLength];
        Character [] values = new Character[dataLength];

        for (int i = 0; i < dataLength; i++) {
                keys[i] = key;
                values[i] = (char)key;
                key++;
        }

        for (int i = 0; i < dataLength; i++) {
            myHashMap.put(keys[i], values[i]);
        }


        System.out.println(myHashMap);

        myHashMap.remove(70);

        System.out.println(myHashMap);

        System.out.println(myHashMap.get(65));
    }

}
