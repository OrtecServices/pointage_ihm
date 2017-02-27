package fr.ortec.pointage.ui;

import com.vaadin.ui.Component;
import com.vaadin.ui.ComponentContainer;
import com.vaadin.ui.CssLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by jerome.millot on 23/02/2017.
 *
 */
public class MenuLayout extends HorizontalLayout{

    CssLayout contentArea = new CssLayout();
    CssLayout menuArea = new CssLayout();

    public MenuLayout(){
        setSizeFull();
        setSpacing(false);

        menuArea.setPrimaryStyleName(ValoTheme.MENU_ROOT);

        contentArea.setPrimaryStyleName("valo-content");
        contentArea.addStyleName("v-scrollable");
        contentArea.setSizeFull();

        addComponents(menuArea, contentArea);
        setExpandRatio(contentArea, 1);

    }

    public ComponentContainer getContentContainer(){
        return contentArea;
    }

    public void addMenu(Component menu){
        menu.addStyleName(ValoTheme.MENU_PART);
        menuArea.addComponent(menu);
    }

}

