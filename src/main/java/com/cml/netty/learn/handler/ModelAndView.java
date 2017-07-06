package com.cml.netty.learn.handler;

import java.util.HashMap;
import java.util.Map;

public class ModelAndView {
	private Map<String, Object> model = new HashMap<String, Object>();
	private String view;

	public ModelAndView(String view) {
		super();
		this.view = view;
	}

	public ModelAndView() {
	}

	public ModelAndView(Map<String, Object> model, String view) {
		super();
		this.model = model;
		this.view = view;
	}

	public Map<String, Object> getModel() {
		return model;
	}

	public void setModel(Map<String, Object> model) {
		this.model = model;
	}

	public String getView() {
		return view;
	}

	public void setView(String view) {
		this.view = view;
	}

}
