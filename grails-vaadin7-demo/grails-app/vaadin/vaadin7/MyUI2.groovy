package vaadin7

import com.vaadin.ui.UI
import com.vaadin.server.VaadinRequest
import com.vaadin.ui.Label
import com.vaadin.ui.VerticalLayout

/**
 *
 *
 * @author Ondrej Kvasnovsky
 */
class MyUI2 extends UI {

    @Override
    protected void init(VaadinRequest request) {
        VerticalLayout layout = new VerticalLayout()
        layout.addComponent(new Label("Second awesome UI! God that's smashing... let's roll the joint! :-D"))
        setContent(layout)
    }
}
