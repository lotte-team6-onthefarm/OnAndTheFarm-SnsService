package com.team6.onandthefarmsnsservice.repository;

import java.util.List;

import com.team6.onandthefarmsnsservice.entity.Scrap;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface ScrapRepository extends CrudRepository<Scrap,Long> {
	@Query("select s from Scrap s where s.memberId =:memberId")
	List<Scrap> findScrapListByMemberId(@Param("memberId") Long memberId);
}
