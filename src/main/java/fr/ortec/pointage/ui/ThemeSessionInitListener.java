package fr.ortec.pointage.ui;

import com.vaadin.server.*;
import org.jsoup.nodes.Element;


/**
 * Created by jerome.millot on 23/02/2017.
 *
 */
public class ThemeSessionInitListener implements SessionInitListener {

    @Override
    public void sessionInit(SessionInitEvent sessionInitEvent) throws ServiceException {

        sessionInitEvent.getService().setSystemMessagesProvider(new SystemMessagesProvider(){

                @Override
                public SystemMessages getSystemMessages(final SystemMessagesInfo systemMessagesInfo){
                    CustomizedSystemMessages csm = new CustomizedSystemMessages();
                    csm.setSessionExpiredNotificationEnabled(false);
                    return csm;
                }
        });
        sessionInitEvent.getSession().addBootstrapListener(new BootstrapListener() {
            @Override
            public void modifyBootstrapFragment(BootstrapFragmentResponse bootstrapFragmentResponse) {
                // TODO
            }

            @Override
            public void modifyBootstrapPage(BootstrapPageResponse bootstrapPageResponse) {
                final Element head = bootstrapPageResponse.getDocument().head();
                head.appendElement("meta").attr("name", "viewport").attr("content", "width=device-width, initial-scale=1");
                head.appendElement("meta").attr("name", "apple-mobile-web-app-capable").attr("content", "yes");
                head.appendElement("meta").attr("name", "apple-mobile-web-app-status-bar-style").attr("content", "black");
            }
        });
    }
}
