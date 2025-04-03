package br.com.gabxdev.model;

import br.com.gabxdev.Audit.Auditable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "groups")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id", callSuper = false)
public class Group extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    private String description;
}
