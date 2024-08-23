<h1 align="center">
<img style="width: 140px" src="docs/imgs/icon.webp"/>

[//]: # (<a href="https://github.com/lhccong/flex-pool" target="_blank">FlexPool</a>)
</h1>
<p align="center">
  <a href="https://www.oracle.com/technetwork/java/javase/downloads/index.html" target="_blank"><img alt="JDK" src="https://img.shields.io/badge/JDK-1.8.0_162-orange.svg"/></a>
</p>

## 快速使用🚀

```xml
<dependency>
  <groupId>com.cong</groupId>
  <artifactId>flex-pool</artifactId>
  <version>1.0.0</version>
</dependency>
```
## 痛点

使用线程池 ThreadPoolExecutor 过程中你是否有以下痛点呢💥？

> 1. 代码中创建了一个 ThreadPoolExecutor，但是不知道那几个核心参数设置多少比较合适
>
> 2. 凭经验设置参数值，上线后发现需要调整，改代码重新发布服务，非常麻烦
>
> 3. 线程池相对开发人员来说是个黑盒，运行情况不能及时感知到，直到出现问题

如果有以上痛点，动态可监控线程池框架（**FlexPool**）或许能帮助到你。

**总结出以下的背景📗**

- **广泛性**：在 Java 开发中，想要提高系统性能，线程池已经是一个 90% 以上开发人员都会选择使用的基础工具

- **不确定性**：项目中可能存在很多线程池，既有 IO 密集型的，也有 CPU 密集型的，但线程池的核心参数并不好确定，需要有套机制在运行过程中动态去调整参数

- **无感知性**：线程池运行过程中的各项指标一般感知不到，需要有套监控报警机制在事前、事中就能让开发人员感知到线程池的运行状况，及时处理

- **高可用性**：配置变更需要及时推送到客户端，需要有高可用的配置管理推送服务，配置中心是现在大多数互联网系统都会使用的组件，与之结合可以极大提高系统可用性
