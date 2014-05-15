package org.esgi.web.layout;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonExtractor {
	
private static List<String> values = new ArrayList<>();
	
	public List<String> extract() throws JsonProcessingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(new File("json.js"), JsonNode.class);
		JsonNode html_layout = rootNode.path("html_layout");
		extractJson (html_layout);
		for (String value : values) {
			System.out.println(value);
		}
		return values;
	}
	
	public void extractJson (JsonNode html_layout) {
		if (html_layout.isArray()) {
			for (int i = 0 ; i < html_layout.size() ; i++) {
				extractJson(html_layout.get(i));
			}
		} else if (html_layout.isObject()) {
			Iterator<String> iterator = html_layout.fieldNames();
			while (iterator.hasNext()) {
				String value = iterator.next();
				extractJson(html_layout.get(value));
				if (!values.contains(value)) {
					values.add(value);
				}
			}
		} else {
			if (!values.contains(html_layout.textValue())) {
				values.add(html_layout.textValue());
			}
		}
	}

}
