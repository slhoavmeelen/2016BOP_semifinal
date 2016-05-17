# 2016BOP_semifinal

A mess of code of team:FDU_Beginners in 2016_BOP_Semifinal....

我们写的Java 因为其他都不怎么会。

主要的几个优化：

  1.优化查询逻辑 
  
    只找与id 或者 AuId 相连的点，其他的用And或者双向来找。
    
    对于被引次数上15w的文章 我们处理方法是修改offset，5000个5000个地（应该还可以设置大点的比如2w）强行找完。
    
  2.手写Json处理（换了几个库感觉速度都不够）
  
  3.用Httpclient库中的PoolingConnectionManager实现连接的重用（长连接）（其实应该用Socket而不是httpclient的）。
  
代码里面一坨一坨的还有很多代号😄，没有注释，不是给人看的，能看懂的我们确实服了..

  
