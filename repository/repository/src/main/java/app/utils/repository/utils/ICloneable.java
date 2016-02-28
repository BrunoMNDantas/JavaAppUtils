package app.utils.repository.utils;

public interface ICloneable<T> {

	public T clone() throws CloneNotSupportedException;
	
}
