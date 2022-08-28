package org.springframework.samples.petclinic.care;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import org.springframework.samples.petclinic.model.BaseEntity;
import org.springframework.samples.petclinic.pet.PetType;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "care")
public class Care extends BaseEntity{
	
	@Size(min = 5, max = 30)
	@NotEmpty
	@Column(name = "name", unique  = true)
    String name;
	
	@Column(name = "description")
	@NotEmpty
    String description;
	
	@NotEmpty
	@ManyToMany(cascade = CascadeType.ALL)
	//PARA AGREGAR TABLA INTERMEDIA
	@JoinTable(name = "compatible_pet_types", joinColumns = @JoinColumn(name = "care_id"),
	inverseJoinColumns = @JoinColumn(name = "type_id"))
    Set<PetType> compatiblePetTypes;
	
	//TEST 4
	
	@ManyToMany(cascade = CascadeType.ALL)
	//SET--> TABLA INTERMEDIA
	@JoinTable(name = "incompatible_cares", joinColumns = @JoinColumn(name = "care_id"),
	inverseJoinColumns = @JoinColumn(name = "incompatible_care_id"))
	Set<Care> incompatibleCares;

}
