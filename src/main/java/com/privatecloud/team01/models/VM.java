package com.privatecloud.team01.models;

import java.util.ArrayList;



public class VM {
	private String vmName;
	
	public VM(){
		//constructor for request mapping
	}
	public VM(String vmName) {
		super();
		this.vmName=vmName;	
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	
	//private String diskMode;
	//private String guestOSId;
	
	//private long memoryMB;
	//private long diskSizeKB;
	
	//private int cpuCount;
	
	//Identify VM by instanceUuid
	
/*	public VM(String vmName, String diskMode, String guestOSId, long memoryMB,
			long diskSizeKB, int cpuCount) {
		super();
		this.vmName = vmName;
		this.diskMode = diskMode;
		this.guestOSId = guestOSId;
		this.memoryMB = memoryMB;
		this.diskSizeKB = diskSizeKB;
		this.cpuCount = cpuCount;
	}

	public String getVmName() {
		return vmName;
	}

	public void setVmName(String vmName) {
		this.vmName = vmName;
	}

	public String getDiskMode() {
		return diskMode;
	}

	public void setDiskMode(String diskMode) {
		this.diskMode = diskMode;
	}

	public String getGuestOSId() {
		return guestOSId;
	}

	public void setGuestOSId(String guestOSId) {
		this.guestOSId = guestOSId;
	}

	public long getMemoryMB() {
		return memoryMB;
	}

	public void setMemoryMB(long memoryMB) {
		this.memoryMB = memoryMB;
	}

	public long getDiskSizeKB() {
		return diskSizeKB;
	}

	public void setDiskSizeKB(long diskSizeKB) {
		this.diskSizeKB = diskSizeKB;
	}

	public int getCpuCount() {
		return cpuCount;
	}

	public void setCpuCount(int cpuCount) {
		this.cpuCount = cpuCount;
	}

	
	*/
	
}
