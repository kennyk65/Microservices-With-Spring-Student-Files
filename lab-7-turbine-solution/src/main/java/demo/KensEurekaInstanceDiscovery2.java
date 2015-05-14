package demo;

import org.springframework.cloud.netflix.turbine.EurekaInstanceDiscovery;
import org.springframework.cloud.netflix.turbine.TurbineProperties;

import com.netflix.appinfo.InstanceInfo;

public class KensEurekaInstanceDiscovery2 extends EurekaInstanceDiscovery {

	public KensEurekaInstanceDiscovery2(TurbineProperties turbineProperties) {
		super(turbineProperties);
	}

	@Override
	protected String getClusterName(InstanceInfo iInfo) {
		return "AllServers";
	}

	
	
}
