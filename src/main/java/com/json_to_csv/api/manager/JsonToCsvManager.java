package com.json_to_csv.api.manager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.opencsv.CSVWriter;


@Service
public class JsonToCsvManager {
	private static final String FILE_URL="https://open-to-cors.s3.amazonaws.com/users.json";
	private static final String[] HEADER= {"id", "phones", "email", "firstname", "lastname", "role", "username", "isActive", "_created_at", "_updated_at"};
	
	Logger logger= LoggerFactory.getLogger(JsonToCsvManager.class);
	
	public byte[] downloadJson() throws Exception {
		
		String generatedFileName= String.valueOf(System.currentTimeMillis());
		String filename = generatedFileName + ".json";
		byte[] fileBytes = {};
		File file = new File("data/" + filename);
		
		FileUtils.copyURLToFile(
				  new URL(FILE_URL), 
				  file, 
				  0, 
				  0);
		
		fileBytes = getByteFromFile(file);
		readJson(generatedFileName);
		return fileBytes;
	}
	
	@SuppressWarnings("unchecked")
	public void readJson(String filename) throws FileNotFoundException, IOException, ParseException {
		
		File file = new File("data/" +filename+".csv");
		
		JSONParser jsonParser = new JSONParser();
		try (FileReader reader = new FileReader("data/"+filename+".json")){
			Object obj = jsonParser.parse(reader);
			JSONArray jsonList = (JSONArray) obj;
			
			FileWriter outputfile = new FileWriter(file);
			CSVWriter writer = new CSVWriter(outputfile);
			writer.writeNext(HEADER);
			
			jsonList.forEach(i -> {
				try {
					parseJsonObject( (JSONObject) i, writer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			});
			writer.close();
		}
	}
	
	private void parseJsonObject(JSONObject object, CSVWriter writer) throws IOException {
		
		List<String> csvRow = new ArrayList<String>();
		if(object.get("id")!= null) {
			String id = (String) object.get("id");
			csvRow.add(id);
		}
		else {
			String id = (String) object.get("_id");
			csvRow.add(id);
		}
		
		String phones = (String) object.get("phones");
		csvRow.add(phones);
		
		String email = (String) object.get("email");
		csvRow.add(email);
		
		String firstname = (String) object.get("firstname");
		csvRow.add(firstname);
		
		String lastname = (String) object.get("lastname");
		csvRow.add(lastname);
		
		String role = String.valueOf(object.get("role"));
		csvRow.add(role);
		
		String username = (String) object.get("username");
		csvRow.add(username);
		
		String isActive = String.valueOf(object.get("isActive"));
		csvRow.add(isActive);
		
		String createdAt = (String) object.get("_created_at");
		csvRow.add(createdAt);
		
		String updatedAt = (String) object.get("_updated_at");
		csvRow.add(updatedAt);
		
		String[] row= getArrayFromList(csvRow);
		writer.writeNext(row);
		
	}
	
	private String[] getArrayFromList(List<String> list) {

		int lengthOfList = list.size();
		String[] array = new String[lengthOfList];
		int i = 0;
		for (String listElement : list) {
			array[i] = listElement;
			i++;
		}
		return array;
	}
	
	private byte[] getByteFromFile(File file) {
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream fileInputStream = new FileInputStream(file);) {
            fileInputStream.read(bytes);
        } catch (Exception e) {
            logger.error("Caught Exception: {}",e);
        }
        return bytes;
    }

}
