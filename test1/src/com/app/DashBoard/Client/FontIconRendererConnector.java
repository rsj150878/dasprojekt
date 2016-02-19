package com.app.DashBoard.Client;

import com.google.web.bindery.event.shared.HandlerRegistration;
import com.vaadin.client.connectors.ClickableRendererConnector;
import com.vaadin.client.renderers.ClickableRenderer;
import com.vaadin.shared.ui.Connect;

import elemental.json.JsonObject;

/**
 * TODO write JAVADOC!!!
 * User: koziolek  
 */
@Connect(com.app.FontIconRenderer.class)
public class FontIconRendererConnector extends ClickableRendererConnector<String> {

	@Override
	public FontIconRenderer getRenderer() {
		return (FontIconRenderer) super.getRenderer();
	}

	@Override
	protected HandlerRegistration addClickHandler(
			ClickableRenderer.RendererClickHandler<JsonObject> handler) {
		return getRenderer().addClickHandler(handler);
	}
}