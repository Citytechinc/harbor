package com.icfolson.aem.harbor.core.components.content.accordion.v1;

import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.widgets.TextField;
import com.icfolson.aem.harbor.api.components.content.accordion.Accordion;
import com.icfolson.aem.harbor.api.components.content.accordion.AccordionItem;
import com.icfolson.aem.harbor.api.components.mixins.classifiable.Classification;
import com.icfolson.aem.harbor.core.components.mixins.classifiable.TagBasedClassification;
import com.icfolson.aem.library.core.components.AbstractComponent;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Model;

@Model(adaptables = Resource.class, adapters = AccordionItem.class, resourceType = DefaultAccordionItem.RESOURCE_TYPE)
public class DefaultAccordionItem extends AbstractComponent implements AccordionItem {

    public static final String RESOURCE_TYPE = "harbor/components/content/accordion/v1/accordionitem";

    private Integer index;
    private Accordion accordion;

    @DialogField(fieldLabel = "Title", fieldDescription = "The title of the accordion item.")
    @TextField
    public String getTitle() {
        return get("title", getDefaultTitle());
    }

    @Override
    public Accordion getAccordion() {
        if (accordion == null) {
            accordion = getResource().getParent().adaptTo(Accordion.class);
        }

        return accordion;
    }

    @Override
    public int getItemIndex() {
        if (index == null) {
            index = getIndex();
        }

        return index;
    }

    @Override
    public String getType() {
        return getResource().getResourceType();
    }

    protected String getDefaultTitle() {
        return "Accordion Item " + this.getItemIndex();
    }

    @Override
    public Classification getClassification() {
        return getResource().adaptTo(TagBasedClassification.class);
    }
}