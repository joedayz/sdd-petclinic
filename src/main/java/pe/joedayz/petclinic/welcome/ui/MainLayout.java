package pe.joedayz.petclinic.welcome.ui;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class MainLayout extends AppLayout {

    final Image logo = new Image("images/petclinic-logo.svg", "PetClinic");
    final Anchor homeLink = new Anchor("/", "Inicio");
    final Anchor findOwnersLink = new Anchor("owners/find", "Buscar dueños");
    final Anchor vetsLink = new Anchor("vets", "Veterinarios");
    final Anchor errorLink = new Anchor("oups", "Error");

    public MainLayout() {
        logo.setHeight("44px");

        HorizontalLayout nav = new HorizontalLayout(
                homeLink, findOwnersLink, vetsLink, errorLink);
        nav.setSpacing(true);
        nav.setAlignItems(Alignment.CENTER);

        HorizontalLayout header = new HorizontalLayout(logo, nav);
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);
        header.getStyle().set("padding", "0 var(--lumo-space-m)");

        addToNavbar(header);
    }
}