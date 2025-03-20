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
public class CreateCarPositionData {

    @Id
    @SequenceGenerator(name = "car_position_id_seq", sequenceName = "car_position_id_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "car_position_id_seq")
    private Long id;

    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "car_brand")
    private String brandName;

    @Column(name = "car_model")
    private String modelName;

    @Column(name = "year_from")
    private Integer yearFrom;

    @Column(name = "year_before")
    private Integer yearTo;

    @Column(name = "mileage_from")
    private  Integer mileageFrom;

    @Column(name = "mileage_before")
    private  Integer mileageBefore;
}
