package org.example;

/**
 * Token class representing a recognized token.
 */
public class Token {
    private final String type;
    private final String value;
    private final int position;
    
    public Token(String type, String value, int position) {
        this.type = type;
        this.value = value;
        this.position = position;
    }
    
    public String getType() { return type; }
    public String getValue() { return value; }
    public int getPosition() { return position; }
    
    @Override
    public String toString() {
        return String.format("Token{type='%s', value='%s', position=%d}", type, value, position);
    }
}