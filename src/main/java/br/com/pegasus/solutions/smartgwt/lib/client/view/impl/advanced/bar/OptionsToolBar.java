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

import java.util.LinkedHashMap;
import java.util.List;

import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.infra.Options;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;
import com.smartgwt.client.widgets.toolbar.ToolStripSpacer;

public class OptionsToolBar extends ToolStrip {
	private Dictionary dc;
	private ToolStripButton firstRowButton;
	private ToolStripButton lastRowButton;
	private ToolStripButton nextRowButton;
	private ToolStripButton backRowButton;
	private ToolStripButton addButton;
	private ToolStripButton deleteButton;
	private ToolStripButton printButton;
	private ToolStripButton refreshButton;
	private ToolStripButton saveButton;
	private ToolStripButton cancelButton;
	private ToolStripButton changeFaceButton;
	private ToolStripButton exporterButton;
	private ToolStripButton saveGridPreferences;

	public OptionsToolBar(LinkedHashMap<String, Boolean> optionsToNotShow, List<ToolStripButton> extraButtons) {
		setWidth100();
		setTop(0);
		setMargin(2);
		this.dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);

		addSpacer(new ToolStripSpacer(4));
		addButtons(optionsToNotShow, extraButtons);
	}

	public OptionsToolBar(LinkedHashMap<String, Boolean> optionsToNotShow) {
		setWidth100();
		setTop(0);
		setMargin(2);
		this.dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);

		addSpacer(new ToolStripSpacer(4));
		addButtons(optionsToNotShow, null);
	}

	private void addButtons(LinkedHashMap<String, Boolean> optionsToNotShow, List<ToolStripButton> extraButtons) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.NAVIGATOR.toString()) == null
				|| (!(Boolean) optionsToNotShow.get(Options.NAVIGATOR.toString()))) {
			addFirstRowButton();
			addBackRowButton();
			addNextRowButton();
			addLastRowButton();
		}

		if (extraButtons != null) {
			for (int i = 0; i < extraButtons.size(); i++) {
				addMember(extraButtons.get(i));

				if (i != extraButtons.size() - 1) {
					addSeparator();
				}
			}
		}

		addChangeFaceButton(optionsToNotShow);
		addAddButton(optionsToNotShow);
		addDeleteButton(optionsToNotShow);
		addSaveButton(optionsToNotShow);
		addPrintButton(optionsToNotShow);
		addRefreshButton(optionsToNotShow);
		addCancelButton(optionsToNotShow);
		addFill();
		addExporterButton(optionsToNotShow);

		addSaveGridPreferences(optionsToNotShow);
	}

	private void addLastRowButton() {
		this.lastRowButton = new ToolStripButton("", "grid/lastPage.gif");
		this.lastRowButton.setTooltip(this.dc.get("last_record"));
		addMember(this.lastRowButton);
	}

	private void addNextRowButton() {
		this.nextRowButton = new ToolStripButton("", "grid/nextPage.gif");
		this.nextRowButton.setTooltip(this.dc.get("next_record"));
		addMember(this.nextRowButton);
		addSeparator();
	}

	private void addBackRowButton() {
		this.backRowButton = new ToolStripButton("", "grid/backPage.gif");
		this.backRowButton.setTooltip(this.dc.get("back_record"));
		addMember(this.backRowButton);
		addSeparator();
	}

	private void addFirstRowButton() {
		this.firstRowButton = new ToolStripButton("", "grid/firstPage.gif");
		this.firstRowButton.setTooltip(this.dc.get("first_record"));
		addMember(this.firstRowButton);
		addSeparator();
	}

	private void addSaveGridPreferences(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.SAVE_PREFERENCES.toString()) == null
				|| (!(Boolean) optionsToNotShow.get(Options.SAVE_PREFERENCES.toString()))) {
			addSeparator();
			this.saveGridPreferences = new ToolStripButton("", "options/saveGridPreferences.png");
			this.saveGridPreferences.setTooltip(this.dc.get("save_preferences"));
			addMember(this.saveGridPreferences);
		}
	}

	private void addExporterButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.EXPORT_DATA.toString()) == null
				|| (!(Boolean) optionsToNotShow.get(Options.EXPORT_DATA.toString()))) {
			this.exporterButton = new ToolStripButton("", "options/fileExport.png");
			this.exporterButton.setTooltip(this.dc.get("export_data"));
			addMember(this.exporterButton);
		}
	}

	private void addCancelButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.CANCEL.toString()) == null || (!(Boolean) optionsToNotShow.get(Options.CANCEL.toString()))) {
			addSeparator();
			this.cancelButton = new ToolStripButton("", "options/cancel.png");
			this.cancelButton.setTooltip(this.dc.get("cancel"));
			addMember(this.cancelButton);
		}
	}

	private void addRefreshButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.REFRESH.toString()) == null
				|| (!(Boolean) optionsToNotShow.get(Options.REFRESH.toString()))) {
			addSeparator();
			this.refreshButton = new ToolStripButton("", "options/refresh.gif");
			this.refreshButton.setTooltip(this.dc.get("refresh"));
			addMember(this.refreshButton);
		}
	}

	private void addPrintButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || (optionsToNotShow.get(Options.PRINT.toString()) == null) || (!(Boolean) optionsToNotShow.get(Options.PRINT.toString()))) {
			addSeparator();
			this.printButton = new ToolStripButton("", "options/printer.png");
			this.printButton.setTooltip(this.dc.get("print"));
			addMember(this.printButton);
		}
	}

	private void addSaveButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.SAVE.toString()) == null || (!(Boolean) optionsToNotShow.get(Options.SAVE.toString()))) {
			addSeparator();
			this.saveButton = new ToolStripButton("", "options/save.png");
			this.saveButton.setTooltip(this.dc.get("save"));
			addMember(this.saveButton);
		}
	}

	private void addDeleteButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.DELETE.toString()) == null || (!(Boolean) optionsToNotShow.get(Options.DELETE.toString()))) {
			addSeparator();
			this.deleteButton = new ToolStripButton("", "options/delete.png");
			this.deleteButton.setTooltip(this.dc.get("delete"));
			addMember(this.deleteButton);
		}
	}

	private void addAddButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.ADD.toString()) == null || (!(Boolean) optionsToNotShow.get(Options.ADD.toString()))) {
			addSeparator();
			this.addButton = new ToolStripButton("", "options/add.png");
			this.addButton.setTooltip(this.dc.get("new"));
			addMember(this.addButton);
		}
	}

	private void addChangeFaceButton(LinkedHashMap<String, Boolean> optionsToNotShow) {
		if (optionsToNotShow == null || optionsToNotShow.get(Options.CHANGE_FACE.toString()) == null
				|| (!(Boolean) optionsToNotShow.get(Options.CHANGE_FACE.toString()))) {
			addSeparator();
			this.changeFaceButton = new ToolStripButton("", "options/gridMode.png");
			this.changeFaceButton.setTooltip(this.dc.get("change_mode"));
			addMember(this.changeFaceButton);
		}
	}

	public ToolStripButton getRemoveButton() {
		return this.deleteButton;
	}

	public ToolStripButton getAddButton() {
		return this.addButton;
	}

	public ToolStripButton getPrintButton() {
		return this.printButton;
	}

	public ToolStripButton getRefreshButton() {
		return this.refreshButton;
	}

	public ToolStripButton getNextRowButton() {
		return this.nextRowButton;
	}

	public ToolStripButton getBackRowButton() {
		return this.backRowButton;
	}

	public ToolStripButton getDeleteButton() {
		return this.deleteButton;
	}

	public ToolStripButton getSaveButton() {
		return this.saveButton;
	}

	public ToolStripButton getCancelButton() {
		return this.cancelButton;
	}

	public ToolStripButton getChangeFaceButton() {
		return this.changeFaceButton;
	}

	public ToolStripButton getSaveGridPreferencesButton() {
		return this.saveGridPreferences;
	}

	public ToolStripButton getFirstRowButton() {
		return this.firstRowButton;
	}

	public ToolStripButton getLastRowButton() {
		return this.lastRowButton;
	}

	public ToolStripButton getExporterButton() {
		return this.exporterButton;
	}
}