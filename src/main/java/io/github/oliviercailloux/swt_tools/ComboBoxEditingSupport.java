package io.github.oliviercailloux.swt_tools;

import java.util.List;

import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.ColumnViewer;
import org.eclipse.jface.viewers.ComboBoxViewerCellEditor;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.widgets.Composite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

/**
 * <p>
 * An editing support with typed elements and values, using a
 * {@link ComboBoxViewerCellEditor} underlying cell editor. Objects of this type
 * convert elements from the untyped (Object) elements, given by the underlying
 * column viewer, to elements of type E. Similarly, objects of this type convert
 * values from the untyped (Object) values given by the underlying cell editor
 * to values of type V. Both conversions are done using a cast.
 * </p>
 * <p>
 * The user of this class must ensure that the underlying column viewer always
 * gives elements of type E. The underlying column viewer is set at construction
 * time.
 * </p>
 * <p>
 * Objects of this type ensure that the underlying combo box cell editor rejects
 * values out of the provided list of items, thus, the end-user may not type
 * into the combo box a value of its choice that would not belong to the
 * pre-defined list of acceptable choices.
 * </p>
 * <p>
 * The user <em>must</em> go through this object to change the validator
 * behavior of the underlying cell editor. The user may <em>not</em> set or
 * remove the validator directly on the underlying cell editor, and may not
 * change the first-level validator.
 * </p>
 * <p>
 * The user of this class must override {@link #getValueTyped(E)} to provide
 * values corresponding to elements; and {@link #setValueTyped(E, V)} to set the
 * values sent by the cell editor back to the model. The user may also override
 * {@link #canEditTyped(E)} (<code>true</code> by default), and
 * {@link #toString(V)} (which by default returns the empty string if the value
 * is <code>null</code>, and otherwise uses {@link Object#toString()}). The
 * latter is used to show the entries in the combo box to the end-user.
 * </p>
 *
 * @author Olivier Cailloux
 *
 * @param <E>
 *            the type of elements returned by the underlying column viewer.
 * @param <V>
 *            the type of values to use with the underlying
 *            {@link ComboBoxViewerCellEditor}.
 */
public abstract class ComboBoxEditingSupport<E, V> extends TypedEditingSupportConstantEditor<E, V> {
	@SuppressWarnings("unused")
	static final Logger LOGGER = LoggerFactory.getLogger(ComboBoxEditingSupport.class);

	private List<V> items;

	/**
	 * @param viewer
	 *            must have a composite as underlying control.
	 */
	public ComboBoxEditingSupport(ColumnViewer viewer, Class<E> classOfElements, Class<V> classOfValues) {
		super(viewer, classOfElements, classOfValues);
		setCellEditor(new ComboBoxViewerCellEditor((Composite) viewer.getControl()));
		/**
		 * We could implement inputChanged on the array content provider to
		 * intercept input.
		 */
		getComboBoxCellEditor().setContentProvider(ArrayContentProvider.getInstance());
		getComboBoxCellEditor().setLabelProvider(new LabelProvider() {
			@Override
			public String getText(Object value) {
				final V typedValue = getTypedValue(value);
				return ComboBoxEditingSupport.this.toString(typedValue);
			}
		});
		setFirstLevelValidator(value -> value == null ? "The selection must be one of the provided choices." : null);
		setItems(null);
	}

	@Override
	public CellEditor getCellEditorTyped(E element) {
		return getCellEditor();
	}

	/**
	 * Returns the underlying cell editor. This returns the same object than
	 * {@link #getCellEditorTyped(Object)}, and the same object than
	 * {@link #getCellEditor()}.
	 *
	 * @return not <code>null</code>.
	 */
	public ComboBoxViewerCellEditor getComboBoxCellEditor() {
		return (ComboBoxViewerCellEditor) getCellEditor();
	}

	/**
	 * Only returns the items if they have been set using this object
	 * constructor or {@link #setItems(List)}. If the items have been set (or
	 * modified) directly on the underlying widget, this method has unspecified
	 * effects.
	 *
	 * @return the items, or <code>null</code> if not set.
	 */
	public List<V> getItems() {
		return items;
	}

	/**
	 * Sets the input items to the underlying ComboBoxCellEditor.
	 *
	 * @param items
	 *            a <code>null</code> value is converted to an empty list.
	 */
	public void setItems(List<V> items) {
		final List<V> its = items == null ? ImmutableList.of() : items;
		this.items = its;
		getComboBoxCellEditor().setInput(items);
	}

	public String toString(V value) {
		return value == null ? "" : value.toString();
	}
}