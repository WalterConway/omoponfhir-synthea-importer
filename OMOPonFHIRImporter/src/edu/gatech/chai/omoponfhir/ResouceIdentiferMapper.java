package edu.gatech.chai.omoponfhir;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ResouceIdentiferMapper {

	
	private  Map<String, BiMap<String, String>> resourceIdMapping = new HashMap<String, BiMap<String, String>>();

	public ResouceIdentiferMapper() {
		super();
	}
	
	public long generateUniqueIdFor(String resourceType, String oldId) {
		long uuid = 0;
		
		do {
			uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
		} while(isRecordTaken(resourceType, Long.toString(uuid)));
		
		recordId(resourceType, oldId, Long.toString(uuid));
		
		return uuid;
	}
	
	public void recordId(String resourceType, String oldId, String newId) {
		if(!StringUtils.isEmpty(resourceType)) {
			createMappingIfNotTaken(resourceType).put(oldId, newId);
		}
	}
	
	/**
	 *  Used to record already taken ids so there is no duplicates.
	 * @param resourceType
	 * @param takenId
	 */
	public void preexistingIds(String resourceType, String takenId) {
		if(!StringUtils.isEmpty(resourceType)) {
			createMappingIfNotTaken(resourceType).put(takenId, takenId);
		}
	}
	
	/**
	 * Is the new Id taken?
	 * @param resourceType
	 * @param idToCheck
	 * @return
	 */
	public boolean isRecordTaken(String resourceType, String idToCheck) {
		BiMap<String, String> tempMap = createMappingIfNotTaken(resourceType);
		return tempMap.containsValue(idToCheck);
	}
	
	
	private BiMap<String, String> createMappingIfNotTaken(String resourceType) {
		BiMap<String, String> tempMap = resourceIdMapping.get(resourceType);
			if(tempMap == null) {
				tempMap = HashBiMap.create(new HashMap<String, String>());
				resourceIdMapping.put(resourceType, tempMap );
			}
			return tempMap;
	}
	
}
