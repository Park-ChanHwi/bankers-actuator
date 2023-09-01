package bankers.fisa.component;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import jakarta.annotation.PostConstruct;

@Component
public class StorageMonitorTotal {

    private final static String METRICS_NAME = "process.storage.total";

    @Autowired
    private MeterRegistry meterRegistry;

    @PostConstruct
    public void init() {
        Gauge.builder(METRICS_NAME, this, StorageMonitorTotal::getProcessStorageTotal)
            .baseUnit("mb")
            .description("Storage Total")
            .register(meterRegistry);
    }

    public Integer getProcessStorageTotal() {
        try {
            File[] drives = File.listRoots();
            int totalSize = 0;
            for(File drive : drives) {
            	totalSize += drive.getTotalSpace() / Math.pow(1024, 2);
            }
            
            return totalSize;
        } catch (Exception ex) {
            return null;
        }
    }
}