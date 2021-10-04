package com.ma.hmcrfidserver.client;

import java.util.List;
import java.util.Set;

import com.google.gwt.core.shared.GWT;
import com.ma.appcommon.shared.auth.AuthUtils;
import com.ma.appcommon.shared.auth.UserData;
import com.ma.common.gwt.client.AlertAsyncCallback;
import com.ma.common.gwt.client.auth.Login;
import com.ma.common.gwt.client.auth.LoginService;
import com.ma.common.gwt.client.auth.LoginServiceAsync;
import com.ma.common.gwt.client.auth.UserDialog;
import com.ma.common.gwt.client.commondata.CommonListPanel;
import com.ma.common.gwt.client.commondata.CommonListPanelWrapper;
import com.ma.common.gwt.client.ui.ContextMenu;
import com.ma.common.shared.cd.CDObject;
import com.ma.common.shared.eventbus.EventBus;
import com.ma.hmcdb.shared.Company;
import com.ma.hmcdb.shared.Company.CompanyType;

public class CompaniesPanel extends CommonListPanelWrapper {
	private static final LoginServiceAsync loginService = GWT.create(LoginService.class);

	public CompaniesPanel(EventBus eventBus, String name, CompanyType companyType, List<String> showFields) {
		super(new CommonListPanel(name, 2000) {
			@Override
			protected void prepareContextMenu(ContextMenu menu, Set<CDObject> set) {
				if (set.size() == 1 && Login.user.hasPermission(UserData.PERMISSION_USERS)) {
					CDObject company = set.iterator().next();
					menu.addItem("Создать пользователя", () -> {
						new UserDialog("Новый пользователь", true, true, true, Login.permissions) {
							@Override
							protected void onOk(String user, String passwordNew, String passwordOld, String email,
									List<String> permissions) {
								String hashNew = AuthUtils.getPwdHash(user, passwordNew);
								String hashCur = AuthUtils.getPwdHash(Login.user.name, passwordOld);
								loginService.addUser(user, hashNew, hashCur, permissions, email, company.getLong("id"),
										new AlertAsyncCallback<Void>(null));
							}
						}.center();
					});
				}

				super.prepareContextMenu(menu, set);
			}
		}.showFields(showFields), Company.class, eventBus);
		if (companyType != null)
			filter.stringMap.put("companyType",
					/* CompanyType.class.getName() + ":" + */ companyType.name());
	}

}