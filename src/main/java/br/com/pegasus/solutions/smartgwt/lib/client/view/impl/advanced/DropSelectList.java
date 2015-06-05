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

import com.smartgwt.client.data.Record;
import com.smartgwt.client.data.RecordList;
import com.smartgwt.client.types.VerticalAlignment;
import com.smartgwt.client.widgets.TransferImgButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.grid.ListGrid;
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.layout.HStack;
import com.smartgwt.client.widgets.layout.VStack;

public final class DropSelectList {
	private ListGrid myList1;
	private ListGrid myList2;

	public DropSelectList(final ListGrid myList1, final ListGrid myList2) {
		this.myList1 = myList1;
		this.myList2 = myList2;
	}

	public HStack build(Record[] myList1Data, Record[] myList2Data) {
		HStack hStack = new HStack(10);
		hStack.setHeight(160);

		myList1.setData(myList1Data);
		hStack.addMember(myList1);

		VStack transferStack = new VStack(3);
		transferStack.setWidth(20);
		transferStack.setAlign(VerticalAlignment.CENTER);

		myList2.setData(myList2Data);
		myList2.setCanAcceptDroppedRecords(true);
		myList2.setCanReorderRecords(true);

		TransferImgButton right = buildRigth();
		TransferImgButton rightAll = buildRigthAll();
		TransferImgButton left = buildLeft();
		TransferImgButton leftAll = biuldLeftAll();

		transferStack.addMember(right);
		transferStack.addMember(left);
		transferStack.addMember(rightAll);
		transferStack.addMember(leftAll);

		hStack.addMember(transferStack);
		hStack.addMember(myList2);

		VStack modifyStack = new VStack(3);
		modifyStack.setWidth(20);
		modifyStack.setAlign(VerticalAlignment.CENTER);

		TransferImgButton up = buildUp();
		TransferImgButton upFirst = buildUpFirst();
		TransferImgButton down = buildDown();
		TransferImgButton downLast = buildDownLast();

		modifyStack.addMember(upFirst);
		modifyStack.addMember(up);
		modifyStack.addMember(down);
		modifyStack.addMember(downLast);

		hStack.addMember(modifyStack);

		return hStack;
	}

	private TransferImgButton buildDownLast() {
		TransferImgButton downLast = new TransferImgButton(TransferImgButton.DOWN_LAST);
		downLast.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord selectedRecord = myList2.getSelectedRecord();
				if (selectedRecord != null) {
					RecordList rs = myList2.getRecordList();
					int numRecords = rs.getLength();
					int idx = myList2.getRecordIndex(selectedRecord);
					if (idx < numRecords - 1) {
						rs.removeAt(idx);
						rs.addAt(selectedRecord, rs.getLength());
					}
				}
			}
		});
		return downLast;
	}

	private TransferImgButton buildDown() {
		TransferImgButton down = new TransferImgButton(TransferImgButton.DOWN);
		down.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord selectedRecord = myList2.getSelectedRecord();
				if (selectedRecord != null) {
					RecordList rs = myList2.getRecordList();
					int numRecords = rs.getLength();
					int idx = myList2.getRecordIndex(selectedRecord);
					if (idx < numRecords - 1) {
						rs.removeAt(idx);
						rs.addAt(selectedRecord, idx + 1);
					}
				}
			}
		});
		return down;
	}

	private TransferImgButton buildUpFirst() {
		TransferImgButton upFirst = new TransferImgButton(TransferImgButton.UP_FIRST);
		upFirst.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord selectedRecord = myList2.getSelectedRecord();
				if (selectedRecord != null) {
					int idx = myList2.getRecordIndex(selectedRecord);
					if (idx > 0) {
						RecordList rs = myList2.getRecordList();
						rs.removeAt(idx);
						rs.addAt(selectedRecord, 0);
					}
				}
			}
		});
		return upFirst;
	}

	private TransferImgButton buildUp() {
		TransferImgButton up = new TransferImgButton(TransferImgButton.UP);
		up.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				ListGridRecord selectedRecord = myList2.getSelectedRecord();
				if (selectedRecord != null) {
					int idx = myList2.getRecordIndex(selectedRecord);
					if (idx > 0) {
						RecordList rs = myList2.getRecordList();
						rs.removeAt(idx);
						rs.addAt(selectedRecord, idx - 1);
					}
				}
			}
		});
		return up;
	}

	private TransferImgButton buildRigth() {
		TransferImgButton right = new TransferImgButton(TransferImgButton.RIGHT);
		right.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				myList2.transferSelectedData(myList1);
			}
		});
		return right;
	}

	private TransferImgButton buildRigthAll() {
		TransferImgButton rightAll = new TransferImgButton(TransferImgButton.RIGHT_ALL);
		rightAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RecordList recordList = myList1.getRecordList();
				if (recordList != null) {
					for (int i = 0; i < recordList.getLength(); i++) {
						myList2.addData(recordList.get(i));
					}
				}

				myList1.setData(new Record[] {});
				myList2.redraw();
			}
		});
		return rightAll;
	}

	private TransferImgButton buildLeft() {
		TransferImgButton left = new TransferImgButton(TransferImgButton.LEFT);
		left.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				myList1.transferSelectedData(myList2);
			}
		});
		return left;
	}

	private TransferImgButton biuldLeftAll() {
		TransferImgButton leftAll = new TransferImgButton(TransferImgButton.LEFT_ALL);
		leftAll.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				RecordList recordList = myList2.getRecordList();
				if (recordList != null) {
					for (int i = 0; i < recordList.getLength(); i++) {
						myList1.addData(recordList.get(i));
					}
				}

				myList2.setData(new Record[] {});
				myList1.redraw();
			}
		});
		return leftAll;
	}

	public ListGrid getMyList2() {
		return myList2;
	}

}
