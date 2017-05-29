package io.github.oliviercailloux.swt_tools;

import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.widgets.TableColumn;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

public class JFace {

	@SuppressWarnings("unused")
	static final Logger LOGGER = LoggerFactory.getLogger(JFace.class);

	public static <E, V> TableViewerColumn getComboBoxTableViewerColumn(TableViewer viewer, TableColumn column,
			ComboBoxEditingSupport<E, V> editingSupport) {
		final TableViewerColumn col = new TableViewerColumn(viewer, column);
		col.setEditingSupport(editingSupport);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				final E typedElement = editingSupport.getTypedElement(element);
				final V value = editingSupport.getValueTyped(typedElement);
				LOGGER.debug("Returning text label (column {}) for {}: {}.", column, element, value);
				return editingSupport.toString(value);
			}
		});
		return col;
	}

	public static <E> TableViewerColumn getTextTableViewerColumn(TableViewer viewer, TableColumn column,
			TextEditingSupport<E> editingSupport) {
		final TableViewerColumn col = new TableViewerColumn(viewer, column);
		col.setEditingSupport(editingSupport);
		col.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				final E typedElement = editingSupport.getTypedElement(element);
				final String value = editingSupport.getValueTyped(typedElement);
				LOGGER.info("Returning text label (column {}) for {}: {}.", column, element, value);
				return Strings.emptyToNull(value);
			}
		});
		return col;
	}

}
