package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class OrganizationRIR implements ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		return resource;
	}



}
