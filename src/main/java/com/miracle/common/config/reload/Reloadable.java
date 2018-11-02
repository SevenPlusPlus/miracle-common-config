package com.miracle.common.config.reload;


public interface Reloadable {

	void reload(String beanName, CommonReloadConfigModel reloadCfgModel);
}
