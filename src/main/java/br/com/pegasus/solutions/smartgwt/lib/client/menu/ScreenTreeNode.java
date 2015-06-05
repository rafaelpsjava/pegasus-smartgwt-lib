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
package br.com.pegasus.solutions.smartgwt.lib.client.menu;

import com.smartgwt.client.widgets.tree.TreeNode;

public class ScreenTreeNode extends TreeNode {

	public ScreenTreeNode(String name, String nodeID, String parentNodeID, String icon, PanelFactory factory, boolean enabled, String idSuffix) {
		if (enabled)
			setName(name);
		else {
			setName("<span style='color:808080'>" + name + "</span>");
		}
		setNodeID(nodeID.replace("-", "_") + idSuffix);
		setParentNodeID(parentNodeID.replace("-", "_") + idSuffix);
		setIcon(icon);
		setFactory(factory);

		if (nodeID.equals("new-category")) {
			setIsOpen(false);
		} else {
			setIsOpen(true);
		}
	}

	public void setSampleClassName(String name) {
		setAttribute("sampleClassName", name);
	}

	public String getSampleClassName() {
		return getAttribute("sampleClassName");
	}

	public void setFactory(PanelFactory factory) {
		setAttribute("factory", factory);
	}

	public PanelFactory getFactory() {
		return (PanelFactory) getAttributeAsObject("factory");
	}

	public void setNodeID(String value) {
		setAttribute("nodeID", value);
	}

	public String getNodeID() {
		return getAttribute("nodeID");
	}

	public void setParentNodeID(String value) {
		setAttribute("parentNodeID", value);
	}

	public void setName(String name) {
		setAttribute("name", name);
	}

	public String getName() {
		return getAttributeAsString("name");
	}

	public void setIcon(String icon) {
		setAttribute("icon", icon);
	}

	public String getIcon() {
		return getAttributeAsString("icon");
	}

	public void setIsOpen(boolean isOpen) {
		setAttribute("isOpen", isOpen);
	}

	public void setIconSrc(String iconSrc) {
		setAttribute("iconSrc", iconSrc);
	}

	public String getIconSrc() {
		return getAttributeAsString("iconSrc");
	}

}