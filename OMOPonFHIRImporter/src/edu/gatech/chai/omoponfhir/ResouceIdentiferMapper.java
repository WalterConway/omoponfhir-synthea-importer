package edu.gatech.chai.omoponfhir;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public class ResouceIdentiferMapper {

	
	private  Map<String, Map<String, String>> resourceIdMapping = new HashMap<String, Map<String, String>>();

	public ResouceIdentiferMapper() {
		super();
	}
	
	public Set<String> getRegisteredIds(String resourceType){
		
		Map<String, String> map = resourceIdMapping.get(resourceType);
		if(map != null) {
			return map.keySet();
		}
		
		return new HashSet<>();
		
	}
	
//	public long generateUniqueIdFor(String resourceType, String oldId) {
//		long uuid = 0;
//		
//		do {
//			uuid = UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
//		} while(isRecordTaken(resourceType, Long.toString(uuid)));
//		
//		recordId(resourceType, oldId, Long.toString(uuid));
//		
//		return uuid;
//	}
	
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
		Map<String, String> tempMap = createMappingIfNotTaken(resourceType);
		return tempMap.containsValue(idToCheck);
	}
	
	public String getReplacementId(String resourceType, String registeredId) {
		Map<String, String> map = resourceIdMapping.get(resourceType);
		if(map != null) {
			return map.get(registeredId);
		}
		return "";
	}
	
	
	private Map<String, String> createMappingIfNotTaken(String resourceType) {
		Map<String, String> tempMap = resourceIdMapping.get(resourceType);
			if(tempMap == null) {
//				tempMap = HashBiMap.create(new HashMap<String, String>());
				tempMap = new HashMap<String, String>();
				resourceIdMapping.put(resourceType, tempMap );
			}
			return tempMap;
	}
	
}
