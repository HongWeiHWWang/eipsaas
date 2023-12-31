<#--测试宏 1-->
<#macro demo1 color,name> 
<font size="+2" color="${color}">Hello ${name}!</font> 
</#macro>

<#--测试宏 2-->
<#macro copyright date> 
<p>Copyright (C) ${date} hotent.com. All rights reserved. 
<br>Email: ${mail}</p> 
</#macro>

<#--作用域变量-->
<#assign mail="hotentm@jee-soft.cn"> 