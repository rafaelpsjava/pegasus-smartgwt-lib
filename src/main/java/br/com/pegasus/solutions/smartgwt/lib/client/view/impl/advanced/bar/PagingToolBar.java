/*
 * Copyright 2015 Pegasus Solutions
 * @author Rafael Peres dos Santos
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at http://www.apache.org/licenses/LICENSE-2.0 Unless required by
 * applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS
 * OF ANY KIND, either express or implied. See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * ================================================================================
 *
 * Direitos autorais 2015 Pegasus Solutions
 * @author Rafael Peres dos Santos
 * 
 * Licenciado sob a Licença Apache, Versão 2.0 ("LICENÇA"); você não pode usar
 * esse arquivo exceto em conformidade com a esta LICENÇA. Você pode obter uma
 * cópia desta LICENÇA em http://www.apache.org/licenses/LICENSE-2.0 A menos que
 * haja exigência legal ou acordo por escrito, a distribuição de software sob
 * esta LICENÇA se dará “COMO ESTÁ”, SEM GARANTIAS OU CONDIÇÕES DE QUALQUER
 * TIPO, sejam expressas ou tácitas. Veja a LICENÇA para a redação específica a
 * reger permissões e limitações sob esta LICENÇA.
 *
 */
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar;

import java.util.ArrayList;
import java.util.List;

import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.AfterFetchIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.InfoForm;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.KeyDownEvent;
import com.smartgwt.client.widgets.form.fields.events.KeyDownHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripSpacer;

/**
 * Description : the find is paginated limited to 9.999.999.999 rows the basic
 * alghorithm use the quantity of total rows divided by the quantity of the
 * pages wich determine the page size,..
 * 
 */

public class PagingToolBar extends ToolStrip {
	public int pageSize = 500;
	private static final Integer ZERO = new Integer(0);
	private static final String ENTER_KEY_NAME = "Enter";

	private ToolStripButton firstPageButton;
	private ToolStripButton lastPageButton;
	private ToolStripButton nextPageButton;
	private ToolStripButton previusPageButton;
	private ToolStripButton refreshButton;
	private ListGrid listGrid;
	private InfoForm pageInfoForm;
	private InfoForm showingInfoForm;

	private String[] rowsPerPageTemplate = { "100", "200", "300", "400", "500" };
	private int rowsPerPage;
	private Integer currentPage = 0;
	private Integer totalPages = 0;
	private Integer totalRows = 0;

	private DynamicForm pageSizeForm;
	private SelectItem rowPerPageSelectItem;
	private boolean showPageInfoForm;
	private boolean showShowingInfoForm;
	private Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);;
	private AfterFetchIF afterFetchActionIF;

	public PagingToolBar(ListGrid listGrid, boolean showPageInfoForm, boolean showShowingInfoForm) {
		this(listGrid, null, null, showPageInfoForm, showShowingInfoForm);
	}

	public PagingToolBar(ListGrid listGrid, int pageSize, boolean showPageInfoForm, boolean showShowingInfoForm) {
		this(listGrid, pageSize, null, showPageInfoForm, showShowingInfoForm);
	}

	public PagingToolBar(ListGrid listGrid, String[] rowsPerPageTemplate, boolean showPageInfoForm, boolean showShowingInfoForm) {
		this(listGrid, null, rowsPerPageTemplate, showPageInfoForm, showShowingInfoForm);
	}

	public PagingToolBar(ListGrid listGrid, Integer pageSize, String[] rowsPerPageTemplate, boolean showPageInfoForm, boolean showShowingInfoForm) {
		setWidth100();
		setTop(0);
		setMargin(2);
		addSpacer(new ToolStripSpacer(4));

		if (pageSize != null) {
			setPageSize(pageSize);
		}

		this.listGrid = listGrid;
		if (rowsPerPageTemplate != null) {
			this.rowsPerPageTemplate = rowsPerPageTemplate;
		}
		this.rowsPerPage = Integer.parseInt(this.rowsPerPageTemplate[this.rowsPerPageTemplate.length - 1]);
		this.showPageInfoForm = showPageInfoForm;
		this.showShowingInfoForm = showShowingInfoForm;

		buildFirstPage();
		addMember(firstPageButton);
		buildPreviusPageButton();
		addMember(previusPageButton);

		if (showPageInfoForm) {
			addSeparator();
			buildPageInfoForm();
			addMember(pageInfoForm.getForm());
		}

		addSeparator();
		buildNextPageButton();
		addMember(nextPageButton);
		buildLastPage();
		addMember(lastPageButton);

		addSeparator();
		buildRowPerPageSelectItem();

		this.pageSizeForm = new DynamicForm();
		pageSizeForm.setWidth(85);
		pageSizeForm.setFields(rowPerPageSelectItem);
		addMember(pageSizeForm);

		addSeparator();
		buildRefreshButton();
		addMember(refreshButton);

		addFill();
		if (showShowingInfoForm) {
			this.showingInfoForm = new InfoForm(dc.get("showing"), dc.get("of"), true, true);
			addMember(showingInfoForm.getForm());
		}

		addButtonEvents();
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
		if (pageSize > 100) {
			List<String> rowsPerPageTemplateLis = new ArrayList<String>();
			for (int i = 100; i <= pageSize; i += 100) {
				rowsPerPageTemplateLis.add("" + i);
			}
			this.rowsPerPageTemplate = rowsPerPageTemplateLis.toArray(new String[] {});
		} else {
			rowsPerPageTemplate = new String[] { "100" };
		}
		this.rowsPerPage = Integer.parseInt(this.rowsPerPageTemplate[this.rowsPerPageTemplate.length - 1]);
	}

	private void buildPageInfoForm() {
		this.pageInfoForm = new InfoForm(dc.get("page"), dc.get("of"), false, true);
		pageInfoForm.enableNumberFilter(true, false);

		// DON'T USE addKeyPressHandler if you're using setKeyPressFilter -
		// because keyPressFilter will be discarded
		pageInfoForm.getFirstField().addKeyDownHandler(new KeyDownHandler() {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if (ENTER_KEY_NAME.equals(event.getKeyName())) {
					if (pageInfoForm.getFirstFieldValue() != null && !"".equals(pageInfoForm.getFirstFieldValue())) {
						try {
							String input = pageInfoForm.getFirstField().getValueAsString();
							if (input.length() > totalPages.toString().length()) {
								currentPage = totalPages;
							} else {
								currentPage = Integer.parseInt(input);
								if (currentPage > totalPages) {
									currentPage = totalPages;
								}
							}
							pageInfoForm.setFirstFieldText(currentPage.toString());

							if (currentPage > totalPages || currentPage == 0) {
								int startRow = currentPage * rowsPerPage;
								fetchData(startRow, startRow + rowsPerPage);
							} else if (currentPage > 0) {
								int startRow = (currentPage - 1) * rowsPerPage;
								fetchData(startRow, startRow + rowsPerPage);
							}
						} catch (Exception ignored) {
						}
					}
				}
			}
		});
	}

	private void buildRowPerPageSelectItem() {
		this.rowPerPageSelectItem = new SelectItem();
		rowPerPageSelectItem.setWidth(80);
		rowPerPageSelectItem.setTitle("");
		rowPerPageSelectItem.setShowTitle(false);
		rowPerPageSelectItem.setValueMap(this.rowsPerPageTemplate);
		rowPerPageSelectItem.setDefaultValue(this.rowsPerPageTemplate[this.rowsPerPageTemplate.length - 1]);
		rowPerPageSelectItem.addChangeHandler(new ChangeHandler() {
			public void onChange(ChangeEvent event) {
				rowsPerPage = Integer.parseInt((String) event.getValue());
				totalPages = totalRows > 0 && totalRows <= rowsPerPage ? 1 : (totalRows % rowsPerPage != 0 ? ((totalRows / rowsPerPage) + 1)
						: (totalRows / rowsPerPage));

				if (currentPage > totalPages) {
					if (totalPages > 0) {
						currentPage = totalPages - 1;
					} else {
						currentPage = 0;
					}
				}
				if (totalPages == 1) {
					currentPage = 0;
				}

				listGrid.clearCriteria();

				int startRow = currentPage * rowsPerPage;
				fetchData(startRow, startRow + rowsPerPage);
			}
		});
	}

	public synchronized void fetchForDetail(Criteria criteria) {
		listGrid.invalidateCache();
		DSRequest dsRequest = new DSRequest();
		dsRequest.setOperationType(DSOperationType.FETCH);
		dsRequest.setStartRow(0);
		dsRequest.setEndRow(pageSize);

		enableButtonsBasedOnConstraints();
		listGrid.fetchData(criteria, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				totalRows = response.getAttributeAsInt("TOTAL_ROWS");
				if (totalRows == null) {
					totalRows = 0;
				}

				updateInfo(totalRows);
			}
		}, dsRequest);
	}

	public synchronized void fetchForDetail(Criteria criteria, final AfterFetchIF afterFetchIF) {
		listGrid.invalidateCache();
		DSRequest dsRequest = new DSRequest();
		dsRequest.setOperationType(DSOperationType.FETCH);
		dsRequest.setStartRow(0);
		dsRequest.setEndRow(pageSize);

		enableButtonsBasedOnConstraints();
		listGrid.fetchData(criteria, new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				totalRows = response.getAttributeAsInt("TOTAL_ROWS");
				if (totalRows == null) {
					totalRows = 0;
				}

				updateInfo(totalRows);
				if (afterFetchIF != null) {
					afterFetchIF.execute();
				}
			}
		}, dsRequest);
	}

	private synchronized void fetchData(final int startRow, final int endRow) {
		listGrid.invalidateCache();

		DSRequest dsRequest = new DSRequest();
		dsRequest.setOperationType(DSOperationType.FETCH);
		dsRequest.setStartRow(startRow);
		dsRequest.setEndRow(endRow);

		enableButtonsBasedOnConstraints();
		listGrid.fetchData(listGrid.getFilterEditorCriteria(), new DSCallback() {
			@Override
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				totalRows = response.getAttributeAsInt("TOTAL_ROWS");

				if (totalRows == null) {
					totalRows = 0;
				}
				if (totalRows != null) {
					totalPages = totalRows > 0 && totalRows <= rowsPerPage ? 1 : (totalRows % rowsPerPage != 0 ? ((totalRows / rowsPerPage) + 1)
							: (totalRows / rowsPerPage));
					if (totalPages > 0) {
						if (startRow == 0) {
							currentPage = 0;
						} else {
							currentPage = startRow / rowsPerPage;
						}
					} else {
						currentPage = 0;
					}

					if (showPageInfoForm) {
						pageInfoForm.setFirstFieldText("" + (totalPages > 0 ? currentPage + 1 : 0));
						pageInfoForm.setSecondText("" + totalPages);
					}
				} else {
					if (showPageInfoForm) {
						pageInfoForm.setFirstFieldText("0");
						pageInfoForm.setSecondText("0");
					}
				}

				if (totalRows == 0) {
					if (showShowingInfoForm) {
						showingInfoForm.setFirstFieldText("0");
						showingInfoForm.setSecondText(totalRows.toString());
					}
				} else {
					if (showShowingInfoForm) {
						showingInfoForm.setFirstFieldText("" + (startRow + 1) + " - " + endRow);
						showingInfoForm.setSecondText(totalRows.toString());
					}
				}

				enableButtonsBasedOnConstraints();
				listGrid.selectSingleRecord(-1);

				if (afterFetchActionIF != null) {
					afterFetchActionIF.execute();
				}
			}
		}, dsRequest);
	}

	public void fetchData() {
		fetchData(currentPage = 0, rowsPerPage);
	}

	public Integer getRowsPerPage() {
		return rowsPerPage;
	}

	public InfoForm getPageInfoForm() {
		return pageInfoForm;
	}

	private void addButtonEvents() {
		if (listGrid == null) {
			MessageUtil.showError(dc.get("developer_error_list_grid_not_null"));
		} else {
			addFirstPageButtonHandlers();
			addNextPageButtonHandlers();
			addPreviusPageButtonHandlers();
			addLastPageButtonHandlers();
			addRefreshButtonHandlers();
		}
	}

	private void addRefreshButtonHandlers() {
		refreshButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listGrid.clearCriteria();

				fetchData(currentPage = 0, rowsPerPage);
			}
		});
	}

	private void addLastPageButtonHandlers() {
		lastPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listGrid.clearCriteria();

				int startRow = totalPages == 1 ? 0 : (totalPages * rowsPerPage) - rowsPerPage;
				fetchData(startRow, startRow + rowsPerPage);
			}
		});
	}

	private void addPreviusPageButtonHandlers() {
		previusPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				if (currentPage >= 1) {
					listGrid.clearCriteria();

					--currentPage;
					int startRow = currentPage * rowsPerPage;
					fetchData(startRow, startRow + rowsPerPage);
				}
			}
		});
	}

	private void addNextPageButtonHandlers() {
		nextPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listGrid.clearCriteria();

				++currentPage;
				int startRow = currentPage * rowsPerPage;
				fetchData(startRow, startRow + rowsPerPage);
			}
		});
	}

	private void addFirstPageButtonHandlers() {
		firstPageButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				listGrid.clearCriteria();

				fetchData(currentPage = 0, rowsPerPage);
			}
		});
	}

	private void enableButtonsBasedOnConstraints() {
		firstPageButton.setDisabled(totalPages == null || currentPage == null || currentPage.compareTo(ZERO) <= 0);
		previusPageButton.setDisabled(totalPages == null || currentPage == null || currentPage.compareTo(ZERO) <= 0);

		nextPageButton.setDisabled(totalPages == null && currentPage == null || totalPages.intValue() == (currentPage + 1));
		lastPageButton.setDisabled(totalPages == null && currentPage == null || totalPages.intValue() == (currentPage + 1));
	}

	private void buildPreviusPageButton() {
		this.previusPageButton = new ToolStripButton("", "grid/backPage.gif");
		previusPageButton.setTooltip(dc.get("back_page"));
		previusPageButton.disable();
	}

	private void buildNextPageButton() {
		this.nextPageButton = new ToolStripButton("", "grid/nextPage.gif");
		nextPageButton.setTooltip(dc.get("next_page"));
	}

	private void buildFirstPage() {
		this.firstPageButton = new ToolStripButton("", "grid/firstPage.gif");
		firstPageButton.setTooltip(dc.get("first_page"));
		firstPageButton.disable();
	}

	private void buildLastPage() {
		this.lastPageButton = new ToolStripButton("", "grid/lastPage.gif");
		lastPageButton.setTooltip(dc.get("last_page"));
	}

	private void buildRefreshButton() {
		this.refreshButton = new ToolStripButton("", "options/refresh.gif");
		refreshButton.setTooltip(dc.get("refresh"));
	}

	public void updateInfo(Integer totalRows) {
		this.totalRows = totalRows;

		if (totalRows != null) {
			totalPages = totalRows > 0 && totalRows <= rowsPerPage ? 1 : (totalRows % rowsPerPage != 0 ? ((totalRows / rowsPerPage) + 1)
					: (totalRows / rowsPerPage));
			if (totalPages > 0) {
				currentPage = 0;
			} else {
				currentPage = 0;
			}

			if (showPageInfoForm) {
				pageInfoForm.setFirstFieldText("" + (totalPages > 0 ? currentPage + 1 : 0));
				pageInfoForm.setSecondText("" + totalPages);
			}
		} else {
			if (showPageInfoForm) {
				pageInfoForm.setFirstFieldText("0");
				pageInfoForm.setSecondText("0");
			}
		}

		if (totalRows == 0) {
			if (showShowingInfoForm) {
				showingInfoForm.setFirstFieldText("0");
				showingInfoForm.setSecondText(totalRows.toString());
			}
		} else {
			if (showShowingInfoForm) {
				showingInfoForm.setFirstFieldText("" + 1 + " - " + pageSize);
				showingInfoForm.setSecondText(totalRows.toString());
			}
		}

		enableButtonsBasedOnConstraints();
		listGrid.selectSingleRecord(-1);
	}

	public void fetchData(AfterFetchIF afterFetchActionIF) {
		this.afterFetchActionIF = afterFetchActionIF;
		fetchData(currentPage = 0, rowsPerPage);
	}

}