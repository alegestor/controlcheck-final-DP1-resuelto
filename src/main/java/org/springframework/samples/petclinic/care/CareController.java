package org.springframework.samples.petclinic.care;

import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.pet.PetService;
import org.springframework.samples.petclinic.pet.Visit;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class CareController {
	
	private CareService careService;
	private PetService petService;
	
	@Autowired
	public CareController(CareService careService, PetService petService) {
		this.careService = careService;
		this.petService = petService;
	}
	
	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@ModelAttribute("cares")
	public List<Care> populateCare() {
		return this.careService.getAllCares();
	}
	
	@GetMapping(value = "/visit/{visitId}/care/create")
	public String processCreationForm(Map<String, Object> model, @PathVariable("visitId") int visitId) {
		Care cares = new Care();
		Visit visit = petService.findVisitById(visitId);
		String petTypeName = visit.getPet().getName();
		List<Care> lista = careService.getAllCompatibleCares(petTypeName);
		model.put("cares", cares);
		model.put("providedCare", lista);
		return "care/createOrUpdateProvidedCareForm";
	}
	
	@PostMapping(value = "/visit/{visitId}/care/create")
	public String processCreationForm(@Valid CareProvision care, BindingResult result, ModelMap model) throws UnfeasibleCareException {
		if (result.hasErrors()) {
			model.put("care", care);
			return "care/createOrUpdateProvidedCareForm";
		}
		else {
			try {
				this.careService.save(care);
			} catch (NonCompatibleCaresException e) {
				result.rejectValue("care", "El cuidado seleccionado no se puede realizar durante esta visita");
				return "care/createOrUpdateProvidedCareForm";
			}
			return "redirect:/";
		}		
	}
    

}
