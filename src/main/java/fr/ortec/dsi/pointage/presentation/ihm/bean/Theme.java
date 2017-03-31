package fr.ortec.dsi.pointage.presentation.ihm.bean;

/**
 * Created by remy.cordoliani on 31/03/2017.
 */
public class Theme {
    private String value;
    private String name;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Theme(String value, String name) {
        this.value = value;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static String getName(Object o) {
        return ((Theme)o).getName();
    }
}
