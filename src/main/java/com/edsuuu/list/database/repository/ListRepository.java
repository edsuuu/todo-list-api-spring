package com.edsuuu.list.database.repository;

import com.edsuuu.list.database.model.ListEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class ListRepository {

    // banco em memoria pra simular o crud
    private final List<ListEntity> lists = new ArrayList<>();

    public List<ListEntity> findAll() {
        return lists;
    }

    public ListEntity save(ListEntity entity) {
        // gerando um id simples
        entity.setId((long) (lists.size() + 1));
        lists.add(entity);
        return entity;
    }

    public Optional<ListEntity> findById(Long id) {
        return lists.stream()
                .filter(entity -> entity.getId().equals(id))
                .findFirst();
    }

    public ListEntity update(Long id, ListEntity updatedEntity) {
        for (int i = 0; i < lists.size(); i++) {
            if (lists.get(i).getId().equals(id)) {
                updatedEntity.setId(id);
                lists.set(i, updatedEntity);
                return updatedEntity;
            }
        }
        return null;
    }

    public boolean delete(Long id) {
        return lists.removeIf(entity -> entity.getId().equals(id));
    }
}
