package org.springframework.samples.petclinic.care;

import java.text.ParseException;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

@Component
public class CareFormatter implements Formatter<Care>{

	private CareService careService;
	
	@Autowired
	public CareFormatter(CareService careService) {
		this.careService = careService;
	}
    @Override
    public String print(Care object, Locale locale) {
        return object.getName();
    }

    @Override
    public Care parse(String text, Locale locale) throws ParseException {
        List<Care> cares = careService.getAllCares();
        for(Care c : cares) {
        	if(c.getName().equals(text)) {
        		return c;
        	}
        }
        throw new ParseException("Care not found", 0);
    }
    
}
