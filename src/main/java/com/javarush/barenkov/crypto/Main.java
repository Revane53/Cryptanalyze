package com.javarush.barenkov.crypto;

import com.javarush.barenkov.crypto.client.Console;
import com.javarush.barenkov.crypto.client.FxGui;

public class Main {
    public static void main(String[] args) {
        if (args.length > 0 && args[0].equals("--console")) {
            Console.main(args);
        } else {
            FxGui.main(args);
        }
    }
}