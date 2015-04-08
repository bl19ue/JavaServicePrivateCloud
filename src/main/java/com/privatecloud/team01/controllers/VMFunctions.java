package com.privatecloud.team01.controllers;

import java.net.URL;
import java.rmi.RemoteException;

import com.privatecloud.team01.models.VMStats;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class VMFunctions {

	public String createVM(String newVmname, String template_id)
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
			si.getServerConnection().logout();
			return "";
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

		// task status
		if (status == Task.SUCCESS) {

			System.out.println("VM got created successfully from template.");

		} else {

			System.out.println("Failure -: VM cannot be created");
			return "";
		}
		return newVmname; // return the uuid of the vm created
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
								si.getServerConnection().logout();
								return true;
							} else if ("poweron".equalsIgnoreCase(op)) {
								Task task = vm.powerOnVM_Task(null);
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " powered on");
									si.getServerConnection().logout();
									return true;
								}
							} else if ("reboot".equalsIgnoreCase(op)) {
								vm.rebootGuest();
								System.out.println(vmname
										+ " guest OS rebooted");
								si.getServerConnection().logout();
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
		si.getServerConnection().logout();
		return false;
	}

	public String VMStatus(String uuid) throws Exception {
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
				ManagedEntity vm_mainfolder = vmFolder.getChildEntity()[0]; // Discovered
																			// virtual
																			// machine

				ManagedEntity[] vms = new InventoryNavigator(vm_mainfolder)
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
						if (uuid.equals(instanceUuid)) {
							return vmri.getPowerState().name();
						}
					}
				}
			}
		}
		return "VM state not found";
	}

	public VMStats VMStatistics(String vmname) throws Exception {
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

				ManagedEntity[] vms = new InventoryNavigator(vmFolder)
						.searchManagedEntities("VirtualMachine"); // find all vm

				for (int j = 0; j < vms.length; j++) {
					System.out.println(vms[j].getName());
					if (vms[j] instanceof VirtualMachine) {
						VirtualMachine vm = (VirtualMachine) vms[j];
						String instanceUuid = vm.getConfig().instanceUuid;
						System.out.println((vm.getName()) + "," + instanceUuid);

						VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm
								.getRuntime();
						if (vmname.equals(vm.getName())) {
							VMStats vmStats = new VMStats();
							vmStats.setName(vm.getName());
							vmStats.setGuestFullName(vm.getSummary()
									.getConfig().guestFullName);
							vmStats.setVersion(vm.getConfig().version);
							vmStats.setNumCPU(vm.getConfig().getHardware().numCPU);
							vmStats.setNumCPU(vm.getConfig().getHardware().memoryMB);
							vmStats.setIPAdress(vm.getGuest().getIpAddress());
							vmStats.setGuestState(vm.getGuest().guestState);
							if (!vm.getGuest().guestState.equals("notRunning")) {
								VirtualMachineQuickStats qs = vm.getSummary()
										.getQuickStats();
								vmStats.setOverallCPUusage(qs
										.getOverallCpuUsage());
								vmStats.setGuestmemoryUsage(qs
										.getGuestMemoryUsage());
								vmStats.setOnsumedOverheadMemory(qs
										.getConsumedOverheadMemory());
								// vmStats.setFtLatencyStatus(qs.getFtLatencyStatus());
								// vmStats.setHeartBeatStatus(qs.getGuestHeartbeatStatus());
								return vmStats;
							}
						}
					}
				}
			}
		}
		return null;
	}

	public boolean getVMList() {
		return false;
	}

	public VMStats getVMStats() {
		return new VMStats();
	}

}
