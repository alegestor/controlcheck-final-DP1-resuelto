package org.springframework.samples.petclinic.care;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.pet.Visit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "care_provision")
public class CareProvision extends BaseEntity {  
	
    @ManyToOne 
	@JoinColumn(name = "visit_id")
    Visit visit;
	
	@Min(value = 0)
	@NotNull
	@Column(name = "duration")
    double duration;
  
    
    @ManyToOne(optional = false)
	@JoinColumn(name = "care_id")
	@NotNull
    Care care;   
}
