package com.meossamos.smore.domain.alarm.alarm.repository;

import com.meossamos.smore.domain.alarm.alarm.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm, Long> {

    @Query("select a from Alarm a where a.receiver.id= :memberId")
    List<Alarm> findAllByMemberId(@Param("memberId") Long memberId);


}

