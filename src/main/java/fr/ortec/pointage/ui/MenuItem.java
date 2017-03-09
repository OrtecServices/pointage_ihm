package fr.ortec.pointage.ui;

import java.io.Serializable;

/**
 * Created by jerome.millot on 07/03/2017.
 *
 */
public class MenuItem implements Serializable{

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
}
