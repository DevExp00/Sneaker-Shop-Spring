package com.marketplace.demo.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "t_item_sizes")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemSize {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "size", length = 2)
    private Integer size;

}
