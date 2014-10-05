package com.citytechinc.aem.harbor.api.lists.rendering;

/**
 * Represents an item in a list which can produce its own rendering
 */
public interface RenderableItem {

	public String render();

}
