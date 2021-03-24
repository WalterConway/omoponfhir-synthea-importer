package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.MedicationRequest;
import org.hl7.fhir.r4.model.Observation;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class ObservationRIR implements  ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		Observation observationRequest = (Observation)resource;
		
		String subjectId = observationRequest.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = observationRequest.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			observationRequest.getSubject().setReferenceElement(subjectIdType);
		}

		String encounterId = observationRequest.getEncounter().getReferenceElement().getIdPart();
		String encounterRId = mapper.getReplacementId("Encounter", encounterId);
		if(encounterRId != null) {
			IIdType subjectIdType = observationRequest.getEncounter().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), encounterRId, subjectIdType.getVersionIdPart());
			observationRequest.getEncounter().setReferenceElement(subjectIdType);
		}
		return resource;
	}


}
