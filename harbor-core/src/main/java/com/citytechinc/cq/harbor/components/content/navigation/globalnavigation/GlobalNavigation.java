package com.citytechinc.cq.harbor.components.content.navigation.globalnavigation;


import com.citytechinc.cq.component.annotations.*;
import com.citytechinc.cq.component.annotations.editconfig.ActionConfig;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.cq.harbor.components.content.navigation.treenavigation.TreeNavigation;
import com.citytechinc.cq.library.components.annotations.AutoInstantiate;
import com.citytechinc.cq.library.content.node.ComponentNode;
import com.citytechinc.cq.library.content.request.ComponentRequest;
import org.apache.sling.api.resource.Resource;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component(value = "Global Navigation",
        actions = {"text: Global Navigation", "-", "edit", "-", "delete"},
        contentAdditionalProperties = {
                @ContentProperty(name="dependencies", value="harbor.components.content.globalnavigation")
        },
        actionConfigs = {
                @ActionConfig(xtype = "tbseparator"),
                @ActionConfig(text = "Add Navigation Column", handler = "function(){ Harbor.Components.GlobalNavigation.addNavigationElement(this) }"),
        },
        listeners = {
                @Listener(name = "afterinsert", value = "REFRESH_PAGE")
        },
        allowedParents = "*/parsys"
)
@AutoInstantiate( instanceName = "globalNavigation" )
public class GlobalNavigation extends AbstractGlobalNavigation {
    private List<NavigationElement> navigationElementList;

    public GlobalNavigation(ComponentRequest request) {
        super(request);

        navigationElementList = new ArrayList<NavigationElement>();
        //Add The child elements of our GlobalNav to the Nav Element list
        Iterator<Resource> navigationResourceIterator = request.getResource().listChildren();

        while (navigationResourceIterator.hasNext()) {
            this.navigationElementList.add(new NavigationElement(navigationResourceIterator.next().adaptTo(ComponentNode.class)));
        }
    }

    @DialogField(fieldLabel = "Auto Generate Navigation?",
            fieldDescription = "")
    @Selection(type=Selection.CHECKBOX, options = {
            @Option(text="", value = "true")
    })
    public Boolean getAutoGenerateNavigation(){
        return get("autoGenerateNavigation", "").equals("true");
    }

    public List<NavigationElement> getNavigationElementList(){
        return navigationElementList;
    }

    public boolean getIsAutoGenerated(){
        return getAutoGenerateNavigation();
    }

}
