package com.edsuuu.list.database.repository;

import com.edsuuu.list.database.model.ListEntity;
import com.edsuuu.list.database.projection.ListSummaryProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ListJpaRepository extends JpaRepository<ListEntity, Long> {

    // Método que retorna a projection em vez da entidade completa
    List<ListSummaryProjection> findAllProjectedBy();

    // Exemplo 1: JPQL (Java Persistence Query Language)
    // O JPQL faz a consulta nas Entidades Java e seus atributos (ListEntity), não nas tabelas do banco.
    @Query("SELECT l FROM ListEntity l WHERE l.name LIKE %:name%")
    List<ListEntity> findByNameContainingWithJpql(@Param("name") String name);

    // Exemplo 2: Native Query
    // A Native Query executa o SQL puro diretamente no banco, ou seja, consulta a tabela (lists) e suas colunas originais.
    @Query(value = "SELECT * FROM lists WHERE description LIKE %:description%", nativeQuery = true)
    List<ListEntity> findByDescriptionContainingWithNative(@Param("description") String description);

    // Exemplo 3: JPQL com Paginação
    // Passando o objeto Pageable, o Spring automaticamente fatia os resultados (ex: página 0, 10 itens por página)
    @Query("SELECT l FROM ListEntity l WHERE l.name LIKE %:name%")
    Page<ListEntity> findByNameContainingWithJpqlPaged(@Param("name") String name, Pageable pageable);

    // Exemplo 4: Native Query com Paginação
    // Para queries nativas com paginação, é uma boa prática (e as vezes necessário) fornecer a countQuery
    // para o Spring saber o total de elementos existentes no banco.
    @Query(
        value = "SELECT * FROM lists WHERE description LIKE %:description%",
        countQuery = "SELECT count(*) FROM lists WHERE description LIKE %:description%",
        nativeQuery = true
    )
    Page<ListEntity> findByDescriptionContainingWithNativePaged(@Param("description") String description, Pageable pageable);
}
