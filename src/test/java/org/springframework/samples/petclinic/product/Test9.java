package org.springframework.samples.petclinic.product;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.care.Care;
import org.springframework.samples.petclinic.care.CareController;
import org.springframework.samples.petclinic.care.CareProvisionRepository;
import org.springframework.samples.petclinic.care.CareService;
import org.springframework.samples.petclinic.care.CareFormatter;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.pet.Pet;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.pet.PetType;
import org.springframework.samples.petclinic.pet.Visit;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(value = CareController.class,
		includeFilters = @ComponentScan.Filter(value = CareFormatter.class, type = FilterType.ASSIGNABLE_TYPE),
		excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class),
		excludeAutoConfiguration= SecurityConfiguration.class)
public class Test9 {
    @MockBean
	CareService feedingService;
    @MockBean
	PetService petService;
	@MockBean
	CareProvisionRepository feedingRepository;
    @Autowired
	private MockMvc mockMvc;

    @WithMockUser(value = "spring")
    @Test
	void testInitCreationForm() throws Exception {
		Visit v=new Visit();
		Pet p=new Pet();
		v.setPet(p);
		PetType pt=new PetType();
		pt.setName("Turtle");
		pt.setId(9);
		p.setType(pt);
		when(petService.findVisitById(1)).thenReturn(v);
		when(feedingService.getAllCompatibleCares("Turtle")).thenReturn(new ArrayList<Care>());
		mockMvc.perform(get("/visit/1/care/create")).andExpect(status().isOk())
				.andExpect(view().name("care/createOrUpdateProvidedCareForm"))
                .andExpect(model().attributeExists("providedCare"))
				.andExpect(model().attributeExists("cares"));
		
		// This line verifies that you are invoking the findVisitById method in the petService, you need it to know the name of the pet type for which we will provide the services 
		// (and invoke getAllCompatibleCares)
		verify(petService).findVisitById(1);
		// This line verifies that you are invoking the getAllCompatibleCares Method in the CareService
		verify(feedingService).getAllCompatibleCares("Turtle");
			
	}
}
