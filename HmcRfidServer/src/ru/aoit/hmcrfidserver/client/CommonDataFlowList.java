package ru.aoit.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.shared.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import ru.nppcrts.common.gwt.client.AlertAsyncCallback;
import ru.nppcrts.common.gwt.client.RequestRepeater;
import ru.nppcrts.common.gwt.client.commondata.CommonDataProvider;
import ru.nppcrts.common.gwt.client.commondata.CommonDataService;
import ru.nppcrts.common.gwt.client.commondata.CommonDataServiceAsync;
import ru.nppcrts.common.gwt.client.commondata.ObjectEditorDialog;
import ru.nppcrts.common.gwt.client.ui.ContextMenu;
import ru.nppcrts.common.shared.cd.CDClass;
import ru.nppcrts.common.shared.cd.CDObject;

public class CommonDataFlowList extends FlowList<CDObject> {
	private final CommonDataServiceAsync service = GWT.create(CommonDataService.class);

	private int lock;
	private Class<?> clazz;
	private CDClass cdClass;

	private CommonDataProvider p = new CommonDataProvider() {

		@Override
		public void getDataVersion(AsyncCallback<String> callback) {
			service.getDataVersion(clazz.getName(), callback);
		}

		@Override
		public void getData(int[] startAndLength, AsyncCallback<List<CDObject>> callback) {
			service.loadRange(clazz.getName(), null, null, callback);
		}

		@Override
		public void getCount(AsyncCallback<Integer> callback) {
		}

		@Override
		public void store(CDObject values, AsyncCallback<Long> callback) {
			service.store(clazz.getName(), values, callback);
		}

		@Override
		public void newCDObject(AsyncCallback<CDObject> callback) {
			service.newCDObject(clazz.getName(), callback);
		}

		@Override
		public void delete(List<Long> ids, AsyncCallback<Void> callback) {
			service.delete(clazz.getName(), ids, callback);
		}
	};

	@Override
	protected Widget createWidget(CDObject t) {
		return new Label(t.get("serialNumber"));
	}

	@Override
	protected void prepareContextMenu(ContextMenu menu, CDObject t, Runnable onPrepared) {

		if (t == null)
			menu.addItem("Создать", () -> {
				p.newCDObject(new AlertAsyncCallback<>(cdObject -> {

					new ObjectEditorDialog("Создать", cdClass, cdObject) {
						@Override
						protected void onOK(CDObject values) {
							p.store(values, new AlertAsyncCallback<>(id -> {
								// afterStoreNewObject(id, additionals);
								refresh();
								values.set("id", id);
								setSel(values);
							}));
						}

						// @Override
						// protected Widget getAdditionals() {
						// return additionals;
						// }

						@Override
						protected void getLookup(String className, AsyncCallback<List<CDObject>> callback) {
							service.loadRange(className, null, null, callback);
						}
					}.center();
				}));
			});

		if (t != null)
			menu.addItem("Удалить", () -> {
				ArrayList<Long> ids = new ArrayList<>();
				ids.add(t.getLong("id"));
				p.delete(ids, new AlertAsyncCallback<>(null));
			});

		onPrepared.run();
	}

	public CommonDataFlowList(Class<?> clazz) {
		this.clazz = clazz;
		service.getCDClass(clazz.getName(), new AlertAsyncCallback<>(cdClass -> {
			CommonDataFlowList.this.cdClass = cdClass;

			RequestRepeater<String> requestRepeater = new RequestRepeater<String>() {
				private String dataVersion = "";

				@Override
				protected void request(AsyncCallback<String> callback) {
					p.getDataVersion(callback);
				}

				@Override
				protected void accept(String ver) {
					if (!ver.equals(dataVersion))
						refresh();
					dataVersion = ver;
				}

				@Override
				protected boolean needsRequest() {
					return lock == 0 /* && needsRequest() */;
				}
			};
			requestRepeater.start(2000);
			requestRepeater.request();
		}));

	}

	private void refresh() {
		p.getData(null, new AlertAsyncCallback<>(list -> setData(list)));
	}

}
