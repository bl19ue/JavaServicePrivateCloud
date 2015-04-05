package com.privatecloud.team01.config;

import com.privatecloud.team01.R;
import com.vmware.vim25.mo.Datacenter;
import com.vmware.vim25.mo.Folder;
import com.vmware.vim25.mo.InventoryNavigator;
import com.vmware.vim25.mo.ResourcePool;
import com.vmware.vim25.mo.ServiceInstance;

public class VMRequirement {
	private Folder vmFolder;
	private Folder rootFolder;
	private ServiceInstance vCenter;
	private Datacenter datacenter;
	private ResourcePool resourcePool;
	
	public VMRequirement(ServiceInstance vCenter) throws Exception{
		this.vCenter = vCenter;
		if(this.vCenter == null){return;}
		
		this.rootFolder = vCenter.getRootFolder();
		this.datacenter = (Datacenter) new InventoryNavigator(rootFolder).searchManagedEntity("Datacenter", R.datacenterName);
		this.resourcePool = (ResourcePool) new InventoryNavigator(datacenter).searchManagedEntities("ResourcePool")[0];
		this.vmFolder =  datacenter.getVmFolder();
		
		
	}
}
