package fr.ortec.pointage.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.Page;
import com.vaadin.server.Responsive;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by jerome.millot on 23/02/2017.
 *
 */
@Theme("facebook")
@PreserveOnRefresh
public class PointageUI extends UI {

    private boolean testMode = false;
    private static LinkedHashMap<String, String> themeVariants = new LinkedHashMap<>();
    static{
        themeVariants.put("valo", "Default");
        themeVariants.put("valo-dark", "Dark");
    }
    // private TestIcon testIcon = new TestIcon(100);

    MenuLayout root = new MenuLayout();
    ComponentContainer viewDisplay = root.getComponentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("testMenu");
    }
    private Navigator navigator;
    private LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();

    @Override
    public void init(VaadinRequest request) {

        if(getPage().getWebBrowser().isIE() && getPage().getWebBrowser().getBrowserMajorVersion() == 9){
            menu.setWidth("320px");
        }

        if(!testMode){
            Responsive.makeResponsive(this);
        }

        getPage().setTitle("Vaadin UI");
        setContent(root);
        root.setWidth("100%");

        root.addMenu(buildMenu());

        navigator = new Navigator(this, viewDisplay);
        // Ajout des view : item du menu navigator.addView("libelle", Class.class);

        String f = Page.getCurrent().getUriFragment();
        if(f == null || "".equals(f)){
            navigator.navigateTo("common");
        }

        navigator.addViewChangeListener(new ViewChangeListener() {
            @Override
            public boolean beforeViewChange(ViewChangeEvent viewChangeEvent) {
                return true;
            }

            @Override
            public void afterViewChange(ViewChangeEvent viewChangeEvent) {
                for(Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();){
                    it.next().removeStyleName("selected");
                }
                for(Map.Entry<String, String> item : menuItems.entrySet()){
                    if(viewChangeEvent.getViewName().equals(item.getKey())){
                        for(Iterator<Component> it = menuItemsLayout.iterator(); it.hasNext();){
                            Component c = it.next();
                            if(c.getCaption() != null && c.getCaption().startsWith(item.getValue())){
                                c.addStyleName("selected");
                                break;
                            }
                        }
                    }
                }
                menu.removeStyleName("valo-menu-visible");
            }
        });

    }

    Component buildMenu(){
        CssLayout menu = new CssLayout();
        menu.addComponent(new Label("a"));
        return menu;
    }
}
