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
package br.com.pegasus.solutions.smartgwt.lib.client.upload;

import br.com.pegasus.solutions.smartgwt.lib.client.factory.FM;
import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.util.StringUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.ISubmitCompleteAction;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.FileUpload;
import com.google.gwt.user.client.ui.FormPanel;
import com.google.gwt.user.client.ui.HTML;
import com.smartgwt.client.widgets.Progressbar;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripButton;

public class FileUploader {
	private FormPanel form;
	private FileUpload fileUpload;
	private Progressbar progressbar;
	private ClickHandler iSubmitAction;
	private String submitBtnName;
	private String iconSubmitBtn;
	private String progressDescription;
	private Timer progressBarTimer;
	private ISubmitCompleteAction iSubmitCompleteAction;
	private ToolStripButton uploadButton;

	private String labelWidth = "120px";
	private String formHeight = "20px";
	private String formWidth = "550px";

	public FileUploader(String textLabelUpload, String labelWidth, String submitBtnName, String iconSubmitBtn, String progressDescription) {
		this.form = new FormPanel();
		this.form.setMethod("post");
		this.form.addStyleName("gwtFormPanel");
		this.form.setEncoding("multipart/form-data");

		this.progressbar = FM.buildProgressBar(500);
		this.submitBtnName = submitBtnName;
		this.iconSubmitBtn = iconSubmitBtn;
		this.progressDescription = progressDescription;
		buildUploadButton();

		VLayout panel = new VLayout();
		panel.setHeight(20);
		this.fileUpload = new FileUpload();
		this.fileUpload.setName("upload");

		com.google.gwt.user.client.ui.Label labelUpload = new com.google.gwt.user.client.ui.Label(textLabelUpload + ":");
		if (labelWidth != null) {
			labelUpload.setWidth(labelWidth);
		} else {
			labelUpload.setWidth(this.labelWidth);
		}
		HLayout line1 = new HLayout();
		line1.addMember(labelUpload);
		line1.addMember(this.fileUpload);

		panel.addMember(line1);

		addEvents();
		this.form.setHeight(formHeight);
		this.form.setWidth(formWidth);
		this.form.add(panel);
	}

	public FileUpload getFileUpload() {
		return fileUpload;
	}

	public void setMultipleFilesTrue() {
		form.add(new HTML("<input type='file' id='fileselect' name='fileselect[]' multiple />"));
	}

	private static native String getFilesSelected() /*-{
													var count = $wnd.$('input:file')[0].files.length;
													var out = "";
													
													for (i = 0; i < count; i++) {
													var file = $wnd.$('input:file')[0].files[i];
													out += file.name + ';' + file.size + ";";
													}
													return out;
													}-*/;

	private void buildUploadButton() {
		if (this.uploadButton == null) {
			this.uploadButton = new ToolStripButton(this.submitBtnName, this.iconSubmitBtn);
			this.uploadButton.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					initialStateProgressBar();

					if (iSubmitAction != null) {
						iSubmitAction.onClick(null);
					} else {
						if (StringUtil.isEmpty(getFileName())) {
							MessageUtil.showMsg(Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE).get("no_file_specified"));
							return;
						}

						progressbar.setPercentDone(20);
						uploadButton.setDisabled(true);
						submit();
					}
				}
			});
		}
	}

	public VLayout getFormInVLayout() {
		VLayout panel = new VLayout();
		panel.setWidth100();
		panel.setHeight(28);
		panel.addMember(this.form);

		return panel;
	}

	private void addEvents() {
		this.form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {
			public void onSubmitComplete(FormPanel.SubmitCompleteEvent event) {
				uploadButton.setDisabled(false);

				if (iSubmitCompleteAction != null) {
					progressbar.setPercentDone(100);
					iSubmitCompleteAction.onSubmitComplete(event);
				}
			}
		});
	}

	public void setiSubmitCompleteAction(ISubmitCompleteAction iSubmitCompleteAction) {
		this.iSubmitCompleteAction = iSubmitCompleteAction;
	}

	public FormPanel getForm() {
		return this.form;
	}

	public void submit() {
		this.form.submit();
	}

	public String getFileName() {
		return this.fileUpload.getFilename();
	}

	public ClickHandler getiSubmitAction() {
		return this.iSubmitAction;
	}

	public void setiSubmitAction(ClickHandler iSubmitAction) {
		this.iSubmitAction = iSubmitAction;
	}

	public void initialStateProgressBar() {
		this.progressbar.setPercentDone(0);
	}

	public void disableUploadButton() {
		this.uploadButton.setDisabled(true);
	}

	public void enableUploadButton() {
		this.uploadButton.setDisabled(false);
	}

	public void setLabelWidth(String labelWidth) {
		this.labelWidth = labelWidth;
	}

	public void setFormHeight(String formHeight) {
		this.formHeight = formHeight;
	}

	public void setFormWidth(String formWidth) {
		this.formWidth = formWidth;
	}

	/**
	 * build tool strip button
	 * 
	 * @param uploadButtons
	 *            {@link ToolStripButton}...
	 * @return {@link ToolStrip}
	 */
	public ToolStrip buildToolStripButtons() {
		ToolStripButton nullBtn = null;
		return buildToolStripButtons(nullBtn);
	}

	/**
	 * build tool strip button
	 * 
	 * @param uploadButtons
	 *            {@link ToolStripButton}...
	 * @return {@link ToolStrip}
	 */
	public ToolStrip buildToolStripButtons(ToolStripButton... uploadButtons) {
		ToolStrip buttonsBar = new ToolStrip();
		buttonsBar.setWidth100();
		buttonsBar.addMember(this.uploadButton);
		if (uploadButtons != null) {
			for (ToolStripButton btn : uploadButtons) {
				if (btn != null) {
					buttonsBar.addSeparator();
					buttonsBar.addMember(btn);
				}
			}
		}
		buttonsBar.addFill();

		com.smartgwt.client.widgets.Label label = new com.smartgwt.client.widgets.Label(this.progressDescription + ":");
		label.setWidth(60);

		HLayout layout = new HLayout();
		layout.setHeight100();
		layout.setPadding(2);
		layout.setMargin(2);
		layout.setWidth(300);
		layout.addMember(label);
		layout.addMember(this.progressbar);
		buttonsBar.addMember(layout);

		return buttonsBar;
	}

	public Progressbar getProgressbar() {
		return this.progressbar;
	}

	public void cancelTimerProgressBar() {
		if (this.progressBarTimer != null) {
			this.progressBarTimer.cancel();
		}
	}

	/**
	 * init timer progressbar
	 * 
	 * @param delayMilles
	 *            int
	 * @return void
	 */
	public void initTimerProgressBar(int delayMilles) {
		if (this.progressBarTimer == null) {
			this.progressBarTimer = new Timer() {
				public void run() {
					getProgressbar().setPercentDone((int) (Math.random() * 95.0D));
				}
			};
		}
		this.progressBarTimer.scheduleRepeating(delayMilles);
	}

}