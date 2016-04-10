package apputils.repository.repository.clonable;

public interface ICloneable<T> {

	public T clone() throws CloneNotSupportedException;
	
}
