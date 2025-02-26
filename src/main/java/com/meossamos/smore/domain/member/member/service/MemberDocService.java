package com.meossamos.smore.domain.member.member.service;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import com.meossamos.smore.domain.member.member.repository.MemberDocRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberDocService {
    private final MemberDocRepository memberDocRepository;
    private final ElasticsearchClient elasticsearchClient;
}
