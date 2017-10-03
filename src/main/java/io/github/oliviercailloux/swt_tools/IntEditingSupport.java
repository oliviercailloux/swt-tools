package io.github.oliviercailloux.swt_tools;

import java.util.function.Function;

import org.eclipse.jface.viewers.ColumnViewer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>
 * A {@link TextEditingSupport} that further restricts its content to values
 * that can be converted to integers.
 * </p>
 * <p>
 * The underlying control does not constrain the user while typing, only when
 * validating. Thus the user is free to type invalid characters (such as
 * letters). The reason for not using verify listeners (that constrain the user
 * while typing) is that, IMHO, it constrains the user without really helping
 * her.
 * <ul>
 * <li>It prevents copy/paste when the string copied includes an invalid
 * character (example: the user wants to paste the string “12455,32” and intends
 * to delete the last part);</li>
 * <li>if user types erroneously a mistaken character, then hits backspace by
 * reflex, the backspace will eat the wrong character;</li>
 * <li>the verify listener will allow pasting “-4244” next to “3411” (which
 * produces an invalid integer) but will refuse pasting “3411-4244”, a behavior
 * that is difficult for the user to understand.</li>
 * </ul>
 * </p>
 *
 * @author Olivier Cailloux
 *
 * @param <E>
 *            the type of elements returned by the underlying column viewer.
 */
public abstract class IntEditingSupport<E> extends TextEditingSupport<E> {

	@SuppressWarnings("unused")
	private static final Logger LOGGER = LoggerFactory.getLogger(IntEditingSupport.class);

	public IntEditingSupport(ColumnViewer viewer, Class<E> classOfElements) {
		super(viewer, classOfElements);
		/** Here we forbid empty strings, strings equal to "-", … */
		setFirstLevelValidator(v -> !v.matches("[-]?[0-9]+") ? "Integer required." : null);
	}

	/**
	 * <p>
	 * Get the value to set to the editor.
	 * </p>
	 * <p>
	 * This method is simply a better typed equivalent to {@link #getValue(Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @return the value shown.
	 */
	public abstract int getIntValue(E element);

	@Override
	public String getValueTyped(E element) {
		final int intValue = getIntValue(element);
		return Integer.toString(intValue);
	}

	/**
	 * <p>
	 * Sets the input validator for this cell editor.
	 * </p>
	 * <p>
	 * The validator is given the (integer) value to be validated, and must return a
	 * string indicating whether the given value is valid; <code>null</code> means
	 * valid, and non-<code>null</code> means invalid, with the result being the
	 * error message to display to the end user.
	 * </p>
	 * <p>
	 * This is simply a better-typed version of {@link #setValidator(Function)}.
	 * </p>
	 *
	 * @param valueToErrorMessage
	 *            the input validator, or <code>null</code> if none
	 */
	public void setIntegerValidator(Function<Integer, String> valueToErrorMessage) {
		setValidator(valueToErrorMessage.compose(v -> Integer.valueOf(v)));
	}

	/**
	 * <p>
	 * Sets the new value on the given element. Note that implementers need to
	 * ensure that <code>getViewer().update(element, null)</code> or similar methods
	 * are called, either directly or through some kind of listener mechanism on the
	 * implementer's model, to cause the new value to appear in the viewer.
	 * </p>
	 *
	 * <p>
	 * This method is simply a better typed equivalent to
	 * {@link #setValue(Object, Object)}.
	 * </p>
	 *
	 * @param element
	 *            the model element
	 * @param value
	 *            the new value
	 */
	public abstract void setIntValue(E element, int value);

	@Override
	public void setValueTyped(E element, String value) {
		assert value != null;
		assert !value.isEmpty();
		final int intValue = Integer.valueOf(value).intValue();
		setIntValue(element, intValue);
	}

}
