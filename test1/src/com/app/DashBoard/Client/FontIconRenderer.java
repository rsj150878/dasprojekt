package com.app.DashBoard.Client;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.ui.HTML;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.client.widget.grid.RendererCellReference;

/**
 * TODO write JAVADOC!!!
 * User: koziolek
 */
public class FontIconRenderer extends ClickableRenderer<String, HTML> {

	@Override
	public HTML createWidget() {
		HTML b = GWT.create(HTML.class);
		b.addClickHandler(this);
		b.setStylePrimaryName("v-nativelabel");
		return b;
	}

	@Override
	public void render(RendererCellReference cell, String text, HTML button) {
		button.setHTML(text);
	}
}
