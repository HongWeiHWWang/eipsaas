package com.hotent.bpm.plugin.task.taskcopyto.def;

import java.util.ArrayList;
import java.util.List;

import com.hotent.bpm.plugin.core.plugindef.AbstractBpmTaskPluginDef;
import com.hotent.bpm.plugin.task.taskcopyto.def.model.CopyToItem;

public class TaskCopyToPluginDef extends AbstractBpmTaskPluginDef{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6236518028442879777L;
	private List<CopyToItem> copyToItems = new ArrayList<CopyToItem>();

	public List<CopyToItem> getCopyToItems() {
		return copyToItems;
	}

	public void setCopyToItems(List<CopyToItem> copyToItems) {
		this.copyToItems = copyToItems;
	}
	
}
