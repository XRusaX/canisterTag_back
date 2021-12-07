package com.ma.hmcapp.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ma.hmcdb.entity.rfid.Report;

@Repository
public interface ReportRepository extends JpaRepository<Report, Long> {
}
