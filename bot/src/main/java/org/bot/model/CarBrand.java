package org.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "car_brand")
public class CarBrand {

    @Id
    @SequenceGenerator(name = "brand_id_seq", sequenceName = "brand_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "brand_id_seq")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "value")
    private String value;
}
