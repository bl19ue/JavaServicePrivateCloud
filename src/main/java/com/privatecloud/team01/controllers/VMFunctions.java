package com.privatecloud.team01.controllers;

import java.net.URL;
import java.rmi.RemoteException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.*;

import com.privatecloud.team01.models.VMStats;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class VMFunctions {

	public JSONObject createVM(String newVmname, String template_id)
			throws Exception {
		// create instance of ServiceInstance
		ServiceInstance si = new ServiceInstance(new URL(
				"https://130.65.132.101/sdk"), "administrator", "12!@qwQW",
				true);

		Folder rootFolder = si.getRootFolder();

		VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder)
				.searchManagedEntity("VirtualMachine", template_id);

		// check whether there is vm or not
		if (vm == null) {

			System.out.println("No VM " + newVmname + " found");
			
			return null;
		}

		/*
		 * if(vm.getConfig().template==true){ //check if the vm is a template or
		 * not System.out.println("yes Template found.");
		 */

		VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec(); // create
																			// an
																			// instance
																			// of
																			// vm
																			// clone
																			// specifications

		cloneSpec.setLocation(new VirtualMachineRelocateSpec()); // set location

		cloneSpec.setPowerOn(true);
		cloneSpec.setTemplate(false);

		String name = vm.getName();
		Task task = vm.cloneVM_Task((Folder) vm.getParent(), newVmname,
				cloneSpec);
		String name1 = vm.getName();
		System.out.println("Launching the VM creation task. "
				+ "Please wait ...");

		String status = task.waitForTask();
		JSONObject jsonObject = new JSONObject();
		
		// task status
		if (status == Task.SUCCESS) {
			
			System.out.println("VM got created successfully from template.");
			VirtualMachine thisNewVM = (VirtualMachine)new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", newVmname);
			
			//Turn on this VM
			Task ontask = thisNewVM.powerOnVM_Task(null);
			if (task.waitForTask() == Task.SUCCESS) {
				System.out.println(newVmname + " powered on");
				
			}
			jsonObject.put("name", newVmname);
			jsonObject.put("type", template_id);
			jsonObject.put("cpu", thisNewVM.getConfig().getHardware().getNumCPU());
			jsonObject.put("ram", thisNewVM.getConfig().getHardware().getMemoryMB());
			jsonObject.put("status", thisNewVM.getRuntime().getPowerState());
			jsonObject.put("created_at", new Date());
			
		} 
		else {

			System.out.println("Failure -: VM cannot be created");
			return null;
		}

//		name: String,
//		type: String,
//		cpu: {type: Number},
//		ram: {type: Number},
//		status: String,
//		created_at: String
		
		return jsonObject;
		// return the uuid of the vm created
		/*
		 * else
		 * 
		 * System.out.println("There is no template");
		 * 
		 * }
		 */
		// return true;

	}

	public boolean startVM() {
		return false;
	}

	// perform all Vm operations
	public boolean VMoperations(String vmname, String op) throws Exception {

		ServiceInstance si = new ServiceInstance(new URL(
				"https://130.65.132.101/sdk"), "administrator", "12!@qwQW",
				true);

		Folder rootFolder = si.getRootFolder();

		System.out.println(rootFolder.getName()); // Datacenters

		ManagedEntity[] mes = rootFolder.getChildEntity();

		for (int i = 0; i < mes.length; i++) {

			System.out.println(mes[i].getName()); // T01-DC

			if (mes[i] instanceof Datacenter) {
				Datacenter dc = (Datacenter) mes[i];

				Folder vmFolder = dc.getVmFolder(); // vm folder

				System.out.println(vmFolder.getName()); // vm

				// ManagedEntity vm_mainfolder = vmFolder.getChildEntity()[0];
				// // Discovered
				// virtual
				// machine

				ManagedEntity[] vms = new InventoryNavigator(vmFolder)
						.searchManagedEntities("VirtualMachine"); // find all vm
				// in the
				// discoverd
				// folder

				for (int j = 0; j < vms.length; j++) {
					System.out.println(vms[j].getName());
					if (vms[j] instanceof VirtualMachine) {
						VirtualMachine vm = (VirtualMachine) vms[j];
						String instanceUuid = vm.getConfig().instanceUuid;
						System.out.println((vm.getName()) + "," + instanceUuid);

						VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm
								.getRuntime();
						if (vmname.equals(vm.getName())) {
							if (vmri.getPowerState() == VirtualMachinePowerState.poweredOn
									&& op.equals("poweroff")) {
								Task task = vm.powerOffVM_Task();
								task.waitForTask();
								System.out.println("vm:" + vm.getName()
										+ " powered off.");
								
								return true;
							} else if ("poweron".equalsIgnoreCase(op)) {
								Task task = vm.powerOnVM_Task(null);
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " powered on");
									
									return true;
								}
							} else if ("reboot".equalsIgnoreCase(op)) {
								vm.rebootGuest();
								System.out.println(vmname
										+ " guest OS rebooted");
								
								return true;
							} else if ("reset".equalsIgnoreCase(op)) {
								Task task = vm.resetVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " reset");
									return true;
								}
							} else if ("standby".equalsIgnoreCase(op)) {
								vm.standbyGuest();
								System.out
										.println(vmname + " guest OS stoodby");
								return true;
							} else if ("suspend".equalsIgnoreCase(op)) {
								Task task = vm.suspendVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " suspended");
									return true;
								}
							} else if ("shutdown".equalsIgnoreCase(op)) {
								Task task = vm.suspendVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " suspended");
									return true;
								}
							}
						}
					}
				}
			}
		}
		
		return false;
	}

	public String VMStatus(String vmname) throws Exception {
		ServiceInstance si = new ServiceInstance(new URL(
				"https://130.65.132.101/sdk"), "administrator", "12!@qwQW",
				true);

		Folder rootFolder = si.getRootFolder();
		System.out.println(rootFolder.getName()); // Datacenters
		ManagedEntity[] mes = rootFolder.getChildEntity();

		for (int i = 0; i < mes.length; i++) {
			System.out.println(mes[i].getName()); // T01-DC
			if (mes[i] instanceof Datacenter) {
				Datacenter dc = (Datacenter) mes[i];
				Folder vmFolder = dc.getVmFolder(); // vm folder
				System.out.println(vmFolder.getName()); // vm
				//ManagedEntity vm_mainfolder = vmFolder.getChildEntity()[0]; // Discovered
																			// virtual
																			// machine

				ManagedEntity[] vms = new InventoryNavigator(vmFolder)
						.searchManagedEntities("VirtualMachine"); // find all vm
																	// in the
																	// discoverd
																	// folder
				for (int j = 0; j < vms.length; j++) {
					System.out.println(vms[j].getName());
					if (vms[j] instanceof VirtualMachine) {
						VirtualMachine vm = (VirtualMachine) vms[j];
						String instanceUuid = vm.getConfig().instanceUuid;
						System.out.println((vm.getName()) + "," + instanceUuid);

						VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm
								.getRuntime();
						if (vmname.equals(vm.getName())) {
							return vmri.getPowerState().name();
						}
					}
				}
			}
		}
		return "VM state not found";
	}


	static ArrayList<Long> cpuStatInfoArrayList = new ArrayList<Long>();
	static ArrayList<Long> memoryStatInfoArrayList = new ArrayList<Long>();
	static ArrayList<String> timeInfoArrayList = new ArrayList<String>();
	ArrayList<ArrayList> statInfo = new ArrayList<ArrayList>();
	static SimpleDateFormat sdf = new SimpleDateFormat("dd,HH,mm");
	
	public ArrayList<ArrayList> getStatistics(String vmname) throws Exception {
		String vCenterUrl = "https://130.65.132.101/sdk";
		String userName = "administrator";
		String password = "12!@qwQW";

		ServiceInstance si = new ServiceInstance(new URL(vCenterUrl), userName,	password, true);		
		//String vmname = "T01-VM03";
//		String vHostName = "130.65.133.132";
//		//String vHostName = "130.65.132.132";
//		ManagedEntity vHost = new InventoryNavigator(si.getRootFolder())
//				.searchManagedEntity("HostSystem", vHostName);

		ManagedEntity vm1 = new InventoryNavigator(si.getRootFolder()).searchManagedEntity("VirtualMachine", vmname);
		
		VirtualMachine vm = (VirtualMachine)vm1;
		
		if (vm == null) {
			return null;
		}
		System.out.println("VM: " + vm.getName());
		System.out.println("get_value: " + vm.getSummary().getVm().get_value());
		PerformanceManager perfMgr = si.getPerformanceManager();

		/*
		 * for(PerfCounterInfo pci:perfMgr.getPerfCounter()){
		 * System.out.println(pci); }
		 */
		// int perfInterval = 1800; // 30 minutes for PastWeek
		int perfInterval = 300;

		// retrieve all the available perf metrics for vm
		// PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null,
		// null, perfInterval);
		PerfMetricId[] pmis = perfMgr.queryAvailablePerfMetric(vm, null, null,
				perfInterval);
		PerfCounterInfo[] o = perfMgr.getPerfCounter();
		Map<String, Integer> perfCounterInfoMap = new HashMap<String, Integer>();
		for (int k = 0; k < o.length; k++) {
			perfCounterInfoMap.put(o[k].getNameInfo().key, o[k].getKey());
			// System.out.println(o[k].getNameInfo().key + "," +
			// o[k].getNameInfo().summary + "," + o[k].getKey() );
			// usage,CPU usage as a percentage during the interval,2
			// usage,Memory usage as percentage of total configured or available
			// memory,24
		}
		// -->>>>>>>>>
		for (PerfMetricId id : pmis) {
			id.setInstance("");
		}
		// -->>>>>>>>>

		Calendar curTime = si.currentTime();

		PerfQuerySpec qSpec = new PerfQuerySpec();
		qSpec.setEntity(vm.getRuntime().getHost());
		// metricIDs must be provided, or InvalidArgumentFault
		qSpec.setMetricId(pmis);
		qSpec.setFormat("normal"); // optional since it's default
		qSpec.setIntervalId(perfInterval);

		Calendar startTime = (Calendar) curTime.clone();
		startTime.roll(Calendar.DATE, -1);
		System.out.println("start:" + startTime.getTime());
		qSpec.setStartTime(startTime);

		Calendar endTime = (Calendar) curTime.clone();
		endTime.roll(Calendar.DATE, 0);
		System.out.println("end:" + endTime.getTime());
		qSpec.setEndTime(endTime);

		PerfCompositeMetric pv = perfMgr.queryPerfComposite(qSpec);
		if (pv != null) {
			// printPerfMetric(pv.getEntity());
			PerfEntityMetricBase[] pembs = pv.getChildEntity();
			System.out.println("pembs length: " + pembs.length);
			for (int i = 0; pembs != null && i < pembs.length; i++) {
				if (vm.getSummary().getVm().get_value()
						.equals(pembs[i].getEntity().get_value())) {
					printPerfMetric(pembs[i]);
				}
			}
		}
		
		return statInfo;
	}

	void printPerfMetric(PerfEntityMetricBase val) {
		String entityDesc = val.getEntity().getType() + ":"
				+ val.getEntity().get_value();
		System.out.println("Entity:" + entityDesc);
		System.out.println();

		if (val instanceof PerfEntityMetric) {
			// ManagedObjectReference source = val.getEntity();
			// System.out.println("MOR: >>>>>>>>>>>>>>>>>>>>>>"+source.get_value());
			printPerfMetric((PerfEntityMetric) val);
		} else if (val instanceof PerfEntityMetricCSV) {
			printPerfMetricCSV((PerfEntityMetricCSV) val);
		} else {
			System.out.println("UnExpected sub-type of "
					+ "PerfEntityMetricBase.");
		}
	}

	void printPerfMetric(PerfEntityMetric pem) {
		System.out.println("PerfEntityMetric for: "
				+ pem.getEntity().get_value());
		PerfMetricSeries[] vals = pem.getValue();
		PerfSampleInfo[] infos = pem.getSampleInfo();
		boolean stop = false;
		long[] statValue;
		for (int i = 0; vals != null && i < vals.length && (cpuStatInfoArrayList.isEmpty() || memoryStatInfoArrayList.isEmpty()); i++) {
			if (vals[i].getId().getCounterId() == 6) {
				System.out.println("CPU statistics >>>>>>>>>>>>>>>>>>>");
				if (vals[i] instanceof PerfMetricIntSeries) {
					PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
					statValue = val.getValue();
					for (int k = 0; k < statValue.length - 1; k++) {
						// cpuStatInfoArrayList.add("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+") , "+statValue[k]+"],");
						System.out.println(sdf.format(infos[k].getTimestamp().getTime())
										.toString() + ") , " + statValue[k]
								+ "],");
						cpuStatInfoArrayList.add(statValue[k]);
						timeInfoArrayList.add(sdf.format(infos[k].getTimestamp().getTime()));
					}
					// cpuStatInfoArrayList.add("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+") , "+statValue[statValue.length-1]+"]");
					System.out.println(sdf.format(
									infos[statValue.length - 1].getTimestamp().getTime()).toString() + ") , "
							+ statValue[statValue.length - 1] + "]");
				}
			}
			if (vals[i].getId().getCounterId() == 24) {
				System.out.println("Memory statistics >>>>>>>>>>>>>>>>>>>");
				if (vals[i] instanceof PerfMetricIntSeries) {
					PerfMetricIntSeries val = (PerfMetricIntSeries) vals[i];
					statValue = val.getValue();
					for (int k = 0; k < statValue.length - 1; k++) {
						// memoryStatInfoArrayList.add("[new Date("+sdf.format(infos[k].getTimestamp().getTime()).toString()+"), "+statValue[k]+"],");
						System.out.println("[new Date("
								+ sdf.format(infos[k].getTimestamp().getTime())
										.toString() + ") , " + statValue[k]
								+ "],");
						memoryStatInfoArrayList.add(statValue[k]);
					}
					// memoryStatInfoArrayList.add("[new Date("+sdf.format(infos[statValue.length-1].getTimestamp().getTime()).toString()+"), "+statValue[statValue.length-1]+"]");
					System.out.println("[new Date("
							+ sdf.format(
									infos[statValue.length - 1].getTimestamp()
											.getTime()).toString() + ") , "
							+ statValue[statValue.length - 1] + "]");
				}
				stop = true;
			}
		}
		/*
		 * System.out.println("CPU Statistics: "); for(Map.Entry<String, Long>
		 * entry: CPUStatInfoMap.entrySet()){
		 * System.out.println(entry.getKey()+" "+entry.getValue()); }
		 * System.out.println("Memory Statistics"); for(Map.Entry<String, Long>
		 * entry: memoryStatInfoMap.entrySet()){
		 * System.out.println(entry.getKey()+" "+entry.getValue()); }
		 */
		statInfo.add(timeInfoArrayList);
		statInfo.add(cpuStatInfoArrayList);
		statInfo.add(memoryStatInfoArrayList);
		// Object[] statInfoAll = (Object[]) new Object();
		// statInfoAll.
	}

	static void printPerfMetricCSV(PerfEntityMetricCSV pems) {
		System.out.println("SampleInfoCSV:" + pems.getSampleInfoCSV());
		PerfMetricSeriesCSV[] csvs = pems.getValue();
		for (int i = 0; i < csvs.length; i++) {
			System.out.println("PerfCounterId:"
					+ csvs[i].getId().getCounterId());
			System.out.println("CSV sample values:" + csvs[i].getValue());
		}
	}
	
	public boolean getVMList() {
		return false;
	}

	public VMStats getVMStats() {
		return new VMStats();
	}

}
