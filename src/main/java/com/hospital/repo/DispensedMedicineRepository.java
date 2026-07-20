package com.hospital.repo;

import com.hospital.entity.DispensedMedicine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DispensedMedicineRepository extends JpaRepository<DispensedMedicine, Long> {

    List<DispensedMedicine> findByPharmacyDispenseId(Long pharmacyDispenseId);

}