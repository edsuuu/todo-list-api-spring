package com.edsuuu.list.database.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Builder
@Table(name = "lists")
public class ListEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * FetchType.LAZY (Lazy Loading): O JPA só vai buscar o usuário no banco de dados quando você 
     * tentar acessar essa propriedade no código (ex: list.getUser().getName()). Melhora a performance.
     * 
     * FetchType.EAGER (Eager Loading): O JPA sempre vai fazer um JOIN e buscar o usuário junto 
     * com a lista logo de cara. Pode deixar a aplicação lenta se tiver muitos dados (problema do N+1).
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String description;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private java.time.LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private java.time.LocalDateTime updatedAt;


    // Para criar uma tabela pivô (associativa) em relações Muitos-para-Muitos (N:N),
    // você pode usar a anotação @ManyToMany com @JoinTable, ou criar uma nova 
    // entidade (model) separada que represente essa tabela intermediária.
}
