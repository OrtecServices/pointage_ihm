package fr.ortec.pointage.view;

import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

/**
 * Created by jerome.millot on 27/02/2017.
 *
 */
public class Accueil extends VerticalLayout implements View {

    public Accueil(){
        setMargin(true);
        setSpacing(true);
        Label h1 = new Label("Tables");
        h1.addStyleName(ValoTheme.LABEL_H1);
        addComponent(h1);
    }


    @Override
    public void enter(ViewChangeListener.ViewChangeEvent viewChangeEvent) {
        // TODO Auto-generated method stub
    }
}
