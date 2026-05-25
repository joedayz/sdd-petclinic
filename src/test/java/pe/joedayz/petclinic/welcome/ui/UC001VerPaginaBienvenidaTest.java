package pe.joedayz.petclinic.welcome.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.vaadin.browserless.quarkus.QuarkusBrowserlessTest;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.html.Anchor;
import io.quarkus.test.junit.QuarkusTest;
import java.util.List;
import org.junit.jupiter.api.Test;
import pe.joedayz.petclinic.UseCase;

/**
 * UC-001: Ver página de bienvenida.
 */
@QuarkusTest
class UC001VerPaginaBienvenidaTest extends QuarkusBrowserlessTest {

    @Test
    @UseCase(id = "UC-001", businessRules = "BR-001")
    void visitanteNavegaARaizYVePaginaDeBienvenida() {
        WelcomeView view = navigate(WelcomeView.class);

        assertNotNull(view);
        assertEquals("",
                UI.getCurrent().getInternals().getActiveViewLocation().getPath());
        assertEquals("Bienvenido", test(view.title).getText());
    }

    @Test
    @UseCase(id = "UC-001")
    void paginaMuestraImagenDecorativa() {
        WelcomeView view = navigate(WelcomeView.class);

        assertEquals("images/pets.svg", view.welcomeImage.getSrc());
        assertTrue(view.welcomeImage.getAlt().orElse("").contains("Mascotas"),
                "la imagen decorativa debe tener texto alternativo descriptivo");
    }

    @Test
    @UseCase(id = "UC-001")
    void paginaMuestraLogoDeLaClinica() {
        WelcomeView view = navigate(WelcomeView.class);
        MainLayout layout = (MainLayout) view.getParent().orElseThrow();

        assertEquals("images/petclinic-logo.svg", layout.logo.getSrc());
        assertTrue(layout.logo.getAlt().orElse("").contains("PetClinic"),
                "el logo debe tener texto alternativo PetClinic");
    }

    @Test
    @UseCase(id = "UC-001")
    void barraDeNavegacionMuestraEnlacesRequeridos() {
        WelcomeView view = navigate(WelcomeView.class);
        MainLayout layout = (MainLayout) view.getParent().orElseThrow();

        List<String> labels = List.of(
                layout.homeLink.getText(),
                layout.findOwnersLink.getText(),
                layout.vetsLink.getText(),
                layout.errorLink.getText());

        assertEquals(List.of("Inicio", "Buscar dueños", "Veterinarios", "Error"), labels);
    }

    @Test
    @UseCase(id = "UC-001")
    void enlacesDeNavegacionApuntanARutasEsperadas() {
        WelcomeView view = navigate(WelcomeView.class);
        MainLayout layout = (MainLayout) view.getParent().orElseThrow();

        assertEquals("/", layout.homeLink.getHref());
        assertEquals("owners/find", layout.findOwnersLink.getHref());
        assertEquals("vets", layout.vetsLink.getHref());
        assertEquals("oups", layout.errorLink.getHref());

        long anchorCount = $(Anchor.class).all().size();
        assertTrue(anchorCount >= 4,
                "deben verse al menos los enlaces de la barra de navegación principal");
    }
}
