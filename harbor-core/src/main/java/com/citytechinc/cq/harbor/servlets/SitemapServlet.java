package com.citytechinc.cq.harbor.servlets;


import com.citytechinc.cq.harbor.components.content.navigation.constructionstrategy.SitemapConstructionStrategy;
import com.citytechinc.cq.harbor.components.content.tree.TreeNode;
import com.citytechinc.cq.harbor.components.content.tree.TreeNodeConstructionStrategy;
import com.citytechinc.cq.harbor.components.content.tree.TreeNodes;
import com.citytechinc.cq.harbor.services.SitemapConfigService;
import com.citytechinc.cq.harbor.util.SitemapXmlGenerator;
import com.citytechinc.cq.library.content.page.PageDecorator;
import com.citytechinc.cq.library.content.page.PageManagerDecorator;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;

import java.util.Set;

@SlingServlet(
    //methods = "GET",
    //selectors = "sitemap",
    //extensions = "xml",
    paths = "/bin/sitemap"
)
public class SitemapServlet extends SlingSafeMethodsServlet {

    @Reference
    private SitemapConfigService siteMapService;


    @Override
    protected void doGet(org.apache.sling.api.SlingHttpServletRequest request, org.apache.sling.api.SlingHttpServletResponse response) throws javax.servlet.ServletException, java.io.IOException {
        //Build Tree from the root

        Set<String> sitemapRootPaths = siteMapService.getSiteMapRootPaths();
        PageManagerDecorator pageManagerDecorator = request.getResourceResolver().adaptTo(PageManagerDecorator.class);
        StringBuffer xmlStringBuffer = new StringBuffer();
        for (String item : sitemapRootPaths){
            //attempt to grab a page decorator.
            PageDecorator rootPage = pageManagerDecorator.getPage(item);
            if(rootPage != null){
                //Construct a sitemap from the rootPage path
                xmlStringBuffer.append(SitemapXmlGenerator.getSitemapXMLFromRootPage(rootPage));
            }
        }
        //Level-order traversal of tree, rendering nodes as we go
        response.getWriter().write(xmlStringBuffer.toString());
    }
}
