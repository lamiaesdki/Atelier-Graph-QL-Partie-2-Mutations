package ma.xproce.inventoryservice.dao.interfaces;

import ma.xproce.inventoryservice.dao.entities.Creator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CreatorRepository extends JpaRepository<Creator, Long> {
    Optional<Creator> findByEmail(String email);
}
