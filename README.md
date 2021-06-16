<h1>
  Market Stock Simulator implementation
</h1>

<h9>
<p>  How to build:  

*   mvn clean verify

</p>
<p>
	How to run:  
		
*   build first
*   go to location of com.cc.stock.market.sim/target
*   java -jar com.cc.stock.market.sim-0.0.1-jar-with-dependencies.jar
</p>

<p>
The next format supported: 
[B,S:name:count:price], 
[E - for exit], [T - show list trades],[A - show market status] 

limitations:
 - offers with same name will be replaced to support modification schema;
</p>
</h9>  

<p>

Tests code coverage:
   com.stock.market.sim - 75.2%