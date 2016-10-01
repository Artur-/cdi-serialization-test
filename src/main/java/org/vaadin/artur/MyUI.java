package org.vaadin.artur;

import javax.inject.Inject;

import com.vaadin.annotations.Theme;
import com.vaadin.cdi.CDIUI;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@CDIUI("")
@Theme("valo")
public class MyUI extends UI {

    @Inject
    private SessionScopedBean sessionScopedBean;

    @Inject
    private UIScopedBean uiScopedBean;

    private VerticalLayout layout;

    private TextField sessionField;
    private TextField uiField;

    @Override
    protected void init(VaadinRequest vaadinRequest) {
        layout = new VerticalLayout();

        sessionField = new TextField();
        sessionField.setCaption("SessionScoped: ");
        sessionField.addValueChangeListener(e -> {
            sessionScopedBean.setString(sessionField.getValue());
        });

        uiField = new TextField();
        uiField.setCaption("uiScoped: ");
        uiField.addValueChangeListener(e -> {
            uiScopedBean.setString(uiField.getValue());
        });

        updateFromBean();

        Button button = new Button("Update from beans");
        button.addClickListener(e -> {
            updateFromBean();
        });

        layout.addComponents(sessionField, uiField, button);
        layout.setMargin(true);
        layout.setSpacing(true);

        setContent(layout);
    }

    private void updateFromBean() {
        sessionField.setValue(sessionScopedBean.getString());
        uiField.setValue(uiScopedBean.getString());
    }

}
