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

import java.util.List;

import br.com.pegasus.solutions.smartgwt.lib.client.util.LocaleUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.events.ICloseWindowAction;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.util.MessageUtil;

import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Window;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.CloseClickEvent;
import com.smartgwt.client.widgets.events.CloseClickHandler;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.toolbar.ToolStrip;
import com.smartgwt.client.widgets.toolbar.ToolStripSpacer;

public class PopUp {
	private Window windowDialog;
	private int width = 440;
	private int height = 160;
	private String title;
	private Layout contentPopUp;
	private String okTitle;
	private List<IButton> extraButtons;
	private IButton okButton;
	private IButton cancelButton;
	private ClickHandler okAction;
	private boolean showOkButton;
	private boolean showCancelButton;
	private boolean showBottomBar;
	private Dictionary dc = Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE);
	private int okWidth;
	private ToolStrip buttonsToolStrip;

	private PopUp() {
		this.windowDialog = new Window();
		this.windowDialog.setPadding(2);
		this.windowDialog.setEdgeSize(10);
		this.windowDialog.setWidth(this.width);
		this.windowDialog.setHeight(this.height);
		this.windowDialog.setIsModal(true);
		this.windowDialog.setShowEdges(true);
		this.windowDialog.setAutoCenter(true);
		this.windowDialog.setShowHeader(true);
	}

	/**
	 * add close window action
	 * 
	 * @param iCloseWindowAction
	 *            {@link ICloseWindowAction}
	 */
	public void addCloseWindowAction(final ICloseWindowAction iCloseWindowAction) {
		this.windowDialog.addCloseClickHandler(new CloseClickHandler() {
			public void onCloseClick(CloseClickEvent event) {
				iCloseWindowAction.executeAction();
			}
		});
	}

	/**
	 * build
	 *
	 * @return void
	 */
	public void build() {
		this.windowDialog.setTitle(this.title);

		if (this.showBottomBar) {
			if (this.showOkButton) {
				this.okButton = new IButton(okTitle != null ? okTitle : this.dc.get("ok"));
				if (this.okWidth > 0) {
					this.okButton.setWidth(okWidth);
				}
				this.okButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						if (okAction != null) {
							okAction.onClick(null);
						}
					}
				});
			}
			if (this.showCancelButton) {
				this.cancelButton = new IButton(this.dc.get("cancel"));
				this.cancelButton.addClickHandler(new ClickHandler() {
					public void onClick(ClickEvent event) {
						getDialog().hide();
					}
				});
			}

			this.buttonsToolStrip = new ToolStrip();
			buttonsToolStrip.setWidth100();
			buttonsToolStrip.setTop(0);
			buttonsToolStrip.setBottom(0);
			buttonsToolStrip.setPadding(2);
			buttonsToolStrip.setMargin(2);
			buttonsToolStrip.addFill();

			if (extraButtons != null) {
				for (IButton iButton : extraButtons) {
					buttonsToolStrip.addMember(iButton);
				}
			}
			if (this.showOkButton) {
				buttonsToolStrip.addMember(this.okButton);
			}
			if (this.showCancelButton) {
				buttonsToolStrip.addSpacer(new ToolStripSpacer(4));
				buttonsToolStrip.addMember(this.cancelButton);
			}
			this.contentPopUp.addMember(buttonsToolStrip);
		}

		getDialog().addItem(contentPopUp);
	}

	public void updateContent(Layout layout) {
		getDialog().removeItem(getContentPopUp());

		if (getButtonsToolStrip() != null) {
			layout.removeMember(getButtonsToolStrip());
			layout.addMember(getButtonsToolStrip());
		}
		setContentPopUp(layout);
		getDialog().addItem(getContentPopUp());
	}

	public ToolStrip getButtonsToolStrip() {
		return buttonsToolStrip;
	}

	public Layout getContentPopUp() {
		return contentPopUp;
	}

	/**
	 * add ok action
	 * 
	 * @param okAction
	 *            {@link ClickHandler}
	 * @return void
	 */
	public void addOkAction(ClickHandler okAction) {
		if (okButton == null) {
			MessageUtil.showError("Necessary call showOkButton(true) on PopUpBuilder!");
		} else {
			okButton.addClickHandler(okAction);
		}
	}

	public Window getDialog() {
		return this.windowDialog;
	}

	public void setWindowDialog(Window windowDialog) {
		this.windowDialog = windowDialog;
	}

	public void setWidth(int width) {
		this.width = width;
		this.windowDialog.setWidth(width);
	}

	public void setHeight(int height) {
		this.height = height;
		this.windowDialog.setHeight(height);
	}

	private void setTitle(String title) {
		this.title = title;
	}

	public void setContentPopUp(Layout contentPopUp) {
		this.contentPopUp = contentPopUp;
		if (this.contentPopUp != null) {
			contentPopUp.setTop(2);
			contentPopUp.setBottom(0);
			contentPopUp.setWidth100();
			contentPopUp.setHeight100();
		}
	}

	public void setShowOkButton(boolean showOkButton) {
		this.showOkButton = showOkButton;
	}

	public void setOkAction(ClickHandler okAction) {
		this.okAction = okAction;
	}

	public void setShowCancelButton(boolean showCancelButton) {
		this.showCancelButton = showCancelButton;
	}

	public void setShowBottomBar(boolean showBottomBar) {
		this.showBottomBar = showBottomBar;
	}

	public String getOkTitle() {
		return okTitle;
	}

	public void setOkTitle(String okTitle) {
		this.okTitle = okTitle;
	}

	public void setOkWidth(int okWidth) {
		this.okWidth = okWidth;
	}

	public IButton getOkButton() {
		return okButton;
	}

	public IButton getCancelButton() {
		return cancelButton;
	}

	public void setExtraButtons(List<IButton> extraButtons) {
		this.extraButtons = extraButtons;
	}

	public static class PopUpBuilder {
		private int width = 440;
		private int height = 160;
		private String title;
		private Layout contentPopUp;
		private String okTitle;
		private List<IButton> extraButtons;
		private boolean showOkButton;
		private boolean showCancelButton = true;
		private boolean showBottomBar = true;
		private ClickHandler okAction;
		private ICloseWindowAction iCloseWindowAction;
		private int okWidth;

		public PopUpBuilder width(int width) {
			this.width = width;
			return this;
		}

		public PopUpBuilder height(int height) {
			this.height = height;
			return this;
		}

		public PopUpBuilder title(String title) {
			this.title = title;
			return this;
		}

		public PopUpBuilder contentPopUp(Layout contentPopUp) {
			this.contentPopUp = contentPopUp;
			return this;
		}

		public PopUpBuilder showOkButton(boolean showOkButton) {
			this.showOkButton = showOkButton;
			return this;
		}

		public PopUpBuilder extraButtons(List<IButton> extraButtons) {
			this.extraButtons = extraButtons;
			return this;
		}

		public PopUpBuilder showCancelButton(boolean showCancelButton) {
			this.showCancelButton = showCancelButton;
			return this;
		}

		public PopUpBuilder showBottomBar(boolean showBottomBar) {
			this.showBottomBar = showBottomBar;
			return this;
		}

		public PopUpBuilder okAction(ClickHandler okAction) {
			this.okAction = okAction;
			return this;
		}

		public PopUpBuilder closeWindowAction(ICloseWindowAction iCloseWindowAction) {
			this.iCloseWindowAction = iCloseWindowAction;
			return this;
		}

		public PopUpBuilder okTitle(String okTitle, int okWidth) {
			this.okTitle = okTitle;
			this.okWidth = okWidth;
			return this;
		}

		public PopUp buildInstance() {
			if (this.contentPopUp == null || this.title == null) {
				MessageUtil.showError(Dictionary.getDictionary("Application_" + LocaleUtil.CURRENT_LOCALE).get("required_filds_to_build_popup"));
				return null;
			}

			PopUp popUp = new PopUp();
			popUp.setTitle(this.title);
			popUp.setContentPopUp(this.contentPopUp);
			popUp.setWidth(this.width);
			popUp.setHeight(this.height);
			popUp.setOkAction(this.okAction);
			popUp.setShowOkButton(this.okAction != null || this.showOkButton);
			popUp.setShowCancelButton(this.showCancelButton);
			popUp.setShowBottomBar(this.showBottomBar);
			popUp.setExtraButtons(extraButtons);
			if (this.iCloseWindowAction != null) {
				popUp.addCloseWindowAction(this.iCloseWindowAction);
			}
			popUp.setOkWidth(this.okWidth);
			popUp.setOkTitle(this.okTitle);
			popUp.build();

			return popUp;
		}
	}

}