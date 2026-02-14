package com.example.laba_1;

import java.util.Locale;

public class VigenerAlgorithm {

    private static final String ALPHABET = "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private static final int ALPHABET_LENGTH = ALPHABET.length();

    public static void main(String[] args) {

        String text = "съешь ещё";
        String key = "ёжик";

        String cipherText = encodeVigener(text, key);

        String plainText = decodeVigener(cipherText, key.toString());
        System.out.println(plainText);
        System.out.println(plainText);
    }

    public static String encodeVigener(String plainText, String keyWord) {

        if (keyWord.isEmpty()) return plainText;

        StringBuilder key = new StringBuilder(keyWord);
        String rusText = plainText.replaceAll("[^А-ЯЁ]", "");
        int substract =  rusText.length() - key.length();

        for (int i = 0; i < substract; i++){
            key.append(rusText.charAt(i));
        }

        StringBuilder cipherText = new StringBuilder();

        int posText, posKey, newPos;
        int indKey = 0;
        for (int i = 0; i < plainText.length(); i++) {
            if (ALPHABET.indexOf(plainText.charAt(i)) >= 0) {
                posText = ALPHABET.indexOf(plainText.charAt(i));
                posKey = ALPHABET.indexOf(key.charAt(indKey++));
                newPos = (posText + posKey) % (ALPHABET.length());

                cipherText.append(ALPHABET.charAt(newPos));
            } else {
                cipherText.append(plainText.charAt(i));
            }
        }

        return cipherText.toString();
    }

    public static String decodeVigener(String cipherText, String key) {

        if (key.isEmpty()) return cipherText;

        StringBuilder plainText = new StringBuilder();
        String rusText = cipherText.replaceAll("[^А-ЯЁ]", "");

        int posText, posKey, newPos;
        int indKey = 0;
        for (int i = 0; i < cipherText.length(); i++) {
            if (ALPHABET.indexOf(cipherText.charAt(i)) >= 0) {
                if (indKey < key.length())
                    posKey = ALPHABET.indexOf(key.charAt(indKey));
                else {
                    while (ALPHABET.indexOf(plainText.charAt(indKey - key.length())) < 0)      // обход в тексте не русских символов
                        indKey++;
                    posKey = ALPHABET.indexOf(plainText.charAt(indKey - key.length()));
                }
               // posKey = ALPHABET.indexOf(indKey < key.length() ? key.charAt(indKey) : plainText.charAt(indKey - key.length()));
                indKey++;
                posText = ALPHABET.indexOf(cipherText.charAt(i));
                newPos = ((posText - posKey) + ALPHABET_LENGTH) % ALPHABET_LENGTH;

                plainText.append(ALPHABET.charAt(newPos));
            } else {
               plainText.append(cipherText.charAt(i));
            }
        }

        return plainText.toString();
    }

}
