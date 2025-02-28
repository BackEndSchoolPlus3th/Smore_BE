package com.meossamos.smore.domain.main.main.repository;

import com.meossamos.smore.domain.main.main.entity.Main;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MainRepository extends JpaRepository<Main, Long> {
}
