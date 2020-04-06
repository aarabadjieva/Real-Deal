package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Car;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, Integer> {
    Optional<Car> findByMakeAndModelAndKilometers(String make, String model, int kilometers);

    @Query("select c from Car c " +
            "order by size(c.pictures) desc , c.make")
    List<Car> findByCarsCountOrderByCarsCountAndMakeAsc();
}
