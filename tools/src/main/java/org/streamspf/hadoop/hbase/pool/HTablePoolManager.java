package org.streamspf.hadoop.hbase.pool;

import org.streamspf.hadoop.util.ConfigUtils;
import java.util.HashMap;
import java.util.Map;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.HTableInterface;

public class HTablePoolManager {

	public static final String DEFAULT_INSTANCE = "default";
	private final static HashMap<String, HTablePoolEnhanced> poolCache = new HashMap<String, HTablePoolEnhanced>();

	public static HTablePoolEnhanced getHTablePool(String hbaseInstanceName, String quorum, String clientPort) {
		HashMap<String, String> config = new HashMap<String, String>();
		config.put("hbase.zookeeper.quorum", quorum);
		config.put("hbase.zookeeper.property.clientPort", clientPort);
		config.put("zookeeper.session.timeout", "180000");

		return getHTablePool(hbaseInstanceName, config);
	}

	private static HTablePoolEnhanced getHTablePool(String hbaseInstanceName, Map<String, String> config) {
		HTablePoolEnhanced hTablePool = poolCache.get(hbaseInstanceName);
		if (hTablePool != null) {
			return hTablePool;
		}

		synchronized (poolCache) {
			hTablePool = poolCache.get(hbaseInstanceName);
			if (hTablePool != null) {
				return hTablePool;
			}

			Configuration configuration = HBaseConfiguration.create();

			for (Map.Entry<String, String> entry : config.entrySet()) {
				configuration.set(entry.getKey(), entry.getValue());
			}
			int maxSize = configuration.getInt("hbase.table.references.max", 1);
			hTablePool = new HTablePoolEnhanced(configuration, maxSize);
			poolCache.put(hbaseInstanceName, hTablePool);
			return hTablePool;
		}
	}

	public static HTablePoolEnhanced getHTablePool(String hbaseInstanceName) {
		HTablePoolEnhanced hTablePool = poolCache.get(hbaseInstanceName);
		if (hTablePool != null) {
			return hTablePool;
		}

		synchronized (poolCache) {
			hTablePool = poolCache.get(hbaseInstanceName);
			if (hTablePool != null) {
				return hTablePool;
			}

			Map<String, String> config = ConfigUtils.getConfig(hbaseInstanceName);
			if (config != null) {
				return getHTablePool(hbaseInstanceName, config);
			}

			return getHTablePool(hbaseInstanceName, "127.0.0.1", "2181");
		}
	}

	public static Configuration getHBaseConfig(String hbaseInstanceName) {
		HTablePoolEnhanced hTablePool = getHTablePool(hbaseInstanceName);
		Configuration config = hTablePool == null ? null : hTablePool.getConfig();

		return config;
	}

	public static HTableInterface getHTable(String tableName, String hbaseInstanceName) {
		return getHTablePool(hbaseInstanceName).getTable(tableName);
	}
}
