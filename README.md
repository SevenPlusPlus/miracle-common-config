#miracle-common-config公共配置基础包

---

## 应用背景
Miracle Framework中使用了disconf作为配置中心，应用项目中会将应用中使用的配置全部置于disconf中进行管理。后来业务发现，业务项目的大部分配置都是后端服务的配置信息如mysql、postgresql、redis集群、rocketmq等，相当于大部分业务应用的配置都是重复的，而这些配置对应不同的环境（dev、test、uat、prod）又都是不同的。这样就导致大量的开发和运维的重复配置的工作量，又容易出错。同时如果有时候后端服务配置需要统一调整时，那么所有相关业务都需要修改，然后重启，很容易遗漏导致很严重的线上问题。

## 我们的解决方案
我们提出了公共配置的概念，我们扩展了disconf的实现，增加了公共配置的功能，由运维人员来统一配置管理后台服务的公共配置，业务应用中可以在disconf中业务应用中直接引用公共配置，但没有修改权限。业务应用启动时会下载引用的公共配置，disconf-client也会watch公共配置的变更事件，当公共配置变更时各个后台服务的starter中有各自的reload实现自动重新加载的逻辑，那么业务应用不需要任何操作则自动实现了后台服务的切换。

## 公共配置的reload接口定义

public interface Reloadable {
	void reload(String beanName, CommonReloadConfigModel reloadCfgModel);
}
