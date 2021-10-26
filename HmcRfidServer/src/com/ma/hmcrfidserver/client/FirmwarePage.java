package com.ma.hmcrfidserver.client;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SuggestBox;
import com.google.gwt.user.client.ui.SuggestOracle;
import com.ma.common.gwtapp.client.AlertAsyncCallback;
import com.ma.common.gwtapp.client.ui.Gap;
import com.ma.common.gwtapp.client.ui.UploadForm;
import com.ma.common.gwtapp.client.ui.panel.HorPanel;
import com.ma.common.gwtapp.client.ui.panel.PropertiesPanel;
import com.ma.common.gwtapp.client.ui.panel.VertPanel;

public class FirmwarePage extends Composite {
	private final HmcServiceAsync hmcService = GWT.create(HmcService.class);

	private UploadForm uploadForm = new UploadForm("api/firmware", true) {
		@Override
		public void onComplete(SubmitCompleteEvent event) {
			updateList();
		}
	};

	private SuggestBox sb = new SuggestBox(new SuggestOracle() {
		@Override
		public void requestSuggestions(Request request, Callback callback) {
			List<Suggestion> suggestions = new ArrayList<>();
			types.forEach(t -> suggestions.add(new Suggestion() {

				@Override
				public String getReplacementString() {
					return t;
				}

				@Override
				public String getDisplayString() {
					return t;
				}
			}));

			callback.onSuggestionsReady(request, new Response(suggestions));
		}
	});

	private PropertiesPanel propertiesPanel = new PropertiesPanel("---");

	private List<String> types;

	public FirmwarePage() {

		sb.addValueChangeHandler(new ValueChangeHandler<String>() {
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				uploadForm.setAction(getUrl(sb.getText()));
			}
		});

		updateList();
		initWidget(new VertPanel().setSp(8).add(propertiesPanel, new HTML("<b>Загрузка прошивки</b>"),
				new HorPanel(new Label("Версия платы"), new Gap(10, 1)).add1(sb), uploadForm));
	}

	private void updateList() {

		hmcService.getFirmwareList(new AlertAsyncCallback<>(list -> {
			types = list.keySet().stream().collect(Collectors.toList());
		}));

		propertiesPanel.removeAllRows();
		hmcService.getFirmwareList(new AlertAsyncCallback<>(map -> {
			map.forEach((line, value) -> {
				propertiesPanel.add(line, new Anchor(value, getUrl(line)));
			});
		}));

	}

	private static String getUrl(String line) {
		return "api/firmware?line=" + line;
	}

}
