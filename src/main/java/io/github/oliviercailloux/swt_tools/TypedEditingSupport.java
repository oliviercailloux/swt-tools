package io.github.oliviercailloux.swt_tools;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.EditingSupport;

/**
 * <p>
 * An EditingSupport with generics. Objects of this type convert elements from
 * the untyped (Object) elements, given by the underlying column viewer, to
 * elements of type E. Similarly, objects of this type convert values from the
 * untyped (Object) values given by the underlying cell editors to values of
 * type V. Both conversions are done using a cast.
 * </p>
 * <p>
 * The user of this class must ensure that the underlying column viewer always
 * gives elements of type E, and that the underlying cell editors return and
 * accept values of type V.
 * </p>
 * <p>
 * The underlying column viewer is set at construction time. The underlying cell
 * editors are set thanks to the {@link #getCellEditorTyped(E)} method (to be
 * overriden).
 * </p>
 * <p>
 * User must also override {@link #getValueTyped(E)} to provide values
 * corresponding to elements; and {@link #setValueTyped(E, V)} to set the values
 * sent by the cell editor back to the model. The user may also override
 * {@link #canEditTyped(E)} (<code>true</code> by default).
 * </p>
 *
 * @author Olivier Cailloux
 *
 * @param <E>
 *            the type of elements returned by the underlying column viewer.
 * @param <V>
 *            the type of values returned, and accepted, by the underlying cell
 *            editors.
 */
public abstract class TypedEditingSupport<E, V> extends EditingSupport {

	private final Class<E> classOfElements;

	private final Class<V> classOfValues;

	public TypedEditingSupport(ColumnViewer viewer, Class<E> classOfElements, Class<V> classOfValues) {
		super(viewer);
		requireNonNull(classOfElements);
		requireNonNull(classOfValues);
		this.classOfElements = classOfElements;
		this.classOfValues = classOfValues;
	}

	/**
	 * <p>
	 * Is the cell editable
	 * </p>
	 * <p>
	 * This method is simply a better typed version of {@link #canEdit(Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @return true if editable
	 */
	public boolean canEditTyped(@SuppressWarnings("unused") E element) {
		return true;
	}

	/**
	 * <p>
	 * The editor to be shown
	 * </p>
	 * <p>
	 * This method is simply a better typed version of
	 * {@link #getCellEditor(Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @return the CellEditor
	 */
	public abstract CellEditor getCellEditorTyped(E element);

	public E getTypedElement(Object element) {
		return element == null ? null : classOfElements.cast(element);
	}

	public V getTypedValue(Object value) {
		return value == null ? null : classOfValues.cast(value);
	}

	/**
	 * <p>
	 * Get the value to set to the editor.
	 * </p>
	 * <p>
	 * This method is simply a better typed version of
	 * {@link #getValue(Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @return the value shown
	 */
	public abstract V getValueTyped(E element);

	/**
	 * <p>
	 * Sets the new value on the given element. Note that implementers need to
	 * ensure that <code>getViewer().update(element, null)</code> or similar
	 * methods are called, either directly or through some kind of listener
	 * mechanism on the implementer's model, to cause the new value to appear in
	 * the viewer.
	 * </p>
	 *
	 * <p>
	 * This method is simply a better typed version of
	 * {@link #setValue(Object, Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @param value
	 *            the new value
	 */
	public abstract void setValueTyped(E element, V value);

	@Override
	protected boolean canEdit(Object element) {
		return canEditTyped(getTypedElement(element));
	}

	@Override
	protected CellEditor getCellEditor(Object element) {
		return getCellEditorTyped(getTypedElement(element));
	}

	@Override
	protected Object getValue(Object element) {
		return getValueTyped(getTypedElement(element));
	}

	@Override
	protected void setValue(Object element, Object value) {
		setValueTyped(getTypedElement(element), getTypedValue(value));
	}
}
