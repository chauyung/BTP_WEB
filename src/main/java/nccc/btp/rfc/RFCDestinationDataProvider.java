package nccc.btp.rfc;

import com.sap.conn.jco.ext.DestinationDataEventListener;
import com.sap.conn.jco.ext.DestinationDataProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class RFCDestinationDataProvider implements DestinationDataProvider {
    private final Map<String, Properties> destinationProperties = new HashMap<>();

    // 新增目的地設定
    public void addDestination(String destinationName, Properties properties) {
        destinationProperties.put(destinationName, properties);
    }

    @Override
    public Properties getDestinationProperties(String destinationName) {
        return destinationProperties.get(destinationName);
    }

    @Override
    public void setDestinationDataEventListener(DestinationDataEventListener eventListener) {
        // 如果需要事件處理，可在此進行實作
    }

    @Override
    public boolean supportsEvents() {
        return false;
    }
}


