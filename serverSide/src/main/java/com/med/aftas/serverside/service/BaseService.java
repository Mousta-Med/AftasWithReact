package com.med.aftas.serverside.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BaseService<C, T, R> {
    R save(C c);

    void delete(T id);

    R update(T id, C c);

    R findOne(T id);

    List<R> findAll();

    Page<R> findWithPagination(Pageable pageable);
}
