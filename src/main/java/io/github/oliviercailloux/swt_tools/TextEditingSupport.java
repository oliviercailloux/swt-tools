package io.github.oliviercailloux.swt_tools;

import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * An Editing Support with support for generic types and a single underlying
 * {@link TextCellEditor}. Objects of this type also permit to set a validator
 * to restrict the values that the underlying cell editor will accept.
 * </p>
 * <p>
 * The user of this class must ensure that the underlying column viewer always
 * gives elements of type E.
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
 * The user must override {@link #getValueTyped(Object) #getValueTyped(E)} to
 * provide values corresponding to elements; and
 * {@link #setValueTyped(Object, Object) #setValueTyped(E, String)} to set the
 * values sent by the text cell editor back to the model. The user may also
 * override {@link #canEditTyped(Object) #canEditTyped(E)} (<code>true</code> by
 * default).
 * </p>
 *
 * @author Olivier Cailloux
 *
 * @param <E>
 *            the type of elements returned by the underlying column viewer.
 */
public abstract class TextEditingSupport<E> extends TypedEditingSupportConstantEditor<E, String> {
	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(TextEditingSupport.class);

	/**
	 * @param viewer
	 *            must have a composite as underlying control.
	 */
	public TextEditingSupport(ColumnViewer viewer, Class<E> classOfElements) {
		super(viewer, classOfElements, String.class);
		setCellEditor(new TextCellEditor((Composite) viewer.getControl()));
	}

	public TextCellEditor getTextCellEditor() {
		return (TextCellEditor) getCellEditor();
	}
}