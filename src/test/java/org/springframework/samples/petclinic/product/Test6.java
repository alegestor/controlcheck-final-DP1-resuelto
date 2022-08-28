package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.care.CareService;
import org.springframework.samples.petclinic.care.Care;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test6 {
    @Autowired
    CareService cs;
    

    @Test
    public void test6(){
        validateFindCaresByPetType();
        validateFindCaresByPetTypeNotFound();
    }

    public void validateFindCaresByPetType(){
        String petTypeName="dog";
        List<Care> cares=cs.getAllCompatibleCares(petTypeName);
        assertNotNull(cares, "getAllCompatibleCares by petTypeName is returning null");
        assertFalse(cares.isEmpty(), "The set of compabile cares for petType 'dog' is empty, but it should have 2 elements");
    }
    
    public void validateFindCaresByPetTypeNotFound(){
        String petTypeName="unicorn";
        List<Care> cares=cs.getAllCompatibleCares(petTypeName);
        assertNotNull(cares, "getAllCompatibleCares by petTypeName is returning null");
        assertTrue(cares.isEmpty(), "The set of compabile cares for petType 'unicorn' should be empty");
    }

}
