package edu.gatech.chai.omoponfhir;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.hl7.fhir.r4.model.Bundle;
import org.hl7.fhir.r4.model.Bundle.BundleEntryComponent;
import org.hl7.fhir.r4.model.Resource;

import com.google.gson.Gson;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
/**
 * Condition, MedicationRequest,
 *  Immunization, Organization,
 *   Device, Patient, DeviceUseStatement,
 *    Practitioner, StructureDefinition, Medication, 
 *    Observation, Encounter, Procedure, 
 *    MedicationStatement, ConceptMap, OperationDefinition,
 *     DocumentReference
 * @author Walter
 *
 */
public class MainApplication {
	public static Set<String> compatiableResourceTypes = new HashSet<String>();
	public static OkHttpClient client = new OkHttpClient();

    public static FhirContext ctx = null;
    public static IParser parser = null;
    public static ResouceIdentiferMapper  ridMapper = new ResouceIdentiferMapper();

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		loadCompatiableResourceTypes();
		Gson gson = new Gson();
		getCurrentIds();
		
		IOFileFilter fileFilter = new IOFileFilter() {
			
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
		File directory = new File("F:\\CS-6440-O01\\miniprojects\\3\\synthea-bin\\output - Copy\\fhir");
		Collection<File> jsonFiles = FileUtils.listFiles(directory, fileFilter, null);
		
		for(File jsonFile : jsonFiles) {
			System.out.println(jsonFile.getName());
			readInJsonBundle(jsonFile);
		}
		
	}
	
	public static void loadCompatiableResourceTypes() {
		compatiableResourceTypes.add("Condition");
		compatiableResourceTypes.add("MedicationRequest");
		compatiableResourceTypes.add("Immunization");
		compatiableResourceTypes.add("Organization");
		compatiableResourceTypes.add("Device");
		compatiableResourceTypes.add("Patient");
		compatiableResourceTypes.add("DeviceUseStatement");
		compatiableResourceTypes.add("Practitioner");
		compatiableResourceTypes.add("StructureDefinition");
		compatiableResourceTypes.add("Medication");
		compatiableResourceTypes.add("Observation");
		compatiableResourceTypes.add("Encounter");
		compatiableResourceTypes.add("Procedure"); 
		compatiableResourceTypes.add("MedicationStatement");
		compatiableResourceTypes.add("ConceptMap");
		compatiableResourceTypes.add("OperationDefinition");
		compatiableResourceTypes.add("DocumentReference");
	}
	
	public static boolean isResourceTypeCompatiable(String resourceType) {
		return compatiableResourceTypes.contains(resourceType);
	}
	
	
	public static Bundle readInJsonBundle(File file) {
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
        	List<BundleEntryComponent> bundleEntryComponents =  tempResource.getEntry();
        	for( BundleEntryComponent bundleEntryComponent : bundleEntryComponents) {
        		String resourceType = bundleEntryComponent.getResource().getResourceType().toString();
        		if(isResourceTypeCompatiable(resourceType)){
        			Resource res = bundleEntryComponent.getResource();
        			long newId = ridMapper.generateUniqueIdFor(resourceType, res.getId());
        			System.out.println(res.getId());
        			
        		}
        		
        	}
        	
           return tempResource;
        }
        catch (Exception e) { 
        	System.err.println("Failed to read " + file.getName() + ".json file.");
        }
        
        return null;
	}
	
	public static void getCurrentIds() throws IOException {
		String BASE_URL = "http://localhost:8080/omoponfhir-r4-server/fhir";
	    Request request = new Request.Builder()
	    	      .url(BASE_URL + "/Patient")
	    	      .addHeader("Authorization", "Basic ImNsaWVudDpzZWNyZXQi")
	    	      .build();
	    

	    	    Call call = client.newCall(request);
	    	    Response response = call.execute();
	    	    if(response.code() == 200) {
	    	    	String responseBody = response.body().string();
	    	    	System.out.println(responseBody);
	    	    }
	}

}
