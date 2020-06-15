package com.example.console.app.service;

import com.example.console.app.model.AbstractEntity;
import com.example.console.app.repository.CommonRepository;
import org.springframework.beans.BeanUtils;

import java.util.List;

public abstract class AbstractService<E extends AbstractEntity, R extends CommonRepository<E>> implements CommonService<E> {

    protected final R repository;

    protected AbstractService(R repository) {
        this.repository = repository;
    }

    @Override
    public List<E> getAll() {
        return repository.findAll();
    }

    @Override
    public E findById(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public E create(E entity) {
        return repository.save(entity);
    }

    @Override
    public E update(Long id, E customEntity) {
        E entity = repository.getOne(id);
        BeanUtils.copyProperties(customEntity, entity, "id");
        return repository.save(entity);
    }

    public E update(Long id, E customEntity, String[] ignoreProperties) {
        E entity = repository.getOne(id);
        BeanUtils.copyProperties(customEntity, entity, ignoreProperties);
        return repository.save(entity);
    }

    @Override
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
