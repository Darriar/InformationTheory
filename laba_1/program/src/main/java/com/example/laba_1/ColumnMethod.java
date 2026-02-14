package com.example.laba_1;

public class ColumnMethod {

    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";

    public static void main(String[] args) {

        String text = "это лабораторная работа по киоки";
        String cipherText = encodeColumn(text, "криптография");

        String plainText = decodeColumn(cipherText, "криптография");
        System.out.println(plainText);
    }

    // формирование строк в таблице ключа
    private static Key calcLettterIndex (String keyWord){
        Key key = new Key(keyWord.toCharArray());
        key.nums = new int[key.keyWord.length];

        for (int i = 0; i < key.nums.length; i++) {
            key.nums[i] = 1;
            for (int j = 0; j < key.nums.length; j++) {
                if (key.keyWord[i] > key.keyWord[j] || (key.keyWord[i] == key.keyWord[j] && i > j))
                    key.nums[i]++;
            }
        }
        return key;
    }

    // постановка символов не русского алфавита на свое место
    private static String addAllLetters(String text, StringBuilder result) {

        for (int i = 0; i < text.length(); i++) {
            if (ALPHABET.indexOf(text.charAt(i)) < 0) {
                result.insert(i, text.charAt(i));
            }
        }
        return result.toString();
    }

    public static String encodeColumn(String text, String keyWord) {

        if (keyWord.isEmpty())
            return text;

        // формирование строк в таблице ключа
        Key key = calcLettterIndex(keyWord);

        String rusText = text.replaceAll("[^А-ЯЁ]", "");    // текст без лишних символов

        // создание таблицы для исходного тексста
        int columns = key.keyWord.length;
        int rows = Math.ceilDiv(rusText.length(), columns);
        char[][] plainText = new char[rows][columns];

        // заполнение таблицы, сам текст
        int index = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns && index < rusText.length(); j++) {
                plainText[i][j] = rusText.charAt(index++);
            }
        }

        StringBuilder cipherText = new StringBuilder();
        // шифровка
        for (int i = 0; i < columns; i++) {
            int j = 0, columnInd = 0;
            while (key.nums[columnInd] != i + 1)
                columnInd++;

            while (j < rows && plainText[j][columnInd] != 0) {
                cipherText.append(plainText[j++][columnInd]);
            }
        }

        return addAllLetters(text, cipherText);
    }

    public static String decodeColumn(String cipherText, String keyWord) {

        if (keyWord.isEmpty())
            return cipherText;

        // формирование строк в таблице ключа
        Key key = calcLettterIndex(keyWord);

        String rusText = cipherText.replaceAll("[^А-ЯЁ]", "");    // текст без лишних символов

        // создание таблицы для исходного тексста
        int columns = key.keyWord.length;
        int rows = Math.ceilDiv(rusText.length(), columns);
        char[][] plainText = new char[rows][columns];

        // дешифрирование
        int cipherInd = 0;
        for (int i = 0; i < columns; i++) {
            int columnInd = 0;
            while (key.nums[columnInd] != i + 1)
                columnInd++;

            for(int j = 0; j < rows && (j * key.keyWord.length + columnInd < rusText.length()); j++) {
                plainText[j][columnInd] = rusText.charAt(cipherInd++);
            }
        }

        // превращение строки из 2 мерного массива в строку обычную
        StringBuilder result = new StringBuilder();

        for (char[] row : plainText) {
            for (char c: row) {
                if (c != '\0')
                    result.append(c);
            }
        }

        return addAllLetters(cipherText, result);
    }

    private static class Key {
        char[] keyWord;
        int[] nums;

        public Key(char[] keyWord) {
            this.keyWord = keyWord;
        }
    }
    
}
