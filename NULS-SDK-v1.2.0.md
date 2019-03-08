# NULS-SDK 


## 介绍
本文档是NULS Java SDK的用户指南，描述了NULS服务提供的账户、交易、区块等相关基础功能接口的使用说明。 
### 约定
- 所有SDK接口统一返回Result(说明见文末附件数据解释)
- 每个接口描述的返回对象是指Result中data属性的内容
- 除特别注明外，本文档NULS都以Na为单位 1NULS = 100,000,000Na
- 运行SDK前，需要确认NULS节点服务已经启动并工作正常
- 推荐使用JDK1.8

### 版本更新记录



|   版本    |  更新日期  |                           更新内容                           |
| :-------: | :--------: | :----------------------------------------------------------: |
| v0.9.11.0 | 2018-06-19 |                对接NULS服务各项基础功能的接口                |
| v0.9.11.1 | 2018-06-22 |    新增获取最新区块的高度和hash的接口(3.6，3.7)，修改2.1     |
|  v0.9.14  | 2018-07-04 | 修改快速入门中SDK初始化方法改为init，以及SDK的使用方式, 修改返回封装对象名称（去掉Dto等） |
|  v0.9.15  | 2018-07-07 |                  新增4个共识接口，4.1——4.4                   |
|  v0.9.16  | 2018-07-11 |                         新增接口4.5                          |
|  v1.0.1   | 2018-07-13 |                          新增错误码                          |
| v1.1.4.1  | 2018-11-22 | 新增协议,新增转账接口（不用计算手续费），新增智能合约创建、调用、删除接口 |
| v1.1.4.2  | 2018-11-29 |        新增协议（多账户转账接口、sendToAddress接口）         |
| v1.1.4.3  |  2019-1-7  |               新增正式环境和测试环境的参数配置               |
| v1.1.4.4  | 2019-1-13  | 新增获取交易序列化信息接口，新增根据高度获取区块序列化信息接口 |
| v1.1.4.5  | 2019-1-17  |                   修改TimeService启动方式                    |
| v1.1.4.6  | 2019-1-22  |                     新增查询账户余额接口                     |
|  v1.2.0   |  2019-3-8  |                     新增查询账户UTXO接口                     |



## 快速入门
### 1.引入文件
使用构建工具导入jar包  

- maven方式

```xml
<dependency>
   <groupId>io.nuls.sdk</groupId>
   <artifactId>sdk-all</artifactId>
   <version>1.1.4.3</version>
</dependency>
```

### 2.创建SDK实例
首先添加SDK命名空间

```java
//SDK启动类命名空间
import io.nuls.sdk.SDKBootstrap;
import io.nuls.sdk.model.Result;
//引入SDK调用工具的命名空间
import io.nuls.sdk.tool.NulsSDKTool;
```

引入后，若SDK需要连接节点钱包（**若只是离线使用，可跳过**），使用下列代码生成一个实例client

- 初始化方法不传参数时，默认PRC ip和端口分别为`127.0.0.1`，`8001`
- **默认SDK是连接主网的钱包，若SDK用于公测网的钱包，需要添加公测网的chainId参数**

```java
//默认 
SDKBootstrap.init();
//传入NULS服务的ip，port
SDKBootstrap.init("192.168.1.88", "8001");
//连接公测网时，需要传入chainId参数，公测网chainId=261
SDKBootstrap.init("192.168.1.88", "8001", 261);
```

- 使用工具类调用接口方法


```java
Result result = NulsSDKTool.createAccount("nuls123456");
```
 *e.g 创建一个带密码的账户的完整示例*

```java
import io.nuls.sdk.SDKBootstrap;
import io.nuls.sdk.model.Result;
//根据需求引入对应模块的命名空间
import io.nuls.sdk.tool.NulsSDKTool;

public static void main(String[] args) {
	SDKBootstrap.init();        //离线钱包无需调用init()接口
	Result result = NulsSDKTool.createAccount("nuls123456");
}
```


### 3.接口定义

文档中有说明离线模式的接口，可在不连接NULS服务节点的状态下使用，其他接口需在调用SDKBootstrap.init()连接节点后才可正常使用，且接口会直接操纵NULS服务节点。

#### 账户 AccountService
#### 1.1 创建账户
**`Result createAccount(int count, String password);`**

说明 
> 调用节点RPC接口创建账户可根据传入的参数创建单个或多个，有密码或没有密码的账户；
> 成功创建的账户信息将被持久化至NULS服务节点钱包里。
>
> 返回成功创建的账户地址集合

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">count</td>
        <td align="center">int</td>
        <td align="center">非必填</td>
        <td align="center">创建账户的数量（默认1）</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">设置账户密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
</table>   

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data":{
	    "list": [ //返回创建账户的地址集合
	        "2Cbkwxu34iCjsiHKBjqZDNjoVbLMcJv",
	        "2Cbkwxu34iCjsiHKBjqZDNjoVbLMcJv"
	    ]
    }
}
```


*e.g 示例代码*

```java
//创建一个没有密码的账户
createAccount();
//创建一个有密码的账户
createAccount("nuls123456");
//创建3个没有密码的账户
createAccount(3);
//创建3个有密码的账户
createAccount(3, "nuls123456");
```
---

#### 1.2 创建账户 （离线模式）
**`Result createOfflineAccount(int count, String password)`**

说明

> 直接离线创建一个账户并完整返回, 不会和NULS服务节点交互,且节点不会进行持久化操作
> Create encrypted off-line accounts (Not saved to the database)
>
> Result.data <a href="#AccountInfo">`List<AccountInfo>`</a> 

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">count</td>
        <td align="center">int</td>
        <td align="center">非必填</td>
        <td align="center">创建账户的数量（默认1）</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">设置账户密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
</table>   

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
	"success": true,
	"data": {
		"list":[
			{
				"address": String, 账户地址
				"alias": String, 账户别名
				"pubKey": String, 公钥
				"extend": String, 扩展
				"createTime": Long, 创建时间
				"encrypted": boolean, 是否加密
				"priKey": String, 私钥（创建无密码离线账户时才会有值）
				"encryptedPriKey": String, 加密私钥（创建有密码离线账户时才会有值）
			},
			{
				"address": "2CiBQg72BCLmLqttRpPfp8ECRCBwbdD",
				"alias": null,
				"pubKey": "020159dc5cc74463f346b71c08dd934a823e9e6fe727d8d3c577e7d462e1a364bb",
				"extend": null,
				"createTime": 1529314943624,
				"encrypted": true,
				"priKey": "",
				"encryptedPriKey": "5664f746654fb111e967bb3922910b16340f1e60ff1b281c7a333179d7b82d6220bb12d1c058d9cd06099d4f443a4cb0"
			}
		]
	}
}
```

*e.g 示例代码*

```java
//创建一个没有密码的离线账户
createOfflineAccount();
//创建一个有密码的离线账户
createOfflineAccount("nuls123456");
//创建3个没有密码的离线账户
createOfflineAccount(3);
//创建3个有密码的离线账户
createOfflineAccount(3, "nuls123456");
```

---

#### 1.3 获取账户

**`Result getAccount(String address)`**  

说明

>  调用节点RPC接口，根据账户地址获取一个节点钱包里创建的账户信息
>
>  Result.data <a href="#AccountInfo">`AccountInfo`</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
	"success": true,
	"data": {
		"address": String, 账户地址
		"alias": String, 账户别名
		"pubKey": String, 公钥
		"extend": String, 扩展
		"createTime": Long, 创建时间
		"encrypted": boolean, 是否加密
		"priKey": String, 私钥（创建无密码离线账户时才会有值）
		"encryptedPriKey": String, 加密私钥（创建有密码离线账户时才会有值）
	}
}
```

*e.g 示例代码*

```java
getAccount("2ChqBTvFXttQsghj8zQpcdv76TQU8G5");
```
---

#### 1.4 获取设置别名的手续费 
**`Result getAliasFee(String address, String alias)`**  

说明

>  调用节点RPC接口，根据账户地址和待设置的别名名称获取设置该别名所需要的手续费(不包含设置别名固定1NULS的花费)，该账户必须是钱包创建的
>
>  Result.data `double`,  unit is `NULS`

**注意!** 此接口返回的手续费单位为`NULS`

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址</td>
    </tr>
    <tr>
        <td align="center">alias</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">待设置的别名名称</td>
    </tr></table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
	"success": true,
	"data":{
		"value": 0.01 //(单位为NULS)
	} 
}
```

*e.g 示例代码*

```java
getAliasFee("2ChqBTvFXttQsghj8zQpcdv76TQU8G5","factory666");
```
---

#### 1.5 获取账户列表 
**`Result getAccountList(int pageNumber, int pageSize)`**

说明

> 调用节点RPC接口，获取节点钱包里的账户列表，根据参数做分页
>
> Result.data Page <a href="#AccountInfo">`List<AccountInfo>`</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">pageNumber</td>
        <td align="center">int</td>
        <td align="center">必填</td>
        <td align="center">页码，必须大于0 </td>
    </tr>
    <tr>
        <td align="center">pageSize</td>
        <td align="center">int</td>
        <td align="center">必填</td>
        <td align="center">每页返回数据记录数量, 取值范围1~100</td>
    </tr></table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
	"success": true,
	"data": {
		"pageNumber": 1,
    	"pageSize": 10,
    	"total": 100,
     	"pages": 10,
		"list": [
			{
				"address": String, 账户地址
				"alias": String, 账户别名
				"pubKey": String, 公钥
				"extend": String, 扩展
				"createTime": Long, 创建时间
				"encrypted": boolean, 是否加密
				"priKey": String, 私钥（创建无密码离线账户时才会有值）
				"encryptedPriKey": String, 加密私钥（创建有密码离线账户时才会有值）
			},
			{
				"address": "2Cid96JrTGA2XaNG6zXrRKh18kLUbLP",
				"alias": null,
				"pubKey": "033da2433ef4ca111bfeefaadd9fe0f5874f3aac5186109f9de10a9eed6f48f184",
				"extend": null,
				"createTime": 1529311250627,
				"encrypted": true,
				"priKey": null,
				"encryptedPriKey": null
			}
			...
			
		]
	}
}
```

*e.g 示例代码*

```java
getAccountList(1, 10);
```
---

#### 1.6 根据账户别名获取账户地址 
**`Result getAddressByAlias(String alias)`**

说明
> 调用节点RPC接口，根据账户别名获取账户地址字符串
>
> Result.data `String`  

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">alias</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户别名 </td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value": "2ChDcC1nvki521xXhYAUzYXt4RLNULS"
    }
}
```
*e.g 示例代码*

```java
 getAddressByAlias("factory666");
```
---

#### 1.7 获取账户私钥 
**`Result getPrikey(String address, String password)`**

说明
> 调用RPC接口，根据账户地址和密码获取账户私钥，返回私钥字符串，必须是钱包创建的账户
>
> Result.data `String`

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">账户密码，如果账户没有加密则不填</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
		"value":"1f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d24"
    }
}
```
*e.g 示例代码* 

```java
//有密码的账户调用方式
getPrikey("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "nuls123456");
//无密码的账户调用方式
getPrikey("2ChqBTvFXttQsghj8zQpcdv76TQU8G5");
```
---

#### 1.8 验证别名是否可以使用
**`Result isAliasUsable(String alias)`**

说明
> 根据别名名称验证别名是否可用（是否没有被使用）
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">alias</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">别名名称</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{	//表示别名可以使用
    "success": true,
    "data": {
		"value":true
    }
}
```
- 返回 别名不可用的情况

```json
{	//表示别名不可以使用
    "success": true,
     "data"：{
        "value": false
     }
}
```
- 返回错误的情况,例如参数错误

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```
*e.g 示例代码* 

```java 
isAliasUsable("factory666");
```
---

#### 1.9 备份账户 
**`Result backupAccount(String address, String path, String password)`**   

说明

> 调用钱包RPC接口，根据账户地址,密码以及输出地址备份账户(导出.keystore文件)，如果账户是加密的则导出的keystore是由当前密码加密的，导入时需要验证此时账户的密码
>
> Result 返回生成的文件地址

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址</td>
    </tr>
    <tr>
        <td align="center">path</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">将要存放备份文件的文件夹，传null时，将备份到NULS服务的当前目录 </td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">账户密码，如果账户没有加密则不填</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
	    "value": "/Users/lichao/Downloads/2ChDcC1nvki521xXhYAUzYXt4RLNULS.accountkeystore"
    }
}
```
*e.g 示例代码*

```java
//备份一个没有密码的账户至当前目录
backupAccount("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", null);
//备份一个有密码的账户至/backup目录
backupAccount("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "/backup", "nuls123456");
```
---

#### 1.10 导入账户 
**`Result importAccountByKeystore`**  

说明

> 调用钱包RPC接口，根据keystore文件导入账户到钱包, 如果keystore文件是由加密的account备份生成的，则需要验证备份时账户的密码。
>
> Result 

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">path/fileReader</td>
        <td align="center">String/FileReader</td>
        <td align="center">必填</td>
        <td align="center">待导入的.keystore文件的url，或者由.keystore文件生成的FileReader对象</td>
    </tr>
    <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">.keystore文件对应的账户密码，如果导出.keystore时对应账户没有加密则不填</td>
    </tr>
     <tr>
        <td align="center">overwrite</td>
        <td align="center">boolean</td>
        <td align="center">必填</td>
        <td align="center">true: 执行覆盖导入; false: 如果该账户已经在钱包里存在，将不执行导入操作，并返回错误提示。</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"2ChDcC1nvki521xXhYAUzYXt4RLNULS"
    }
}
```
*e.g 示例代码*

```java
//导入一个有密码的账户
importAccountByKeystore("/backup/XXXXXX.keystore", "nuls123456", true);
importAccountByKeystore(fileReader, "nuls123456", true);
//导入一个没有密码的账户
importAccountByKeystore("/backup/XXXXXX.keystore", false);
importAccountByKeystore(fileReader, false);
```
---

#### 1.11 导入账户(私钥)  
接口

**`Result importAccountByPriKey(String privateKey, String password, boolean overwrite)`**  

说明

> 调用钱包RPC接口，根据私钥导入账户到钱包
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">privateKey</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户私钥 </td>
    </tr>
    <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">设置新密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
     <tr>
        <td align="center">overwrite</td>
        <td align="center">boolean</td>
        <td align="center">必填</td>
        <td align="center">true: 执行覆盖导入; false: 如果该账户已经在钱包里存在，将不执行导入操作，并返回错误提示。</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"2CiU1CmB6c9jmSLDNBe6PouA7NgNULS"
    }
}
```
*e.g 示例代码*

```java
//导入账户时设置密码
importAccountByPriKey("1f9d3ad044e0e120......", "nuls123456", true);
//导入账户时不设置密码
importAccountByPriKey("1f9d3ad044e0e120......", true);
```
---

#### 1.12 验证账户是否加密  
**`Result isEncrypted(String address)`**

说明

> 调用钱包RPC接口，验证账户是否加密，必须是钱包创建的账户
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{	//表示已加密
    "success": true,
    "data": {
		"value":true
    }
}
```
- 返回 未加密

```json
{
    "success": true,
     "data"：{
        "value": false
     }
}
```
- 返回错误的情况,例如参数错误

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```

*e.g 示例代码*

```java
isEncrypted("2ChqBTvFXttQsghj8zQpcdv76TQU8G5");
```
---

#### 1.13 移除账户 
**`Result removeAccount(String address, String password)`**

说明

> 调用钱包RPC接口，将地址对应的账户从钱包里移除
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">账户密码，如果账户没有加密则不填</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{	//表示成功
    "success": true,
    "data": {
		"value":true
    }
}
```

- 返回不成功 以及错误的情况,例如参数错误

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```
*e.g 示例代码*

```java
removeAccount("2ChqBTvFXttQsghj8zQpcdv76TQU8G5");
removeAccount("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "nuls123456");
```
---

#### 1.13 设置密码
**`Result setPassword(String address, String password）`**

说明

> 调用钱包RPC接口，为钱包内未加密的账户设置密码，已加密账户不能调用此接口
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">设置新密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{	//表示成功
    "success": true,
    "data": {
		"value":true
    }
}
```

- 返回不成功 以及错误的情况,例如参数错误

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```
*e.g 示例代码*

```java
setPassword("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "nuls123456");
```
---

#### 1.14 修改密码 
**`Result resetPassword(String address, String password, String newPassword)`**

说明

> 调用钱包RPC接口，为钱包内已加密的账户修改密码，未加密账户不能调用此接口
>
> Result

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户当前密码</td>
    </tr>
     <tr>
        <td align="center">newPassword</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">新密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{	//表示成功
    "success": true,
    "data": {
		"value":true
    }
}
```

- 返回不成功 以及错误的情况,例如参数错误

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```
*e.g 示例代码*

```java
resetPassword("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "nuls123456", "NULS111111");
```
---

#### 1.15 设置别名 
**`Result setAlias(String address, String alias, String password)`**

说明

> 调用钱包RPC接口，为钱包内账户设置一个别名，调用此接口会发送一个注册别名交易
>
> Result 返回设置别名的交易hash

<table>
    <tr>
       <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址</td>
    </tr>
     <tr>
        <td align="center">alias</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">待设置的别名名称</td>
    </tr>
        <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">密码</td>
        <td align="center">账户密码，如果账户没有加密则不填</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data":{		
		"value":"0020d7a69747778f6f02e2b0171640bc98aa19c53700988b7765c195ae691f3202c6"
	}
}
```
- 错误示例

```json
{
    "success": false,
     "data"：{
        "code": "ACT007",
        "msg": "The account already set an alias"
     }
}
```
*e.g 示例代码*

```java
setAlias("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "factory666", "NULS111111");
setAlias("2ChqBTvFXttQsghj8zQpcdv76TQU8G5", "factory666");
```
---

#### 1.16 设置账户密码 （离线模式）
**`Result setPasswordOffline(String address, String priKey, String password)`**

说明

> 设置离线账户密码, sdk中独立加密,不与NULS服务交互
>
> Result 返回加密后的私钥（encryptedPriKey）

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
     <tr>
        <td align="center">priKey</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的私钥</td>
    </tr>
        <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">新密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"a770c1886f566c973b6eb99543ef03825a89ed16e20d8dbe320aed64a85d5863ca23df43ef16ce0475424a49e192b6f9"
    }
}
```
*e.g 示例代码*

```java
setPasswordOffline("2CacFwqMwcJiGNNBwiwV7bCL7bjwNBr","00e4bfd347351ea899b5f0ae2c0a3e7a6951b202eaf72432d1a63a2dc85c59c82a","nuls123456");
```
---

#### 1.17 修改账户密码  （离线模式）
**`Result resetPasswordOffline(String address, String encryptedPriKey, String password, String newPassword)`**

说明

> 修改离线账户密码, sdk中独立修改, 不与NULS服务交互
>
> Result 返回新密码生成的加密后的私钥（encryptedPriKey）

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址 </td>
    </tr>
     <tr>
        <td align="center">encryptedPriKey</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">加密后的私钥</td>
    </tr>
        <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">原密码</td>
    </tr>
     <tr>
        <td align="center">newPassword</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">新密码, 密码长度8~20，需同时包含字母和数字，不能输入空格</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"a770c1886f566c973b6eb99543ef03825a89ed16e20d8dbe320aed64a85d5863ca23df43ef16ce0475424a49e192b6f9"
    }
}
```
*e.g 示例代码*

```java
resetPasswordOffline("2CacFwqMwcJiGNNBwiwV7bCL7bjwNBr","25368dbc0ff7eea4fc6da22bc37e85d7976a3846f8b58d4dc0cf484e740ba1b61f96395fbe1ddf70ece9fd21fcd95e7a","NULS111111", "nuls123456");
```
---



#### 1.18 验证地址格式是否正确（离线模式）

**`Result validateAddress(String address)`**

说明

> 验证账户地址格式, 不与NULS服务交互
>
> Result  验证结果

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户的地址 </td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"success": "ture",
        "errorCode":"10000"
    }
}
```

*e.g 示例代码*

```java
resetPasswordOffline("2CacFwqMwcJiGNNBwiwV7bCL7bjwNBr","25368dbc0ff7eea4fc6da22bc37e85d7976a3846f8b58d4dc0cf484e740ba1b61f96395fbe1ddf70ece9fd21fcd95e7a","NULS111111", "nuls123456");
```

------



### 交易 AccountLedgerService  

#### 2.1 根据交易hash查询交易详情
**`Result getTxByHash(String hash)`**

说明

> 根据交易hash查询交易详情
>
> Result.data <a href="#Transaction">`Transaction`</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">hash</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">交易的hash值</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2",
        "type": 2,
        "time": 1529323198461,
        "blockHeight": 1884,
        "fee": 100000,
        "value": 99900000000,
        "remark": "转账",
        "scriptSig": "21036dd27c9fa786a1e83df204e9b31ddc24745c378f1f6b42731d07f05347167c0000473045022100ff3372711d78eb554be331aa40cd7af246641ecd3bc06f2fdca7faefb25f74e50220743a2f2d9d01b5a77a878349b996cbe4953af5d1a946519a5ce4d1129cf99848",
        "status": 1,
        "confirmCount": 14,
        "size": 255,
        "inputs": [
            {
                "fromHash": "0020ab020707282932e6ec701f0b64e22e937fdd03ce9b37aab498aed2e00b6fa8e7",
                "fromIndex": 0,
                "address": "2ChDcC1nvki521xXhYAUzYXt4RLNULS",
                "value": 9999899000000
            }
        ],
        "outputs": [
            {
                "txHash": "00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2",
                "index": 0,
                "address": "2CiVA3n1VoNQobAax4d7qNEBZAfehLN",
                "value": 99900000000,
                "lockTime": 0,
                "status": 0
            },
            {
                "txHash": "00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2",
                "index": 1,
                "address": "2ChDcC1nvki521xXhYAUzYXt4RLNULS",
                "value": 9899998900000,
                "lockTime": 0,
                "status": 0
            }
        ]
    }
}
```
*e.g 示例代码*

```java
getTxByHash("041f3d2ceed........");
```
---

#### 2.2 转账

**`Result transfer(String address, String toAddress, String password, long amount, String remark)`**

说明

> 发起转账交易，转账金额amount不包含手续费
>
> Result 返回交易hash

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">转出者账户的地址</td>
    </tr>
     <tr>
        <td align="center">toAddress</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">接收者账户的地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">转出者账户的密码，如果账户没有加密则不填</td>
    </tr>
     <tr>
        <td align="center">amount</td>
        <td align="center">long</td>
        <td align="center">必填</td>
        <td align="center">转账金额(单位:Na)</td>
    </tr>
     <tr>
        <td align="center">remark</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">备注</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2"
    }
}
```
*e.g 示例代码*

```java
	//账户有密码
transfer("2ChDcC1nvki521xXhYAUzYXt4RLNULS", "2CiU1CmB6c9jmSLDNBe6PouA7NgXXXX", "nuls123456", 8888800000000, "备注1NULS=10000000Na");
//账户没有密码
transfer("2ChDcC1nvki521xXhYAUzYXt4RLNULS", "2CiU1CmB6c9jmSLDNBe6PouA7NgNULS", 8888800000000, "备注1NULS=10000000Na");
```
---

#### 2.3 转账

**`Result sendToAddress(String address, String toAddress, String password, long amount, String remark)`**

说明

> 发起转账交易，转账金额amount包含手续费
>
> Result 返回交易hash

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">转出者账户的地址</td>
    </tr>
     <tr>
        <td align="center">toAddress</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">接收者账户的地址</td>
    </tr>
     <tr>
        <td align="center">password</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">转出者账户的密码，如果账户没有加密则不填</td>
    </tr>
     <tr>
        <td align="center">amount</td>
        <td align="center">long</td>
        <td align="center">必填</td>
        <td align="center">转账金额(单位:Na)</td>
    </tr>
     <tr>
        <td align="center">remark</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">备注</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value":"00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2"
    }
}
```

*e.g 示例代码*

```java
	//账户有密码
sendToAddress("2ChDcC1nvki521xXhYAUzYXt4RLNULS", "2CiU1CmB6c9jmSLDNBe6PouA7NgXXXX", "nuls123456", 8888800000000, "备注1NULS=10000000Na");
//账户没有密码
sendToAddress("2ChDcC1nvki521xXhYAUzYXt4RLNULS", "2CiU1CmB6c9jmSLDNBe6PouA7NgNULS", 8888800000000, "备注1NULS=10000000Na");
```

------

#### 2.4 多地址转账交易

**`Result multipleAddressTransfer(List<TransferFrom> inputs, List<TransferTo> outputs, String remark)`**

说明

> 通过用户传入的交易输入\输出，发送多地址转账交易。input里的每一个账户都是单独密码，没有密码的账户可以传空字符串。详见e.g示例。**示例中input和output的属性都为必填项**。
>
> **注：**input里没有说明需要传入金额，该接口会默认将inputs里的所有账户的未花费零钱从小到大排序后逐条使用，直到金额足够支付转入金额+手续费，若有找零，会返还给inputs里其中一个地址上。
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
  <tr>
  	<th align="center">参数</th>
    <th align="center">类型</th>
    <th align="center">是否必填</th>
    <th align="center">说明</th>
  </tr>
  <tr>
  <td align="center">inputs</td>
  <td align="center">List&lt;TransferFrom&gt;</td>
  <td align="center">必填</td>
  <td align="center">账户对应的地址和密码</td>
  </tr>
   <tr>
  <td align="center">outputs</td>
  <td align="center">List&lt;TransferTo&gt;</td>
  <td align="center">必填</td>
  <td align="center">接收转账的地址和金额</td>
  </tr>
  <tr>
  <td align="center">remark</td>
  <td align="center">String</td>
  <td align="center">非必填</td>
  <td align="center">交易备注</td>
  </tr> 
</table>

*e.g 示例代码*

```java
List<TransferFrom> inputs = new ArrayList<>();
TransferFrom from1 = new TransferFrom("Nse5oPtPjgbyHujSxXu2YbWRmmf3ksCo","abcd1234");
inputs.add(from1);

TransferFrom from2 = new TransferFrom("Nsdz9go1hcQrrssG2Kqu57h6v9rH8puC","abcd5678");
inputs.add(from2);

List<TransferTo> outputs = new ArrayList<>();
TransferTo to1 = new TransferTo("Nse7N3aVXqaKdECrepueKMYCfcXrwLxE",10000000000L);
outputs.add(to1);

TransferTo to2 = new TransferTo("NsduyVrtxo4G2UrBHGMsVj8vTtRtdfRM",10000000000L);
outputs.add(to2);

String remark = "test multipleAddressTransfer";

Result result = NulsSDKTool.multipleAddressTransfer(inputs, outputs, remark);
```

------

#### 2.5 查询账户余额 (钱包创建的账户)

**`Result getBalance(String address)`**

说明

> 获取账户余额
>
> Result.data <a href="#BalanceInfo">`BalanceInfo `</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "balance": 1009899998900000,
        "usable": 1009899998900000,
        "locked": 0
    }
}
```
*e.g 示例代码*

```java
getBalance("2ChDcC1nvki521xXhYAUzYXt4RLNULS");
```
---

#### 2.5 查询账户余额 （所有已上链的账户）

**`Result getAccountBalance(String address)`**

说明

> 获取账户余额
>
> Result.data <a href="#BalanceInfo">`BalanceInfo `</a>



#### 2.6 创建转账交易 （离线模式）

**`Result createTransaction(List<Input> inputs, List<Output> outputs, String remark)`**

说明

> 通过用户传入的交易输入\输出，组装交易。传入的参数为json对象，详见e.g示例。**示例中input和output的属性都为必填项**。交易手续费为inputs和outputs总额的差值，手续费会根据交易大小存在一个最小值，低于最小值时，交易不会被打包。**有关手续费的计算会在后面详细说明**
>
> 注：此接口只支持一对多转账
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
  <tr>
  	<th align="center">参数</th>
    <th align="center">类型</th>
    <th align="center">是否必填</th>
    <th align="center">说明</th>
  </tr>
  <tr>
  <td align="center">inputs</td>
  <td align="center">List&lt;Input&gt;</td>
  <td align="center">必填</td>
  <td align="center">交易引用的未花费输出</td>
  </tr>
   <tr>
  <td align="center">outputs</td>
  <td align="center">List&lt;Output&gt;</td>
  <td align="center">必填</td>
  <td align="center">交易新生成的未花费输出</td>
  </tr>
  <tr>
  <td align="center">remark</td>
  <td align="center">String</td>
  <td align="center">非必填</td>
  <td align="center">交易备注</td>
  </tr>
</table>

*e.g 示例代码*

```java
String remark = "create transaction demo";
long fee = 100000;
List<Input> inputs = new ArrayList<>();
List<Output> outputs = new ArrayList<>();

//组装交易的inputs,示例中的input属性均必填
Input input = new Input();
input.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
input.setFromIndex(1);
input.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
input.setValue(1000000000L);      
input.setLockTime(0);
inputs.add(input);

//组装交易的outputs,示例中的output属性均必填
Output output = new Output();
output.setAddress("2CjPVMKST7h4Q5Dqa8Q9P9CwYSmN7mG");
output.setValue(1000000L);
output.setLockTime(0L);
outputs.add(output);

output = new Output();
output.setAddress("2CXJEuoXZMajeTEgL6TgiSxTRRMwiMM");
output.setValue(1000000000L - 1000000 - fee);
output.setLockTime(0L);
outputs.add(output);

Result result = NulsSDKTool.createTransaction(inputs, outputs, remark);
```

> **交易手续费的计算**：手续费单价 \* 交易大小
>
> **手续费单价(min)**：100000 NA/1KB
>
> **交易大小的计算**：（124 + 50  * inputs.length + 38 * outputs.length + remark.bytes.length ）/1024 
> 124为基本信息的固定长度，50为单条input的长度，38为单条output的长度，remark为非必填字段，有需要传入时，按照UTF-8字符编码计算字节长度。交易大小用KB作为单位，最大值为300KB，不足1KB的部分，记为1KB。
> 出块节点在验证每笔交易时，将输入与输出的差值视为用户发送此交易时所支付的手续费，再根据交易的大小计算出手续费的最小值。如果用户的手续费小于最小值，则视为不合法交易，不予打包。因此，在创建交易的时候，手续费的计算应谨慎处理，避免交易失败。

------

#### 2.7 交易签名 （离线模式）

接口

**`Result signTransaction(String txHex, String priKey, String address, String password)`**

说明

> 通过私钥，给交易签名
>
> Result.data `String` 签名后的交易，16进制后的序列化字符串

<table>
  <tr>
  	<th align="center">参数</th>
	<th align="center">类型</th>
	<th align="center">是否必填</th>
	<th align="center">说明</th>
  </tr>
  <tr>
  <td align="center">txHex</td>
  <td align="center">String</td>
  <td align="center">必填</td>
  <td align="center">十六进制的交易序列化数据</td>
  </tr>
   <tr>
  <td align="center">priKey</td>
  <td align="center">String</td>
  <td align="center">必填</td>
  <td align="center">交易的私钥</td>
  </tr>
   <tr>
  <td align="center">address</td>
  <td align="center">String</td>
  <td align="center">必填</td>
  <td align="center">私钥对应的地址，用于验证私钥合法性</td>
  </tr>
   <tr>
  <td align="center">password</td>
  <td align="center">String</td>
  <td align="center">非必填</td>
  <td align="center">私钥的密码，如果私钥未加密可不传</td>
  </tr>
  </table>

*e.g 示例代码*

```java
String txHex ="020197320f96301001a78cb7fb8bb7b1710b4afa390d8341080fba7a47e8d030000000000000000";
String priKey ="252f6d9d55b7ef539ea58df99ebaf71c9929bd9d0054338baf7a59c9b85b4fa631f816907c8";
String address = "2CXJEuoXZMajeTEgL6TgiSxTRRMwiMM";
String password = "NULS6352s!f";
Result result = NulsSDKTool.signTransaction(txHex, priKey, address, password);
```

------

#### 2.8 创建多地址转账交易 （离线模式）

**`Result createMultipleInputAddressTransaction(List<Input> inputs, List<Output> outputs, String remark)`**

说明

> 通过用户传入的交易输入\输出，组装交易。传入的参数为json对象，详见e.g示例。**示例中input和output的属性都为必填项**。交易手续费为inputs和outputs总额的差值，手续费会根据交易大小存在一个最小值，低于最小值时，交易不会被打包。**有关手续费的计算会在后面详细说明**
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
  <tr>
  	<th align="center">参数</th>
    <th align="center">类型</th>
    <th align="center">是否必填</th>
    <th align="center">说明</th>
  </tr>
  <tr>
  <td align="center">inputs</td>
  <td align="center">List&lt;Input&gt;</td>
  <td align="center">必填</td>
  <td align="center">交易引用的未花费输出</td>
  </tr>
   <tr>
  <td align="center">outputs</td>
  <td align="center">List&lt;Output&gt;</td>
  <td align="center">必填</td>
  <td align="center">交易新生成的未花费输出</td>
  </tr>
   <tr>
  <td align="center">nInputAccount</td>
  <td align="center">int</td>
  <td align="center">必填</td>
  <td align="center">转账账户数</td>
  </tr>
  <tr>
  <td align="center">remark</td>
  <td align="center">String</td>
  <td align="center">非必填</td>
  <td align="center">交易备注</td>
  </tr> 
</table>
*e.g 示例代码*

```java
String remark = "create transaction demo";
long fee = 100000;
List<Input> inputs = new ArrayList<>();
List<Output> outputs = new ArrayList<>();
int nInputAccount = 2;

//组装交易的inputs,示例中的input属性均必填
Input input = new Input();
input.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
input.setFromIndex(1);
input.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
input.setValue(1000000000L);      
input.setLockTime(0);
inputs.add(input);

//组装交易的outputs,示例中的output属性均必填
Output output = new Output();
output.setAddress("2CjPVMKST7h4Q5Dqa8Q9P9CwYSmN7mG");
output.setValue(1000000L);
output.setLockTime(0L);
outputs.add(output);

output = new Output();
output.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5");
output.setValue(1000000000L - 1000000 - fee);
output.setLockTime(0L);
outputs.add(output);

Result result = NulsSDKTool.createMultipleInputAddressTransaction(inputs,nInputAccount, outputs, remark);
```

> **交易手续费的计算**：手续费单价 \* 交易大小
>
> **手续费单价(min)**：100000 NA/1KB
>
> **交易大小的计算**：（124*nInputAccount + 50  * inputs.length + 38 * outputs.length + remark.bytes.length ）/1024 
> 124为基本信息的固定长度，50为单条input的长度，38为单条output的长度，remark为非必填字段，有需要传入时，按照UTF-8字符编码计算字节长度。交易大小用KB作为单位，最大值为300KB，不足1KB的部分，记为1KB。
> 出块节点在验证每笔交易时，将输入与输出的差值视为用户发送此交易时所支付的手续费，再根据交易的大小计算出手续费的最小值。如果用户的手续费小于最小值，则视为不合法交易，不予打包。因此，在创建交易的时候，手续费的计算应谨慎处理，避免交易失败。

------

#### 2.9 多地址转账交易签名 （离线模式）

**`Result signMultipleAddressTransaction(String txHex, List<String> privKeys, List<String> passwords)`**

说明

> 通过私钥，给交易签名
>
> Result.data `String` 签名后的交易，16进制后的序列化字符串

<table>
  <tr>
  	<th align="center">参数</th>
	<th align="center">类型</th>
	<th align="center">是否必填</th>
	<th align="center">说明</th>
  </tr>
  <tr>
  <td align="center">txHex</td>
  <td align="center">String</td>
  <td align="center">必填</td>
  <td align="center">十六进制的交易序列化数据</td>
  </tr>
   <tr>
  <td align="center">privKeys</td>
  <td align="center">List<String></String></td>
  <td align="center">必填</td>
  <td align="center">交易的私钥</td>
  </tr>
   <tr>
  <td align="center">passwords</td>
  <td align="center">List<String></String></td>
  <td align="center">必填</td>
  <td align="center">私钥对应的密码（多个账户密码必须一样）</td>
  </tr>
  </table>

*e.g 示例代码*

```java
String txHex ="020197320f96301001a78cb7fb8bb7b1710b4afa390d8341080fba7a47e8d030000000000000000";
List<String> priKeys = Arrays.asList("08a605b754bd1be1ba765fabd5cd218a545eb38b54ad26a7eb8a3378f724e5be", "00a710f9679fb6b5953bcfbea67a198e9c0d8888c43bad78a7241258e36aeaf65d");
List<String> passwords = Arrays.asList("123456","123456");
Result result = NulsSDKTool.signMultiTransaction(txHex, priKeys, passwords);
```

#### 2.10 交易验证 

**`Result validateTransaction(String txHex);`**

说明

> 验证离线创建的交易是否正确
>
> Result.data 

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">txHex</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">十六进制交易序列化数据</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"success": "ture",
        "errorCode":"10000"
    }
}

```

*e.g 示例代码*

```java
String txHex = "1f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d241f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d24";
Result result = service.validateTransaction(txHex);
```

------

#### 2.11 创建转账交易 （离线模式 - 不计算手续费）

**`Result createTransaction(String address, String toAddress, long amount, String remark, List<Input> utxos)`**

说明

> 创建离线转账接口，不用计算手续费，需要传入转出者账户的未花费输出（如果转出者账户的未花费输出太多，则组装的未花费输出金额 > 转账金额(单位:Na) + 30000000(单位:Na)即可）
>
> 注：此接口只支持一对一转账
>
> Result.data 
>
>  - `String` 交易Hash
>  - `String` 16进制后的交易序列化字符串
>  - `List<Input>` 交易引用的未花费输出
>  - `List<Output>` 交易新生成的未花费输出
>
> 通过返回的`String` 16进制后的交易序列化字符串再去做离线交易签名
>
> 通过返回的`List<Input>` 交易引用的未花费输出和`List<Output>` 交易新生成的未花费输出来维护自己的数据库



<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">转出者账户的地址</td>
    </tr>
     <tr>
        <td align="center">toAddress</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">接收者账户的地址</td>
    </tr>
     <tr>
        <td align="center">amount</td>
        <td align="center">long</td>
        <td align="center">必填</td>
        <td align="center">转账金额(单位:Na)</td>
    </tr>
     <tr>
        <td align="center">remark</td>
        <td align="center">String</td>
        <td align="center">非必填</td>
        <td align="center">备注</td>
    </tr>
    <tr>
      <td align="center">inputs</td>
      <td align="center">List&lt;Input&gt;</td>
      <td align="center">必填</td>
      <td align="center">转出者账户的未花费输出</td>
    </tr>
</table>



*e.g 示例代码*

```java
String from = "2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5";
String to = "2CjPVMKST7h4Q5Dqa8Q9P9CwYSmN7mG";
long amount = 2000000000L;
String remark = "create transaction demo";
List<Input> utxos = new ArrayList<>();

//组装转出者账户的未花费输出的utxos, 属性必填
Input utxo = new Input();
utxo.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo.setFromIndex(1);
utxo.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo.setValue(1000000000L);      
utxo.setLockTime(0);
utxos.add(utxo);

Input utxo1 = new Input();
utxo1.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo1.setFromIndex(2);
utxo1.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo1.setValue(1040000000L);      
utxo1.setLockTime(0);
utxos.add(utxo1);

Result result = NulsSDKTool.createTransaction(from, to, amount, remark, utxos);
```

------


#### 2.12 广播交易 

接口

**`Result broadcastTransaction(String txHex);`**

说明

> 广播交易
>
> Result.data String 返回交易hash

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">txHex</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">十六进制交易序列化数据</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value": "002023c66d10cf9047dbcca12aee2235ff9dfe0f13db3c921a2ec22e0dd63331cb85"
    }
}

```

*e.g 示例代码*

```java
String txHex = "1f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d241f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d24";
Result result = service.broadcastTransaction(txHex);
if(result.isSuccess()) {
   String txHash = (String)result.getData();
}
```

------



#### 2.13 查询账户UTXO

接口

**`JsonRPCResult getUtxo(String address, long amount);`**

说明

> 此交易可以根据账户地址，查询已上链的所有账户的UTXO，专门用于离线账户组装交易时，需要账户的UTXO生成交易的inputs数据。
>
> amount参数为交易业务需要的金额，返回的utxo里，会包含amount金额和业务所需最大值手续费，若账户金额不足，则返回当前账户所有utxo；若utxo过多，一次性会最多返回6000条utxo
>
> 建议此接口和2.11接口接合使用
>
> Result.data String 返回交易hash

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">address</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">账户地址</td>
    </tr>
    <tr>
        <td align="center">amount</td>
        <td align="center">long</td>
        <td align="center">必填</td>
        <td align="center">代币数量</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
    	"value": "002023c66d10cf9047dbcca12aee2235ff9dfe0f13db3c921a2ec22e0dd63331cb85"
    }
}

```

*e.g 示例代码*

```java
String txHex = "1f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d241f9d3ad044e0e1201e117b041f3d2ceedacb44688e57969620f3ad7a4d6e9d24";
Result result = service.broadcastTransaction(txHex);
if(result.isSuccess()) {
   String txHash = (String)result.getData();
}
```

------



### 区块 BlockService

#### 3.1 根据区块高度获取区块头
接口

**`Result getblockHeader(int height)`**

说明

> 根据区块高度获取区块头信息
>
> Result.data <a href="#BlockHeader">`BlockHeader `</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">height</td>
        <td align="center">int</td>
        <td align="center">必填</td>
        <td align="center">区块高度</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "002078656b6e32f4f1e9e881e7b6c4c5de036ae81ec5bf78861bde9480f5ff3a1b33",
        "preHash": "0020ee5d28fde669adb0ad16f3ed426f1ee8df40560bed0ab30bb99cbf95df276d64",
        "merkleHash": "0020cc37658e2d110c1d42f64c7fd3dcb56d9653d4edc4d3a8406cb263a41f9f5488",
        "time": 1529299160000,
        "height": 4,
        "txCount": 4,
        "packingAddress": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
        "scriptSig": "2102e18d02154e0f68900898efea7ba72d6d14e37d7d173a62146df2871f40996d7300473045022100d4d92a9518ffd855441c7712f4b31bd003291dc108fa2b455fe26d51e54625f102202ae8375bd69bf1928f9967edac82619ff78f30550c17797cc489d5effd3202bf",
        "roundIndex": 419517,
        "consensusMemberCount": 1,
        "roundStartTime": 1529299150000,
        "packingIndexOfRound": 1,
        "confirmCount": 1909,
        "reward": 0,
        "fee": 0,
        "size": 1
    }
}
```
*e.g 示例代码*

```java
getblockHeader(10);
```
---

#### 3.2 根据区块hash获取区块头 
接口

**`Result getblockHeader(String hash)`**

说明

> 根据区块hash获取区块头 
>
> Result.data <a href="#BlockHeader">`BlockHeader`</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">hash</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">区块hash值</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "002078656b6e32f4f1e9e881e7b6c4c5de036ae81ec5bf78861bde9480f5ff3a1b33",
        "preHash": "0020ee5d28fde669adb0ad16f3ed426f1ee8df40560bed0ab30bb99cbf95df276d64",
        "merkleHash": "0020cc37658e2d110c1d42f64c7fd3dcb56d9653d4edc4d3a8406cb263a41f9f5488",
        "time": 1529299160000,
        "height": 4,
        "txCount": 4,
        "packingAddress": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
        "scriptSig": "2102e18d02154e0f68900898efea7ba72d6d14e37d7d173a62146df2871f40996d7300473045022100d4d92a9518ffd855441c7712f4b31bd003291dc108fa2b455fe26d51e54625f102202ae8375bd69bf1928f9967edac82619ff78f30550c17797cc489d5effd3202bf",
        "roundIndex": 419517,
        "consensusMemberCount": 1,
        "roundStartTime": 1529299150000,
        "packingIndexOfRound": 1,
        "confirmCount": 1909,
        "reward": 0,
        "fee": 0,
        "size": 1
    }
}
```
*e.g 示例代码*

```java
getblockHeader("041f3d2ceed........");
```
---

#### 3.3 根据区块高度获取区块 
接口

**`Result getBlock(int height)`**

说明

> 根据区块高度获取区块
>
> Result.data <a href="#Block">`Block `</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">height</td>
        <td align="center">int</td>
        <td align="center">必填</td>
        <td align="center">区块高度</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "00209bbcd98110b57f1ecd66c9d94d1a2381e6c03c3b9aa77db25b6eb5955bb658d3",
        "preHash": "00201c0fa53c98595c7f9ba817fca6367aa2da1c1e480f801766b7a4a16b39c54b67",
        "merkleHash": "00200c2dfa0560229ef728cd6bdc858334ef4072d8e25c3b0831c8366f4d4cb0c168",
        "time": 1529323210000,
        "height": 1884,
        "txCount": 1884,
        "packingAddress": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
        "scriptSig": "2102e18d02154e0f68900898efea7ba72d6d14e37d7d173a62146df2871f40996d73004730450221009d1015b7cab19ac8099245390ae41ca14da9f47d5c28b3b7780a53bd8adcccd802202bf74bb764f9de248dfb892d98e9ff91bad25daa107cfdf4effd3fb1c266e5ae",
        "roundIndex": 420416,
        "consensusMemberCount": 5,
        "roundStartTime": 1529323170000,
        "packingIndexOfRound": 4,
        "confirmCount": 33,
        "reward": 100000,
        "fee": 100000,
        "size": 5,
        "txList": [
            {
                "hash": "00202b9737b15bf7e4ebc74a58554e461fbed6fedf3e289c6ef41afa80d83f67babc",
                "type": 1,
                "time": 1529323210000,
                "blockHeight": 1884,
                "fee": 0,
                "value": 0,
                "remark": null,
                "scriptSig": null,
                "status": 0,
                "confirmCount": 33,
                "size": 54,
                "inputs": [],
                "outputs": [
                    {
                        "address": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
                        "value": 100000,
                        "lockTime": 2884
                    }
                ]
            },
            {
                "hash": "00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2",
                "type": 2,
                "time": 1529323198461,
                "blockHeight": 1884,
                "fee": 100000,
                "value": 0,
                "remark": "转账",
                "scriptSig": "21036dd27c9fa786a1e83df204e9b31ddc24745c378f1f6b42731d07f05347167c0000473045022100ff3372711d78eb554be331aa40cd7af246641ecd3bc06f2fdca7faefb25f74e50220743a2f2d9d01b5a77a878349b996cbe4953af5d1a946519a5ce4d1129cf99848",
                "status": 0,
                "confirmCount": 33,
                "size": 255,
                "inputs": [
                    {
                        "fromHash": "0020ab020707282932e6ec701f0b64e22e937fdd03ce9b37aab498aed2e00b6fa8e7",
                        "fromIndex": 0,
                        "address": null,
                        "value": 9999899000000
                    }
                ],
                "outputs": [
                    {
                        "address": "2CiVA3n1VoNQobAax4d7qNEBZAfehLN",
                        "value": 99900000000,
                        "lockTime": 0
                    },
                    {
                        "address": "2ChDcC1nvki521xXhYAUzYXt4RLNULS",
                        "value": 9899998900000,
                        "lockTime": 0
                    }
                ]
            },
            {
                "hash": "002040370fcb2ad080abdcd2d91f952826c8f6e55bda7231c1c15f25d9d74dc8ad7f",
                "type": 7,
                "time": 1529323210000,
                "blockHeight": 1884,
                "fee": 0,
                "value": 0,
                "remark": null,
                "scriptSig": null,
                "status": 0,
                "confirmCount": 33,
                "size": 38,
                "inputs": [],
                "outputs": []
            }
        ]
    }
}
```
*e.g 示例代码*

```java
getBlock(10);
```
---

#### 3.4 根据区块hash获取区块 
接口

**`Result getBlock(String hash)`**

说明

> 根据区块hash获取区块
>
> Result.data <a href="#Block">`Block `</a>

<table>
    <tr>
        <th align="center">参数</th>
        <th align="center">类型</th>
        <th align="center">是否必填</th>
        <th align="center">说明</th>
    </tr>
    <tr>
        <td align="center">hash</td>
        <td align="center">String</td>
        <td align="center">必填</td>
        <td align="center">区块hash值</td>
    </tr>
    </table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "00209bbcd98110b57f1ecd66c9d94d1a2381e6c03c3b9aa77db25b6eb5955bb658d3",
        "preHash": "00201c0fa53c98595c7f9ba817fca6367aa2da1c1e480f801766b7a4a16b39c54b67",
        "merkleHash": "00200c2dfa0560229ef728cd6bdc858334ef4072d8e25c3b0831c8366f4d4cb0c168",
        "time": 1529323210000,
        "height": 1884,
        "txCount": 1884,
        "packingAddress": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
        "scriptSig": "2102e18d02154e0f68900898efea7ba72d6d14e37d7d173a62146df2871f40996d73004730450221009d1015b7cab19ac8099245390ae41ca14da9f47d5c28b3b7780a53bd8adcccd802202bf74bb764f9de248dfb892d98e9ff91bad25daa107cfdf4effd3fb1c266e5ae",
        "roundIndex": 420416,
        "consensusMemberCount": 5,
        "roundStartTime": 1529323170000,
        "packingIndexOfRound": 4,
        "confirmCount": 33,
        "reward": 100000,
        "fee": 100000,
        "size": 5,
        "txList": [
            {
                "hash": "00202b9737b15bf7e4ebc74a58554e461fbed6fedf3e289c6ef41afa80d83f67babc",
                "type": 1,
                "time": 1529323210000,
                "blockHeight": 1884,
                "fee": 0,
                "value": 0,
                "remark": null,
                "scriptSig": null,
                "status": 0,
                "confirmCount": 33,
                "size": 54,
                "inputs": [],
                "outputs": [
                    {
                        "address": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
                        "value": 100000,
                        "lockTime": 2884
                    }
                ]
            },
            {
                "hash": "00203a169b42e5e142e20b273ac925e55f773b5a38c5f5c907efdbc43abb7d7a67b2",
                "type": 2,
                "time": 1529323198461,
                "blockHeight": 1884,
                "fee": 100000,
                "value": 0,
                "remark": "转账",
                "scriptSig": "21036dd27c9fa786a1e83df204e9b31ddc24745c378f1f6b42731d07f05347167c0000473045022100ff3372711d78eb554be331aa40cd7af246641ecd3bc06f2fdca7faefb25f74e50220743a2f2d9d01b5a77a878349b996cbe4953af5d1a946519a5ce4d1129cf99848",
                "status": 0,
                "confirmCount": 33,
                "size": 255,
                "inputs": [
                    {
                        "fromHash": "0020ab020707282932e6ec701f0b64e22e937fdd03ce9b37aab498aed2e00b6fa8e7",
                        "fromIndex": 0,
                        "address": null,
                        "value": 9999899000000
                    }
                ],
                "outputs": [
                    {
                        "address": "2CiVA3n1VoNQobAax4d7qNEBZAfehLN",
                        "value": 99900000000,
                        "lockTime": 0
                    },
                    {
                        "address": "2ChDcC1nvki521xXhYAUzYXt4RLNULS",
                        "value": 9899998900000,
                        "lockTime": 0
                    }
                ]
            },
            {
                "hash": "002040370fcb2ad080abdcd2d91f952826c8f6e55bda7231c1c15f25d9d74dc8ad7f",
                "type": 7,
                "time": 1529323210000,
                "blockHeight": 1884,
                "fee": 0,
                "value": 0,
                "remark": null,
                "scriptSig": null,
                "status": 0,
                "confirmCount": 33,
                "size": 38,
                "inputs": [],
                "outputs": []
            }
        ]
    }
}
```
*e.g 示例代码*

```java
getBlock("041f3d2ceed........");
```
---

#### 3.5 获取最新区块头 
接口

**`Result getNewestBlockHash()`**

说明

> 获取最新的区块头
>
> Result.data <a href="#BlockHeader">`BlockHeader`</a>


返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "hash": "002078656b6e32f4f1e9e881e7b6c4c5de036ae81ec5bf78861bde9480f5ff3a1b33",
        "preHash": "0020ee5d28fde669adb0ad16f3ed426f1ee8df40560bed0ab30bb99cbf95df276d64",
        "merkleHash": "0020cc37658e2d110c1d42f64c7fd3dcb56d9653d4edc4d3a8406cb263a41f9f5488",
        "time": 1529299160000,
        "height": 4,
        "txCount": 4,
        "packingAddress": "2CWsZb9w8XXTE58TUhBGczxf4U6NULS",
        "scriptSig": "2102e18d02154e0f68900898efea7ba72d6d14e37d7d173a62146df2871f40996d7300473045022100d4d92a9518ffd855441c7712f4b31bd003291dc108fa2b455fe26d51e54625f102202ae8375bd69bf1928f9967edac82619ff78f30550c17797cc489d5effd3202bf",
        "roundIndex": 419517,
        "consensusMemberCount": 1,
        "roundStartTime": 1529299150000,
        "packingIndexOfRound": 1,
        "confirmCount": 1909,
        "reward": 0,
        "fee": 0,
        "size": 1
    }
}

```
*e.g 示例代码*

```java
getNewestBlockHash();
```
---

#### 3.6 获取最新区块的高度 
接口

**`Result getNewestBlockHight()`**

说明

> 获取最新区块的高度
>
> Result.data 高度(Long)

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "value": 5210
    }
}}

```
*e.g 示例代码*

```java
getNewestBlockHight();
```
---

#### 3.7 获取最新区块的块Hash
接口

**`Result getNewestBlockHash()`**

说明

> 获取最新区块的块Hash
>
> Result.data Hash值(String)

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
    "success": true,
    "data": {
        "value": "0020a2e1c99951184700927472c431a5a65847c7974cac0bbb97b242c7adf56ad27b"
    }
}}

```
*e.g 示例代码*

```java
getNewestBlockHash();
```
---



### 共识 ConsensusService

#### 4.1 组装创建节点交易（离线模式）

接口

**` Result createAgentTransaction(AgentInfo agentInfo, List<Input> inputs, Na fee) `**

说明

> 离线组装创建节点交易
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
<tr>
<th align="center">参数</th>
<th align="center">类型</th>
<th align="center">是否必填</th>
<th align="center">说明</th>
</tr>
<tr>
<td align="center">agentInfo</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">创建节点信息</td>
</tr>
<tr>
<td align="center">inputs</td>
<td align="center">List</td>
<td align="center">必填</td>
<td align="center">输入信息</td>
</tr>
<tr>
<td align="center">fee</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">交易手续费</td>
</tr>
</table>

*e.g 示例代码*

```java
//组装交易的inputs,示例中的input属性均必填
List<Input> inputs = new ArrayList<>();
Input input = new Input();
input.setFromHash("0020b0a75a26caad17b4ea6cec7f059ac0e426d71696a6096f75bb2e9f30c11c60d6");
input.setFromIndex(1);
input.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
input.setValue(999998760000000L);
inputs.add(input);

//创建节点信息，示例总AgentInfo的属性均必填
AgentInfo info = new AgentInfo();
info.setAgentAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");       //申请共识节点的地址
info.setPackingAddress("NsdwUo8XU52DtB9Zqjo2YkuLBW8VhGaQ");     //实际打包区块的地址
info.setDeposit(200000 * 100000000L);//创建节点的保证金， 最低20000NULS，最高200000NULS     
info.setCommissionRate(10.0); //佣金比例 1-100

//创建节点的手续费
Na fee = Na.valueOf(1000000L);

Result result = NulsSDKTool.createAgentTransaction(info, inputs, fee);
Map<String, Object> map = (Map<String, Object>) result.getData();
String txHex = (String) map.get("value");
```

------

> **创建共识节点交易手续费的计算**：手续费单价 \* 交易大小
>
> **手续费单价(min)**：1000000 NA/1KB
>
> **交易大小的计算**：（288 + 50  * inputs.length）/1024 ，
> 其中210为基本信息的固定长度，50为单条input的长度，38为单条output的长度，remark为非必填字段，有需要传入时，按照UTF-8字符编码计算字节长度。交易大小用KB作为单位，最大值为300KB，不足1KB的部分，记为1KB。
> 出块节点在验证每笔交易时，将输入与输出的差值视为用户发送此交易时所支付的手续费，再根据交易的大小计算出手续费的最小值。如果用户的手续费小于最小值，则视为不合法交易，不予打包。因此，在创建交易的时候，手续费的计算应谨慎处理，避免交易失败。

------



#### 4.2 组装委托共识交易（离线模式）

接口

**` Result createDepositTransaction(DepositInfo depositInfo, List<Input> inputs, Na fee) `**

说明

> 离线组装委托共识交易
>
> Result.data `String` 16进制后的交易序列化字符串



<table>
<tr>
<th align="center">参数</th>
<th align="center">类型</th>
<th align="center">是否必填</th>
<th align="center">说明</th>
</tr>
<tr>
<td align="center">depositInfo</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">创建节点信息</td>
</tr>
<tr>
<td align="center">inputs</td>
<td align="center">List</td>
<td align="center">必填</td>
<td align="center">输入信息</td>
</tr>
<tr>
<td align="center">fee</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">交易手续费</td>
</tr>
</table>

*e.g 示例代码*

```java
//组装交易的inputs,示例中的input属性均必填
List<Input> inputs = new ArrayList<>();
Input input = new Input();
input.setFromHash("0020b0a75a26caad17b4ea6cec7f059ac0e426d71696a6096f75bb2e9f30c11c60d6");
input.setFromIndex(1);
input.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
input.setValue(999998760000000L);
inputs.add(input);

//委托共识信息，示例中DepositInfo的属性均必填
DepositInfo info = new DepositInfo();
info.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");            //委托共识的地址
info.setDeposit(300000 * 100000000L);//委托代币数量:2000NULS —— 500000NULS
info.setAgentHash("0020a467827d5f06feb3e78b4603eb03677711219cb5232d145b3e9d4ab48a3eb366");//创建共识节点的交易id

//创建节点的手续费
Na fee = Na.valueOf(1000000L);

Result result = NulsSDKTool.createDepositTransaction(info, inputs, fee);
Map<String, Object> map = (Map<String, Object>) result.getData();
String txHex = (String) map.get("value");
```

> **创建委托交易手续费的计算**：手续费单价 \* 交易大小
>
> **手续费单价(min)**：1000000 NA/1KB
>
> **交易大小的计算**：（288 + 50  * inputs.length）/1024 ，
> 其中210为基本信息的固定长度，50为单条input的长度，38为单条output的长度，remark为非必填字段，有需要传入时，按照UTF-8字符编码计算字节长度。交易大小用KB作为单位，最大值为300KB，不足1KB的部分，记为1KB。
> 出块节点在验证每笔交易时，将输入与输出的差值视为用户发送此交易时所支付的手续费，再根据交易的大小计算出手续费的最小值。如果用户的手续费小于最小值，则视为不合法交易，不予打包。因此，在创建交易的时候，手续费的计算应谨慎处理，避免交易失败。



#### 4.3 组装退出委托共识交易（离线模式）

接口

**` Result createCancelDepositTransaction(Output output) `**

说明

> 离线组装退出委托共识交易
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
<tr>
<th align="center">参数</th>
<th align="center">类型</th>
<th align="center">是否必填</th>
<th align="center">说明</th>
</tr>
<tr>
<td align="center">output</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">参与共识时被锁定的那条output</td>
</tr>
</table>

*e.g 示例代码*

```java
//参与委托共识交易被打包后，有一条lockTime = -1的output记录，就是用户委托共识后被锁定的委托金额，
//将output作为参数，生成退出委托共识交易
Output output = new Output();
output.setTxHash("0020b0a75a26caad17b4ea6cec7f059ac0e426d71696a6096f75bb2e9f30c11c60d6");
output.setIndex(0);
output.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
output.setValue(20000000000000L);
output.setLockTime(-1);

Result result = NulsSDKTool.createCancelDepositTransaction(output);
Map<String, Object> map = (Map<String, Object>) result.getData();
String txHex = (String) map.get("value");
```

------



#### 4.4 组装删除共识节点交易（离线模式）

接口

**` Result createStopAgentTransaction(Output output) `**

说明

> 离线组装删除共识节点交易
>
> Result.data `String` 16进制后的交易序列化字符串

<table>
<tr>
<th align="center">参数</th>
<th align="center">类型</th>
<th align="center">是否必填</th>
<th align="center">说明</th>
</tr>
<tr>
<td align="center">output</td>
<td align="center">Object</td>
<td align="center">必填</td>
<td align="center">创建节点时被锁定的那条output</td>
</tr>
</table>

*e.g 示例代码*

```java
//创建节点交易被打包后，有一条lockTime = -1的output记录，就是用户创建节点被锁定的保证金，
//将output作为参数，生成退出委托共识交易
Output output = new Output();
output.setTxHash("0020b0a75a26caad17b4ea6cec7f059ac0e426d71696a6096f75bb2e9f30c11c60d6");
output.setIndex(0);
output.setValue(20000000000000L);
output.setAddress("Nse5x9foSzFjuwkwZLSvSjAHHLVf3MKJ");
output.setLockTime(-1);

Result result = NulsSDKTool.createStopAgentTransaction(output);
Map<String, Object> map = (Map<String, Object>) result.getData();
String txHex = (String) map.get("value");
```



#### 4.5 获取委托共识列表

接口

**` Result getDeposits(String address, int pageNumber, int pageSize) `**

说明

> 获取地址的委托共识列表
>
> Result.data List委托列表信息

<table>
<tr>
<th align="center">参数</th>
<th align="center">类型</th>
<th align="center">是否必填</th>
<th align="center">说明</th>
</tr>
<tr>
<td align="center">address</td>
<td align="center">String</td>
<td align="center">必填</td>
<td align="center">委托地址</td>
</tr>
<tr>
<td align="center">pageNumber</td>
<td align="center">Int</td>
<td align="center">必填</td>
<td align="center">页码</td>
</tr>
<tr>
<td align="center">pageSize</td>
<td align="center">Int</td>
<td align="center">必填</td>
<td align="center">每页显示条数，1-100之间</td>
</tr>
</table>

返回结果  

- 返回结果为`Result`对象，格式如下：

```json
{
  "success":true,
  "data":{
    "pageNumber":1,
    "pageSize":10,
    "total":1,
    "pages":1,
      "list":[
        {
        "deposit":20000010000000,
        "agentHash":"00207654b3371e9c99295a4b3d0371a6cfec48ee31684825dabc04dc5ea314da0a0c",
        "address":"NsdyD94pXWpxZudbtJ4zpkBHhh8XmBQA",
        "txHash":"002029411a2e6797e2d3bec54c48008f73275c8208a240b4835be534a137452bc29e",
        "blockHeight":98,
        "agentAddress":"NsdyD94pXWpxZudbtJ4zpkBHhh8XmBQA"
        }
      ]
  }
}
```

*e.g 示例代码*

```java
Result result = NulsSDKTool.getDeposits(address, 1, 10);
```

### 智能合约 ContractService

#### 5.1 创建智能合约交易 （离线模式 - 不计算手续费）

**`Result createContractTransaction(String sender, Long gasLimit, Long price, String contractCodeHex, Object[] args, String remark, List<Input> utxos)`**

说明

> 创建智能合约交易接口，不用计算手续费，需要传入合约创建者账户的未花费输出（如果合约创建者账户的未花费输出太多，则组装的未花费输出金额 > 合约执行消耗费用(gasLimit * price)(单位:Na) + 30000000(单位:Na)即可）
>
>
> Result.data 
>
>  - `String` 合约地址
>  - `String` 交易Hash
>  - `String` 16进制后的交易序列化字符串
>  - `List<Input>` 交易引用的未花费输出
>  - `List<Output>` 交易新生成的未花费输出
>
> 通过返回的`String` 16进制后的交易序列化字符串再去做离线交易签名
>
> 通过返回的`List<Input>` 交易引用的未花费输出和`List<Output>` 交易新生成的未花费输出来维护自己的数据库

注意

> 创建交易时组装的未花费输出还没有真正消耗掉，在`Result.data`中返回的`List<Input>`和`List<Output>`才是交易中消耗的和生成的未花费输出


|   参数    |  类型 |    是否必填    |    说明    |
| :-------: | :--------: | :-------------------: | :-------------------: |
|sender   |String           |必填|合约创建者账户的地址|
|gasLimit |Long           |必填|合约执行的gas限制|
|price   |Long             |必填|gas单价(单位:Na)|
|contractCodeHex   |String    |必填|合约代码的字节码（16进制字符串）|
|args   |Object[]           |非必填|创建合约的构造方法的参数（如果有）|
|remark   |String           |非必填|备注|
|utxos   |List&lt;Input&gt;|必填|合约创建者账户的未花费输出|


*e.g 示例代码*

```java
String sender = "2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5";
long gasLimit = 30000L;
Long price = 25L;
String contractCodeHex = "504b03040a00000800002082754d0000000000000000000000000a00040063726f...";
Object[] args = {"args1", "args2"};
String remark = "create contract demo";
List<Input> utxos = new ArrayList<>();

//组装合约创建者账户的未花费输出的utxos, 属性必填
Input utxo = new Input();
utxo.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo.setFromIndex(1);
utxo.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo.setValue(750000L);      
utxo.setLockTime(0);
utxos.add(utxo);

Input utxo1 = new Input();
utxo1.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo1.setFromIndex(2);
utxo1.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo1.setValue(40000000L);      
utxo1.setLockTime(0);
utxos.add(utxo1);

Result result = NulsSDKTool.createContractTransaction(sender, gasLimit, price, contractCodeHex, args, remark, utxos);
```

#### 5.2 创建调用智能合约的交易 （离线模式 - 不计算手续费）

**`Result callContractTransaction(String sender, Long value, Long gasLimit, Long price, String contractAddress, String methodName, String methodDesc, Object[] args, String remark, List<Input> utxos)`**

说明

> 创建调用智能合约交易接口，不用计算手续费，需要传入合约调用者账户的未花费输出（如果合约调用者账户的未花费输出太多，则组装的未花费输出金额 > `value`调用者向合约地址转入的NULS(单位:Na) + 合约执行消耗费用(gasLimit * price)(单位:Na) + 30000000(单位:Na)即可）
>
>
> Result.data 
>
>  - `String` 合约地址
>  - `String` 交易Hash
>  - `String` 16进制后的交易序列化字符串
>  - `List<Input>` 交易引用的未花费输出
>  - `List<Output>` 交易新生成的未花费输出
>
> 通过返回的`String` 16进制后的交易序列化字符串再去做离线交易签名
>
> 通过返回的`List<Input>` 交易引用的未花费输出和`List<Output>` 交易新生成的未花费输出来维护自己的数据库

注意

> 创建交易时组装的未花费输出还没有真正消耗掉，在`Result.data`中返回的`List<Input>`和`List<Output>`才是交易中消耗的和生成的未花费输出

|   参数    |  类型 |    是否必填    |    说明    |
| :-------: | :--------: | :-------------------: | :-------------------: |
|sender   |String           |必填|合约调用者账户的地址|
|value |Long           |必填|合约调用者向合约地址转入的NULS(单位:Na)|
|gasLimit |Long           |必填|合约执行的gas限制|
|price   |Long             |必填|gas单价(单位:Na)|
|contractAddress   |String    |必填|合约地址|
| methodName   |String    |必填|合约方法|
| methodDesc   |String    |非必填|合约方法描述(如果合约内有重名方法则必填)|
|args   |Object[]           |非必填|调用合约方法的参数（如果有）|
|remark   |String           |非必填|备注|
|utxos   |List&lt;Input&gt;|必填|合约调用者账户的未花费输出|


*e.g 示例代码*

```java
String sender = "2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5";
Long value = 100000000L;
Long gasLimit = 30000L;
Long price = 25L;
String contractAddress = "2CXJEuoXZMajeTEgL6TgiSxTRRMwiMM";
String methodName = "testMethod";
String methodDesc = "";
Object[] args = {"args1", "args2"};
String remark = "call contract demo";
List<Input> utxos = new ArrayList<>();

//组装合约调用者账户的未花费输出的utxos, 属性必填
Input utxo = new Input();
utxo.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo.setFromIndex(1);
utxo.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo.setValue(750000L);      
utxo.setLockTime(0);
utxos.add(utxo);

Input utxo1 = new Input();
utxo1.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo1.setFromIndex(2);
utxo1.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo1.setValue(140000000L);      
utxo1.setLockTime(0);
utxos.add(utxo1);

Result result = NulsSDKTool.callContractTransaction(sender, value, gasLimit, price, contractAddress, methodName, methodDesc, args, remark, utxos);
```

#### 5.3 创建删除智能合约的交易 （离线模式 - 不计算手续费）

**`Result deleteContractTransaction(String sender, String contractAddress, String remark, List<Input> utxos)`**

说明

> 创建删除智能合约交易接口，不用计算手续费，需要传入合约删除者账户的未花费输出（如果合约删除者账户的未花费输出太多，则组装的未花费输出金额 > 30000000(单位:Na)即可）
>
>
> Result.data 
>
>  - `String` 合约地址
>  - `String` 交易Hash
>  - `String` 16进制后的交易序列化字符串
>  - `List<Input>` 交易引用的未花费输出
>  - `List<Output>` 交易新生成的未花费输出
>
> 通过返回的`String` 16进制后的交易序列化字符串再去做离线交易签名
>
> 通过返回的`List<Input>` 交易引用的未花费输出和`List<Output>` 交易新生成的未花费输出来维护自己的数据库

注意

> 创建交易时组装的未花费输出还没有真正消耗掉，在`Result.data`中返回的`List<Input>`和`List<Output>`才是交易中消耗的和生成的未花费输出


|   参数    |  类型 |    是否必填    |    说明    |
| :-------: | :--------: | :-------------------: | :-------------------: |
|sender   |String           |必填|合约删除者账户的地址|
|contractAddress   |String    |必填|合约地址|
|remark   |String           |非必填|备注|
|utxos   |List&lt;Input&gt;|必填|合约删除者账户的未花费输出|


*e.g 示例代码*

```java
String sender = "2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5";
String contractAddress = "2CXJEuoXZMajeTEgL6TgiSxTRRMwiMM";
String remark = "delete contract demo";
List<Input> utxos = new ArrayList<>();

//组装合约删除者账户的未花费输出的utxos, 属性必填
Input utxo = new Input();
utxo.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
utxo.setFromIndex(1);
utxo.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
utxo.setValue(40000000L);      
utxo.setLockTime(0);
utxos.add(utxo);

Result result = NulsSDKTool.deleteContractTransaction(sender, contractAddress, remark, utxos);
```

*e.g 示例代码*

```java
String from = "2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5";
String to = "2CjPVMKST7h4Q5Dqa8Q9P9CwYSmN7mG";
long amount = 2000000000L;
String remark = "create transaction demo";
List<Input> inputs = new ArrayList<>();

//组装转出者账户的未花费输出的inputs, 属性必填
Input input = new Input();
input.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
input.setFromIndex(1);
input.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
input.setValue(1000000000L);      
input.setLockTime(0);
inputs.add(input);

Input input = new Input();
input.setFromHash("002023c66d10cf9047dbcca12aee3db3c921a2ec22e0dd63331cb85"); 
input.setFromIndex(2);
input.setAddress("2ejPVMKST7h4Qsd5Dqa8Q9Psr47mj5")
input.setValue(1040000000L);      
input.setLockTime(0);
inputs.add(input);

Result result = NulsSDKTool.createTransaction(from, to, amount, remark, inputs);
```



### 附录

#### 接口返回Result示例
##### Result

- 表示接口访问正常，业务返回具体的数据

```json
{	
    "success": true,//接口执行的正确性
    "data": data
}
```
- 表示接口访问正常，业务返回true

```json
{	//表示接口访问正常，业务返回true
    "success": true,//接口执行的正确性
    "data": {
		"value":true //接口业务功能的返回结果
    }
}
```
- 表示接口访问正常，业务返回false

```json
{	
    "success": true, //接口执行的正确性
     "data"：{
        "value": false //接口业务功能的返回结果
     }
}
```
- 返回错误的情况,例如参数错误，异常等

```json
{	//表示错误的情况
    "success": false,
     "data"：{
        "code": "ACT005",
        "msg": "xxxxxx...."
     }
}
```

- 这是创建一个带密码的离线账户的完整返回结果

```json
{
    "success": true,
    //data为List<account>
    "data": {
    	"list":[
        	{
	           "address": "2CacFwqMwcJiGNNBwiwV7bCL7bjwNBr",
	            "alias": null,
	            "pubKey": "030d4e752b5aa5d784f19a1fcc73b02afb6f756752fd00ebc2fcaabc8d0979c4f0",
	            "extend": null,
	            "createTime": 1529041525794,
	            "encrypted": false,
	            "priKey": "00e4bfd347351ea899b5f0ae2c0a3e7a6951b202eaf72432d1a63a2dc85c59c82a",
	            "encryptedPriKey": ""
	    	}
	    ]
    }
}
```
---

##### <a name="Account"> Account </a>
```json
{
	"address": String, 账户地址
	"alias": String, 账户别名
	"pubKey": String, 公钥
	"extend": String, 扩展
	"createTime": Long, 创建时间
	"encrypted": boolean, 是否加密
	"priKey": String, 私钥（创建无密码离线账户时才会有值）
	"encryptedPriKey": String, 加密私钥（创建有密码离线账户时才会有值）
}
```
---

##### <a name="Input"> Input </a>
```json
 {
	"fromHash": String, 来源output的txHash         
	"fromIndex": Integer, 来源output的outIndex
	"address": String, 转入地址                   
	"value": Long 转入金额
    "lockTime": Long 锁定时间
}
```
---

##### <a name="Output"> Output </a>
```json
 {
	"txHash": String, 交易hash
	"index": Integer, 索引
	"address": String, 地址
	"value": Long, 数量
	"lockTime": Long, 锁定时间
	"status": Integer 状态 0:usable(未花费), 1:timeLock(高度锁定), 2:consensusLock(参与共识锁定), 3:spent(已花费)
}
```
---

##### <a name="Transaction"> Transaction </a>

```json
{
	"hash": String, 交易的hash值
	"type": Integer, 交易类型
	"time": Long, 交易发起时间
	"blockHeight": Long, 区块高度
	"fee": Long, 交易手续费
	"value": Long, 交易金额
	"remark": String, 备注
	"scriptSig": String, 签名
	"status": Integer, 交易状态 0:unConfirm(待确认), 1:confirm(已确认)
	"confirmCount": Long, 确认次数
	"size": int, 大小
	"inputs": [
		{
			"fromHash": String, 来源output的txHash
			"fromIndex": Integer, 来源output的outIndex
			"address": String, 转入地址
			"value": Long 转入金额
		}
		],
	"outputs": [
		{
			"txHash": String, 交易hash
			"index": Integer, 索引
			"address": String, 地址
			"value": Long, 数量
			"lockTime": Long, 锁定时间
			"status": Integer 状态 0:usable(未花费), 1:timeLock(高度锁定), 2:consensusLock(参与共识锁定), 3:spent(已花费)
		}
	]
}
```
---

##### <a name="BalanceInfo"> BalanceInfo </a>
```json
{
	"balance": long, 余额
	"usable": long, 可用余额
	locked": long 锁定余额
}
```
---

##### <a name="BlockHeader"> BlockHeader</a>

```json
{
	"hash": String, 区块的hash值
	"preHash": String, 上一个区块的hash值
	"merkleHash": String, 梅克尔hash
	"time": Long, 区块生成时间
	"height": Long, 区块高度
	"txCount": Long, 区块打包交易数量
	"packingAddress": String, 打包地址
	"scriptSig": String, 签名
	"roundIndex": Long, 共识轮次
	"consensusMemberCount": Integer, 参与共识成员数量
	"roundStartTime": Long, 当前共识轮开始时间
	"packingIndexOfRound": Integer, 当前轮次打包出块的名次
	"confirmCount": Long, 确认次数
	"reward": Long, 共识奖励
	"fee": Long, 获取的打包手续费
	"size": int, 大小
}
```
---

##### <a name="Block"> Block</a>

```json
{
	"hash": String, 区块的hash值
	"preHash": String, 上一个区块的hash值
	"merkleHash": String, 梅克尔hash
	"time": Long, 区块生成时间
	"height": Long, 区块高度
	"txCount": Long, 区块打包交易数量
	"packingAddress": String, 打包地址
	"scriptSig": String, 签名
	"roundIndex": Long, 共识轮次
	"consensusMemberCount": Integer, 参与共识成员数量
	"roundStartTime": Long, 当前共识轮开始时间
	"packingIndexOfRound": Integer, 当前轮次打包出块的名次
	"confirmCount": Long, 确认次数
	"reward": Long, 共识奖励
	"fee": Long, 获取的打包手续费
	"size": int, 大小
	"txList": [
		{
			"hash": String, 交易的hash值
			"type": Integer, 交易类型
			"time": Long, 交易发起时间
			"blockHeight": Long, 区块高度
			"fee": Long, 交易手续费
			"value": Long, 交易金额
			"remark": String, 备注
			"scriptSig": String, 签名
			"status": Integer, 交易状态 0:unConfirm(待确认), 1:confirm(已确认)
			"confirmCount": Long, 确认次数
			"size": int, 大小
			"inputs": [ 输入
				{
					"fromHash": String, 来源output的txHash
					"fromIndex": Integer, 来源output的outIndex
					"address": String, 转入地址
					"value": Long 转入金额
				}
			],
			"outputs": [ 输出
				{
					"address": String, 地址
					"value": Long, 数量
					"lockTime": Long, 锁定时间
				}
			]
		}
	]
}
```
---

#### 错误码
```java
10000=成功
10001=失败
10002=未知错误
10003=数据解析错误
10004=线程名称重复
10005=语言类型不能设置为空
10006=流读写错误
10007=数据大小错误
10008=配置错误
10009=签名错误
10010=服务器拒绝访问
10011=交易数据过大, 建议单笔最大交易金额：
10012=参数错误
10013=参数不能为空
10014=数据错误
10015=数据不存在
10016=下载新版本数据错误
10017=转换JSON数据错误
10018=文件操作错误
10019=类安全权限异常
10020=实例化错误
10021=升级中
10022=不在升级中
10023=不是最新的版本
10024=序列化错误
10025=反序列化错误
10026=散列值错误
10027=余额不足
10028=黑洞地址
10029=地址不属于主链
10030=验证器没有完全执行
10031=区块数据不存在
10032=版本过低
10033=签名公钥不能重复
10034=多签账户使用的UTXO必须为脚本类型
10035=已是最新版本
10036=等待同步区块完成
10037=等待分叉处理完成

20000=数据存储模块启动失败
20001=数据存储异常
20002=会话未初始化
20003=不能保存空数据
20004=批量保存数量超出限制
20005=数据不正确
20006=保存失败
20007=更新失败
20008=数据存储回滚失败
20009=数据库区域已存在
20010=数据库区域不存在
20011=创建数据库区域超出限制
20012=创建数据库区域异常
20013=创建数据库区域路径异常
20014=删除数据库区域异常
20015=数据库批量操作已关闭

30001=区块头标记检查失败
30002=区块头字段检查失败
30003=区块字段检查失败
30004=区块过大
30005=梅克尔散列是错误的

31001=UTXO不可花费
31002=UTXO状态错误
31004=交易输入无效
31005=交易输出无效
31006=孤儿交易
31007=孤儿块
31008=交易数据验证错误
31009=手续费不正确
31010=回滚交易失败
31011=重复交易
31012=转账金额太小
31013=交易过大
31014=保存交易失败
31015=交易不存在
31016=交易CoinData不存在
31017=交易类型错误
31018=无效交易或在当前版本不可用

40001=网络服务启动失败
40002=网络消息错误
40003=网络消息异或错误
40004=网络消息长度错误
40006=网络节点组已存在
40007=网络节点区域已存在
40008=网络节点组不存在
40009=网络节点区域不存在
40010=网络节点不存在
40011=消息广播失败
40012=没有网络节点可广播
40013=网络节点已销毁
40014=网络节点通道已销毁

50000=密码错误
50001=账户不存在
50002=账户已经加密
50003=账户已存在
50004=地址错误
50005=别名已存在
50006=别名不存在
50007=账户已经设置过别名
50008=账户还没加密
50009=多个别名交易设置了相同的别名
50010=存在已加密账户
50012=存在未加密账户
50013=删除别名错误
50014=AccountKeystore文件不存在
50015=AccountKeystore文件被损坏
50016=别名格式错误
50017=密码格式错误
50018=解密账户错误
50019=账户被加密且锁定
50020=私钥错误
50021=输入小于输出
50022=需要烧毁一个NULS
50023=签名人数(m)过大
50024=签名地址与该多签账户不匹配

60001=未知的消息类型

70001=超时
70002=保证金不正确
70003=保证金不足
70004=共识会议中异常
70005=佣金超出范围
70006=信用不足
70007=超出可委托数量
70008=超出委托金额
70009=共识节点已停止
70010=委托已经被取消
70011=委托还没有被取消
70012=更新委托失败
70014=更新共识节点失败
70015=未达到停止共识节点锁定时间
70016=共识节点不存在
70017=共识节点已存在
70018=共识节点被惩罚
70019=分叉
70020=黄牌惩罚交易错误
70021=共识种子节点
70022=没有双花
70023=红牌验证未通过
70024=共识节点出块地址已被使用
70025=共识节点地址和节点出块地址不能相同
70026=共识节点奖励地址和节点出块地址不能相同

80001=获取P2PKH签名脚本错误

100001=合约执行错误
100002=合约地址不存在
100003=创建合约交易错误
100004=非法的合约地址
100005=非合约交易
100006=非合约交易的资金不能转入合约地址
100007=名称格式错误
100008=非NRC20合约
100009=非视图方法
100010=非法的合约代码
100011=代币名称重复
100012=符号的格式错误
100013=合约锁定中
100014=decimals的最大值是18
100015=totalSupply的最大值是 2^256 - 1
100016=price的最小值是25
100017=余额不为0时不能删除合约
100018=非合约创建者不能删除合约
100019=合约已经被删除
100020=超过了合约调用的最大GAS限制
```