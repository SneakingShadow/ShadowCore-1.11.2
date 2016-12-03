package com.sneakingshadow.core.multiblock.initializer;

public class ValueInitializer {

    private Character character;

    ValueInitializer(Character character) {
        this.character = character;
    }

    public Character getCharacter() {
        return character;
    }

    public boolean isSpecialCharacter(Object value) {
        return this.character.equals(value);
    }

}
