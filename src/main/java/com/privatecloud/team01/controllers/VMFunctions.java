package com.privatecloud.team01.controllers;

import java.net.URL;
import java.rmi.RemoteException;

import com.privatecloud.team01.models.VMStats;
import com.vmware.vim25.mo.ServiceInstance;
import com.vmware.vim25.mo.VirtualMachine;
import com.vmware.vim25.*;
import com.vmware.vim25.mo.*;

public class VMFunctions {
	public boolean createVM(String newVmname, String template_id) throws Exception {
        //create instance of ServiceInstance
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.101/sdk"), "administrator", "12!@qwQW", true);
		
		Folder rootFolder = si.getRootFolder();
		
	    VirtualMachine vm = (VirtualMachine) new InventoryNavigator(rootFolder).searchManagedEntity("VirtualMachine", newVmname);
	    
	    //check whether there is vm or not
	    if(vm==null)
	    {
	    	System.out.println("No VM " + newVmname + " found");
	    	si.getServerConnection().logout();
	    	return false;
	    }
	    
	    VirtualMachineCloneSpec cloneSpec = new VirtualMachineCloneSpec(); //create an instance of vm clone specifications
	    cloneSpec.setLocation(new VirtualMachineRelocateSpec());   //set location ,power and template values
	    cloneSpec.setPowerOn(true);
	    cloneSpec.setTemplate(false);

	    Task task = vm.cloneVM_Task((Folder) vm.getParent(), template_id, cloneSpec); 
	    
	    System.out.println("Launching the VM creation task. " +	"Please wait ...");

	    String status = task.waitForTask();   
	    
	    //task status
	    if(status==Task.SUCCESS)                
	    {
	    	System.out.println("VM got created successfully from template.");
	    }
	    else
	    {
	    	System.out.println("Failure -: VM cannot be created");
	    	return false;
	    }
	    
	    return true;
	}

	public boolean startVM() {
		return false;
	}

	public boolean VMoperations(String uuid, String op) throws Exception {

		String vmname = "";
		ServiceInstance si = new ServiceInstance(new URL("https://130.65.132.101/sdk"), "administrator", "12!@qwQW", true);

		Folder rootFolder = si.getRootFolder();
		
		System.out.println(rootFolder.getName()); // Datacenters
		
		ManagedEntity[] mes = rootFolder.getChildEntity();

		for (int i = 0; i < mes.length; i++) {
			
			System.out.println(mes[i].getName()); // T01-DC
			
			if (mes[i] instanceof Datacenter) {
				Datacenter dc = (Datacenter) mes[i];
				
				Folder vmFolder = dc.getVmFolder(); // vm folder
				
				System.out.println(vmFolder.getName()); // vm
				
				ManagedEntity vm_mainfolder = vmFolder.getChildEntity()[0]; // Discovered virtual machine
																			
				ManagedEntity[] vms = new InventoryNavigator(vm_mainfolder)
						.searchManagedEntities("VirtualMachine"); // find all vm in the discoverd folder
																	
				for (int j = 0; j < vms.length; j++) {
					System.out.println(vms[j].getName());
					if (vms[j] instanceof VirtualMachine) {
						VirtualMachine vm = (VirtualMachine) vms[j];
						String instanceUuid = vm.getConfig().instanceUuid;
						System.out.println((vm.getName()) + "," + instanceUuid);
						
						VirtualMachineSummary summary = (VirtualMachineSummary) (vm.getSummary());
						
						System.out.println(summary.toString());
						
						VirtualMachineRuntimeInfo vmri = (VirtualMachineRuntimeInfo) vm
								.getRuntime();
						if (uuid.equals(instanceUuid)) {
							if (vmri.getPowerState() == VirtualMachinePowerState.poweredOn
									&& op.equals("poweroff")) {
								Task task = vm.powerOffVM_Task();
								task.waitForTask();
								System.out.println("vm:" + vm.getName()
										+ " powered off.");
							} else if ("poweron".equalsIgnoreCase(op)) {
								Task task = vm.powerOnVM_Task(null);
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " powered on");
								}
							} else if ("reboot".equalsIgnoreCase(op)) {
								vm.rebootGuest();
								System.out.println(vmname+ " guest OS rebooted");
							} else if ("reset".equalsIgnoreCase(op)) {
								Task task = vm.resetVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " reset");
								}
							} else if ("standby".equalsIgnoreCase(op)) {
								vm.standbyGuest();
								System.out
										.println(vmname + " guest OS stoodby");
							} else if ("suspend".equalsIgnoreCase(op)) {
								Task task = vm.suspendVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " suspended");
								}
							} else if ("shutdown".equalsIgnoreCase(op)) {
								Task task = vm.suspendVM_Task();
								if (task.waitForTask() == Task.SUCCESS) {
									System.out.println(vmname + " suspended");
								}
							} else {
								System.out.println("Invalid operation. Exiting...");
							}
							
							si.getServerConnection().logout();
						}
					}
				}
			}
		}

			return false;
	}

	public boolean getVMList() {
		return false;
	}

	public VMStats getVMStats() {
		return new VMStats();
	}

}
