package org.backend.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "position_price")
public class PositionPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private CarPosition position;

    @Column(name = "price")
    private Integer price;

    @Column(name = "date")
    private LocalDate date;
}
