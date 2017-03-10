package fr.ortec.dsi.pointage.presentation.ihm.ui;

import java.io.Serializable;

/**
 * Created by jerome.millot on 10/03/2017.
 *
 */
public class MenuItem implements Serializable {

    private String name;
    private String label;
    private String viewClass;
    private boolean header;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getViewClass() {
        return viewClass;
    }

    public void setViewClass(String viewClass) {
        this.viewClass = viewClass;
    }

    public boolean isHeader() {
        return header;
    }

    public void setHeader(boolean header) {
        this.header = header;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
