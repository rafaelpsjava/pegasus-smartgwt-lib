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
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.field;

import br.com.pegasus.solutions.smartgwt.lib.client.AbstractApplication;
import br.com.pegasus.solutions.smartgwt.lib.client.menu.AbstractFactoryScreenTreeNode;
import br.com.pegasus.solutions.smartgwt.lib.client.menu.ScreenTreeNode;
import br.com.pegasus.solutions.smartgwt.lib.client.rpc.impl.GenericGwtRpcDataSource;
import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.SelectedItemListGridIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.PopUp;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.PagingToolBar;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.types.CriteriaPolicy;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DoubleClickEvent;
import com.smartgwt.client.widgets.events.DoubleClickHandler;
import com.smartgwt.client.widgets.form.fields.PickerIcon;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.FormItemClickHandler;
import com.smartgwt.client.widgets.form.fields.events.FormItemIconClickEvent;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.VLayout;

public class IdsTextItem extends TextItem {
	private Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
	private static final int PAGE_SIZE = 2000;

	private String idFieldName;
	private String idFieldValue;
	private String descriptionFieldName;
	private String descriptionFieldValue;
	private ListGrid listGrid;

	private SelectedItemListGridIF selectedEvent;
	private boolean doubleClickEnabledOnListGrid = true;
	private PagingToolBar pagingToolBar;
	private PickerIcon clearPicker;
	private PickerIcon searchPicker;
	private PickerIcon goToPicker;
	private PopUp popUp;
	private int width = 670;
	private int height = 320;

	private static AbstractApplication app;

	public static void setApp(AbstractApplication app) {
		IdsTextItem.app = app;
	}

	public IdsTextItem() {
		this(null, null, 670, 320, null);
	}

	public IdsTextItem(String name, String title, String nodeId) {
		this(name, title, 670, 320, nodeId);
	}

	public IdsTextItem(String name, String title, boolean disabled, String nodeId) {
		this(name, title, 670, 320, nodeId);
		setDisabled(disabled);
	}

	public IdsTextItem(String name, int width, int height) {
		this(name, null, width, height, null);
	}

	public IdsTextItem(String name, String title, int width, int height, final String nodeId) {
		super(name, title);
		this.width = width;
		this.height = height;

		this.clearPicker = new PickerIcon(PickerIcon.CLEAR, new FormItemClickHandler() {
			public void onFormItemClick(FormItemIconClickEvent event) {
				getSelectedEvent().action(idFieldValue = null, descriptionFieldValue = null);
				getListGrid().selectSingleRecord(-1);
			}
		});
		this.searchPicker = new PickerIcon(PickerIcon.SEARCH, new FormItemClickHandler() {
			public void onFormItemClick(FormItemIconClickEvent event) {
				pagingToolBar.fetchData();
				popUp.getDialog().show();
			}
		});
		this.goToPicker = new PickerIcon(PickerIcon.COMBO_BOX, new FormItemClickHandler() {
			public void onFormItemClick(FormItemIconClickEvent event) {
				ScreenTreeNode screenTreeNode = AbstractFactoryScreenTreeNode.getScreenTreeNode("", nodeId);

				if (screenTreeNode != null && app != null) {
					app.openScreen(screenTreeNode, screenTreeNode.getFactory());
				}
			}
		});

		setNeverDisableIcons(true);

		setIcons(clearPicker, searchPicker, goToPicker);
	}

	public void setNeverDisableIcons(boolean search) {
		this.clearPicker.setNeverDisable(search);
		this.searchPicker.setNeverDisable(search);
		this.goToPicker.setNeverDisable(search);
	}

	/**
	 * add contend in dialog the dictionary must have a property gridName
	 * 
	 * @param dialogTitle
	 *            {@link String}
	 * @param listGrid
	 *            {@link ListGrid}
	 * @param id
	 *            {@link String}
	 * @param title
	 *            {@link String}
	 * @param selectedEvent
	 *            {@link SelectedItemListGridIF}
	 * @return void
	 */
	public void addContentDialog(@SuppressWarnings("rawtypes") GenericGwtRpcDataSource genericDS, String id, String title, ListGrid listGrid,
			SelectedItemListGridIF selectedEvent) {
		Dictionary dcRec = genericDS.getDC();
		this.listGrid = listGrid;
		this.idFieldName = dcRec.get(id);
		this.descriptionFieldName = dcRec.get(title);
		this.selectedEvent = selectedEvent;

		addDefaultListGridDefinitions(listGrid);
		this.pagingToolBar = new PagingToolBar(listGrid, PAGE_SIZE, true, false);

		if (this.doubleClickEnabledOnListGrid) {
			getListGrid().addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					setIdFieldValue();
					setDescriptionFieldValue();

					if (hasSelectedRecord()) {
						getSelectedEvent().action(idFieldValue, descriptionFieldValue);
						popUp.getDialog().hide();
					}
				}
			});
		}

		VLayout listGridLayout = new VLayout();
		listGridLayout.setWidth100();
		listGridLayout.setHeight("*");
		listGridLayout.setMargin(5);
		listGridLayout.addMember(listGrid);

		VLayout searchContent = new VLayout();
		searchContent.setTop(2);
		searchContent.setBottom(0);
		searchContent.setMargin(5);
		searchContent.setWidth100();
		searchContent.setHeight100();

		searchContent.addMember(listGridLayout);
		searchContent.addMember(this.pagingToolBar);

		this.popUp = new PopUp.PopUpBuilder().title(dcRec.get("gridName")).contentPopUp(searchContent).width(width).height(height).okAction(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setIdFieldValue();
				setDescriptionFieldValue();

				if (hasSelectedRecord()) {
					getSelectedEvent().action(idFieldValue, descriptionFieldValue);
					popUp.getDialog().hide();
				} else {
					MessageUtil.showError(dc.get("select_one_record"));
				}

			}
		}).buildInstance();

		this.pagingToolBar.fetchData();
	}

	/**
	 * add contend in dialog the dictionary must have a property gridName
	 * 
	 * @param dialogTitle
	 *            {@link String}
	 * @param listGrid
	 *            {@link ListGrid}
	 * @param id
	 *            {@link String}
	 * @param title
	 *            {@link String}
	 * @param selectedEvent
	 *            {@link SelectedItemListGridIF}
	 * @return void
	 */
	public void addContentDialog(@SuppressWarnings("rawtypes") GenericGwtRpcDataSource genericDS, String dialogTitleId, String id, String title,
			ListGrid listGrid, SelectedItemListGridIF selectedEvent) {
		Dictionary dcRec = genericDS.getDC();
		this.listGrid = listGrid;
		this.idFieldName = dcRec.get(id);
		this.descriptionFieldName = dcRec.get(title);
		this.selectedEvent = selectedEvent;

		addDefaultListGridDefinitions(listGrid);
		this.pagingToolBar = new PagingToolBar(listGrid, PAGE_SIZE, null, true, false);

		if (this.doubleClickEnabledOnListGrid) {
			getListGrid().addDoubleClickHandler(new DoubleClickHandler() {
				public void onDoubleClick(DoubleClickEvent event) {
					setIdFieldValue();
					setDescriptionFieldValue();

					if (hasSelectedRecord()) {
						getSelectedEvent().action(idFieldValue, descriptionFieldValue);
						popUp.getDialog().hide();
					}
				}
			});
		}

		VLayout listGridLayout = new VLayout();
		listGridLayout.setWidth100();
		listGridLayout.setHeight("*");
		listGridLayout.setMargin(5);
		listGridLayout.addMember(listGrid);

		VLayout searchContent = new VLayout();
		searchContent.setTop(2);
		searchContent.setBottom(0);
		searchContent.setMargin(5);
		searchContent.setWidth100();
		searchContent.setHeight100();

		searchContent.addMember(listGridLayout);
		searchContent.addMember(this.pagingToolBar);

		this.popUp = new PopUp.PopUpBuilder().title(dcRec.get(dialogTitleId)).contentPopUp(searchContent).width(width).height(height)
				.okAction(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						setIdFieldValue();
						setDescriptionFieldValue();

						if (hasSelectedRecord()) {
							getSelectedEvent().action(idFieldValue, descriptionFieldValue);
							popUp.getDialog().hide();
						} else {
							MessageUtil.showError(dc.get("select_one_record"));
						}

					}
				}).buildInstance();

		this.pagingToolBar.fetchData();
	}

	/**
	 * add default listgrid definitions
	 * 
	 * @param listGrid
	 *            {@link ListGrid}
	 * @return void
	 */
	private void addDefaultListGridDefinitions(ListGrid listGrid) {
		listGrid.setWidth100();
		listGrid.setTop(1);
		listGrid.setHeight("*");
		listGrid.setAutoFetchData(false);
		listGrid.setDataPageSize(PAGE_SIZE);
		listGrid.setDataFetchMode(FetchMode.PAGED);
		listGrid.setSelectionType(SelectionStyle.MULTIPLE);
		listGrid.setShowAllRecords(false);
		listGrid.setShowFilterEditor(true);
		listGrid.setEmptyMessage(this.dc.get("none_record_found"));

		DataSource dataSource = listGrid.getDataSource();
		dataSource.setCacheAllData(false);
		dataSource.setCriteriaPolicy(CriteriaPolicy.DROPONCHANGE);
	}

	private void setIdFieldValue() {
		this.idFieldValue = "";

		if (getListGrid().getSelectedRecords() != null) {
			ListGridRecord[] selectedRecords = getListGrid().getSelectedRecords();
			for (ListGridRecord selectedRecord : selectedRecords) {
				this.idFieldValue += selectedRecord.getAttribute(idFieldName) + ",";
			}
			this.idFieldValue = this.idFieldValue.substring(0, idFieldValue.lastIndexOf(","));
		} else {
			this.idFieldValue = "";
		}
	}

	private void setDescriptionFieldValue() {
		this.descriptionFieldValue = "";

		if (getListGrid().getSelectedRecord() != null) {
			ListGridRecord[] selectedRecords = getListGrid().getSelectedRecords();
			for (ListGridRecord selectedRecord : selectedRecords) {
				this.descriptionFieldValue += selectedRecord.getAttribute(descriptionFieldName) + ", ";
			}
			this.descriptionFieldValue = this.descriptionFieldValue.substring(0, descriptionFieldValue.lastIndexOf(", "));

		} else {
			this.descriptionFieldValue = "";
		}
	}

	public ListGrid getListGrid() {
		return this.listGrid;
	}

	public ListGridRecord getSelectedRecord() {
		return getListGrid().getSelectedRecord();
	}

	public SelectedItemListGridIF getSelectedEvent() {
		return this.selectedEvent;
	}

	private boolean hasSelectedRecord() {
		return getListGrid().getSelectedRecord() != null;
	}

	public void setDoubleClickEnabledOnListGrid(boolean doubleClickEnabledOnListGrid) {
		this.doubleClickEnabledOnListGrid = doubleClickEnabledOnListGrid;
	}

}