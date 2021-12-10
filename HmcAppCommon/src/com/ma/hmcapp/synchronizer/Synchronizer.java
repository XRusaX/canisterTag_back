package com.ma.hmcapp.synchronizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.ma.appcommon.datasource.EM;
import com.ma.hmcapp.entity.Company;

public abstract class Synchronizer<T> {

	protected Company company;

	protected abstract long getId(T t);

	protected abstract void setId(T t, long id);

	protected abstract String getName(T t);

	protected abstract Company getCompany(T t);

	protected abstract Date getModifTime(T t);

	protected abstract Date getStoreTime(T t);

	protected abstract List<T> getModified(EM em, Date lastSync);

	protected abstract T load(EM em, Long id);

	protected abstract T loadByName(EM em, String name);

	protected abstract void store(EM em, T t);

	public Synchronizer(Company company) {
		this.company = company;
	}

	public List<T> sync(EM em, List<T> list, Date lastSync) {
		List<T> updated = new ArrayList<>();
		if (list != null)
			updated = updateList(em, list);
		List<T> changedByOthers = getModified(em, lastSync);
		changedByOthers.removeAll(updated);
		return changedByOthers;
	}

	private List<T> updateList(EM em, List<T> list) {
		List<T> changed = new ArrayList<>();
		list.forEach(o -> updateObject(em, o, changed));
		return changed;
	}

	private void updateObject(EM em, T ext, List<T> changed) {

		T stored = null;

		long extId = getId(ext);

		if (extId != 0) {
			stored = load(em, extId);
			if (stored == null) {
				return;
			} else if (getCompany(stored) != company) {
				// throw new RuntimeException("Компания оператора и владелец МГЦ
				// не совпадают");
				// msgLogger.add(null, Severity.WARNING, "Компания оператора и
				// владелец МГЦ не совпадают");
				return;
			}
		} else {
			stored = loadByName(em, getName(ext));
		}

		if (stored == null || getModifTime(stored).getTime() < getModifTime(ext).getTime()) {
			if (stored != null)
				setId(ext, getId(stored));
			store(em, ext);
			if (extId != 0)
				changed.add(ext);
		}
	}

}
