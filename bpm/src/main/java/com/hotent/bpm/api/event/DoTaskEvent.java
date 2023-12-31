package com.hotent.bpm.api.event;

import org.springframework.context.ApplicationEvent;

public class DoTaskEvent extends ApplicationEvent{
	private static final long serialVersionUID = 1L;

	public DoTaskEvent(Object source) {
		super(source);
	}
}
