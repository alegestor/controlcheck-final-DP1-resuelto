package org.springframework.samples.petclinic.care;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CareService {    

	private CareProvisionRepository careProvisionRepository;
	
	@Autowired
	public CareService( CareProvisionRepository careProvisionRepository) {
		this.careProvisionRepository = careProvisionRepository;
		
	}
	
    public List<Care> getAllCares(){
        return careProvisionRepository.findAllCares();
    }

    public List<Care> getAllCompatibleCares(String petTypeName){
        return careProvisionRepository.findCompatibleCares(petTypeName);
    }

    public Care getCare(String careName) {
        return careProvisionRepository.findCareByName(careName);
    }

    /**
     * @param p
     * @return
     * @throws NonCompatibleCaresException
     * @throws UnfeasibleCareException
     * @data Si durante la visita ya se ha prestado un cuidado que es incompatible con el que se desea grabar, se debe lanzar una excepción de tipo 
     * “NonCompatibleCaresException” (esta clase está ya creada en el paquete cares). 
     * 
     * Para poder hacer esto deberá implementar una consulta personalizada para buscar los cuidados realizados durante una visita
     * 
     * 
     * Además, el servicio debe asegurarse de que el cuidado es compatible con el tipo de mascota al que se pretende prestar 
     * (no debemos permitir registrar cuidaos de corte de pelo a  peces, por ejemplo. 
     * Si el tipo de la mascota que viene a la clínica durante la visita no está entre las compatibles con el cuidado, deberá 
     * lanzarse una excepción de tipo “UnfeasibleCareException”
     */
    @Transactional
    public CareProvision save(CareProvision cp) throws NonCompatibleCaresException, UnfeasibleCareException {
    	List<CareProvision> careProvisions = getCaresPerformed(cp.getVisit().getId());
    	for(CareProvision careProvision : careProvisions) {
    		if(!careProvision.equals(cp)) {
    			throw new NonCompatibleCaresException();
    		} else if(!cp.getCare().getCompatiblePetTypes().contains(cp.getVisit().getPet().getType())) {
    			throw new UnfeasibleCareException();
    		}
    	}
        return careProvisionRepository.save(cp);   
    }

    public List<CareProvision> getAllCaresProvided(){
        return careProvisionRepository.findAll();
    }

    public List<CareProvision> getCaresPerformed(Integer visitId){
        return careProvisionRepository.getCaresPerformed(visitId);

    }
    
}
