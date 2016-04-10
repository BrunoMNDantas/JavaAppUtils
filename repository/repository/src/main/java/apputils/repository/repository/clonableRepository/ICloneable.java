package apputils.repository.repository.clonableRepository;

public interface ICloneable<T> {

	public T clone() throws CloneNotSupportedException;
	
}
