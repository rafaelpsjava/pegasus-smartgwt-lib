package xptopackage.client.ds;

import java.util.List;
import java.util.ArrayList;

import br.com.pegasus.solutions.zws.gwt.client.api.service.MarcaService;
import br.com.pegasus.solutions.zws.gwt.client.api.service.MarcaServiceAsync;
import br.com.pegasus.solutions.zws.gwt.client.util.DictionaryUtil;
import br.com.pegasus.solutions.smartgwt.lib.client.rpc.impl.GenericGwtRpcDataSource;
import br.com.pegasus.solutions.smartgwt.lib.client.view.impl.advanced.bar.PagingToolBar;
import br.com.pegasus.solutions.zws.gwt.shared.dto.MarcaDTO;

import com.google.gwt.core.client.GWT;
import br.com.pegasus.solutions.smartgwt.lib.client.factory.FM;
import com.google.gwt.i18n.client.Dictionary;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.data.fields.DataSourceTextField;
import com.smartgwt.client.data.fields.DataSourceDateField;
import com.smartgwt.client.data.fields.DataSourceBooleanField;


public final class MarcaDS extends GenericGwtRpcDataSource<MarcaDTO, ListGridRecord, MarcaServiceAsync> {
	private PagingToolBar pagingToolBar;

	private MarcaDS() {
	}

	public static MarcaDS getInstance() {
		return new MarcaDS();
	}
	
	@Override
	public void copyValues(ListGridRecord from, MarcaDTO to) {
		to.setId(from.getAttributeAsString(getDC().get("id_id")));
		to.setNome(from.getAttributeAsString(getDC().get("id_nome")));
		to.setDataCadastro(from.getAttributeAsDate(getDC().get("id_dataCadastro")));
		to.setDataAlteracao(from.getAttributeAsDate(getDC().get("id_dataAlteracao")));
		to.setUsuarioUltimoDUC(from.getAttributeAsString(getDC().get("id_usuarioUltimoDUC")));
		to.setFlagEnabled((Boolean) from.getAttributeAsObject(getDC().get("id_flagEnabled")));

	}

	@Override
	public void copyValues(MarcaDTO from, ListGridRecord to) {
		to.setAttribute(getDC().get("id_id"), from.getId());
		to.setAttribute(getDC().get("id_nome"), from.getNome());
		to.setAttribute(getDC().get("id_dataCadastro"), from.getDataCadastro());
		to.setAttribute(getDC().get("id_dataAlteracao"), from.getDataAlteracao());
		to.setAttribute(getDC().get("id_usuarioUltimoDUC"), from.getUsuarioUltimoDUC());
		to.setAttribute(getDC().get("id_flagEnabled"), from.getFlagEnabled());
		
	}

	@Override
	public List<DataSourceField> getDataSourceFields() {
		List<DataSourceField> fields = new ArrayList<DataSourceField>();

		DataSourceTextField idField = FM.newPKDataSourceTextField(getDC(), "id_id");
		fields.add(idField);

		DataSourceDateField dataCadastroField = FM.newDataSourceDateField(getDC(), "id_dataCadastro");
		fields.add(dataCadastroField);

		DataSourceDateField dataAlteracaoField = FM.newDataSourceDateField(getDC(), "id_dataAlteracao");
		fields.add(dataAlteracaoField);

		DataSourceBooleanField flagEnabledField = FM.newDataSourceBooleanField(getDC(), "id_flagEnabled");
		fields.add(flagEnabledField);


		return fields;
	}

	@Override
	public MarcaDTO getNewDataObjectInstance() {
		return new MarcaDTO();
	}

	@Override
	public ListGridRecord getNewRecordInstance() {
		return new ListGridRecord();
	}

	@Override
	public MarcaServiceAsync getServiceAsync() {
		return GWT.create(MarcaService.class);
	}

	public Dictionary getDC() {
		return DictionaryUtil.getMarca();
	}
	
	@Override
	public PagingToolBar getPagingToolbar() {
		return pagingToolBar;
	}

	@Override
	public void setPagingToolbar(PagingToolBar pagingToolBar) {
		this.pagingToolBar = pagingToolBar;
	}
	
}