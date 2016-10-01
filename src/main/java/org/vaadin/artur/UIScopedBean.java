package org.vaadin.artur;

import java.io.Serializable;

import com.vaadin.cdi.UIScoped;

@UIScoped
public class UIScopedBean implements Serializable {

    private String string = "initial";
    private int number = -1;

    /**
     * @return the string
     */
    public String getString() {
        return string;
    }

    /**
     * @param string
     *            the string to set
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * @return the number
     */
    public int getNumber() {
        return number;
    }

    /**
     * @param number
     *            the number to set
     */
    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "MyBean [string=" + string + ", number=" + number + "] ("
                + super.toString() + ")";
    }

}
