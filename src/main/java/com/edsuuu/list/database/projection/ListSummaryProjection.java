package com.edsuuu.list.database.projection;

public interface ListSummaryProjection {
    Long getId();
    String getName();
    String getDescription();
    
    // O Spring Data JPA é inteligente o suficiente para entender que
    // "getUserName" deve buscar a propriedade "name" dentro da propriedade "user" da ListEntity
    String getUserName();
}
