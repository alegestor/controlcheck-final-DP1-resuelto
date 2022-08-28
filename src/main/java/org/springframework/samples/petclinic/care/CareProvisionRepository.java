package org.springframework.samples.petclinic.care;

import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;


public interface CareProvisionRepository extends CrudRepository<CareProvision, Integer>{
    List<CareProvision> findAll();   
 
    @Query("SELECT c FROM Care c")
	List<Care> findAllCares();
    
    //TEST 6
    
    @Query("SELECT c FROM Care c JOIN PetType pt WHERE pt.name = ?1")
    List<Care> findCompatibleCares(String petTypeName);
    
    @Query("SELECT c FROM Care c WHERE c.name = ?1")
    Care findCareByName(String name);
    
    //TEST 8
    
    /**
     * @param visitId
     * @return los cuidados realizados durante una visita
     */
    @Query("SELECT cp FROM CareProvision cp WHERE cp.visit.id = ?1")
    List<CareProvision> getCaresPerformed(Integer visitId);
    
}
