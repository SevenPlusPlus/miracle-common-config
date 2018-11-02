package com.miracle.common.config.reload;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.miracle.common.miracle_utils.ConfigUtils;
import com.miracle.common.miracle_utils.StringUtils;

public class CommonReloadConfigModel {

	private final String commonKey;
	private final String commonVal;
		
	private Multimap<String/*beanName*/, Properties> beanPropertiesArrayMap = ArrayListMultimap.create();

	private Multimap<String/*beanName*/, String/*prefix*/> beanPrefixListMap = ArrayListMultimap.create();
	
	public String getCommonKey() {
		return commonKey;
	}

	public String getCommonVal() {
		return commonVal;
	}
	
	public CommonReloadConfigModel(String configKey, String configVal)
	{
		this.commonKey = configKey;
		this.commonVal = configVal;
		
		init();
	}
	
	private String getBeanName(Properties prop)
	{
		if(prop.containsKey("sourceName"))
		{
			return prop.getProperty("sourceName");
		}
		else if(prop.containsKey("beanName"))
		{
			return prop.getProperty("beanName");
		}
		else if(prop.containsKey("name"))
		{
			return prop.getProperty("name");
		}
		else if(prop.containsKey("consumerName"))
		{
			return prop.getProperty("consumerName");
		}
		else if(prop.containsKey("producerName"))
		{
			return prop.getProperty("producerName");
		}
		return null;
	}
	
	private void init()
	{
		if(StringUtils.isEmpty(commonVal))
			return;
		
		Properties origProp = ConfigUtils.string2Properties(commonVal);
		Map<String/*prefix*/, Properties> prefixPropertiesMap = Maps.newHashMap();

		for(Map.Entry<Object, Object> entry : origProp.entrySet())
		{
			String key = (String) entry.getKey();
			String value = (String)entry.getValue();
			int lastDotIdx = key.lastIndexOf('.');
			String prefix = key.substring(0, lastDotIdx);
			String simpleKey = key.substring(lastDotIdx+1);
			
			Properties prefixProperties = prefixPropertiesMap.get(prefix);
			if(prefixProperties == null)
			{
				prefixProperties = new Properties();
				prefixPropertiesMap.put(prefix, prefixProperties);
			}
			
			prefixProperties.put(simpleKey, value);
		}
		
		for(Map.Entry<String, Properties> entry: prefixPropertiesMap.entrySet())
		{
			String tmpPrefix = entry.getKey();
			Properties tmpProperties = entry.getValue();
			if(getBeanName(tmpProperties) != null)
			{
				beanPrefixListMap.put(getBeanName(tmpProperties), tmpPrefix);
			}
		}
		
		for(String beanName : beanPrefixListMap.keySet())
		{
			for(String prefix : beanPrefixListMap.get(beanName))
			{
				Properties newPrefixProp = new Properties();
				for(Map.Entry<Object, Object> entry : origProp.entrySet())
				{
					String key = (String) entry.getKey();
					String value = (String)entry.getValue();
					if(key.startsWith(prefix))
					{
						String subKey = key.substring(prefix.length() + 1);
						newPrefixProp.put(subKey, value);
					}
				}
				beanPropertiesArrayMap.put(beanName, newPrefixProp);
			}
		}
	}

	public Multimap<String, Properties> getBeanPropertiesArrayMap() {
		return beanPropertiesArrayMap;
	}

	public Multimap<String, String> getBeanPrefixListMap() {
		return beanPrefixListMap;
	}
	
	public List<String> getBeanNames()
	{
		List<String> beanNames = Lists.newArrayList();
		if(beanPrefixListMap.size() > 0)
		{
			beanNames.addAll(beanPrefixListMap.keySet());
		}
		return beanNames;
	}
	
}
