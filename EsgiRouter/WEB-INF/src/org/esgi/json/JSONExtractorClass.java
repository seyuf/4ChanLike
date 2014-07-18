package org.esgi.json;



import java.io.File;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.esgi.web.action.IContext;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JSONExtractorClass {

	private static LinkedHashSet<String> elements = new LinkedHashSet<String>();
	private static Hashtable<String, LinkedHashSet<String>> dependencies = new Hashtable<String, LinkedHashSet<String>>();
	
	
	public static LinkedList<String> getDependencies(String file) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		JsonNode rootNode = mapper.readValue(new File(file), JsonNode.class);
		
		rootNode = mapper.readTree(new File(file));
		
		Iterator<Map.Entry<String,JsonNode> > fieldsIterator = rootNode.fields();
		traverse(fieldsIterator, mapper);
		
		return getDependenciesInOrder(dependencies, elements);
	}
	
	private static LinkedList<String> getDependenciesInOrder(Hashtable<String, LinkedHashSet<String>> deps, LinkedHashSet<String> els) {
		LinkedList<String> ret = new LinkedList<String>();
		
		for (String s : els) {
			if (!deps.containsKey(s)) {
				deps.put(s, new LinkedHashSet<String>());
			}
		}
		
		while (ret.size() < els.size()) {
			out:
			for (String key : deps.keySet()) {
				System.out.println("dependencies"+key);
			    if (deps.get(key).isEmpty()) {
			    	ret.add(key);
			    	deps.remove(key);
			    } else {
			    	int dep_number = deps.get(key).size();
			    	for(String s : deps.get(key)) {
			    		if (ret.contains(s)) {
			    			dep_number--;
			    		}
			    	}
			    	if (dep_number<=0) {
		    			ret.add(key);
		    			deps.remove(key);
		    			break out;
		    		}
			    }
			}
		}
		return ret;
	}
	
	private static LinkedHashSet<String> traverse(Iterator<Map.Entry<String,JsonNode> > e_field, ObjectMapper e_mapper) throws JsonParseException, JsonMappingException, IOException {
		LinkedHashSet<String> tmp_vec = new LinkedHashSet<String>();
		while (e_field.hasNext()) {
			Map.Entry<String,JsonNode> field = e_field.next();
			
			if (field.getValue().isArray()) {
				TypeReference<List<JsonNode>> typeRef = new TypeReference<List<JsonNode>>(){};
				List<JsonNode> list = e_mapper.readValue(field.getValue().traverse(), typeRef);
		        for (JsonNode n : list) {
		        	if (n.isObject()) {
		        		tmp_vec.addAll(traverse(n.fields(), e_mapper));
		        	} else {
		        		//System.out.println(n.asText().toString() + " depends on : " + tmp_vec);
		        		if (!elements.contains(n.asText().toString()))
		    				elements.add(n.asText().toString());	
		        		tmp_vec.add(n.asText().toString());
		        	}
		        }
			}
			if (!elements.contains(field.getKey().toString()))
				elements.add(field.getKey().toString());
			
			dependencies.put(field.getKey().toString(), tmp_vec);
			//System.out.println(field.getKey().toString() + " depends on : " + tmp_vec);
		}
		return tmp_vec;
	}
}
