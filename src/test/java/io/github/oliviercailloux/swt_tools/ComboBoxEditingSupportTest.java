package io.github.oliviercailloux.swt_tools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.HashBiMap;
import com.google.common.collect.Lists;

public class ComboBoxEditingSupportTest {

	@SuppressWarnings("unused")
	static final Logger LOGGER = LoggerFactory.getLogger(ComboBoxEditingSupportTest.class);

	private Display display;

	private Shell shell;

	final HashBiMap<String, Double> strToD = HashBiMap.create();

	public void fireView() {
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	@Test
	public void test() {
		LOGGER.info("Start.");
		strToD.put("one", 1d);
		strToD.put("three", 3d);
		strToD.put("seven", 7d);
		strToD.put("fourteen", 14d);

		LOGGER.info("Starting display.");
		display = new Display();
		LOGGER.info("Starting shell.");
		shell = new Shell(display);
		LOGGER.info("Starting table viewer.");
		final TableViewer tableViewer = new TableViewer(shell);

		LOGGER.info("Creating es.");
		final ComboBoxEditingSupport<String, Double> ed = new ComboBoxEditingSupport<String, Double>(tableViewer,
				String.class, Double.class) {
			@Override
			public Double getValueTyped(String element) {
				return strToD.get(element);
			}

			@Override
			public void setValueTyped(String element, Double value) {
				fail("Do not use");
			}

			@Override
			public String toString(Double value) {
				LOGGER.debug("Value: {}.", value);
				return "The double: " + value;
			}
		};
		LOGGER.info("Asserting.");
		assertNull(ed.getComboBoxCellEditor().getValue());
		ed.setItems(Lists.newArrayList(strToD.values()));

		ed.getComboBoxCellEditor().setValue(3d);
		assertTrue(ed.getComboBoxCellEditor().isValueValid());
		assertEquals(3d, ed.getComboBoxCellEditor().getValue());
		ed.getComboBoxCellEditor().setValue(null);
		assertFalse(ed.getComboBoxCellEditor().isValueValid());
		assertNull(ed.getComboBoxCellEditor().getValue());
		LOGGER.info("End.");
	}

}
