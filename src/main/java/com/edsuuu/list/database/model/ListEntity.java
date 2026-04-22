package com.edsuuu.list.database.model;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ListEntity {
    private Long id;
    private String userId;
    private String name;
    private String description;
}
