package org.springframework.samples.petclinic.product;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BooleanSupplier;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.care.CareProvision;
import org.springframework.samples.petclinic.care.CareProvisionRepository;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.samples.petclinic.care.Care;
import org.springframework.stereotype.Service;


@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class Test3 {
    @Autowired
    CareProvisionRepository fr;
    
    @Test
    public void test3(){
        testInitialFeeding();
        testInitialFeedingTypes();
    }
    
    public void testInitialFeeding(){
        List<CareProvision> products=fr.findAll();
        assertTrue(products.size()==2, "Exactly two feedings should be present in the DB");

        Optional<CareProvision> p1=fr.findById(1);
        assertTrue(p1.isPresent(),"There should exist a care provision with id:1");
        assertEquals(p1.get().getVisit().getId(),1, "The care with id 1 should be associate to the visit with id 1");
        assertEquals(p1.get().getDuration(),0.5, "The duration of the care provision with id:1 should be 0.5");
        assertEquals(p1.get().getCare().getName(),"Chemical flea removal","The name of the pet of the feeding with id:2 should be 'Chemical flea removal'");

        Optional<CareProvision> p2=fr.findById(2);
        assertTrue(p2.isPresent(),"There should exist a care provision with id:2");
        assertEquals(p2.get().getVisit().getId(),2,"The care with id:2 should be associated to the visit with id 2");
        assertEquals(p2.get().getDuration(),0.25, "The weeks duration of the feeding with id:1 should be 6");
        assertEquals(p2.get().getCare().getName(),"Hair brushing","The name of the care of the care provision with id:1 should be 'Hair brushing'");

    }

    public void testInitialFeedingTypes()
    {
        List<Care> feedingTypes = new ArrayList<Care>();
        try {
            Method findAllCares = CareProvisionRepository.class.getDeclaredMethod("findAllCares");
            if(fr!=null){
                feedingTypes = (List<Care>) findAllCares.invoke(fr);
            }else
                fail("The repository was not injected into the tests, its autowired value was null");
        } catch(NoSuchMethodException e) {
            fail("There is no method findAllCares in CareProvisionRepository", e);
        } catch (IllegalAccessException e) {
            fail("There is no public method findAllCares in CareProvisionRepository", e);
        } catch (IllegalArgumentException e) {
            fail("There is no method findAllCares() in CareProvisionRepository", e);
        } catch (InvocationTargetException e) {
            fail("There is no method findAllCares() in CareProvisionRepository", e);
        }

        assertTrue(feedingTypes.size()==2,"Exactly two feeding types should be present in the DB");
        
        for(Care v:feedingTypes) {
            if(v.getName().equals("Hair brushing")){
                assertEquals(v.getName(),"Hair brushing","The name of the care with id:1 should be 'Dog hair brushing'");
                assertEquals(v.getDescription(),"We will brush the hair of your pet.","The description of the care type with id:1 is not correct");
                assertTrue(containPetTypeNamed(v.getCompatiblePetTypes(),"dog"),"The care with id 1 is not associate to the pet type named 'dog'");
                assertTrue(containPetTypeNamed(v.getCompatiblePetTypes(),"cat"),"The care with id 1 is not associate to the pet type named 'cat'");

            }else if(v.getName().equals("Chemical flea removal")){
                assertEquals(v.getName(),"Chemical flea removal","The name of the care type with id:2 should be 'Chemical flea removal'");
                assertEquals(v.getDescription(),"We will apply strong chemical products in the hair of your pet to remove any kind of flea or insect present.","The description of the care with id:2 is not correct");
                assertTrue(containPetTypeNamed(v.getCompatiblePetTypes(),"dog"),"The care with id 2 is not associate to the pet type named 'dog'");
                assertTrue(containPetTypeNamed(v.getCompatiblePetTypes(),"cat"),"The care with id 2 is not associate to the pet type named 'cat'");
            }else {
                fail("The name of the care is note correct");
            }
        }
        
    }

    private Boolean containPetTypeNamed(Set<PetType> compatiblePetTypes, String string) {
        Boolean result=false;
        for(PetType pt:compatiblePetTypes)
            if(pt.getName().equals(string))
                result=true;
        return result;
    }
}
