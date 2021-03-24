package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Condition;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class ConditionRIR implements ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		Condition condition = (Condition)resource;

		String subjectId = condition.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = condition.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			condition.getSubject().setReferenceElement(subjectIdType);
		}

		String encounterId = condition.getEncounter().getReferenceElement().getIdPart();
		String encounterRId = mapper.getReplacementId("Encounter", encounterId);
		if(encounterRId != null) {
			IIdType subjectIdType = condition.getEncounter().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), encounterRId, subjectIdType.getVersionIdPart());
			condition.getEncounter().setReferenceElement(subjectIdType);
		}
		
		return resource;
	}


}
