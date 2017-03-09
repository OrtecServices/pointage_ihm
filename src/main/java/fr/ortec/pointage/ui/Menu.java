package fr.ortec.pointage.ui;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jerome.millot on 07/03/2017.
 *
 */
public class Menu implements Serializable{

    List<MenuItem> menuitems;

    public List<MenuItem> getMenuitems() {
        return menuitems;
    }

    public void setMenuitems(List<MenuItem> menuitems) {
        this.menuitems = menuitems;
    }
}
