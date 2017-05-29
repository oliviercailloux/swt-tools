package io.github.oliviercailloux.swt_tools;

import static java.util.Objects.requireNonNull;

import java.util.function.Function;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ICellEditorValidator;

/**
 * <p>
 * An Editing Support with support for generic types (see
 * {@link TypedEditingSupport}) and a single underlying cell editor. Objects of
 * this type also permit to set a validator to restrict the values that the
 * underlying cell editor will accept.
 * </p>
 * <p>
 * This object accepts two validators, a “first-level” validator and a
 * (unqualified) validator. The effective validator is a composition of those
 * two validators: if the first-level validator reports an error, the effective
 * validator reports that error (the unqualified validator is not invoked); and
 * otherwise, the unqualified validator is invoked. If a validator is set to
 * <code>null</code>, it considers everything acceptable (but the other
 * validator still operates). Both validators are <code>null</code> by default
 * (subclasses may change this).
 * </p>
 * <p>
 * The user <em>must</em> go through this object to change the validator
 * behavior of the underlying cell editor. The user may <em>not</em> set or
 * remove the validator directly on the underlying cell editor.
 * </p>
 * <p>
 * The user must call {@link #setCellEditor(CellEditor)}; override
 * {@link #getValueTyped(Object) #getValueTyped(E)} to provide values
 * corresponding to elements; and {@link #setValueTyped(Object, Object)
 * #setValueTyped(E, V)} to set the values sent by the cell editor back to the
 * model. The user may also override {@link #canEditTyped(Object)
 * #canEditTyped(E)} (<code>true</code> by default).
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
public abstract class TypedEditingSupportConstantEditor<E, V> extends TypedEditingSupport<E, V> {

	private CellEditor editor;

	Function<V, String> valueToErrorMessage1;

	Function<V, String> valueToErrorMessage2;

	public TypedEditingSupportConstantEditor(ColumnViewer viewer, Class<E> classOfElements, Class<V> classOfValues) {
		super(viewer, classOfElements, classOfValues);
		valueToErrorMessage1 = null;
		valueToErrorMessage2 = null;
	}

	/**
	 * Returns the cell editor underlying this object.
	 *
	 * @return <code>null</code> iff has never been set.
	 */
	public CellEditor getCellEditor() {
		return editor;
	}

	@Override
	public CellEditor getCellEditorTyped(E element) {
		return editor;
	}

	/**
	 * Sets the cell editor underlying this object.
	 *
	 * @param editor
	 *            not <code>null</code>.
	 */
	public void setCellEditor(CellEditor editor) {
		requireNonNull(editor);
		this.editor = editor;
		setValidator();
	}

	/**
	 * <p>
	 * Sets the input validator for this cell editor.
	 * </p>
	 * <p>
	 * The validator is given the value to be validated, and must return a
	 * string indicating whether the given value is valid; <code>null</code>
	 * means valid, and non-<code>null</code> means invalid, with the result
	 * being the error message to display to the end user.
	 * </p>
	 *
	 * @param valueToErrorMessage
	 *            the input validator, or <code>null</code> if none
	 */
	public void setValidator(Function<V, String> valueToErrorMessage) {
		this.valueToErrorMessage2 = valueToErrorMessage;
	}

	private void setValidator() {
		getCellEditor().setValidator(new ICellEditorValidator() {
			@Override
			public String isValid(Object value) {
				final V typedValue = getTypedValue(value);
				final String firstLevelErrorMessage = valueToErrorMessage1 == null ? null
						: valueToErrorMessage1.apply(typedValue);
				if (firstLevelErrorMessage != null) {
					return firstLevelErrorMessage;
				}
				return valueToErrorMessage2 == null ? null : valueToErrorMessage2.apply(typedValue);
			}
		});
	}

	/**
	 * <p>
	 * Sets the first-level input validator for this cell editor.
	 * </p>
	 * <p>
	 * The validator is given the value to be validated, and must return a
	 * string indicating whether the given value is valid; <code>null</code>
	 * means valid, and non-<code>null</code> means invalid, with the result
	 * being the error message to display to the end user.
	 * </p>
	 *
	 * @param valueToErrorMessage
	 *            the input validator, or <code>null</code> if none
	 */
	protected void setFirstLevelValidator(Function<V, String> valueToErrorMessage) {
		this.valueToErrorMessage1 = valueToErrorMessage;
	}

}
