package com.citytechinc.aem.harbor.core.content.page.lists.construction;

import java.util.List;

import com.citytechinc.aem.bedrock.api.page.PageDecorator;
import com.citytechinc.aem.bedrock.api.request.ComponentRequest;
import com.citytechinc.cq.component.annotations.DialogField;
import com.citytechinc.cq.component.annotations.Option;
import com.citytechinc.cq.component.annotations.widgets.Selection;
import com.citytechinc.aem.harbor.api.content.page.HomePage;
import com.citytechinc.aem.harbor.api.content.page.SectionLandingPage;
import com.citytechinc.aem.harbor.api.lists.construction.ListConstructionStrategy;
import com.citytechinc.aem.harbor.core.components.content.page.TrailPage;
import com.citytechinc.aem.harbor.core.content.page.impl.PagePredicates;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;

public class PageTrailListConstructionStrategy implements ListConstructionStrategy<TrailPage> {

	private final ComponentRequest request;

	private List<TrailPage> constructedList;

	private PageDecorator currentPage;

	@DialogField(name = "./rootPageType", fieldLabel = "Breadcrumb Root")
	@Selection(type = Selection.SELECT, options = { @Option(text = "Home Page", value = HomePage.RDF_TYPE),
		@Option(text = "Section Landing Page", value = SectionLandingPage.RDF_TYPE) })
	private Predicate<PageDecorator> rootPagePredicate;

	@DialogField(fieldLabel = "Include Current Page In Trail")
	@Selection(type = Selection.CHECKBOX, options = { @Option(text = "Yes", value = "true") })
	private Boolean includeCurrentPageInTrail;

	@DialogField(fieldLabel = "Include Root Page In Trail")
	@Selection(type = Selection.CHECKBOX, options = { @Option(text = "Yes", value = "true") })
	private Boolean includeRootPageInTrail;

	public PageTrailListConstructionStrategy(ComponentRequest request) {
		this.request = request;
	}

	@Override
	public List<TrailPage> construct() {

		if (constructedList == null) {

			List<TrailPage> listUnderConstruction = Lists.newArrayList();

			PageDecorator currentPageInTrail = getCurrentPage();

			if (isIncludeCurrentPageInTrail()) {

				if (getRootPagePredicate().apply(currentPageInTrail)) {
					listUnderConstruction.add(TrailPage.forRootCurrentPage(currentPageInTrail));
				} else {
					listUnderConstruction.add(TrailPage.forRootCurrentPage(currentPageInTrail));
				}

			}

			currentPageInTrail = currentPageInTrail.getParent();

			while (currentPageInTrail != null && !isPageRoot(currentPageInTrail)) {
				listUnderConstruction.add(TrailPage.forCurrentPage(currentPageInTrail));
				currentPageInTrail = currentPageInTrail.getParent();
			}

			if (currentPageInTrail != null && isIncludeRootPageInTrail()) {
				listUnderConstruction.add(TrailPage.forRootPage(currentPageInTrail));
			}

			constructedList = Lists.reverse(listUnderConstruction);

		}

		return constructedList;

	}

	protected PageDecorator getCurrentPage() {
		if (currentPage == null) {
			currentPage = request.getCurrentPage();
		}

		return currentPage;
	}

	protected Predicate<PageDecorator> getRootPagePredicate() {
		if (rootPagePredicate == null) {
			String rootPageType = request.getComponentNode().get("rootPageType", HomePage.RDF_TYPE);

			if (SectionLandingPage.RDF_TYPE.equals(rootPageType)) {
				rootPagePredicate = PagePredicates.SECTION_LANDING_PAGE_PREDICATE;
			} else {
				rootPagePredicate = PagePredicates.HOME_PAGE_PREDICATE;
			}
		}

		return rootPagePredicate;
	}

	protected Boolean isIncludeCurrentPageInTrail() {
		if (includeCurrentPageInTrail == null) {
			includeCurrentPageInTrail = request.getComponentNode().get("includeCurrentPageInTrail", false);
		}

		return includeCurrentPageInTrail;
	}

	protected Boolean isIncludeRootPageInTrail() {
		if (includeRootPageInTrail == null) {
			includeRootPageInTrail = request.getComponentNode().get("includeRootPageInTrail", false);
		}

		return includeRootPageInTrail;
	}

	protected Boolean isPageRoot(PageDecorator pageDecorator) {
		return getRootPagePredicate().apply(pageDecorator);
	}

}
