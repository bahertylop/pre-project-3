package org.example.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "car_position")
public class CarPosition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "brand_id", nullable = false)
    private CarBrand brand;

    @ManyToOne
    @JoinColumn(name = "model_id", nullable = false)
    private CarModel model;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "year_from")
    private Integer yearFrom;

    @Column(name = "year_before")
    private Integer yearBefore;

    @Column(name = "mileage_from")
    private Integer mileageFrom;

    @Column(name = "mileage_before")
    private Integer mileageBefore;

    @OneToMany(mappedBy = "position", cascade = CascadeType.ALL)
    private List<PositionPrice> prices;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CarPosition that = (CarPosition) o;
        return Objects.equals(brand.getId(), that.brand.getId()) &&
                Objects.equals(model.getId(), that.model.getId()) &&
                Objects.equals(user.getId(), that.user.getId()) &&
                Objects.equals(yearFrom, that.yearFrom) &&
                Objects.equals(yearBefore, that.yearBefore) &&
                Objects.equals(mileageFrom, that.mileageFrom) &&
                Objects.equals(mileageBefore, that.mileageBefore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(brand.getId(), model.getId(), user.getId(), yearFrom, yearBefore, mileageFrom, mileageBefore);
    }
}
