package org.ogz.model.mail;

public class Body {
    public Body() {
    }

    public Body(String data, int size) {
        this.data = data;
        this.size = size;
    }

    private String data;
    private int size;
}
