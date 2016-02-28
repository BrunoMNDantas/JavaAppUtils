package app.utils.repository;

public class Person {
	
	public String name;
	public int age;
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	public boolean Equals(Object other){
		if(other == null || !(other instanceof Person) )
			return false;
		
		return ((Person)other).name.equals(this.name) && ((Person)other).age == this.age; 
	}
	
}
