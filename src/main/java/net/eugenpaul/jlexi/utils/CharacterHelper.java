package net.eugenpaul.jlexi.utils;

public class CharacterHelper {
    private CharacterHelper() {
    }

    public static boolean isPrintable(Character c) {
        int type = Character.getType(c);
        return (type >= Character.UPPERCASE_LETTER //
                && type <= Character.PARAGRAPH_SEPARATOR) //
                || (type >= Character.DASH_PUNCTUATION //
                        && type <= Character.FINAL_QUOTE_PUNCTUATION//
                ) //
        ;
    }
}
