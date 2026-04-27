package com.edsuuu.list.service;

import com.edsuuu.list.database.model.ListEntity;
import com.edsuuu.list.database.repository.ListJpaRepository;
import com.edsuuu.list.dto.CreateListDTO;
import com.edsuuu.list.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListService {

    private final ListJpaRepository listRepository;

    public ListService(ListJpaRepository listRepository) {
        this.listRepository = listRepository;
    }

    public List<ListEntity> listAll() {
        return listRepository.findAll();
    }

    public ListEntity createList(CreateListDTO dto) {
        ListEntity newEntity = new ListEntity();
        newEntity.setName(dto.getName());
        newEntity.setDescription(dto.getDescription());
        
        return listRepository.save(newEntity);
    }

    public ListEntity updateList(Long id, CreateListDTO dto) {
        ListEntity entityToUpdate = listRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Lista não encontrada com o id: " + id));

        entityToUpdate.setName(dto.getName());
        entityToUpdate.setDescription(dto.getDescription());

        return listRepository.save(entityToUpdate);
    }

    public void deleteList(Long id) {
        if (!listRepository.existsById(id)) {
            throw new NotFoundException("Lista não encontrada com o id: " + id);
        }
        listRepository.deleteById(id);
    }
}
