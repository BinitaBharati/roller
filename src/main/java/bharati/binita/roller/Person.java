package bharati.binita.roller;

import java.io.Serializable;

/**
 * 
 * @author binita.bharati@gmail.com
 * Bean class used to map the protobuf autogenerated class.
 *
 */

public class Person implements Serializable{
	
	private int id;
	private String name;
	
	public Person(int id, String name) {
		this.id = id;
		this.name = name;
	}
		
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "{id : "+ this.id +"}";
	}

}