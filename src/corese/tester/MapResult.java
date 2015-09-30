package corese.tester;

import java.util.HashMap;

public class MapResult extends HashMap<String, String> implements Comparable<MapResult> {

	private static final long serialVersionUID = -7349301776572964336L;

	@Override
	public int compareTo(MapResult map) {
		int comparing;
		for(String key : keySet()){
			if(map.containsKey(key)){
				comparing = get(key).compareTo(map.get(key));
				if(comparing != 0){
					return comparing;
				}
			} else{
				return -1;
			}
		}
		
		return 0;
	}
	
	public boolean equals(Object o){
		MapResult map = (MapResult)o;
		for(String key : keySet()){
			if(map.containsKey(key)){
				if(!get(key).equals(map.get(key))){
					return false;
				}
			} else{
				return false;
			}
		}
		return true;
	}
}
