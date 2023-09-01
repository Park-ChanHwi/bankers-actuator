package bankers.fisa.component;

import java.lang.management.ManagementFactory;

import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@Component
public class RamMonitorTotal {

    private final static String METRICS_NAME = "process.ram.total";

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Gauge.builder(METRICS_NAME, this, RamMonitorTotal::getProcessRamTotal)
            .baseUnit("mb")
            .description("Ram Total")
            .register(meterRegistry);
    }

    public Integer getProcessRamTotal() {
        try {
            MBeanServer mbs = ManagementFactory.getPlatformMBeanServer();
            ObjectName name = ObjectName.getInstance("java.lang:type=OperatingSystem");
            AttributeList list = mbs.getAttributes(name, new String[]{"TotalPhysicalMemorySize"});

            Long result = (Long.parseLong(list.get(0).toString().split(" = ")[1])/1024/1024);
            
            return result.intValue();
        } catch (Exception ex) {
            return null;
        }
    }
}