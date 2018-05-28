package com.csgroup.repositories;

import com.csgroup.model.DbEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;


@Transactional()
public interface DbEventRepository extends JpaRepository<DbEvent, String> {

}
