package com.privatecloud.team01.controllers;

import org.json.simple.*;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.*;

import com.privatecloud.team01.models.VM;

import java.util.*;

@RequestMapping("/")
@RestController
public class VMController {
	VMFunctions vmfunctions = new VMFunctions();
	// get the list of templates
	//view the VM, user passes the uuid
	@RequestMapping(value="/vm/{uuid}", method=RequestMethod.GET)
    @ResponseBody
    public String getVM(@PathVariable("uuid") String uuid){
		findVm(uuid);
    	return "You have got the VM " + uuid;
    }
	
	//start the vm, after the user gives the uuid
	@RequestMapping(value="/vm/{uuid}/start", method=RequestMethod.GET)
    @ResponseBody
    public String startVM(@PathVariable("id") String uuid) throws Exception{
		boolean vm_status=vmfunctions.VMoperations(uuid, "poweron");
		if(vm_status==true)
    	return "You have started the VM " + uuid;
		else 
			return "You have started the VM " + uuid;
    }
	
	//reboot the vm, after the user gives the uuid
		@RequestMapping(value="/vm/{uuid}/reboot", method=RequestMethod.GET)
	    @ResponseBody
	    public String rebootVM(@PathVariable("id") String uuid) throws Exception{
			boolean vm_status=vmfunctions.VMoperations(uuid, "reboot");
			if(vm_status==true)
	    	return "You have started the VM " + uuid;
			else 
				return "You have started the VM " + uuid;
	    }
		
		//reset the vm, after the user gives the uuid
		@RequestMapping(value="/vm/{uuid}/reset", method=RequestMethod.GET)
	    @ResponseBody
	    public String resetVM(@PathVariable("id") String uuid) throws Exception{
			boolean vm_status=vmfunctions.VMoperations(uuid, "reset");
			if(vm_status==true)
	    	return "You have started the VM " + uuid;
			else 
				return "You have started the VM " + uuid;
	    }
		
		//standby the vm, after the user gives the uuid
				@RequestMapping(value="/vm/{uuid}", method=RequestMethod.GET)
			    @ResponseBody
			    public String standbyVM(@PathVariable("id") String uuid) throws Exception{
					boolean vm_status=vmfunctions.VMoperations(uuid, "reset");
					if(vm_status==true)
			    	return "You have standby the VM " + uuid;
					else 
						return "You have standby the VM " + uuid;
			    }
	// stop the vm
	@RequestMapping(value="/vm/{uuid}", method=RequestMethod.GET)
    @ResponseBody
    public String stopVM(@PathVariable("uuid") String uuid) throws Exception{
		boolean vm_status=vmfunctions.VMoperations(uuid,"poweroff");
		if(vm_status==true)
    	return "You have stopped the VM " + uuid;
		else
			return "VM cannot be stopped " + uuid;
			
    }
	 //create the Virtual machine and return the unique instance id
	 @RequestMapping(value = "/vm/{template_id}",method = RequestMethod.POST)
	  public String createVM(@RequestBody VM vm,@PathVariable("template_id") String template_id) throws Exception{
		return vmfunctions.createVM(vm.getVmName(),template_id);
	  }
	 
	 //get the Virtual machine status
	 @RequestMapping(value="/vm/{uuid}/status",method=RequestMethod.GET)
	 public String vmStatus(@PathVariable("uuid")String uuid) throws Exception{
		 return vmfunctions.VMStatus(uuid);	 
	 }
	 
	 public void findVm(String uuid){
		 
	 }

}
