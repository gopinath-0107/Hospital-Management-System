package com.hospital.repo;

import com.hospital.entity.Symptom;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SymptomRepository extends JpaRepository<Symptom, Long> {
    List<Symptom> findByDepartmentId(Long departmentId);
    boolean existsBySymptomName(String symptomName);
    List<Symptom> findBySymptomNameIn(List<String> symptomNames);
}
