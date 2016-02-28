package app.utils.repository;

import app.utils.repository.utils.ICloneable;

public class Person implements ICloneable<Person>{
	
	public String name;
	public int age;
	
	public Person(String name, int age) {
		this.name = name;
		this.age = age;
	}
	
	@Override
	public boolean equals(Object other){
		if(other == null || !(other instanceof Person) )
			return false;
		
		return ((Person)other).name.equals(this.name) && ((Person)other).age == this.age; 
	}
	
	@Override
	public Person clone(){
		return new Person(name, age);
	}
	
}
