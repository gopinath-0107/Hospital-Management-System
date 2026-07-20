package com.hospital.repo;

import com.hospital.entity.Medicine;
import com.hospital.enums.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine, Long> {

    Optional<Medicine> findByMedicineName(String medicineName);

    Optional<Medicine> findByMedicineCode(String medicineCode);

    List<Medicine> findByStatus(Status status);

    List<Medicine> findByCompany(String company);

    long countByStockQuantityLessThan(int quantity);

    boolean existsByMedicineName(String medicineName);

    boolean existsByMedicineCode(String medicineCode);

    @Query("SELECT COALESCE(SUM(m.stockQuantity), 0) FROM Medicine m")
    Long getTotalMedicineStock();

}