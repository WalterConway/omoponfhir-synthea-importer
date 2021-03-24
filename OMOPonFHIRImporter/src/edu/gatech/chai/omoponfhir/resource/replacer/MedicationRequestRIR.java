package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class MedicationRequestRIR implements  ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		MedicationRequest medicationRequest = (MedicationRequest)resource;
		
		String subjectId = medicationRequest.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = medicationRequest.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			medicationRequest.getSubject().setReferenceElement(subjectIdType);
		}

		String encounterId = medicationRequest.getEncounter().getReferenceElement().getIdPart();
		String encounterRId = mapper.getReplacementId("Encounter", encounterId);
		if(encounterRId != null) {
			IIdType subjectIdType = medicationRequest.getEncounter().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), encounterRId, subjectIdType.getVersionIdPart());
			medicationRequest.getEncounter().setReferenceElement(subjectIdType);
		}
		
		String requesterId = medicationRequest.getRequester().getReferenceElement().getIdPart();
		String requesterRId = mapper.getReplacementId("Practitioner", requesterId);
		if(requesterRId != null) {
			IIdType subjectIdType = medicationRequest.getRequester().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), requesterRId, subjectIdType.getVersionIdPart());
			medicationRequest.getRequester().setReferenceElement(subjectIdType);
		}
		return resource;
	}



}
