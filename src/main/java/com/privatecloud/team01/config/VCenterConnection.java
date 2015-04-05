package com.privatecloud.team01.config;

import java.net.URL;

import com.vmware.vim25.mo.ServiceInstance;

public class VCenterConnection {
	private static String username = "administrator";
	private static String password = "12!@qwQW";
	private static String url = "https://130.65.132.101/sdk";
	private ServiceInstance vCenter;
	
	public VCenterConnection() throws Exception{
		if(this.vCenter == null){
			this.vCenter = new ServiceInstance(new URL(url), username, password, true);
		}
	}
	
	public ServiceInstance getVCenter(){
		return this.vCenter;
	}
	
}
