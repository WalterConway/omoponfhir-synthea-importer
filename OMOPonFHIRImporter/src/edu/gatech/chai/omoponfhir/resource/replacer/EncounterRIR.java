package edu.gatech.chai.omoponfhir.resource.replacer;

import java.util.List;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.Encounter;
import org.hl7.fhir.r4.model.Encounter.EncounterParticipantComponent;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class EncounterRIR implements  ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {

		Encounter encounter = (Encounter)resource;
		String subjectId = encounter.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = encounter.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			encounter.getSubject().setReferenceElement(subjectIdType);
			List<EncounterParticipantComponent> participantComps = encounter.getParticipant();
			
			for(EncounterParticipantComponent participant : participantComps) {
				String docId = participant.getIndividual().getReferenceElement().getIdPart();
				String rDocId = mapper.getReplacementId("Practitioner", docId);
				if(rDocId != null) {
					participant.getIndividual().getReferenceElement().setValue(rDocId);
					IIdType docIdType = participant.getIndividual().getReferenceElement();
					docIdType.setParts(docIdType.getBaseUrl(), docIdType.getResourceType(), rDocId, docIdType.getVersionIdPart());
					participant.getIndividual().setReferenceElement(docIdType);
				}
			}
		}


		return resource;
	}



}
