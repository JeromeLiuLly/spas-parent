package com.candao.spas.convert.swing.base.constants;

import com.candao.spas.convert.swing.base.event.EventDispatcher;

public class Errors {

	public static final String ERROR = "ERROR";

	public static EventDispatcher dispatcher = new EventDispatcher();

	public static void dispatch(String msg, Exception exception) {
		System.out.println("Error: " + msg);

		if (exception != null) {
			exception.printStackTrace();
		}

		dispatcher.dispatch(ERROR, new Object[] { msg, exception });
	}

}