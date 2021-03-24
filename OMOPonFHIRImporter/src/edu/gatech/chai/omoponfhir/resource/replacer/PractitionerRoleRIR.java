package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.r4.model.PractitionerRole;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class PractitionerRoleRIR implements ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		PractitionerRole practitionerRole = (PractitionerRole)resource;
		String pId = practitionerRole.getPractitioner().getId();
		String prId = mapper.getReplacementId("Practitioner", pId);
		practitionerRole.getPractitioner().setId(prId);
		String oId = practitionerRole.getOrganization().getId();
		String orId = mapper.getReplacementId("Organization", oId);
		practitionerRole.getOrganization().setId(orId);
		return resource;
	}

}
