package pe.joedayz.petclinic.welcome.ui;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Bienvenido | PetClinic")
public class WelcomeView extends VerticalLayout {

    final H1 title = new H1("Bienvenido");
    final Paragraph welcomeMessage = new Paragraph(
            "Bienvenido a PetClinic. Use la barra de navegación para buscar dueños, ver veterinarios o registrar una mascota.");
    final Image welcomeImage = new Image("images/pets.svg", "Mascotas de PetClinic");

    public WelcomeView() {
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        welcomeImage.setMaxWidth("480px");

        add(title, welcomeMessage, welcomeImage);
    }
}
