package fr.ortec.dsi.pointage.presentation.ihm.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.annotations.PreserveOnRefresh;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.Title;
import com.vaadin.data.Property;
import com.vaadin.event.ShortcutAction;
import com.vaadin.event.ShortcutListener;
import com.vaadin.navigator.Navigator;
import com.vaadin.navigator.View;
import com.vaadin.navigator.ViewChangeListener;
import com.vaadin.server.*;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.*;
import com.vaadin.ui.MenuBar.MenuItem;
import com.vaadin.ui.themes.ValoTheme;
import fr.ortec.dsi.pointage.presentation.ihm.ui.Menu;
import fr.ortec.dsi.pointage.presentation.ihm.helper.HelpManager;
import fr.ortec.dsi.pointage.presentation.ihm.ui.Menu;
import fr.ortec.dsi.pointage.presentation.ihm.ui.MenuLayout;
import fr.ortec.dsi.pointage.presentation.ihm.view.TestIcon;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map.Entry;

/**
 * Created by jerome.millot on 23/02/2017.
 *
 */
@Theme("valo-ortec")
@Title("Pointage DSI")
@PreserveOnRefresh
public class PointageUI extends UI {

    private static final Logger LOGGER = LoggerFactory.getLogger(PointageUI.class);

    private HelpManager helpManager;

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
    private CssLayout loginRoot = new CssLayout();
    private MenuLayout root = new MenuLayout();
    private ComponentContainer viewDisplay = root.getContentContainer();
    private CssLayout menu = new CssLayout();
    private CssLayout menuItemsLayout = new CssLayout();
    {
        menu.setId("menu");
    }

    private VerticalLayout loginLayout;
    private Navigator navigator;
    private LinkedHashMap<String, String> menuItems = new LinkedHashMap<>();
    private Menu appMenu;
    final private String basepath = VaadinService.getCurrent().getBaseDirectory().getAbsolutePath();

    @Override
    public void init(VaadinRequest request) {

        helpManager = new HelpManager(this);
        setLocale(Locale.FRANCE);

        getPage().setTitle("Pointage DSI");

        setContent(loginRoot);
        loginRoot.setSizeFull();

        buildLoginView(false);
    }

    private void buildMainView(){

        if (browserCantRenderFontsConsistently()) {
            getPage().getStyles().add(".v-app.v-app.v-app {font-family: Sans-Serif;}");
        }

        if (getPage().getWebBrowser().isIE() && getPage().getWebBrowser().getBrowserMajorVersion() == 9) {
            menu.setWidth("320px");
        }

        helpManager.closeAll();

        // root.removeComponent(loginLayout);

        ObjectMapper mapper = new ObjectMapper();
        try {
            appMenu = mapper.readValue(new File(basepath + "/WEB-INF/classes/config_menu.json"), Menu.class);
        }catch(IOException ex){
            LOGGER.error(ex.getMessage());
        }

        root.addMenu(buildMenu());

        navigator = new Navigator(this, viewDisplay);
        // Ajout des view : item du menu navigator.addView("libelle", Class.class);

        for(fr.ortec.dsi.pointage.presentation.ihm.ui.MenuItem menuItem : appMenu.getMenuitems()){
            try {
                if(menuItem.getViewClass() == null){
                    continue;
                }
                Class<? extends View> clazz = (Class<? extends View>) Class.forName(menuItem.getViewClass());
                //Object obj = clazz.newInstance();
                navigator.addView(menuItem.getName(), clazz);
            }catch(ClassNotFoundException exception){
                LOGGER.error(exception.getMessage());
            }
        }

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

        setContent(root);

    }

    private boolean browserCantRenderFontsConsistently() {
        // PhantomJS renders font correctly about 50% of the time, so
        // disable it to have consistent screenshots
        // https://github.com/ariya/phantomjs/issues/10592

        return getPage().getWebBrowser().getBrowserApplication()
                .contains("PhantomJS");
    }

    private Component buildMenu(){

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
        MenuItem settingsItem = settings.addItem("Ton nom ici", new ThemeResource("../valo-ortec/img/profile-pic.png"), null);
                              settingsItem.addItem("Edit Profile", null);
        settingsItem.addItem("Preferences", null);
        settingsItem.addSeparator();
        settingsItem.addItem("Sign Out", null);
        menu.addComponent(settings);

        menuItemsLayout.setPrimaryStyleName("valo-menuitems");
        menu.addComponent(menuItemsLayout);

        // add items
        Label label;
        for(final fr.ortec.dsi.pointage.presentation.ihm.ui.MenuItem menuItem : appMenu.getMenuitems()){

            if(menuItem.isHeader()) {
                label = new Label(menuItem.getLabel(), ContentMode.HTML);
                label.setPrimaryStyleName(ValoTheme.MENU_SUBTITLE);
                label.addStyleName(ValoTheme.LABEL_H4);
                label.setSizeUndefined();
                menuItemsLayout.addComponent(label);
            }else {
                menuItems.put(menuItem.getName(), menuItem.getLabel());
                Button b = new Button(menuItem.getLabel(), new Button.ClickListener() {
                    @Override
                    public void buttonClick(Button.ClickEvent event) {
                        navigator.navigateTo(menuItem.getName());
                    }
                });
                b.setCaptionAsHtml(true);
                b.setPrimaryStyleName(ValoTheme.MENU_ITEM);
                b.setIcon(testIcon.get());
                menuItemsLayout.addComponent(b);
            }
        }
        return menu;
    }

    private Component createThemeSelect(){
        // Keep theme select the same size as in the current screenshots
        double width = 96;
        @SuppressWarnings("deprecation") WebBrowser browser = VaadinSession.getCurrent().getBrowser();
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

    private void buildLoginView(boolean exit){

        if(exit){
            // vide l'interface
            loginRoot.removeAllComponents();
        }

        helpManager.closeAll();
        addStyleName("facebook");

        loginLayout = new VerticalLayout();
        loginLayout.setHeight(100.0f, Unit.PERCENTAGE);

        loginRoot.addComponent(loginLayout);

        final CssLayout loginPanel = new CssLayout();
        loginPanel.addStyleName(ValoTheme.PANEL_WELL);
        loginPanel.setWidth(300.0f, Unit.PIXELS);
        //loginPanel.setHeight(500.0f, Unit.PIXELS);

        HorizontalLayout labels = new HorizontalLayout();
        labels.setWidth("100%");
        labels.setMargin(true);

        FileResource resource = new FileResource(new File(basepath + "/images/ORTEC_spirale.png"));
        Image image = new Image("", resource);
        image.setWidth(240.00f, Unit.PIXELS);
        labels.addComponent(image);
        labels.setComponentAlignment(image, Alignment.MIDDLE_CENTER);
        loginPanel.addComponent(labels);

        HorizontalLayout userNameHL = new HorizontalLayout();
        userNameHL.setWidth(100.0f, Unit.PERCENTAGE);
        userNameHL.setMargin(true);
        final TextField username = new TextField("Login");
        username.focus();
        userNameHL.addComponent(username);
        userNameHL.setComponentAlignment(username, Alignment.MIDDLE_CENTER);
        loginPanel.addComponent(userNameHL);

        HorizontalLayout passwordHL = new HorizontalLayout();
        passwordHL.setMargin(true);
        passwordHL.setWidth(100.0f, Unit.PERCENTAGE);
        final PasswordField password = new PasswordField("Password");
        passwordHL.addComponent(password);
        passwordHL.setComponentAlignment(password, Alignment.MIDDLE_CENTER);
        loginPanel.addComponent(passwordHL);

        HorizontalLayout buttonHL = new HorizontalLayout();
        buttonHL.setMargin(true);
        buttonHL.setWidth(100.0f, Unit.PERCENTAGE);
        final Button signin = new Button("S'identifier");
        buttonHL.addComponent(signin);
        buttonHL.setComponentAlignment(signin, Alignment.MIDDLE_CENTER);
        loginPanel.addComponent(buttonHL);

        final ShortcutListener enter = new ShortcutListener("S'identifier", ShortcutAction.KeyCode.ENTER, null) {
            @Override
            public void handleAction(Object sender, Object target) {
                signin.click();
            }
        };

        signin.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                String current_username = username.getValue();
                String current_password = password.getValue();

                if (current_username != null && !"".equals(current_username) && current_password != null && !"".equals(current_password)){
                    try{
                        // TODO Authenticate collaborateur = collaborateurDao.getCollaborateurByLogin(username.getValue());
                    }catch(Exception ex){
                        LOGGER.error("Error in DAO", ex);
                    }

                    if(!"".equals(current_username) && !"".equals(current_password)){
                        signin.removeShortcutListener(enter);
                        getSession().setAttribute("username", current_username);
                        getSession().setAttribute("password", current_password);
                        buildMainView();
                    }else{
                        LOGGER.warn("Error dans la saisie des identifiants");
                    }
                } else {
                    if (loginPanel.getComponentCount() > 2) {
                        // Remove the previous error message
                        loginPanel.removeComponent(loginPanel.getComponent(2));
                    }
                    // Add new error message
                    Label error = new Label("Mauvais nom d'utilisateur ou mot de passe. <span>Hint: try empty values</span>", ContentMode.HTML);
                    error.addStyleName("error");
                    error.setSizeUndefined();
                    // Add animation
                    error.addStyleName("v-animate-reveal");
                    loginPanel.addComponent(error);
                    username.focus();
                }
            }
        });

        signin.addShortcutListener(enter);
        loginLayout.addComponent(loginPanel);
        loginLayout.setComponentAlignment(loginPanel, Alignment.MIDDLE_CENTER);
    }
}
