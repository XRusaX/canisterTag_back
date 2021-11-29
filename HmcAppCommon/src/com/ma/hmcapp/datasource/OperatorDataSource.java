package com.ma.hmcapp.datasource;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ma.appcommon.datasource.CommonData;
import com.ma.appcommon.datasource.DataSourceImpl;
import com.ma.appcommon.datasource.EM;
import com.ma.appcommon.db.Database2;
import com.ma.appcommon.logger.MsgLoggerImpl;
import com.ma.appcommon.shared.Filter;
import com.ma.common.shared.Pair;
import com.ma.hmcdb.entity.Company;
import com.ma.hmcdb.entity.Operator;

@Component
public class OperatorDataSource extends DataSourceImpl<Operator> {

	@Autowired
	private Database2 database;

	@Autowired
	private CommonData commonDataImpl;

	@Autowired
	private MsgLoggerImpl msgLogger;

	@PostConstruct
	private void init() {
		super.init(Operator.class, database);
		commonDataImpl.addDataSource(Operator.class, this);
	}

	public Operator loadByName(EM em, String name, Company company) {
		return Database2.select(em.em, Operator.class).whereEQ("name", name).whereEQ("company", company)
				.getResultStream().findFirst().orElse(null);
	}

	@Override
	protected synchronized void onBeforeStore(EM em, Operator obj) {
		obj.storeTime = new Date();
	}

	@Override
	public void delete(EM em, Collection<Long> list) {
		list.forEach(id -> {
			Operator op = load(em, id);
			op.removed = true;
			store(em, op);
		});
	}

	@Override
	public Pair<String, List<Operator>> loadRange(EM em, Filter filter, int[] range) {
		filter.addNEQ("removed", "true");
		return super.loadRange(em, filter, range);
	}

	@Override
	public int getCount(EM em, Filter filter) {
		filter.addNEQ("removed", "true");
		return super.getCount(em, filter);
	}

}
