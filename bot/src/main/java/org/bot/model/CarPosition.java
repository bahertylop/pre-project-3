package org.bot.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Table(name = "car_position")
public class CarPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "brand")
    private String brandName;

    @Column(name = "model")
    private String modelName;

    @Column(name = "year_from")
    private Integer yearFrom;

    @Column(name = "year_to")
    private Integer yearTo;

    @Column(name = "mileage_from")
    private  Integer mileageFrom;

    @Column(name = "mileage_before")
    private  Integer mileageBefore;
}
