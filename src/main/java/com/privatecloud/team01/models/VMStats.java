package com.privatecloud.team01.models;

public class VMStats {
	String name;
	String guestFullName;
	String version;
	int numCPU;
	int memory;
	String IPAdress;
	String guestState;
	// for running vm
	Integer overallCPUusage;
	Integer guestmemoryUsage;
	Integer onsumedOverheadMemory;
	Integer ftLatencyStatus;
	Integer heartBeatStatus;

	public VMStats(){}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGuestFullName() {
		return guestFullName;
	}

	public void setGuestFullName(String guestFullName) {
		this.guestFullName = guestFullName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getNumCPU() {
		return numCPU;
	}

	public void setNumCPU(int numCPU) {
		this.numCPU = numCPU;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public String getIPAdress() {
		return IPAdress;
	}

	public void setIPAdress(String iPAdress) {
		IPAdress = iPAdress;
	}

	public String getGuestState() {
		return guestState;
	}

	public void setGuestState(String guestState) {
		this.guestState = guestState;
	}

	public Integer getOverallCPUusage() {
		return overallCPUusage;
	}

	public void setOverallCPUusage(Integer overallCPUusage) {
		this.overallCPUusage = overallCPUusage;
	}

	public Integer getGuestmemoryUsage() {
		return guestmemoryUsage;
	}

	public void setGuestmemoryUsage(Integer guestmemoryUsage) {
		this.guestmemoryUsage = guestmemoryUsage;
	}

	public Integer getOnsumedOverheadMemory() {
		return onsumedOverheadMemory;
	}

	public void setOnsumedOverheadMemory(Integer onsumedOverheadMemory) {
		this.onsumedOverheadMemory = onsumedOverheadMemory;
	}

	public Integer getFtLatencyStatus() {
		return ftLatencyStatus;
	}

	public void setFtLatencyStatus(Integer ftLatencyStatus) {
		this.ftLatencyStatus = ftLatencyStatus;
	}

	public Integer getHeartBeatStatus() {
		return heartBeatStatus;
	}

	public void setHeartBeatStatus(Integer heartBeatStatus) {
		this.heartBeatStatus = heartBeatStatus;
	}

}
