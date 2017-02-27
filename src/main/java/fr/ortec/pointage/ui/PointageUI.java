package fr.ortec.pointage.ui;

import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.Label;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import fr.ortec.pointage.view.Accueil;
import fr.ortec.pointage.view.TestIcon;
import fr.ortec.pointage.view.TextFields;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

/**
 * Created by jerome.millot on 23/02/2017.
 *
 */
@Theme("valo-ortec")
@Title("Pointage DSI")
@PreserveOnRefresh
public class PointageUI extends UI {

    private boolean testMode = false;

    private static LinkedHashMap<String, String> themeVariants = new LinkedHashMap<>();
    static{
        themeVariants.put("valo-ortec", "Default");
        themeVariants.put("valo-blueprint", "Blueprint");
        themeVariants.put("valo-dark", "Dark");
        themeVariants.put("valo-facebook", "Facebook");
        themeVariants.put("valo-flat", "Flat");
        themeVariants.put("valo-flatdark", "Flatdark");
        themeVariants.put("valo-light", "Light");
        themeVariants.put("valo-metro", "Metro");
        themeVariants.put("valo-reindeer", "Reindeer");
    }
    private TestIcon testIcon = new TestIcon(100);

    MenuLayout root = new MenuLayout();
    ComponentContainer viewDisplay = root.getContentContainer();
    CssLayout menu = new CssLayout();
    CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("menu");
    }
    private Navigator navigator;
    private LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();

    @Override
    public void init(VaadinRequest request) {

        if (request.getParameter("test") != null) {
            testMode = true;

            if (browserCantRenderFontsConsistently()) {
                getPage().getStyles()
                        .add(".v-app.v-app.v-app {font-family: Sans-Serif;}");
            }
        }

        if(getPage().getWebBrowser().isIE() && getPage().getWebBrowser().getBrowserMajorVersion() == 9){
            menu.setWidth("320px");
        }

        if(!testMode){
            Responsive.makeResponsive(this);
        }

        getPage().setTitle("Pointage DSI");
        setContent(root);
        root.setWidth("100%");

        root.addMenu(buildMenu());

        navigator = new Navigator(this, viewDisplay);
        // Ajout des view : item du menu navigator.addView("libelle", Class.class);

        navigator.addView("accueil", Accueil.class);
        navigator.addView("textfields", TextFields.class);

        String f = Page.getCurrent().getUriFragment();
        if(f == null || "".equals(f)){
            navigator.navigateTo("accueil");
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
                for(Entry<String, String> item : menuItems.entrySet()){
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

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS");
    }

    static boolean isTestMode(){
        return ((PointageUI) getCurrent()).testMode;
    }

    Component buildMenu(){

        // add items
        menuItems.put("accueil", "Accueil");

        menuItems.put("recherche", "Recherche");
        menuItems.put("myproject", "Mes Projets");
        menuItems.put("newproject", "Nouveau Projet");

        menuItems.put("portofolio", "Portefeuille");
        menuItems.put("charge", "Plan de charge");

        menuItems.put("input", "Saisie");
        menuItems.put("validation", "Validation");

        menuItems.put("program", "Programme");
        menuItems.put("projecttype", "Type de Projet");
        menuItems.put("doctype", "Type de Document");

        HorizontalLayout top = new HorizontalLayout();
        top.setWidth("100%");
        top.setSpacing(false);
        top.setDefaultComponentAlignment(Alignment.MIDDLE_LEFT);
        top.addStyleName(ValoTheme.MENU_TITLE);
        menu.addComponent(top);
        menu.addComponent(createThemeSelect());

        Button showMenu = new Button("Menu", new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent clickEvent) {
                if(menu.getStyleName().contains("valo-menu-visible")){
                    menu.removeStyleName("valo-menu-visible");
                } else {
                    menu.addStyleName("valo-menu-visible");
                }
            }
        });
        showMenu.addStyleName(ValoTheme.BUTTON_PRIMARY);
        showMenu.addStyleName(ValoTheme.BUTTON_SMALL);
        showMenu.addStyleName("valo-menu-toggle");
        showMenu.setIcon(FontAwesome.LIST);
        menu.addComponent(showMenu);

        Label title = new Label("<h3>Pointage <strong>DSI</strong></h3>", ContentMode.HTML);
        title.setSizeUndefined();
        top.addComponent(title);
        top.setExpandRatio(title, 1);

        MenuBar settings = new MenuBar();
        settings.addStyleName("user-menu");
        MenuItem settingsItem = settings.addItem("caption 1", new ThemeResource("../valo-ortec/img/profile-pic.png"), null);
        settingsItem.addItem("Edit Profile", null);
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        /**
         * Gestion des entêtes de section du menu
         * la suite ici : https://github.com/vaadin/framework/blob/master/uitest/src/main/java/com/vaadin/tests/themes/valo/ValoThemeUI.java
         * ligne 288
        }*/

        Label label = null;
        int count = -1;
        for (final Entry<String, String> item : menuItems.entrySet()) {
            if (item.getKey().equals("recherche")) {
                label = new Label("Projet", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("portofolio")) {
                label.setValue(
                        label.getValue() + " <span class=\"valo-menu-badge\">"
                                + count + "</span>");
                count = 0;
                label = new Label("PPM", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("input")) {
                label.setValue(
                        label.getValue() + " <span class=\"valo-menu-badge\">"
                                + count + "</span>");
                count = 0;
                label = new Label("Imputations", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            if (item.getKey().equals("program")) {
                label.setValue(
                        label.getValue() + " <span class=\"valo-menu-badge\">"
                                + count + "</span>");
                count = 0;
                label = new Label("Paramétrage", ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }
            Button b = new Button(item.getValue(), new Button.ClickListener() {
                @Override
                public void buttonClick(Button.ClickEvent event) {
            navigator.navigateTo(item.getKey());
                }
            });
            /**if (count == 2) {
                b.setCaption(b.getCaption()
                        + " <span class=\"valo-menu-badge\">123</span>");
            }*/
            b.setCaptionAsHtml(true);
            b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
            b.setIcon(testIcon.get());
            menuItemsLayout.addComponent(b);
            count++;
        }
        label.setValue(label.getValue() + " <span class=\"valo-menu-badge\">" + count + "</span>");

        return menu;
    }

    private Component createThemeSelect(){
        // Keep theme select the same size as in the current screenshots
        double width = 96;
        WebBrowser browser = VaadinSession.getCurrent().getBrowser();
        // TODO : a revoir
        if(browser.isChrome()){
            width = 95;
        } else if(browser.isIE()){
            width = 95.39;
        } else if(browser.isFirefox()){
            width = 98;
        }
        getPage().getStyles().add("#themeSelect select {width: " + width + "px;}");

        final NativeSelect  ns = new NativeSelect();
        ns.setNullSelectionAllowed(false);
        ns.setId("themeSelect");
        ns.addContainerProperty("caption", String.class, "");
        ns.setItemCaptionPropertyId("caption");
        for(String identifier : themeVariants.keySet()){
            ns.addItem(identifier).getItemProperty("caption").setValue(themeVariants.get(identifier));
        }

        ns.setValue("valo-ortec");
        ns.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent valueChangeEvent) {
                setTheme((String) ns.getValue());
            }
        });
        return ns;
    }
}
