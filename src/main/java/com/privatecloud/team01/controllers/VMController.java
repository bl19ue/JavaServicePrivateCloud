package com.privatecloud.team01.controllers;

import org.json.simple.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import com.privatecloud.team01.models.VM;
import com.privatecloud.team01.models.VMList;
import com.privatecloud.team01.models.VMStats;

import java.util.*;

@RequestMapping("")
@RestController
public class VMController {
	VMFunctions vmfunctions = new VMFunctions();

	// get the list of templates
	// view the VM, user passes the uuid
	@RequestMapping(value = "/vm/{uuid}", method = RequestMethod.GET)
	@ResponseBody
	public String getVM(@PathVariable("uuid") String uuid) {
		// findVm(uuid);
		return "You have got the VM " + uuid;
	}

	// create the Virtual machine and return the unique instance id
	@RequestMapping(value = "/vm/{template_id}/create", method = RequestMethod.POST)
	@ResponseBody
	public JSONObject createVM(@PathVariable("template_id") String template_id,
			@RequestBody VM vm) throws Exception {
		System.out.println("Creating VM");
		System.out.println(vm);
		Thread t1 = new Thread() {
			public void run() {
				// creation();
			}
		};
		return vmfunctions.createVM(vm.getVmName(), template_id);
	}

	// stop the virtual machine
	@RequestMapping(value = "/vm/{vmname}/stop", method = RequestMethod.GET)
	@ResponseBody
	public String stopVM(@PathVariable("vmname") String vmname)
			throws Exception {
		boolean vm_status = vmfunctions.VMoperations(vmname, "poweroff");
		if (vm_status == true)
			return vmname;
		else
			return "VM cannot be stopped " + vmname;

	}

	// start the vm, after the user gives the uuid

	@RequestMapping(value = "/vm/{vmname}/start", method = RequestMethod.GET)
	@ResponseBody
	public String startVM(@PathVariable("vmname") String vmname)
			throws Exception {
		boolean vm_status = vmfunctions.VMoperations(vmname, "poweron");
		if (vm_status == true)
			return "You have started the VM " + vmname;
		else
			return "You have started the VM " + vmname;
	}

	@RequestMapping(value = "/vm/{vmname}/reboot", method = RequestMethod.GET)
	@ResponseBody
	public String rebootVM(@PathVariable("vmname") String vmname)
			throws Exception {
		boolean vm_status = vmfunctions.VMoperations(vmname, "reboot");
		if (vm_status == true)
			return "You have started the VM " + vmname;
		else
			return "You have started the VM " + vmname;
	}

	// get the Virtual machine status

	@RequestMapping(value = "/vm/{vmname}/status", method = RequestMethod.GET)
	public String vmStatus(@PathVariable("vmname") String vmname)
			throws Exception {
		return vmfunctions.VMStatus(vmname);
	}

	// get the statistics
	@RequestMapping(value = "/vm/{vmname}/statistics", method = RequestMethod.GET)
	public ArrayList<ArrayList> vmStatistics(@PathVariable("vmname") String vmname)
			throws Exception {
		ArrayList<ArrayList> vmstats = vmfunctions.getStatistics(vmname);
		if (vmstats != null)
			return vmstats;
		return null;
	}

//	@RequestMapping(value = "/vm/statistics", method = RequestMethod.POST)
//	public JSONObject vmListStatistics(@RequestBody VMList vmList) throws Exception {
//		int len = vmList.getVmName().size();
//		if (len>0){
//			ArrayList<Integer> cpuArr = new ArrayList<Integer>();
//			ArrayList<Integer> ramArr = new ArrayList<Integer>();
//			JSONObject jsonObject = new JSONObject();
//			for(String vmName : vmList.getVmName()){
//				cpuArr.add(vmfunctions.VMStatistics(vmName).getOverallCPUusage());
//				ramArr.add(vmfunctions.VMStatistics(vmName).getOnsumedOverheadMemory());
////				jsonObject.put("cpu", vmfunctions.VMStatistics(vmName).)
//			}
//			jsonObject.put("cpu", cpuArr);
//			jsonObject.put("ram", ramArr);			
//			return jsonObject;
//		}
//		return null;
//	}
	/*
	 * 
	 * 
	 * 
	 * //standby the vm, after the user gives the uuid
	 * 
	 * @RequestMapping(value="/vm/{uuid}/standby", method=RequestMethod.GET)
	 * 
	 * @ResponseBody public String standbyVM(@PathVariable("id") String uuid)
	 * throws Exception{ boolean vm_status=vmfunctions.VMoperations(uuid,
	 * "reset"); if(vm_status==true) return "You have standby the VM " + uuid;
	 * else return "You have standby the VM " + uuid; } // stop the vm
	 * 
	 * @RequestMapping(value="/vm/{uuid}/stop", method=RequestMethod.GET)
	 * 
	 * @ResponseBody public String stopVM(@PathVariable("uuid") String uuid)
	 * throws Exception{ boolean
	 * vm_status=vmfunctions.VMoperations(uuid,"poweroff"); if(vm_status==true)
	 * return "You have stopped the VM " + uuid; else return
	 * "VM cannot be stopped " + uuid;
	 * 
	 * }
	 * 
	 * 
	 * 
	 * public void findVm(String uuid) {
	 * 
	 * }
	 */
}
