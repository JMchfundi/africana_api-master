package co.ke.tucode.admin.repositories.houserepos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import co.ke.tucode.admin.entities.houseentities.ProjectLocationPricingData;

public interface ProjectLocationPricingDataRepo extends JpaRepository<ProjectLocationPricingData, Integer> {
    // public List<ProjectInfo> findByProjectname(String projectname);
    // public boolean existsByProjectname(String name); 
    // public void deleteByProjectname(String name);
}
