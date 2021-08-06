package ru.aoit.hmcserver.client;

import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.dom.client.Style.FontWeight;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;

import ru.aoit.hmcserver.shared.ConnectionStatus;
import ru.aoit.hmcserver.shared.ConnectionStatus.ConnectionType;
import ru.nppcrts.common.gwt.client.ui.datagrid.DataGrid1;
import ru.nppcrts.common.gwt.client.ui.panel.LayoutHeaderPanel;

public class ConnectionList extends ResizeComposite {

	private DataGrid1<ConnectionStatus> grid = new DataGrid1<>(50, sc -> sc == null ? null : sc.sessionId, false);
	private ConnectionType connectionType;

	public ConnectionList(String title, ConnectionType connectionType) {
		this.connectionType = connectionType;
		grid.addTextColumn("Время", cs -> cs.connectingTime.toString());
		grid.addTextColumn("Хост", cs -> cs.host);
		grid.addTextColumn("Сессия", cs -> cs.sessionId);
		grid.addTextColumn("Пользователь", cs -> cs.userName);

		Label label = new Label(title);
		label.getElement().getStyle().setFontWeight(FontWeight.BOLD);

		initWidget(new LayoutHeaderPanel(label, grid));

		getElement().getStyle().setMargin(1, Unit.EM);
	}

	public void setRowData(List<ConnectionStatus> list) {
		grid.setRowData(list.stream().filter(cs -> cs.connectionType == connectionType).collect(Collectors.toList()));
	}

}
