package softuni.exam.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entities.Offer;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Integer> {

    Optional<Offer> findByDescriptionAndAddedOn(String description, LocalDate addedOn);
}
