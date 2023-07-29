package com.codify92.reminderappmaterialdesign.Others;

public class TodoModelClass {
    private String text;

    private int chosenColor;

    private String subtext;

    private CharSequence date;

    public int getChosenColor() {
        return chosenColor;
    }

    public void setChosenColor(int chosenColor) {
        this.chosenColor = chosenColor;
    }

    public CharSequence getDate() {
        return date;
    }

    public void setDate(CharSequence date) {
        this.date = date;
    }

    public String getText() {
        return text;
    }

    public String getSubtext() {
        return subtext;
    }

    public void setSubtext(String subtext) {
        this.subtext = subtext;
    }

    public void setText(String text) {
        this.text = text;
    }

}
