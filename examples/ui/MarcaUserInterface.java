package xptopackage.client.ui;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import br.com.pegasus.solutions.zws.gwt.client.ds.MarcaDS;
import br.com.pegasus.solutions.smartgwt.lib.client.view.api.ViewIF;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.ViewImpl;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.form.PegasusDynamicForm;
import br.com.pegasus.solutions.smartgwt.lib.client.menu.PanelFactory;
import br.com.pegasus.solutions.zws.gwt.shared.dto.MarcaDTO;
import br.com.pegasus.solutions.zws.gwt.client.listgrid.SystemListGrid;
import br.com.pegasus.solutions.smartgwt.lib.client.factory.FM;

import com.smartgwt.client.types.CharacterCasing;
import com.smartgwt.client.widgets.Canvas;
import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.layout.Layout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.events.DrawEvent;
import com.smartgwt.client.widgets.events.DrawHandler;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.form.fields.DateItem;
import com.smartgwt.client.widgets.form.fields.CheckboxItem;

public class MarcaUserInterface implements ViewIF {
	private Dictionary dc;
	private PegasusDynamicForm form;
	private ListGrid listGrid;
	private ViewImpl viewImpl;

	private TextItem idField;
	private TextItem nomeField;
	private DateItem dataCadastroField;
	private DateItem dataAlteracaoField;
	private TextItem usuarioUltimoDUCField;
	private CheckboxItem flagEnabledField;
	private MarcaDS ds;

	@Override
	public ViewImpl buildView() {
		this.ds = MarcaDS.getInstance();
		this.dc = ds.getDC();
		this.idField = FM.newTextItem(ds, "id_id");
		this.nomeField = FM.newTextItem(ds, "id_nome");
		this.nomeField.setWidth(300);
		this.nomeField.setLength(100);
		this.nomeField.setRequired(true);
		this.nomeField.setCharacterCasing(CharacterCasing.UPPER);

		this.dataCadastroField = FM.newDateField(ds, "id_dataCadastro");
		this.dataCadastroField.setVisible(false);
		this.dataAlteracaoField = FM.newDateField(ds, "id_dataAlteracao");
		this.dataAlteracaoField.setVisible(false);
		this.usuarioUltimoDUCField = FM.newTextItem(ds, "id_usuarioUltimoDUC");
		this.usuarioUltimoDUCField.setVisible(false);
		this.flagEnabledField = FM.newCheckBoxItem(ds, "id_flagEnabled");
		this.flagEnabledField.setVisible(false);

		this.form = new PegasusDynamicForm(ds);
		form.setFields(idField, nomeField, dataCadastroField, dataAlteracaoField, usuarioUltimoDUCField, flagEnabledField);

		form.addDrawHandler(new DrawHandler() {
			public void onDraw(DrawEvent event) {
				idField.setDisabled(true);

			}
		});

		VLayout gridModeLayout = new VLayout(0);
		gridModeLayout.setWidth100();
		gridModeLayout.addMember(getListGrid());

		Layout layoutFaceOne = new VLayout();
		layoutFaceOne.addMember(form);

		this.viewImpl = ViewImpl.ViewImplBuilder.getInstance().faceOne(layoutFaceOne, form).faceTwo(gridModeLayout, getListGrid()).fieldToFocus(null)
				.primaryKeys(idField).optionsToNotShow(null).view(this).build();

		return viewImpl;
	}

	@Override
	public List<? extends Serializable> getDataToRemove() {
		@SuppressWarnings("deprecation")
		final ListGridRecord[] selectedRecords = getListGrid().getSelection();
		List<MarcaDTO> marcasDTO = new ArrayList<MarcaDTO>();
		for (ListGridRecord selectedRecord : selectedRecords) {
			MarcaDTO marcaDTO = new MarcaDTO();
			marcaDTO.setId(selectedRecord.getAttributeAsString(dc.get("id_id")));

			marcasDTO.add(marcaDTO);
		}
		return marcasDTO;
	}

	@Override
	public ListGrid getListGrid() {
		if (this.listGrid == null) {
		  this.listGrid = new ListGrid();
		  listGrid.setDataSource(ds);

		  ListGridField idField = FM.newListGridField(ds, "id_id");
		  idField.setWidth("70");
		  idField.setCanEdit(false);

		  ListGridField nomeField = FM.newListGridField(ds, "id_nome");
		  nomeField.setWidth("*");
		  nomeField.setCanEdit(false);

		  listGrid.setFields(idField, nomeField);
		}

		return listGrid;
	}

	public static class Factory implements PanelFactory {
		private String id;

		public Canvas create() {
			MarcaUI panel = new MarcaUI();
			VLayout wrapperPanelContent = new VLayout();
			wrapperPanelContent.addMember(panel.buildView().getViewContent());
			id = wrapperPanelContent.getID();

			return wrapperPanelContent;
		}

		public String getID() {
			return id;
		}
	}

}