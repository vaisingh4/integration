package com.sirionlabs.integration.services;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sirionlabs.integration.entities.MappingJson;

@Service
public class MappingServiceImpl implements MappingService {

	@Override
	public ResponseEntity<?> getMappingJsonFile(MultipartFile jsonFile, MultipartFile textFile, String type) {
		List<String> fields = processFileds(textFile);
		JSONObject result = processJsonFieldsFile(jsonFile, textFile, fields);
		
		if (type.equals("1")) {
			return new ResponseEntity<>(result, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(fields, HttpStatus.OK);
		}

	}

	private JSONObject processJsonFieldsFile(MultipartFile jsonFile, MultipartFile textFile, List<String> fields) {
		JSONObject joc = new JSONObject();
		JSONObject json = processJson(jsonFile);
		return generateFieldMapping(joc, fields, json);

	}

	private JSONObject generateFieldMapping(JSONObject joc, List<String> fields, JSONObject json) {
		Map address = ((Map) ((Map) ((Map) json.get("body")).get("layoutInfo")).get("layoutComponent"));
		final String FIELDS = "fields";
		JSONArray jasonArrayLevel = (JSONArray) address.get(FIELDS);
		JSONObject jsonObjectLevel = (JSONObject) jasonArrayLevel.get(0);
		JSONArray ja1 = (JSONArray) jsonObjectLevel.get(FIELDS);
		JSONArray ja2 = ja1;
		JSONArray ja3 = ja2;

		JSONObject jo1 = null;

		for (int i = 0; i < ja1.size(); i++) {
			jsonObjectLevel = (JSONObject) ja1.get(i);
			ja2 = (JSONArray) jsonObjectLevel.get(FIELDS);
			if (ja2 != null) {
				for (int x = 0; x < ja2.size(); x++) {
					jo1 = (JSONObject) ja2.get(x);
					ja3 = (JSONArray) jo1.get("fields");
					if (ja3 != null) {
						joc = extracted(fields, ja3, joc);
					} else {
						joc = extracted(fields, ja2, joc);
					}
				}
			}
		}
		return joc;
	}

	private JSONObject extracted(List<String> fields, JSONArray ja3, JSONObject joc) {
		JSONObject jo2;
		for (int z = 0; z < ja3.size(); z++) {
			jo2 = (JSONObject) ja3.get(z);
			for (int y = 0; y < fields.size(); y++) {
				if ((jo2.get("id") != null) && (jo2.get("id").toString().equalsIgnoreCase(fields.get(y)))) {
					fields.remove(y);
					String s = (String) jo2.get("name");
					if (s != null) {
						Map<Object, Object> m = new LinkedHashMap<>(4);
						if (s.contains("dyn"))
							m.put("type", "dynamic");
						else
							m.put("type", "static");
						m.put("name", jo2.get("name"));
						if (jo2.get("type").toString().equals("textarea")) {
							m.put("htmlType", "text");
						} else if (jo2.get("type").toString().equals("select")) {
							m.put("htmlType", "dropdown");
						} else
							m.put("htmlType", jo2.get("type"));
						m.put("uiName", jo2.get("label"));
						joc.put(jo2.get("id"), m);
					}
				}
			}
		}
		return joc;

	}

	private List<String> processFileds(MultipartFile textFile) {
		BufferedReader br;
		String line;
		List<String> mappingFields = new ArrayList<>();
		try {
			InputStream is = textFile.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				mappingFields.add(line);
			}
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return mappingFields;
	}

	private JSONObject processJson(MultipartFile jsonFile) {
		JSONObject json = null;
		try {
			BufferedReader br;
			InputStream is = jsonFile.getInputStream();
			br = new BufferedReader(new InputStreamReader(is));
			Object obj = new JSONParser().parse(br);
			json = (JSONObject) obj;
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		return json;
	}

	@Override
	public MappingJson getMappingJsonFile() {
		return new MappingJson(HttpStatus.BAD_REQUEST, "Some thing went wrong",
				"Type is incorrect or file is not selected", "/integration/mapping");
	}

}
