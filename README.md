# 2016BOP_semifinal

A mess of code in 2016_BOP_Semifinal....

队名：FDU_Beginners 成员们 陈楷予(Me)，董依菡，俞博远

成绩：最后测试前49.96，排名20+，最后测试居然Rank10 Orz 看来我们队人品很好啊

主要的几个优化：

    1.优化查询逻辑 
  
      只找与id 或者 AuId 相连的点，其他的用And或者双向来找。
    
      对于被引次数上15w的文章 我们处理方法是修改offset，5000个5000个地（应该还可以设置大点的比如2w）强行找完。
    
    2.手写Json处理（换了几个库感觉速度都不够）
  
    3.用Httpclient库中的PoolingConnectionManager实现连接的重用（长连接）（其实应该用Socket而不是httpclient的）。
  
代码里面一坨一坨的还有很多代号😄，没有注释，不是给人看的，要是能看懂的我们确实服了..

#代码结构
myservlet.java 是servlet的部分，处理询问和返回结果

Work.java 查询的主要逻辑部分

NodeExtend.java 处理Work.java的 查询某节点相连的节点 的请求

MAGnode.java 定义的图上的节点类

ExtendRunnalbe.java 负责最开始未知AuId和Id的并行查询逻辑


  
