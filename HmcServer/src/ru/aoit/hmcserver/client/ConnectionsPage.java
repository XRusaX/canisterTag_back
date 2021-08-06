package ru.aoit.hmcserver.client;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.ResizeComposite;

import ru.aoit.hmcserver.shared.ConnectionStatus;
import ru.aoit.hmcserver.shared.ConnectionStatus.ConnectionType;
import ru.nppcrts.common.gwt.client.RequestRepeater;
import ru.nppcrts.common.gwt.client.ui.panel.DockLayoutPanelX;

public class ConnectionsPage extends ResizeComposite {
	private final ServiceAsync service = GWT.create(Service.class);

	public ConnectionsPage() {

		ConnectionList webConnections = new ConnectionList("WEB", ConnectionType.WEB);
		ConnectionList writerConnections = new ConnectionList("Запись меток", ConnectionType.WRITER);

		new RequestRepeater<List<ConnectionStatus>>() {
			@Override
			protected void request(AsyncCallback<List<ConnectionStatus>> callback) {
				service.getConnectionStatusList(callback);
			}

			@Override
			protected void accept(List<ConnectionStatus> list) {
				webConnections.setRowData(list);
				writerConnections.setRowData(list);
			}
		}.start(2000);

		initWidget(new DockLayoutPanelX(Unit.PCT).addW(webConnections, 50).addX(writerConnections));
	}
}
