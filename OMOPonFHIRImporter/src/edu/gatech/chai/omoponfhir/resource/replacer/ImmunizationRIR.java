package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class ImmunizationRIR implements ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		Immunization immunization = (Immunization)resource;
		
		String patientId = immunization.getPatient().getReferenceElement().getIdPart();
		String patientRId = mapper.getReplacementId("Patient", patientId);
		if(patientRId != null) {
			IIdType patientIdType = immunization.getPatient().getReferenceElement();
			patientIdType.setParts(patientIdType.getBaseUrl(), patientIdType.getResourceType(), patientRId, patientIdType.getVersionIdPart());
			immunization.getPatient().setReferenceElement(patientIdType);
		}

		String encounterId = immunization.getEncounter().getReferenceElement().getIdPart();
		String encounterRId = mapper.getReplacementId("Encounter", encounterId);
		if(encounterRId != null) {
			IIdType ecounterIdType = immunization.getEncounter().getReferenceElement();
			ecounterIdType.setParts(ecounterIdType.getBaseUrl(), ecounterIdType.getResourceType(), encounterRId, ecounterIdType.getVersionIdPart());
			immunization.getEncounter().setReferenceElement(ecounterIdType);
		}
		return resource;
	}


}
