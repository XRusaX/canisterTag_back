package com.ma.hmcapp.rest;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ma.hmcapp.entity.Hmc;

@Repository
public interface HmcRepository extends JpaRepository<Hmc, Long> {
}
