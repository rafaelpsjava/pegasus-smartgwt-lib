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
package br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced;

import java.util.LinkedHashMap;
import java.util.List;

import br.com.pegasus.solutions.smartgwt.lib.client.rpc.api.GenericGwtRpcServiceAsync;
import br.com.pegasus.solutions.smartgwt.lib.client.rpc.impl.GenericGwtRpcDataSource;
import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.util.StringUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.AfterFetchIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.DetailTabIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.ViewIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.AfterChangeRecordIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.AfterSaveActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.BeforeSaveActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.ChangeFaceIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.ClearAditionDataActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.CustomRemoveActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.ExtraValidationActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.OverrideDefaultSaveActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.SyncronizeDataActionIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.OptionsToolBar;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.PagingToolBar;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.infra.ExporterData;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.infra.PreferenceRecord;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.PegasusDynamicForm;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.Criteria;
import com.smartgwt.client.data.DSCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.types.CriteriaPolicy;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.Offline;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.FormItem;
import com.smartgwt.client.widgets.form.fields.SelectItem;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.RecordDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionChangedHandler;
import com.smartgwt.client.widgets.grid.events.SelectionEvent;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class ViewImpl {
	private VLayout vLayout;
	private OptionsToolBar optionsToolBar;
	private PagingToolBar pagingToolBar;
	private Boolean isOnFaceOne = false;
	private Integer idxSelectedRecord = -1;
	private boolean autoFetchData = true;
	private boolean fetchDataOnLoad = true;
	private boolean dataWasFetched = false;
	private Boolean firstTimeChangingFace = true;
	private LinkedHashMap<String, Boolean> optionsToNotShow;
	private FormItem fieldToFocus;
	private FormItem[] primaryKeys;
	private DynamicForm cabMasterForm;
	private List<String> cabFieldsToBind;
	private List<String> fieldsToFilter;
	private List<DetailTabIF> detailsToLoad;
	private ViewIF view;
	@SuppressWarnings("rawtypes")
	private GenericGwtRpcServiceAsync serviceAsync;
	private ListGrid listGrid;
	private DynamicForm form;
	private Layout faceOne;
	private Layout faceTwo;
	private PopUp exporterPopUp;
	private String savePreferencesStateId;
	private String gridNameToShowOnPrintPreview;
	private boolean isMasterDetailForm = false;
	private boolean lastActionWasRefresh = false;
	private boolean showPagingToolbar = true;
	private String[] rowsPerPageTemplate;

	private Tab[] tabs;
	private TabSet masterDetailTabSet;
	private static final float DRAW_AHEAD_RATIO = 1.3F;
	private static final int FACE_ONE_MEMBER_NUMBER = 1;
	private static final int FACE_TWO_MEMBER_NUMBER = 2;
	private static int PK_COUNTER;
	private DataSource preferencesDS;
	private ListGrid pickListProperties;
	private SelectItem preferenceSelectItem;
	private Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
	private String exportFileName;

	@SuppressWarnings("rawtypes")
	private GenericGwtRpcDataSource dsListGrid;
	private AfterFetchIF afterFetchIF;
	private PegasusDynamicForm[] extraForms;

	private ViewImpl() {
		this.vLayout = new VLayout();
		this.vLayout.setWidth100();
		this.vLayout.setHeight100();
	}

	@SuppressWarnings("rawtypes")
	private void buildView() {
		this.optionsToolBar = getOptionsToolBar();
		this.vLayout.addMember(this.optionsToolBar);
		buildAfterFetchActionIF();

		if (this.faceOne == null) {
			MessageUtil.showError("Developer error, faceOne need return a Canvas.");
		} else {
			Layout faceOneWrapperLayout = buildContentLayout(this.faceOne);
			faceOneWrapperLayout.setVisible(false);
			this.vLayout.addMember(faceOneWrapperLayout);

			if (this.faceTwo == null) {
				this.isOnFaceOne = true;
				faceOneWrapperLayout.setVisible(true);
			} else {
				if (this.listGrid == null && this.optionsToNotShow == null) {
					faceOneWrapperLayout.setVisible(true);
					this.isOnFaceOne = true;

					MessageUtil.showError(dc.get("developer_error_options_toolbar_will_not_work_properly"));
				} else {
					addDefaultListGridDefinitions();
					addListGridCallBack();

					if (showPagingToolbar) {
						this.pagingToolBar = new PagingToolBar(this.listGrid, this.rowsPerPageTemplate, true, true);
					}

					if (listGrid.getDataSource() != null && listGrid.getDataSource() instanceof GenericGwtRpcDataSource) {
						this.dsListGrid = (GenericGwtRpcDataSource) listGrid.getDataSource();

						if (showPagingToolbar) {
							this.dsListGrid.setPagingToolbar(pagingToolBar);
						}
						if (savePreferencesStateId == null) {
							if (StringUtil.isEmpty(dsListGrid.getDicValue("id_gridId"))) {
								SC.say(dc.get("developer_error_not_enough_info_to_build_view_impl"));
								return;
							}
							setSavePreferencesStateId(dsListGrid.getDicValue("id_gridId"));
						}
						if (gridNameToShowOnPrintPreview == null) {
							if (StringUtil.isEmpty(dsListGrid.getDicValue("gridName"))) {
								SC.say(dc.get("developer_error_not_enough_info_to_build_view_impl"));
								return;
							}
							setGridNameToShowOnPrintPreview(dsListGrid.getDicValue("gridName"));
						}
						if (exportFileName == null) {
							if (StringUtil.isEmpty(dsListGrid.getDicValue("exportFileName"))) {
								SC.say(dc.get("developer_error_not_enough_info_to_build_view_impl"));
								return;
							}
							setExportFileName(dsListGrid.getDicValue("exportFileName"));
						}
					}

					VLayout pagingToolbarLayout = new VLayout();
					pagingToolbarLayout.setHeight100();
					pagingToolbarLayout.setWidth100();
					pagingToolbarLayout.addMember(this.faceTwo);
					if (showPagingToolbar) {
						pagingToolbarLayout.addMember(this.pagingToolBar);
					}

					buildFaceTwo(pagingToolbarLayout);
				}
				disableOptionsBasedOnCurrentFace();
			}
		}
	}

	public Layout getFaceOne() {
		return faceOne;
	}

	public VLayout getvLayout() {
		return vLayout;
	}

	private OptionsToolBar getOptionsToolBar() {
		if (optionsToolBar == null) {
			return new OptionsToolBar(this.optionsToNotShow);
		}

		return optionsToolBar;
	}

	public void setOptionsToolBar(OptionsToolBar optionsToolBar) {
		this.optionsToolBar = optionsToolBar;
	}

	private void buildFaceTwo(VLayout pagingToolbarLayout) {
		Layout faceTwoWrapperLayout = buildContentLayout(pagingToolbarLayout);

		if (faceTwoWrapperLayout != null) {
			faceTwoWrapperLayout.setVisible(true);
			this.vLayout.addMember(faceTwoWrapperLayout);

			if (this.optionsToolBar.getChangeFaceButton() != null) {
				addChangeCallBack();
			}
			if (this.optionsToolBar.getRefreshButton() != null) {
				addRefreshCallBack();
			}
			if (this.optionsToolBar.getCancelButton() != null && this.form != null) {
				addCancelCallBack();
			}
			if (this.optionsToolBar.getSaveButton() != null) {
				addSaveCallback();
			}
			if (this.optionsToolBar.getSaveGridPreferencesButton() != null) {
				this.preferencesDS = new DataSource();

				DataSourceTextField pkField = new DataSourceTextField("pk");
				pkField.setHidden(true);
				pkField.setPrimaryKey(true);
				DataSourceTextField preferenceField = new DataSourceTextField("name", "Name");
				DataSourceTextField stateField = new DataSourceTextField("viewState", "View State");

				this.preferencesDS.setFields(pkField, preferenceField, stateField);
				this.preferencesDS.setClientOnly(true);

				this.pickListProperties = new ListGrid();
				this.pickListProperties.setEmptyMessage(dc.get("none_preference_saved"));

				buildPreferenceSelectItem();
				DynamicForm formPreferences = new DynamicForm();
				formPreferences.setWidth(100);
				formPreferences.setFields(this.preferenceSelectItem);
				this.optionsToolBar.addMember(formPreferences);

				addSavePreferencesCallBack();
			}
			if (this.optionsToolBar.getAddButton() != null) {
				addAddCallBack();
			}
			if (this.optionsToolBar.getDeleteButton() != null) {
				this.optionsToolBar.getDeleteButton().setDisabled(true);
				addDeleteCallBack();
			}

			if (this.optionsToolBar.getPrintButton() != null) {
				if (this.gridNameToShowOnPrintPreview != null && !this.gridNameToShowOnPrintPreview.isEmpty()) {
					addPrintCallBack(faceTwoWrapperLayout);
				} else {
					MessageUtil.showError(dc.get("grid_print_preview_need_return_not_null"));
				}
			}

			if (this.optionsToolBar.getExporterButton() != null) {
				addExporterCallBack();
			}

			if (this.fetchDataOnLoad) {
				this.dataWasFetched = true;

				if (showPagingToolbar) {
					this.pagingToolBar.fetchData();
				}
			}
		}
	}

	public ToolStripButton getSaveButton() {
		return this.optionsToolBar.getSaveButton();
	}

	private void buildPreferenceSelectItem() {
		this.preferenceSelectItem = new SelectItem("name");
		this.preferenceSelectItem.setTitle(dc.get("preferences"));
		this.preferenceSelectItem.setPickListProperties(this.pickListProperties);
		this.preferenceSelectItem.setOptionDataSource(this.preferencesDS);
		this.preferenceSelectItem.addChangedHandler(new ChangedHandler() {
			public void onChanged(ChangedEvent event) {
				final String preferenceName = (String) preferenceSelectItem.getValue();
				Criteria criteria = new Criteria("name", preferenceName);

				preferencesDS.fetchData(criteria, new DSCallback() {
					public void execute(DSResponse response, Object rawData, DSRequest request) {
						Record[] data = response.getData();
						if (data.length != 0) {
							Offline.put(getLastSelectedPreference(), preferenceName);

							listGrid.setViewState(data[0].getAttribute("viewState"));
						}
					}
				});
			}
		});
	}

	private void addExporterCallBack() {
		this.optionsToolBar.getExporterButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (exporterPopUp == null) {
					VLayout contentPopUp = new VLayout();
					contentPopUp.setWidth100();
					contentPopUp.setHeight("*");
					contentPopUp.setMargin(5);
					contentPopUp.addMember(ExporterData.buildLayout(listGrid, exportFileName));

					exporterPopUp = new PopUp.PopUpBuilder().title(dc.get("export_data")).contentPopUp(contentPopUp).showBottomBar(false).buildInstance();
				}
				exporterPopUp.getDialog().show();
			}
		});
	}

	private void addDefaultListGridDefinitions() {
		this.listGrid.setWidth100();
		this.listGrid.setTop(1);
		this.listGrid.setHeight("*");

		this.listGrid.setAutoFetchData(this.autoFetchData);
		this.listGrid.setDataFetchMode(FetchMode.PAGED);

		this.listGrid.setCanMultiSort(true);
		this.listGrid.setShowAllRecords(false);
		this.listGrid.setShowFilterEditor(true);
		this.listGrid.setDataPageSize(500);
		this.listGrid.setDrawAheadRatio(DRAW_AHEAD_RATIO);
		this.listGrid.setEmptyMessage(dc.get("none_record_found"));

		DataSource dataSource = this.listGrid.getDataSource();
		dataSource.setCacheAllData(false);
		dataSource.setCriteriaPolicy(CriteriaPolicy.DROPONCHANGE);
	}

	private void addListGridCallBack() {
		if (this.optionsToolBar.getBackRowButton() != null) {
			addNavigatorCallBack();
		}
		this.listGrid.addSelectionChangedHandler(new SelectionChangedHandler() {
			public void onSelectionChanged(SelectionEvent evt) {
				defaultSelectionChangedHandlerAction(evt.getSelectedRecord());

				if (idxSelectedRecord >= 0 && !lastActionWasRefresh && evt.getSelectedRecord() != null) {
					if (isMasterDetailForm && cabFieldsToBind != null) {
						for (String field : cabFieldsToBind) {
							cabMasterForm.setValue(field, evt.getSelectedRecord().getAttributeAsString(field));
						}
					}

					if (detailsToLoad != null) {
						for (DetailTabIF detailTab : detailsToLoad) {
							detailTab.fetchDetail(getListGrid().getRecord(idxSelectedRecord).getAttributeAsString((String) fieldsToFilter.get(0)));
						}
					}
				}
			}
		});

		this.listGrid.addRecordDoubleClickHandler(new RecordDoubleClickHandler() {
			public void onRecordDoubleClick(final RecordDoubleClickEvent evt) {
				idxSelectedRecord = evt.getRecordNum();
				lastActionWasRefresh = false;
				getListGrid().selectSingleRecord(evt.getRecord());

				changeFace();
			}
		});
	}

	private void addDeleteCallBack() {
		this.optionsToolBar.getDeleteButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord[] selectedRecords = listGrid.getSelectedRecords();
				if (selectedRecords == null || selectedRecords.length == 0) {
					disableAddButton(false);
					disableRemoveButton(true);

					MessageUtil.showError(dc.get("select_one_or_more_record_to_be_delete"));
				} else {
					SC.ask(dc.get("confirmation_delete_records"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doRemoveAction();
							}
						}
					});
				}
			}
		});
	}

	@SuppressWarnings("unchecked")
	private void doRemoveAction() {
		if (view instanceof CustomRemoveActionIF) {
			((CustomRemoveActionIF) view).customRemove();

			if (view instanceof ClearAditionDataActionIF) {
				((ClearAditionDataActionIF) view).doClearAditionData();
			}
		} else {
			serviceAsync.remove(view.getDataToRemove(), new AsyncCallback<Void>() {
				public void onFailure(Throwable e) {
					lastActionWasRefresh = false;

					MessageUtil.showError(e.getMessage());
				}

				public void onSuccess(Void result) {
					onRemoveSuccess();
				}
			});
		}
	}

	private void onRemoveSuccess() {
		lastActionWasRefresh = false;
		getListGrid().removeSelectedData(new DSCallback() {
			public void execute(DSResponse response, Object rawData, DSRequest request) {
				if (response.getStatus() == 0) {
					positionInCorrectRecordAfterRemove();

					listGrid.selectSingleRecord(-1);
					if (form != null) {
						form.clearValues();
					}

					if (view instanceof ClearAditionDataActionIF) {
						((ClearAditionDataActionIF) view).doClearAditionData();
					}
					if (isMasterDetailForm && getListGrid().getRecords() != null && getListGrid().getRecords().length == 0) {
						setDisabledTabs(true);
						cabMasterForm.clearValues();

						for (String field : cabFieldsToBind) {
							cabMasterForm.setValue(field, "");
						}
						cabMasterForm.redraw();
					}
					disableRemoveButton(true);
				}
			}
		}, null);
	}

	private void setDisabledTabs(boolean flag) {
		if (flag) {
			if (masterDetailTabSet != null) {
				this.masterDetailTabSet.selectTab(0);
			}
		}
		if (tabs != null) {
			for (int i = 1; i < this.tabs.length; i++) {
				this.tabs[i].setDisabled(flag);
			}
		}
	}

	private void addAddCallBack() {
		this.optionsToolBar.getAddButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								addAction();
							}
						}
					});

				} else {
					addAction();
				}
			}
		});
	}

	private void addAction() {
		lastActionWasRefresh = false;

		if (form != null) {
			form.clearValues();
		}
		if (extraForms != null) {
			for (DynamicForm extraForm : extraForms) {
				extraForm.clearValues();
			}
		}

		if (view instanceof ClearAditionDataActionIF) {
			((ClearAditionDataActionIF) view).doClearAditionData();
		}

		listGrid.selectSingleRecord(-1);
		if (isMasterDetailForm) {
			if (cabMasterForm != null) {
				cabMasterForm.clearValues();
			}

			setDisabledTabs(true);
		}

		showNavigatorButtonsIfNecessary();
		disableAddButton(true);
		disableRemoveButton(true);

		if (!isOnFaceOne) {
			changeFace();
		} else {
			focusInFieldToFocus();
		}
	}

	private void addChangeCallBack() {
		this.optionsToolBar.getChangeFaceButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lastActionWasRefresh = false;
				changeFace();
			}
		});
	}

	private void addPrintCallBack(final Layout faceTwoWrapperLayout) {
		this.optionsToolBar.getPrintButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lastActionWasRefresh = false;
				Canvas.showPrintPreview(faceTwoWrapperLayout);
			}
		});
	}

	private void addSaveCallback() {
		this.optionsToolBar.getSaveButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				boolean isValid = true;
				if (view instanceof ExtraValidationActionIF) {
					isValid = ((ExtraValidationActionIF) view).doExtraValidation();
				}
				if (view instanceof BeforeSaveActionIF) {
					((BeforeSaveActionIF) view).doBeforeSave();
				}

				if (isValid) {
					boolean isChangedValuesInExtraForm = false;
					boolean hasErrorInExtraForm = false;
					if (extraForms != null) {
						for (DynamicForm extraForm : extraForms) {
							if (extraForm.getChangedValues() != null && extraForm.getChangedValues().size() > 0) {
								isChangedValuesInExtraForm = true;
								break;
							}
							if (extraForm.hasErrors()) {
								hasErrorInExtraForm = true;
								break;
							}
						}
					}
					if (view instanceof OverrideDefaultSaveActionIF) {
						if (form != null && !form.hasErrors()) {
							((OverrideDefaultSaveActionIF) view).saveAction();

							lastActionWasRefresh = false;
							optionsToolBar.getSaveButton().setDisabled(true);
						}

					} else if (isMasterDetailForm) {
						if (view instanceof DetailTabIF) {
							((DetailTabIF) view).setMasterPk();
						}
						if (view instanceof SyncronizeDataActionIF) {
							((SyncronizeDataActionIF) view).syncronizeData();
						}
						if (cabMasterForm != null
								&& cabMasterForm.validate()
								&& !cabMasterForm.hasErrors()
								&& form != null
								&& form.validate()
								&& !form.hasErrors()
								&& ((form.getChangedValues() != null && form.getChangedValues().size() > 0)
										|| (cabMasterForm.getChangedValues() != null && cabMasterForm.getChangedValues().size() > 0) || isChangedValuesInExtraForm
										&& !hasErrorInExtraForm)) {
							lastActionWasRefresh = false;
							optionsToolBar.getSaveButton().setDisabled(true);
							saveOrUpdateData();
						}
					} else {
						// save only if form is a valid and if has any change
						if (form != null && form.validate()
								&& ((form.getChangedValues() != null && form.getChangedValues().size() > 0) || isChangedValuesInExtraForm)) {
							if (!form.hasErrors() && !hasErrorInExtraForm) {
								lastActionWasRefresh = false;
								optionsToolBar.getSaveButton().setDisabled(true);
								saveOrUpdateData();
							}
						}
					}
				}
			}
		});
	}

	private void addRefreshCallBack() {
		this.optionsToolBar.getRefreshButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("continue_changes_will_lost_in_form"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doRefreshAction();
							}
						}
					});
				} else {
					doRefreshAction();
				}
			}

		});
	}

	public void doRefreshAction() {
		lastActionWasRefresh = true;
		dataWasFetched = true;

		disableAddButton(false);
		disableRemoveButton(true);
		if (showPagingToolbar) {
			pagingToolBar.fetchData(afterFetchIF);
		}
		focusInFieldToFocus();
		if (form != null) {
			form.clearValues();
		}

		idxSelectedRecord = -1;
		listGrid.selectSingleRecord(idxSelectedRecord);

		if (this.optionsToolBar.getSaveButton() != null) {
			this.optionsToolBar.getSaveButton().setDisabled(!this.isOnFaceOne);
		}
		if (this.optionsToolBar.getSaveGridPreferencesButton() != null) {
			this.optionsToolBar.getSaveGridPreferencesButton().setDisabled(this.isOnFaceOne);
		}
		if (isMasterDetailForm) {
			restoreDetailsState();
		}
		if (view instanceof ClearAditionDataActionIF) {
			((ClearAditionDataActionIF) view).doClearAditionData();
		}
	}

	public void buildAfterFetchActionIF() {
		if (afterFetchIF == null) {
			this.afterFetchIF = new AfterFetchIF() {
				@Override
				public void execute() {
					disableOptionsBasedOnCurrentFace();
				}
			};
		}
	}

	private void restoreDetailsState() {
		if (cabMasterForm != null) {
			this.cabMasterForm.clearValues();

			setDisabledTabs(true);
			if (this.detailsToLoad != null) {
				for (DetailTabIF detailTab : this.detailsToLoad) {
					detailTab.getListGrid().selectSingleRecord(-1);
					detailTab.getListGrid().setData(new ListGridRecord[0]);
					detailTab.clearMasterPk();
				}
			}
			if (this.cabFieldsToBind != null) {
				for (String field : this.cabFieldsToBind) {
					this.cabMasterForm.setValue(field, "");
				}
			}
			this.cabMasterForm.redraw();
		}
	}

	private void addCancelCallBack() {
		this.optionsToolBar.getCancelButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				lastActionWasRefresh = false;

				if (form.valuesHaveChanged()) {
					form.setValues(form.getOldValues());
				}
				if (form.getErrors() != null && form.getErrors().size() > 0) {
					form.clearErrors(true);
				}

				if (isMasterDetailForm) {
					if (cabMasterForm.valuesHaveChanged()) {
						cabMasterForm.setValues(form.getOldValues());
					}
					if (cabMasterForm.getErrors() != null && cabMasterForm.getErrors().size() > 0) {
						cabMasterForm.clearErrors(true);
					}
				}
				disableAddButton(false);
				focusInFieldToFocus();
			}
		});
	}

	private void addSavePreferencesCallBack() {
		this.optionsToolBar.getSaveGridPreferencesButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				askNameOfPreference();
			}

			private void askNameOfPreference() {
				SC.ask(dc.get("question_add_new_preference"), new BooleanCallback() {
					public void execute(Boolean value) {
						if (value != null && value) {
							SC.askforValue(dc.get("type_the_name_preference"), new ValueCallback() {
								public void execute(final String preferenceName) {
									lastActionWasRefresh = false;

									if (!StringUtil.isEmpty(preferenceName)) {
										preferencesDS.fetchData(new Criteria(), new DSCallback() {
											public void execute(DSResponse response, Object rawData, DSRequest request) {
												savePreferenceAction(preferenceName, response);
											}
										});
									} else {
										askNameOfPreference();
									}
								}
							});
						}
					}
				});
			}
		});
		addListGridDrawHandler();
	}

	private void addListGridDrawHandler() {
		this.listGrid.addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				preferencesDS.addData(new PreferenceRecord(savePreferencesStateId + ++ViewImpl.PK_COUNTER, dc.get("default"), listGrid.getViewState()));
				String preferences = (String) Offline.get(getSaveAllPreferencesId());
				String lastSelectedPreference = (String) Offline.get(getLastSelectedPreference());

				if (preferences != null) {
					String[] preferencesTokens = preferences.split("__PREFERENCE_SEPARATOR_");

					if (preferencesTokens != null) {
						for (String preferencesToken : preferencesTokens) {
							String preferenceName = preferencesToken.substring(0, preferencesToken.indexOf("("));
							String viewState = preferencesToken.substring(preferencesToken.indexOf("("));

							preferencesDS.addData(new PreferenceRecord(savePreferencesStateId + ++ViewImpl.PK_COUNTER, preferenceName, viewState));
							preferenceSelectItem.setValue(preferenceName);
						}
					}
				}

				if (lastSelectedPreference != null) {
					preferenceSelectItem.setValue(lastSelectedPreference);
					preferenceSelectItem.fireEvent(new ChangedEvent(preferenceSelectItem.getJsObj()));
				}
			}
		});
	}

	private void savePreferenceAction(final String preferenceName, DSResponse response) {
		Record[] data = response.getData();
		if (data.length != 0) {
			boolean existPreferenceName = false;
			for (Record record : data) {
				if (record.getAttribute("name").equals(preferenceName)) {
					existPreferenceName = true;
					break;
				}
			}
			if (existPreferenceName) {
				MessageUtil.showError(dc.get("this_preference_name_exists_try_again"));
			} else {
				String viewState = listGrid.getViewState();
				PreferenceRecord preferenceRecord = new PreferenceRecord(savePreferencesStateId + ++ViewImpl.PK_COUNTER, preferenceName, viewState);
				preferencesDS.addData(preferenceRecord);
				preferenceSelectItem.setValue(preferenceName);

				String preferences = (String) Offline.get(getSaveAllPreferencesId());
				if (preferences != null) {
					preferences = preferences + "__PREFERENCE_SEPARATOR_" + preferenceName + viewState;
				} else {
					preferences = preferenceName + viewState;
				}

				Offline.put(getSaveAllPreferencesId(), preferences);
				Offline.put(getLastSelectedPreference(), preferenceName);
			}
		}
	}

	private String getLastSelectedPreference() {
		return this.savePreferencesStateId + "LastSelectedPreference";
	}

	private String getSaveAllPreferencesId() {
		return this.savePreferencesStateId + "AllPreferences";
	}

	private void addNavigatorCallBack() {
		this.optionsToolBar.getFirstRowButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.valuesHaveChanged() != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doSelectFirstRow();
							}
						}
					});
				} else {
					doSelectFirstRow();
				}
			}
		});
		this.optionsToolBar.getLastRowButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.valuesHaveChanged() != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doSelectLastRow();
							}
						}
					});
				} else {
					doSelectLastRow();
				}
			}
		});
		this.optionsToolBar.getBackRowButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.valuesHaveChanged() != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doSelectBackRow();
							}
						}
					});
				} else {
					doSelectBackRow();
				}
			}

		});
		this.optionsToolBar.getNextRowButton().addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				if (form.valuesHaveChanged() != null && form.valuesHaveChanged()) {
					SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
						public void execute(Boolean value) {
							if (value != null && value) {
								doSelectNextRecord();
							}
						}
					});
				} else {
					doSelectNextRecord();
				}
			}
		});
	}

	private void doSelectFirstRow() {
		lastActionWasRefresh = false;
		try {
			getListGrid().selectSingleRecord(0);
			idxSelectedRecord = 0;
		} catch (Exception ignored) {
		}
		bindFormWithListGrid();

		disableAddButton(false);
		disableRemoveButtonIfNecessary();
		showNavigatorButtonsIfNecessary();
		focusInFieldToFocus();

		if (isOnFaceOne && view instanceof AfterChangeRecordIF) {
			((AfterChangeRecordIF) view).doAfterChangeRecord();
		}
	}

	private void doSelectLastRow() {
		lastActionWasRefresh = false;
		try {
			int lastRow = getListGrid().getRecords().length - 1;
			getListGrid().selectSingleRecord(lastRow);
			idxSelectedRecord = lastRow;
		} catch (Exception ignored) {
		}
		bindFormWithListGrid();

		disableAddButton(false);
		disableRemoveButtonIfNecessary();
		showNavigatorButtonsIfNecessary();
		focusInFieldToFocus();

		if (isOnFaceOne && view instanceof AfterChangeRecordIF) {
			((AfterChangeRecordIF) view).doAfterChangeRecord();
		}
	}

	private void doSelectNextRecord() {
		lastActionWasRefresh = false;
		synchronized (this.idxSelectedRecord) {
			try {
				getListGrid().selectSingleRecord(++this.idxSelectedRecord);
			} catch (Exception e) {
				this.idxSelectedRecord = this.idxSelectedRecord - 1;
			}
		}

		bindFormWithListGrid();

		disableAddButton(false);
		disableRemoveButtonIfNecessary();
		showNavigatorButtonsIfNecessary();
		focusInFieldToFocus();

		if (isOnFaceOne && view instanceof AfterChangeRecordIF) {
			((AfterChangeRecordIF) view).doAfterChangeRecord();
		}
	}

	private void doSelectBackRow() {
		lastActionWasRefresh = false;
		synchronized (this.idxSelectedRecord) {
			if (this.idxSelectedRecord - 1 >= 0) {
				try {
					getListGrid().selectSingleRecord(--this.idxSelectedRecord);
				} catch (Exception ignored) {
					this.idxSelectedRecord = this.idxSelectedRecord + 1;
				}
			}
		}

		bindFormWithListGrid();

		disableAddButton(false);
		disableRemoveButtonIfNecessary();
		showNavigatorButtonsIfNecessary();
		focusInFieldToFocus();

		if (isOnFaceOne && view instanceof AfterChangeRecordIF) {
			((AfterChangeRecordIF) view).doAfterChangeRecord();
		}
	}

	public void changeFace() {
		if (this.firstTimeChangingFace) {
			this.firstTimeChangingFace = false;

			if (this.vLayout.getMember(FACE_TWO_MEMBER_NUMBER) == null) {
				MessageUtil.showError(dc.get("not_possible_change_face"));
				return;
			}
		}

		if (this.isOnFaceOne && form != null && this.form.valuesHaveChanged()) {
			SC.ask(dc.get("confirmation_lost_changes"), new BooleanCallback() {
				public void execute(Boolean value) {
					if (value != null && value) {
						if (!isOnFaceOne) {
							bindFormWithListGrid();
						}
						isOnFaceOne = !isOnFaceOne;

						vLayout.getMember(FACE_ONE_MEMBER_NUMBER).setVisible(isOnFaceOne);
						vLayout.getMember(FACE_TWO_MEMBER_NUMBER).setVisible(!isOnFaceOne);

						disableAddButton(false);
						disableRemoveButtonIfNecessary();
						disableOptionsBasedOnCurrentFace();
						focusInFieldToFocus();

						if (isOnFaceOne && view instanceof ChangeFaceIF) {
							((ChangeFaceIF) view).extraAction();
						}

						if (isOnFaceOne && idxSelectedRecord >= 0) {
							if (view instanceof AfterChangeRecordIF) {
								((AfterChangeRecordIF) view).doAfterChangeRecord();
							}
						}
					}
				}
			});
		} else {
			if (!this.isOnFaceOne) {
				bindFormWithListGrid();
			}
			this.isOnFaceOne = !this.isOnFaceOne;

			this.vLayout.getMember(FACE_ONE_MEMBER_NUMBER).setVisible(this.isOnFaceOne);
			this.vLayout.getMember(FACE_TWO_MEMBER_NUMBER).setVisible(!this.isOnFaceOne);

			disableAddButton(false);
			disableRemoveButtonIfNecessary();
			disableOptionsBasedOnCurrentFace();
			focusInFieldToFocus();

			if (isOnFaceOne) {
				if (view instanceof ChangeFaceIF) {
					((ChangeFaceIF) view).extraAction();
				}

				if (idxSelectedRecord >= 0 && view instanceof AfterChangeRecordIF) {
					((AfterChangeRecordIF) view).doAfterChangeRecord();
				}
			}
		}
	}

	private void focusInFieldToFocus() {
		if (this.isOnFaceOne && this.fieldToFocus != null) {
			this.fieldToFocus.focusInItem();
		}
	}

	private void disableOptionsBasedOnCurrentFace() {
		if (this.isOnFaceOne) {
			showNavigatorButtonsIfNecessary();
		} else {
			if (this.optionsToolBar.getNextRowButton() != null) {
				this.optionsToolBar.getNextRowButton().setDisabled(true);
				this.optionsToolBar.getBackRowButton().setDisabled(true);

				this.optionsToolBar.getFirstRowButton().setDisabled(true);
				this.optionsToolBar.getLastRowButton().setDisabled(true);
			}
		}
		if (this.optionsToolBar.getSaveButton() != null) {
			this.optionsToolBar.getSaveButton().setDisabled(!this.isOnFaceOne);
		}
		if (this.optionsToolBar.getSaveGridPreferencesButton() != null) {
			this.optionsToolBar.getSaveGridPreferencesButton().setDisabled(this.isOnFaceOne);
		}
	}

	private void showNavigatorButtonsIfNecessary() {
		if (this.optionsToolBar.getBackRowButton() != null) {
			ListGridRecord[] records = this.listGrid.getRecords();

			if (records == null || records.length == 0) {
				this.optionsToolBar.getFirstRowButton().setDisabled(true);
				this.optionsToolBar.getLastRowButton().setDisabled(true);

				this.optionsToolBar.getBackRowButton().setDisabled(true);
				this.optionsToolBar.getNextRowButton().setDisabled(true);
			} else {
				this.optionsToolBar.getFirstRowButton().setDisabled(!this.isOnFaceOne || this.idxSelectedRecord <= 0);
				this.optionsToolBar.getLastRowButton().setDisabled(!this.isOnFaceOne || this.idxSelectedRecord == records.length - 1);

				this.optionsToolBar.getBackRowButton().setDisabled(!this.isOnFaceOne || this.idxSelectedRecord <= 0);
				this.optionsToolBar.getNextRowButton().setDisabled(!this.isOnFaceOne || this.idxSelectedRecord == records.length - 1);
			}
		}
	}

	private void bindFormWithListGrid() {
		if (this.form != null && this.listGrid != null) {
			this.form.clearValues();
			this.form.editSelectedData(this.listGrid);

			if (extraForms != null) {
				for (DynamicForm extraForm : extraForms) {
					extraForm.clearValues();
					extraForm.editSelectedData(this.listGrid);
				}
			}
		}
	}

	public void bindFormWithListGrid(ListGrid listGrid) {
		if (this.form != null && listGrid != null) {
			this.form.clearValues();
			this.form.editSelectedData(listGrid);
		}
	}

	public void disableAddButton(boolean enableAddButton) {
		if (this.optionsToolBar.getAddButton() != null) {
			this.optionsToolBar.getAddButton().setDisabled(enableAddButton);
		}
	}

	public void setLastActionWasRefresh(boolean lastActionWasRefresh) {
		this.lastActionWasRefresh = lastActionWasRefresh;
	}

	private void disableRemoveButton(boolean flag) {
		if (this.optionsToolBar.getDeleteButton() != null) {
			this.optionsToolBar.getDeleteButton().setDisabled(flag);
		}
	}

	private void disableRemoveButtonIfNecessary() {
		if (this.optionsToolBar.getDeleteButton() != null) {
			this.optionsToolBar.getDeleteButton().setDisabled(
					(lastActionWasRefresh && idxSelectedRecord == -1) || this.listGrid == null || this.listGrid.getSelectedRecords() == null
							|| this.listGrid.getSelectedRecords().length <= 0);
		}
	}

	private synchronized void saveOrUpdateData() {
		ListGridRecord record = new ListGridRecord();
		if (form != null) {
			FormItem[] fields = this.form.getFields();
			if (fields != null) {
				for (int i = 0; i < fields.length; i++) {
					record.setAttribute(fields[i].getName(), fields[i].getValue());
				}
			}
			if (this.isMasterDetailForm && cabMasterForm != null) {
				FormItem[] cabMasterFields = this.cabMasterForm.getFields();
				for (int i = 0; i < cabMasterFields.length; i++) {
					record.setAttribute(cabMasterFields[i].getName(), cabMasterFields[i].getValue());
				}
			}
			if (extraForms != null) {
				for (DynamicForm extraForm : extraForms) {
					FormItem[] fieldsExtraForm = extraForm.getFields();
					if (fieldsExtraForm != null) {
						for (int i = 0; i < fieldsExtraForm.length; i++) {
							record.setAttribute(fieldsExtraForm[i].getName(), fieldsExtraForm[i].getValue());
						}
					}
				}
			}

			boolean isNewRecord = true;
			if (primaryKeys != null) {
				for (FormItem primaryKey : this.primaryKeys) {
					if (primaryKey.getValue() != null) {
						isNewRecord = false;
						break;
					}
					String nullValue = null;
					record.setAttribute(primaryKey.getName(), nullValue);
				}
			}

			if (isNewRecord) {
				addDataInListGrid(record);
			} else {
				updateDataInListGrid(record);
			}
		}
	}

	private void updateDataInListGrid(ListGridRecord record) {
		if (listGrid != null) {
			this.listGrid.updateData(record, new DSCallback() {
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					lastActionWasRefresh = false;
					optionsToolBar.getSaveButton().setDisabled(false);

					if (response.getStatus() == 0) {
						disableAddButton(false);

						Record[] data = response.getData();
						if (data != null && data.length > 0 && data[0] instanceof ListGridRecord) {
							try {
								listGrid.selectRecord(data[0]);
								bindFormWithListGrid();

								if (view instanceof AfterSaveActionIF) {
									((AfterSaveActionIF) view).doAfterSave();
								}

							} catch (Exception ignored) {
							}
						}
					}
				}
			});
		}
	}

	private void addDataInListGrid(ListGridRecord record) {
		if (listGrid != null) {
			this.listGrid.addData(record, new DSCallback() {
				public void execute(DSResponse response, Object rawData, DSRequest request) {
					lastActionWasRefresh = false;
					optionsToolBar.getSaveButton().setDisabled(false);

					if (response.getStatus() == 0) {
						showNavigatorButtonsIfNecessary();
						disableAddButton(false);

						Record[] data = response.getData();
						if (data != null && data.length > 0 && data[0] instanceof ListGridRecord) {
							try {
								getListGrid().selectRecord(data[0]);
								bindFormWithListGrid();

								RecordList recordList = listGrid.getRecordList();
								if (recordList != null) {
									idxSelectedRecord = recordList.getLength() - 1;
								}
								recordList = null;

								if (view instanceof AfterSaveActionIF) {
									((AfterSaveActionIF) view).doAfterSave();
								}
							} catch (Exception ignored) {
							}
						}
					}
				}
			});
		}
	}

	public void setShowPagingToolbar(boolean showPagingToolbar) {
		this.showPagingToolbar = showPagingToolbar;
	}

	private void setFetchDataOnLoad(boolean fetchDataOnLoad) {
		this.fetchDataOnLoad = fetchDataOnLoad;
	}

	private void setListGrid(ListGrid listGrid) {
		this.listGrid = listGrid;
	}

	private void setSavePreferencesStateId(String savePreferencesStateId) {
		this.savePreferencesStateId = savePreferencesStateId;
	}

	private void setPrimaryKeys(FormItem[] primaryKeys) {
		this.primaryKeys = primaryKeys;
	}

	private void setGridNameToShowOnPrintPreview(String gridNameToShowOnPrintPreview) {
		this.gridNameToShowOnPrintPreview = gridNameToShowOnPrintPreview;
	}

	private void setFieldToFocus(FormItem fieldToFocus) {
		this.fieldToFocus = fieldToFocus;
	}

	private void setFaceOne(Layout faceOne) {
		this.faceOne = faceOne;
	}

	private void setFaceTwo(Layout faceTwo) {
		this.faceTwo = faceTwo;
	}

	private void setOptionsToNotShow(LinkedHashMap<String, Boolean> optionsToNotShow) {
		this.optionsToNotShow = optionsToNotShow;
	}

	private void setForm(DynamicForm form) {
		this.form = form;
	}

	private void setRowsPerPageTemplate(String[] rowsPerPageTemplate) {
		this.rowsPerPageTemplate = rowsPerPageTemplate;
	}

	public PagingToolBar getPagingToolBar() {
		return this.pagingToolBar;
	}

	public final Boolean getIsOnFaceOne() {
		return this.isOnFaceOne;
	}

	public int getIdxSelectedRecord() {
		return this.idxSelectedRecord;
	}

	public String[] getRowsPerPageTemplate() {
		return this.rowsPerPageTemplate;
	}

	public void setExportFileName(String exportFileName) {
		this.exportFileName = exportFileName;
	}

	public void fetchData() {
		if (!this.autoFetchData && !this.dataWasFetched) {
			this.dataWasFetched = true;
			this.pagingToolBar.fetchData();
		}
	}

	public void defaultSelectionChangedHandlerAction(ListGridRecord recordSelected) {
		if (recordSelected != null) {
			this.idxSelectedRecord = this.listGrid.getRecordIndex(recordSelected);

			if (this.isMasterDetailForm && this.idxSelectedRecord >= 0) {
				setDisabledTabs(false);
			}
		} else {
			this.idxSelectedRecord = -1;
		}

		if (this.isOnFaceOne) {
			showNavigatorButtonsIfNecessary();
		}

		disableRemoveButtonIfNecessary();
	}

	public synchronized void positionInCorrectRecordAfterRemove() {
		try {
			this.idxSelectedRecord = 0;
			this.listGrid.selectSingleRecord(this.idxSelectedRecord);
			bindFormWithListGrid();

			disableRemoveButtonIfNecessary();
			showNavigatorButtonsIfNecessary();
		} catch (Exception ignored) {
			this.idxSelectedRecord = -1;
			this.form.clearValues();

			if (this.isMasterDetailForm) {
				this.cabMasterForm.clearValues();
			}
		}
	}

	public Layout buildContentLayout(Canvas viewPanel) {
		HLayout wrapper = new HLayout();
		wrapper.setWidth100();
		wrapper.addMember(viewPanel);

		Layout layout = new HLayout();
		layout.setWidth100();
		layout.setMargin(5);
		layout.addMember(wrapper);
		return layout;
	}

	@SuppressWarnings("rawtypes")
	public void setView(ViewIF view) {
		this.view = view;
		this.serviceAsync = ((GenericGwtRpcDataSource) view.getListGrid().getDataSource()).getServiceAsync();
	}

	public ListGrid getListGrid() {
		return this.listGrid;
	}

	public VLayout getViewContent() {
		return this.vLayout;
	}

	public void masterDetail(List<String> cabFieldsToBind, DynamicForm cabMasterForm, List<String> fieldsToFilter, List<DetailTabIF> detailsToLoad,
			TabSet masterDetailTabSet) {
		if (cabFieldsToBind != null && cabMasterForm != null) {
			this.isMasterDetailForm = true;
		}

		this.cabFieldsToBind = cabFieldsToBind;
		this.cabMasterForm = cabMasterForm;
		this.fieldsToFilter = fieldsToFilter;
		this.detailsToLoad = detailsToLoad;
		this.masterDetailTabSet = masterDetailTabSet;
		if (masterDetailTabSet != null) {
			this.tabs = masterDetailTabSet.getTabs();
		}

		if (this.tabs != null) {
			setDisabledTabs(true);
		}
	}

	public void clearForm() {
		if (this.form != null && this.listGrid != null) {
			this.form.clearValues();

			if (this.isMasterDetailForm) {
				this.cabMasterForm.clearValues();
			}
		}
	}

	public void setAutoFetchData(boolean autoFetchData) {
		this.autoFetchData = autoFetchData;
	}

	public static class ViewImplBuilder {
		private String savePreferencesStateId;
		private String gridNameToShowOnPrintPreview;
		private FormItem fieldToFocus;
		private FormItem[] primaryKeys;
		private Layout faceOne;
		private Layout faceTwo;
		private boolean autoFetchData = true;
		private LinkedHashMap<String, Boolean> optionsToNotShow;
		private DynamicForm form;
		private ListGrid listGrid;
		private String[] rowsPerPageTemplate;
		private ViewIF view;
		private boolean fetchDataOnLoad = true;
		private boolean showPagingToolbar = true;
		private boolean isMasterDetailForm = false;

		private List<String> cabFieldsToBind;
		private DynamicForm cabDetailForm;
		private List<DetailTabIF> detailsToLoad;
		private List<String> fieldsToFilter;
		private TabSet masterDetailTabSet;
		private Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
		private String exportFileName;
		private OptionsToolBar optionsToolBar;
		private PegasusDynamicForm[] extraForms;

		public static ViewImplBuilder getInstance() {
			return new ViewImplBuilder();
		}

		public ViewImplBuilder showPagingToolbar(boolean showPagingToolbar) {
			this.showPagingToolbar = showPagingToolbar;
			return this;
		}

		public ViewImplBuilder savePreferencesStateId(String savePreferencesStateId) {
			this.savePreferencesStateId = savePreferencesStateId;
			return this;
		}

		public ViewImplBuilder gridNameToShowOnPrintPreview(String gridNameToShowOnPrintPreview) {
			this.gridNameToShowOnPrintPreview = gridNameToShowOnPrintPreview;
			return this;
		}

		public ViewImplBuilder fieldToFocus(FormItem fieldToFocus) {
			this.fieldToFocus = fieldToFocus;
			return this;
		}

		public ViewImplBuilder optionsToolBar(OptionsToolBar optionsToolBar) {
			this.optionsToolBar = optionsToolBar;
			return this;
		}

		public ViewImplBuilder primaryKeys(FormItem... primaryKeys) {
			this.primaryKeys = primaryKeys;
			return this;
		}

		public ViewImplBuilder optionsToNotShow(LinkedHashMap<String, Boolean> optionsToNotShow) {
			this.optionsToNotShow = optionsToNotShow;
			return this;
		}

		public ViewImplBuilder faceOne(Layout faceOne, DynamicForm form) {
			this.faceOne = faceOne;
			this.form = form;
			return this;
		}

		public ViewImplBuilder faceTwo(Layout faceTwo, ListGrid listGrid) {
			this.faceTwo = faceTwo;
			this.listGrid = listGrid;
			return this;
		}

		public ViewImplBuilder autoFetchData(boolean autoFetchData) {
			this.autoFetchData = autoFetchData;
			this.fetchDataOnLoad = autoFetchData;
			return this;
		}

		public ViewImplBuilder fetchDataOnLoad(boolean fetchDataOnLoad) {
			this.fetchDataOnLoad = fetchDataOnLoad;
			this.autoFetchData = fetchDataOnLoad;
			return this;
		}

		public ViewImplBuilder rowsPerPageTemplate(String[] rowsPerPageTemplate) {
			this.rowsPerPageTemplate = rowsPerPageTemplate;
			return this;
		}

		public ViewImplBuilder view(ViewIF view) {
			this.view = view;
			return this;
		}

		public ViewImplBuilder masterDetail(List<String> cabFieldsToBind, DynamicForm cabDetailForm, List<String> fieldsToFilter,
				List<DetailTabIF> detailsToLoad, TabSet masterDetailTabSet) {
			this.cabFieldsToBind = cabFieldsToBind;
			this.cabDetailForm = cabDetailForm;
			this.fieldsToFilter = fieldsToFilter;
			this.detailsToLoad = detailsToLoad;
			this.masterDetailTabSet = masterDetailTabSet;

			return this;
		}

		public ViewImpl build() {
			if (this.faceOne != null && this.view != null) {
				String errorMessage = "";
				if (this.isMasterDetailForm) {
					if (this.cabFieldsToBind == null) {
						errorMessage = errorMessage + dc.get("developer_error_inform_cab_fields_to_bind");
					}
					if (this.detailsToLoad == null) {
						errorMessage = errorMessage + dc.get("developer_error_inform_details_to_load");
					}
					if (this.fieldsToFilter.get(0) == null) {
						errorMessage = errorMessage + dc.get("developer_error_fields_to_filter");
					}
					if (this.masterDetailTabSet == null) {
						errorMessage = errorMessage + dc.get("developer_error_master_detail_tab_set");
					}
				} else if (this.fieldsToFilter != null || this.detailsToLoad != null || this.masterDetailTabSet != null) {
					if (this.fieldsToFilter == null) {
						errorMessage = errorMessage + dc.get("developer_error_fields_to_filter");
					}
					if (this.detailsToLoad == null) {
						errorMessage = errorMessage + dc.get("developer_error_inform_details_to_load");
					}
					if (this.masterDetailTabSet == null) {
						errorMessage = errorMessage + dc.get("developer_error_master_detail_tab_set");
					}
				}

				if (!errorMessage.isEmpty()) {
					MessageUtil.showError(errorMessage);
					return null;
				}

				ViewImpl viewImpl = new ViewImpl();
				viewImpl.setOptionsToNotShow(this.optionsToNotShow);
				viewImpl.setSavePreferencesStateId(this.savePreferencesStateId);
				viewImpl.setGridNameToShowOnPrintPreview(this.gridNameToShowOnPrintPreview);
				viewImpl.setFieldToFocus(this.fieldToFocus);
				viewImpl.setPrimaryKeys(this.primaryKeys);
				viewImpl.setView(this.view);
				viewImpl.setFaceOne(this.faceOne);
				viewImpl.setFaceTwo(this.faceTwo);
				viewImpl.setForm(this.form);
				viewImpl.setListGrid(this.listGrid);
				viewImpl.setExportFileName(this.exportFileName);
				viewImpl.setAutoFetchData(autoFetchData);
				viewImpl.setOptionsToolBar(optionsToolBar);
				viewImpl.setShowPagingToolbar(showPagingToolbar);
				viewImpl.setFetchDataOnLoad(this.fetchDataOnLoad);
				viewImpl.setRowsPerPageTemplate(this.rowsPerPageTemplate);
				viewImpl.masterDetail(this.cabFieldsToBind, this.cabDetailForm, this.fieldsToFilter, this.detailsToLoad, this.masterDetailTabSet);
				viewImpl.setExtraForms(this.extraForms);

				viewImpl.buildView();

				return viewImpl;
			}
			SC.say(dc.get("developer_error_not_enough_info_to_build_view_impl"));

			return null;
		}

		public ViewImplBuilder exportFileName(String exportFileName) {
			this.exportFileName = exportFileName;
			return this;
		}

		public ViewImplBuilder extraForms(PegasusDynamicForm... forms) {
			this.extraForms = forms;
			return this;
		}

	}

	public AfterFetchIF getAfterFetchIF() {
		return this.afterFetchIF;
	}

	public void setExtraForms(PegasusDynamicForm[] extraForms) {
		this.extraForms = extraForms;
	}

}