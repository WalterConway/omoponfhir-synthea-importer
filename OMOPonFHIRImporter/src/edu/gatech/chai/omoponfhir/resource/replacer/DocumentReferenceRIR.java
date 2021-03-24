package edu.gatech.chai.omoponfhir.resource.replacer;

import java.util.Iterator;
import java.util.List;

import org.hl7.fhir.instance.model.api.IIdType;
import org.hl7.fhir.r4.model.DocumentReference;
import org.hl7.fhir.r4.model.Reference;
import org.hl7.fhir.r4.model.Resource;

import edu.gatech.chai.omoponfhir.ResouceIdentiferMapper;

public class DocumentReferenceRIR implements ResourceIdReplacer {

	@Override
	public Resource replace(Resource resource, ResouceIdentiferMapper mapper) {

		DocumentReference docReference = (DocumentReference)resource;
		String subjectId = docReference.getSubject().getReferenceElement().getIdPart();
		String subjectRId = mapper.getReplacementId("Patient", subjectId);
		if(subjectRId != null) {
			IIdType subjectIdType = docReference.getSubject().getReferenceElement();
			subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), subjectRId, subjectIdType.getVersionIdPart());
			docReference.getSubject().setReferenceElement(subjectIdType);
		}

		List<Reference> authors = docReference.getAuthor();
		for(Reference author : authors) {
			String docId = author.getReferenceElement().getIdPart();
			String rDocId = mapper.getReplacementId("Practitioner", docId);
			if(rDocId != null) {
				 author.getReferenceElement().setValue(rDocId);
				IIdType docIdType =  author.getReferenceElement();
				docIdType.setParts(docIdType.getBaseUrl(), docIdType.getResourceType(), rDocId, docIdType.getVersionIdPart());
				author.setReferenceElement(docIdType);
			}
		}
		if(docReference.getContext() != null) {
			if(docReference.getContext().getEncounter() != null) {
				Iterator<Reference> referenceIt = docReference.getContext().getEncounter().iterator();
				while(referenceIt.hasNext()) {
					Reference encounterRef = referenceIt.next();
					
					String encounterId = encounterRef.getReferenceElement().getIdPart();
					String encounterRId = mapper.getReplacementId("Encounter", encounterId);
					
					if(encounterRId != null) {
						IIdType subjectIdType = encounterRef.getReferenceElement();
						subjectIdType.setParts(subjectIdType.getBaseUrl(), subjectIdType.getResourceType(), encounterRId, subjectIdType.getVersionIdPart());
						encounterRef.setReferenceElement(subjectIdType);
					}
				}

			}
		}
		
		
		return resource;
	}



}
