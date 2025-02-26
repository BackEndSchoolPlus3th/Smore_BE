package com.meossamos.smore.domain.member.member.repository;

import com.meossamos.smore.domain.member.member.entity.MemberDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberDocRepository extends ElasticsearchRepository<MemberDoc, String> {
}
