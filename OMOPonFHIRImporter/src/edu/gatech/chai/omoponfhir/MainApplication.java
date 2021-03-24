package edu.gatech.chai.omoponfhir;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Identifier;
import org.hl7.fhir.r4.model.Practitioner;
import org.hl7.fhir.r4.model.Resource;
import org.ini4j.Ini;
import org.ini4j.IniPreferences;

import com.google.gson.Gson;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import edu.gatech.chai.omoponfhir.resource.replacer.ConditionRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.DocumentReferenceRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.EncounterRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.ImmunizationRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.MedicationRequestRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.ObservationRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.OrganizationRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.PatientRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.PractitionerRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.ProcedureRIR;
import edu.gatech.chai.omoponfhir.resource.replacer.ResourceIdReplacer;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Main application launcher
 * 
 * @author Walter
 *
 */
public class MainApplication {
	public static Set<String> compatiableResourceTypes = new HashSet<String>();
	public static OkHttpClient client = new OkHttpClient();
	public static String TOKEN = "";
	public static String BASE_URL = "";

    public static FhirContext ctx = null;
    public static IParser parser = null;
    public static ResouceIdentiferMapper  ridMapper = new ResouceIdentiferMapper();

	public static void main(String[] args) throws Exception {
		String configurationFilePath = null;
		if(args != null && args.length > 0) {
			configurationFilePath = args[0];
		} else {
			throw new RuntimeException("Must have one parameter: Directory Path to Synthea's JSON files.");
		}
		 
		File configurationFile = new File(configurationFilePath);
		
		Ini ini = new Ini(configurationFile);
		java.util.prefs.Preferences prefs = new IniPreferences(ini);
		String directoryPath = prefs.node("SYNTHEA-OUTPUT").get("DIRECTORY", null);
		String compatableResourcesStr = prefs.node("COMPATIABLE-RESOURCES").get("LISTINGS", null);
		TOKEN = prefs.node("WEBSERVER-CONFIG").get("TOKEN", null);
		BASE_URL = prefs.node("WEBSERVER-CONFIG").get("BASE_URL", null);
		String[] compatableResources = compatableResourcesStr.split(",");
		loadCompatiableResourceTypes(Arrays.asList(compatableResources));
		
		
		Gson gson = new Gson();
		File directory = new File(directoryPath);
		Collection<File> fileCollection = FileUtils.listFiles(directory, getFileFilter(), null);
		List<File> jsonFiles = new ArrayList<File>(fileCollection);
		//hospitals and practitioner are first
		jsonFiles.sort(getFileComparator());
		
		//removes any problem areas in the files.
		//TODO should possibly be stored in memory or temporary location
		normalizeFiles(jsonFiles);
		
		

		for(File jsonFile : jsonFiles) {
			System.out.println("Loading: " + jsonFile.getName());
			Bundle bundle = getBundleFromJSON(jsonFile);
	    	List<BundleEntryComponent> bundleEntryComponents =  bundle.getEntry();
	    	for( BundleEntryComponent bundleEntryComponent : bundleEntryComponents) {
	    		String resourceType = bundleEntryComponent.getResource().getResourceType().toString();
	    		if(isResourceTypeCompatiable(resourceType)){
	    			Resource res = bundleEntryComponent.getResource();
    				replaceReferencedIds(res);
	    			storeIdTemporary(res);
	    			String newID = persistResource(resourceType, res);
	    			if(newID != null) {
	    				replaceTemporaryId(res, newID);
	    			} else {
	    				System.out.println("Not Good");
	    			}
	    		}
	    	}
		}
	}
	
	public static void replaceReferencedIds(Resource resource) {
		String resourceType = resource.getResourceType().toString();
		
		ResourceIdReplacer rir = null;
		
		switch(resourceType) {
		case "Organization":
			rir = new OrganizationRIR();
			break;
		case "Practitioner":
			rir = new PractitionerRIR();
			break;
		case "Patient":
			rir = new PatientRIR();
			break;
		case "Encounter":
			rir = new EncounterRIR();
			break;
		case "Condition":
			rir = new ConditionRIR();
			break;
		case "MedicationRequest":
			rir = new MedicationRequestRIR();
			break;
		case "DocumentReference":
			rir = new DocumentReferenceRIR();
			break;
		case "Observation":
			rir = new ObservationRIR();
			break;
		case "Procedure":
			rir = new ProcedureRIR();
			break;
		case "Immunization":
			rir = new ImmunizationRIR();
			break;
		}
		if(rir == null) {
			System.out.println("Missing resource: " + resourceType);
		} else {
			rir.replace(resource, ridMapper);
		}
		
	}
	
	public static void replaceTemporaryId(Resource resource, String newId) {

		String resourceId = resource.getIdElement().getIdPart();
		String resourceType = resource.getResourceType().toString();
		
		if(resourceType.contains("Practitioner")) {
			Practitioner p = (Practitioner)resource;
			List<Identifier> ids = p.getIdentifier();
			for(Identifier id : ids) {
				ridMapper.recordId(resourceType, id.getValue(), newId);
			}
		}
		
		ridMapper.recordId(resourceType, resourceId, newId);
	}
	
	public static void storeIdTemporary(Resource resource) {
		String resourceId = resource.getIdElement().getIdPart();
		String resourceType = resource.getResourceType().toString();
		
		if(resourceType.contains("Practitioner")) {
			Practitioner p = (Practitioner)resource;
			List<Identifier> ids = p.getIdentifier();
			for(Identifier id : ids) {
				ridMapper.recordId(resourceType, id.getValue(), "TEMPORARY_ID");
			}
		}
		
		ridMapper.recordId(resourceType, resourceId, "TEMPORARY_ID");
	}
	
	public static void normalizeFiles(Collection<File> jsonFiles) throws Exception {
		for(File jsonFile : jsonFiles) {
			//clean files
			List<String> oldTexts = new ArrayList<String>();
			List<String> newTexts = new ArrayList<String>();
			
			oldTexts.add("urn:uuid:");
			newTexts.add("");

			oldTexts.add("Practitioner\\?identifier=http:\\/\\/hl7.org\\/fhir\\/sid\\/us-npi\\|");
			newTexts.add("Practitioner\\?identifier=http:\\/\\/hl7.org\\/fhir\\/Practitioner\\/");
			
			substitute(jsonFile, oldTexts, newTexts );
			
		}
	}
	
	public static void substituteIdentifiers(File file) throws Exception {
		List<String> registeredIds =  new ArrayList<String>(ridMapper.getRegisteredIds("test"));
		List<String> replacementIds = new ArrayList<String>();
		
		for(String registeredId : registeredIds) {
			String newId = ridMapper.getReplacementId("test", registeredId);
			replacementIds.add(newId);
		}
		
		substitute(file, registeredIds, replacementIds);
		
	}
	
	public static void substitute(File file, Collection<String> oldTexts, Collection<String> newTexts) throws Exception {
		if(oldTexts.size() != newTexts.size()) {
			throw new Exception("Uneven lists, cannot replace.");
		}
		Charset charset = StandardCharsets.UTF_8;

		String content = new String(Files.readAllBytes(file.toPath()), charset);

		Iterator<String> oldTInterator = oldTexts.iterator();
		Iterator<String> newTInterator = newTexts.iterator();
		while(oldTInterator.hasNext() && newTInterator.hasNext()) {
			String oldText = oldTInterator.next();
			String newText = newTInterator.next();
			content = content.replaceAll(oldText, newText);	
		}
		
		Files.write(file.toPath(), content.getBytes(charset));		

	}
	
	
	
	public static void loadCompatiableResourceTypes(List<String> compatableResources) {
		for(String compatableResource : compatableResources) {
			compatiableResourceTypes.add(compatableResource.trim());
		}
	}
	
	public static boolean isResourceTypeCompatiable(String resourceType) {
		return compatiableResourceTypes.contains(resourceType);
	}
	
	
	public static Bundle getBundleFromJSON(File file) {
        // Creating a FHIR context is an expensive operation, so this code is here to ensure that it is only built
        // if it is not already such as when running a single test method. If implementing JSON parsing in your other
        // tests, you should copy this section of code.
		String resourceJson = "";
        if (ctx == null) {
            ctx = FhirContext.forR4();
            parser = ctx.newJsonParser();
        }

        try {
        	resourceJson = new String(Files.readAllBytes(Paths.get(file.getAbsolutePath())));
        	Bundle tempResource =  parser.parseResource(Bundle.class, resourceJson);
           return tempResource;
        }
        catch (Exception e) { 
        	System.err.println("Failed to read " + file.getName() + ".json file.");
        }
        
        return null;
	}
	
	
	public static Resource getResourceFromJSON(String json) {
		// Creating a FHIR context is an expensive operation, so this code is here to ensure that it is only built
		// if it is not already such as when running a single test method. If implementing JSON parsing in your other
		// tests, you should copy this section of code.
		String resourceJson = "";
		if (ctx == null) {
			ctx = FhirContext.forR4();
			parser = ctx.newJsonParser();
		}

		Resource tempResource =  parser.parseResource(Resource.class, resourceJson);

		return tempResource;

	}
	
	public static String getJsonOfResource(Resource resource) {
		
		// Create a FHIR context
		FhirContext ctx = FhirContext.forR4();


		// Instantiate a new JSON parser
		IParser parser = ctx.newJsonParser();

		// Serialize it
		String serialized = parser.encodeResourceToString(resource);
		
		return serialized;
		
		
	}
	
	public static void getCurrentIds() throws IOException {
		String BASE_URL = "http://localhost:8080/omoponfhir-r4-server/fhir";
	    Request request = new Request.Builder()
	    	      .url(BASE_URL + "/Patient")
	    	      .addHeader("Authorization", "Basic "+ TOKEN)
	    	      .build();

	    	    Call call = client.newCall(request);
	    	    Response response = call.execute();
	    	    if(response.code() == 200) {
	    	    	String responseBody = response.body().string();
	    	    }
	}
	
	public Resource getCurrentId(String resource, String externalId) throws IOException {
		Resource returnResource = null;
	    Request request = new Request.Builder()
	    	      .url(BASE_URL + "/" + resource + "?identifer="+externalId)
	    	      .addHeader("Authorization", "Basic " + TOKEN)
	    	      .build();

	    	    Call call = client.newCall(request);
	    	    Response response = call.execute();
	    	    if(response.code() == 200) {
	    	    	String responseBody = response.body().string();
	    	    	returnResource = getResourceFromJSON(responseBody);
	    	    }
	    	    
	    	    return returnResource;
	}
	
	public static String persistResource(String resourceType, Resource resource) throws IOException {
		String resourceLocation = resource.getResourceType().name();
		
		String resourceJson = getJsonOfResource(resource);
		
		RequestBody requestBody = RequestBody.create(resourceJson,MediaType.parse("application/json"));
		
	    Request request = new Request.Builder()
	    	      .url(BASE_URL + "/" + resourceLocation)
	    	      .post(requestBody)
	    	      .addHeader("Authorization", "Basic " + TOKEN)
	    	      .build();

	    	    Call call = client.newCall(request);

	    	    Response response = call.execute();
	    	    try {
	    	    if(response.code() == 200 || response.code() == 201) {
	    	    	String location = response.header("location");
	    	    	URL locURL = new URL(location);
	    	    	String locationPath = locURL.getPath();
	    	    	String[] segments = locationPath.split("/");
	    	    	String newId = segments[segments.length-1];
	    	    	System.out.println("Persisted Resource: " + resourceLocation + " with Id: " + newId);
	    	    	return newId;
	    	    } else {
	    	    	String responseBody = response.body().string();
	    	    	System.out.println(responseBody);
	    	    }
	    	    } finally {
	    	    	response.body().close();
	    	    }
	    	    
				return null;
	}
	
	public static Comparator<File> getFileComparator(){
		
		return new Comparator<File>() {

			@Override
			public int compare(File o1, File o2) {

				if (o1.getName().contains("hospital") && o2.getName().contains("hospital")) {
					return  0;
				}
				if (o1.getName().contains("practitioner") && o2.getName().contains("practitioner")) {
					return  0;
				}
				if (o1.getName().contains("hospital") && o2.getName().contains("practitioner")) {
					return  -1;
				}
				if (o1.getName().contains("practitioner") && o2.getName().contains("hospital")) {
					return  1;
				}
				if (o1.getName().contains("hospital")) {
					return -1;
				}
				if (o2.getName().contains("hospital")) {
					return 1;
				}
				if (o1.getName().contains("practitioner")) {
					return -1;
				}
				if (o2.getName().contains("practitioner")) {
					return 1;
				}

				return o1.getName().compareTo(o2.getName());
			}
		};
	}
	
	public static IOFileFilter getFileFilter() {
		
		return  new IOFileFilter() {
			
			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return false;
			}
			
			@Override
			public boolean accept(File file) {
				if("json".equalsIgnoreCase(FilenameUtils.getExtension(file.getName()))) {
					return true;
				}
				return false;
			}
		};
	}

}
