package edu.gatech.chai.omoponfhir.resource.replacer;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Immunization;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Procedure;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class ProcedureRIR implements  ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {
		Procedure procedure = (Procedure)resource;
		String subjectId = procedure.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = procedure.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			procedure.getSubject().setReferenceElement(subjectIdType);
		}

		String encounterId = procedure.getEncounter().getReferenceElement().getIdPart();
		String encounterRId = mapper.getReplacementId("Encounter", encounterId);
		if(encounterRId != null) {
			IIdType subjectIdType = procedure.getEncounter().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), encounterRId, subjectIdType.getVersionIdPart());
			procedure.getEncounter().setReferenceElement(subjectIdType);
		}
		return resource;
	}


}
