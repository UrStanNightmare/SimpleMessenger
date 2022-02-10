package ru.ao.simplemessenger.client.utils;

import javafx.scene.input.KeyCode;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class CustomKeyCombo {
    private List<KeyCode> neededCodes;
    private List<KeyCode> actualKeys = new LinkedList<>();

    public CustomKeyCombo(KeyCode... codes) {
        this.neededCodes = Arrays.asList(codes);
    }

    public void addKey(KeyCode keyCode) {
        this.actualKeys.add(keyCode);
    }

    public void removeKey(KeyCode keyCode) {
        this.actualKeys.remove(keyCode);
    }

    public boolean checkCombo() {
        return this.actualKeys.containsAll(this.neededCodes);
    }

    public void cleanKeys() {
        this.actualKeys.clear();
    }

}
