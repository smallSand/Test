package test;

import org.json.JSONArray;
import org.json.JSONObject;

public class JsonPare {

	public static void main(String arg[]){
		JSONObject obj=new JSONObject();
		obj.put("year", "2015");
		obj.put("quarter", "1");
		obj.put("probability", "290%");
		JSONArray arr=new JSONArray();
		arr.put(obj);
		System.out.println(arr.toString());
	}
}
