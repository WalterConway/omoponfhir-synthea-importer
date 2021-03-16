package edu.gatech.chai.omoponfhir;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class StateUtil {
	
	private static StateUtil instance = null;
	
	private static Map<String, String> stateToAbbrevMap = new HashMap<String, String>();
	private static Map<String, String> abbrevToStateMap = new HashMap<String, String>();
	
	

	private StateUtil() {
		super();
		initiateUnitedStates();
	}
	
	private static StateUtil getInstance() {
	    if (instance == null) 
	    	instance = new StateUtil(); 
  
        return instance; 
	}
	
	public void initiateUnitedStates() {
		stateToAbbrevMap.put("ALABAMA","AL");
		stateToAbbrevMap.put("ALASKA","AK");
		stateToAbbrevMap.put("ALBERTA","AB");
		stateToAbbrevMap.put("AMERICAN SAMOA","AS");
		stateToAbbrevMap.put("ARIZONA","AZ");
		stateToAbbrevMap.put("ARKANSAS","AR");
		stateToAbbrevMap.put("ARMED FORCES (AE)","AE");
		stateToAbbrevMap.put("ARMED FORCES AMERICAS","AA");
		stateToAbbrevMap.put("ARMED FORCES PACIFIC","AP");
		stateToAbbrevMap.put("BRITISH COLUMBIA","BC");
		stateToAbbrevMap.put("CALIFORNIA","CA");
		stateToAbbrevMap.put("COLORADO","CO");
		stateToAbbrevMap.put("CONNECTICUT","CT");
		stateToAbbrevMap.put("DELAWARE","DE");
		stateToAbbrevMap.put("DISTRICT OF COLUMBIA","DC");
		stateToAbbrevMap.put("FLORIDA","FL");
		stateToAbbrevMap.put("GEORGIA","GA");
		stateToAbbrevMap.put("GUAM","GU");
		stateToAbbrevMap.put("HAWAII","HI");
		stateToAbbrevMap.put("IDAHO","ID");
		stateToAbbrevMap.put("ILLINOIS","IL");
		stateToAbbrevMap.put("INDIANA","IN");
		stateToAbbrevMap.put("IOWA","IA");
		stateToAbbrevMap.put("KANSAS","KS");
		stateToAbbrevMap.put("KENTUCKY","KY");
		stateToAbbrevMap.put("LOUISIANA","LA");
		stateToAbbrevMap.put("MAINE","ME");
		stateToAbbrevMap.put("MANITOBA","MB");
		stateToAbbrevMap.put("MARYLAND","MD");
		stateToAbbrevMap.put("MASSACHUSETTS","MA");
		stateToAbbrevMap.put("MICHIGAN","MI");
		stateToAbbrevMap.put("MINNESOTA","MN");
		stateToAbbrevMap.put("MISSISSIPPI","MS");
		stateToAbbrevMap.put("MISSOURI","MO");
		stateToAbbrevMap.put("MONTANA","MT");
		stateToAbbrevMap.put("NEBRASKA","NE");
		stateToAbbrevMap.put("NEVADA","NV");
		stateToAbbrevMap.put("NEW BRUNSWICK","NB");
		stateToAbbrevMap.put("NEW HAMPSHIRE","NH");
		stateToAbbrevMap.put("NEW JERSEY","NJ");
		stateToAbbrevMap.put("NEW MEXICO","NM");
		stateToAbbrevMap.put("NEW YORK","NY");
		stateToAbbrevMap.put("NEWFOUNDLAND","NF");
		stateToAbbrevMap.put("NORTH CAROLINA","NC");
		stateToAbbrevMap.put("NORTH DAKOTA","ND");
		stateToAbbrevMap.put("NORTHWEST TERRITORIES","NT");
		stateToAbbrevMap.put("NOVA SCOTIA","NS");
		stateToAbbrevMap.put("NUNAVUT","NU");
		stateToAbbrevMap.put("OHIO","OH");
		stateToAbbrevMap.put("OKLAHOMA","OK");
		stateToAbbrevMap.put("ONTARIO","ON");
		stateToAbbrevMap.put("OREGON","OR");
		stateToAbbrevMap.put("PENNSYLVANIA","PA");
		stateToAbbrevMap.put("PRINCE EDWARD ISLAND","PE");
		stateToAbbrevMap.put("PUERTO RICO","PR");
		stateToAbbrevMap.put("QUEBEC","QC");
		stateToAbbrevMap.put("RHODE ISLAND","RI");
		stateToAbbrevMap.put("SASKATCHEWAN","SK");
		stateToAbbrevMap.put("SOUTH CAROLINA","SC");
		stateToAbbrevMap.put("SOUTH DAKOTA","SD");
		stateToAbbrevMap.put("TENNESSEE","TN");
		stateToAbbrevMap.put("TEXAS","TX");
		stateToAbbrevMap.put("UTAH","UT");
		stateToAbbrevMap.put("VERMONT","VT");
		stateToAbbrevMap.put("VIRGIN ISLANDS","VI");
		stateToAbbrevMap.put("VIRGINIA","VA");
		stateToAbbrevMap.put("WASHINGTON","WA");
		stateToAbbrevMap.put("WEST VIRGINIA","WV");
		stateToAbbrevMap.put("WISCONSIN","WI");
		stateToAbbrevMap.put("WYOMING","WY");
		stateToAbbrevMap.put("YUKON TERRITORY","YT");
		stateToAbbrevMap.put("XX","XX");
		
	    abbrevToStateMap.put("AL", "ALABAMA");
	    abbrevToStateMap.put("AK", "ALASKA");
	    abbrevToStateMap.put("AB", "ALBERTA");
	    abbrevToStateMap.put("AZ", "ARIZONA");
	    abbrevToStateMap.put("AR", "ARKANSAS");
	    abbrevToStateMap.put("BC", "BRITISH COLUMBIA");
	    abbrevToStateMap.put("CA", "CALIFORNIA");
	    abbrevToStateMap.put("CO", "COLORADO");
	    abbrevToStateMap.put("CT", "CONNECTICUT");
	    abbrevToStateMap.put("DE", "DELAWARE");
	    abbrevToStateMap.put("DC", "DISTRICT OF COLUMBIA");
	    abbrevToStateMap.put("FL", "FLORIDA");
	    abbrevToStateMap.put("GA", "GEORGIA");
	    abbrevToStateMap.put("GU", "GUAM");
	    abbrevToStateMap.put("HI", "HAWAII");
	    abbrevToStateMap.put("ID", "IDAHO");
	    abbrevToStateMap.put("IL", "ILLINOIS");
	    abbrevToStateMap.put("IN", "INDIANA");
	    abbrevToStateMap.put("IA", "IOWA");
	    abbrevToStateMap.put("KS", "KANSAS");
	    abbrevToStateMap.put("KY", "KENTUCKY");
	    abbrevToStateMap.put("LA", "LOUISIANA");
	    abbrevToStateMap.put("ME", "MAINE");
	    abbrevToStateMap.put("MB", "MANITOBA");
	    abbrevToStateMap.put("MD", "MARYLAND");
	    abbrevToStateMap.put("MA", "MASSACHUSETTS");
	    abbrevToStateMap.put("MI", "MICHIGAN");
	    abbrevToStateMap.put("MN", "MINNESOTA");
	    abbrevToStateMap.put("MS", "MISSISSIPPI");
	    abbrevToStateMap.put("MO", "MISSOURI");
	    abbrevToStateMap.put("MT", "MONTANA");
	    abbrevToStateMap.put("NE", "NEBRASKA");
	    abbrevToStateMap.put("NV", "NEVADA");
	    abbrevToStateMap.put("NB", "NEW BRUNSWICK");
	    abbrevToStateMap.put("NH", "NEW HAMPSHIRE");
	    abbrevToStateMap.put("NJ", "NEW JERSEY");
	    abbrevToStateMap.put("NM", "NEW MEXICO");
	    abbrevToStateMap.put("NY", "NEW YORK");
	    abbrevToStateMap.put("NF", "NEWFOUNDLAND");
	    abbrevToStateMap.put("NC", "NORTH CAROLINA");
	    abbrevToStateMap.put("ND", "NORTH DAKOTA");
	    abbrevToStateMap.put("NT", "NORTHWEST TERRITORIES");
	    abbrevToStateMap.put("NS", "NOVA SCOTIA");
	    abbrevToStateMap.put("NU", "NUNAVUT");
	    abbrevToStateMap.put("OH", "OHIO");
	    abbrevToStateMap.put("OK", "OKLAHOMA");
	    abbrevToStateMap.put("ON", "ONTARIO");
	    abbrevToStateMap.put("OR", "OREGON");
	    abbrevToStateMap.put("PA", "PENNSYLVANIA");
	    abbrevToStateMap.put("PE", "PRINCE EDWARD ISLAND");
	    abbrevToStateMap.put("PR", "PUERTO RICO");
	    abbrevToStateMap.put("QC", "QUEBEC");
	    abbrevToStateMap.put("RI", "RHODE ISLAND");
	    abbrevToStateMap.put("SK", "SASKATCHEWAN");
	    abbrevToStateMap.put("SC", "SOUTH CAROLINA");
	    abbrevToStateMap.put("SD", "SOUTH DAKOTA");
	    abbrevToStateMap.put("TN", "TENNESSEE");
	    abbrevToStateMap.put("TX", "TEXAS");
	    abbrevToStateMap.put("UT", "UTAH");
	    abbrevToStateMap.put("VT", "VERMONT");
	    abbrevToStateMap.put("VI", "VIRGIN ISLANDS");
	    abbrevToStateMap.put("VA", "VIRGINIA");
	    abbrevToStateMap.put("WA", "WASHINGTON");
	    abbrevToStateMap.put("WV", "WEST VIRGINIA");
	    abbrevToStateMap.put("WI", "WISCONSIN");
	    abbrevToStateMap.put("WY", "WYOMING");
	    abbrevToStateMap.put("YT", "YUKON TERRITORY");
	    abbrevToStateMap.put("XX", "XX");
	}
	
	public static String getAbbreviationFromState(String state) {
		getInstance();
		state = StringUtils.defaultString(state, "XX");
		state = state.trim().toUpperCase();
		return stateToAbbrevMap.get(state);
	}

	public static String getStateFromAbbreviation(String abbreviation) {
		getInstance();
		abbreviation = StringUtils.defaultString(abbreviation, "XX");
		abbreviation = abbreviation.trim().toUpperCase();
		return abbrevToStateMap.get(abbreviation);
	}

}
