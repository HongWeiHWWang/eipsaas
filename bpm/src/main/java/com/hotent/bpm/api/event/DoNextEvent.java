package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

public class DoNextEvent extends ApplicationEvent{

	/**
	 * serialVersionUID
	 * @since 1.0.0
	 */
	private static final long serialVersionUID = 2596106062449086209L;
	
	public DoNextEvent(DoNextModel source) {
		super(source);
	}

	
	
	

}
