
# IRLS(IRLS-Iteratively Reweighted Least Squares)迭代加权最小二乘
IRLS 的java语言的实现


## IRLS用于解决这种目标函数的优化问题（实际上是用2范数来近似替代p范数，特殊的如1范数）。
![](https://github.com/winston-xing/irls-in-machine-learning-java/raw/master/2ab1f2fb977f9622f8df7ad88ce7a952.png)  
## 可将其等价变形为加权的线性最小二乘问题：
![](https://github.com/winston-xing/irls-in-machine-learning-java/raw/master/b604b4cd50f84ed54538acda152396c2.png)  
## 其中W（t）可看成对角矩阵，每步的w可用下面的序列代替
![](https://github.com/winston-xing/irls-in-machine-learning-java/raw/master/f6497e0226d3da832fcc9b9cc206e670.png)  
## 如果 p=1,则将w(t)换为这种形式
![](https://github.com/winston-xing/irls-in-machine-learning-java/raw/master/272106421452570.png)  
## 有时为了保证分母不为零，加上了一个比较项
![](https://github.com/winston-xing/irls-in-machine-learning-java/raw/master/272104175674434.png)

## 依赖的类库
   colt.jar，concurrent.jar ：Colt是一个高性能的数学库，由以下几个子库构成：Colt库:基本的动态数组、稀疏矩阵、线性代数。
   commons-lang3-3.3.2.jar
