# ChatGPT微信小程序的后端 Java Api
<p align=center>
  <img src="logo.png" alt="logo.png" style="zoom: 25%;" />
</p>

<p align=center>
   基于ChatGPT 3.5 + Java 8 + springboot 2.4.1 的后端 API 程序，扩展用于AI 聊天等。
支持异步流式响应 + websocket通信。觉得不错请右上角Star。
</p>
<p align="center">
    	<img src="https://img.shields.io/hexpm/l/plug.svg" ></img>
		<img src="https://img.shields.io/badge/JDK-1.8+-green.svg" ></img>
        <img src="https://img.shields.io/badge/springboot-2.4.1.RELEASE-green" ></img>
        <img src="https://img.shields.io/badge/hutools--all-5.4.3-brightgreen" ></img>
        <img src="https://img.shields.io/badge/okhttp--sse-3.14.9-orange" ></img>
        <img src="https://img.shields.io/badge/openAiAuth-1.0.7-yellow" ></img>



## 项目介绍

1. 本项目是基于springBoot开发，采用 stream + websocket 与小程序进行通信(微信小程序不支持SSE)，异步响应无需等待! 支持记住上下文的连续对话，全AI对话和AI生成图片。
其中上下文的连续对话，全AI对话要配合[前端小程序](https://github.com/gulihua10010/wxmini-chatgpt)使用。
可以通过账号密码、token(感谢[acheong08](https://github.com/acheong08/ChatGPT)提供的代理) 方式，以及 API-Key 方式登录，其中**账号密码、token方式**有被<span style='color:red'>**永久封禁账号**</span>的风险，不建议使用!!!
2. 本项目实现了百姓网提供的api接口方式的调用，需要申请，点击跳转[申请地址](https://jinshuju.net/f/gzUO2t),大概两三天能收到短信回复。
3. 防止有人恶意请求 api 和 socket，本项目设置了速度限制，api默认5个请求/秒/IP ,websocket默认 20个请求/分钟/IP。
4. 未登录用户每天默认可以有20次的提问机会。为了防止api.openai.com的请求速率限制，通过使用一些免费的chatgpt帐户(有18$的额度)来实现，每请求一次就切换到下一个账户。
5. AI生成的图片是在一个*.blob.core.windows.net的国外域名， 由于微信小程序不支持未备案的域名，目前暂时的方法是先保存到本地，有效期是 1 天，然后通过每天凌晨的自动任务进行删除前天生成的图片。 

## 运行配置

最低配置：1核2G 

推荐配置：2核4G 【[狂欢特惠](https://curl.qcloud.com/TYzPgyNC)】【博主目前配置】

最近，腾讯云和阿里云的优惠力度非常大，如果有需求的小伙伴，可以了解一下~

> 【阿里云】云服务器狂欢特惠，爆款云服务器 **s6 2核4G** 低至**0.46/天**，具体规则查看活动详情 [点我传送](https://www.aliyun.com/minisite/goods?userCode=9h1yheau)
>
> 【腾讯云】云产品限时秒杀，爆款 **2核2G4M** 云服务器，免费续3个月/送同配置3个月，仅需**10.67元/月**【**博主强烈推荐**】[点我传送](https://cloud.tencent.com/act/pro/cps_3?fromSource=gwzcw.6688284.6688284.6688284&utm_medium=cps&utm_id=gwzcw.6688284.6688284.6688284&cps_key=9104b24ca77f9c56518fcd93f67e15a1)


## 项目特点

- 友好的代码结构及注释，便于阅读及二次开发
- 采用**Stream**+**Websocket**和小程序/客户端进行通信，实时响应!
- 采用枚举+多实现的服务调用，便于扩展其他的 api
- 采用 **AOP** + 自定义注解 + **Cache** 实现限制IP接口访问次数

## 项目地址

目前项目托管在 **Gitee** 和 **Github** 平台上中，欢迎大家 **Star** 和 **Fork** 支持~

- Gitee地址：https://gitee.com/gulihua/chatGptApi
- Github地址：https://github.com/gulihua10010/chatGptApi

## 其他Chatgpt相关项目
- 基于uni-app构建的微信小程序:https://github.com/gulihua10010/wxmini-chatgpt
- openAi授权SDK:https://github.com/gulihua10010/openAiAuth
- chatGpt API SDK:https://github.com/gulihua10010/chatGptApiSdk
- chatGpt命令行客户端:https://github.com/gulihua10010/ChatGpt
          
### 后端技术

|       技术       |       说明        |                             官网                             |
|:--------------:|:---------------:| :----------------------------------------------------------: |
|   SpringBoot   |      MVC框架      | [ https://spring.io/projects/spring-boot](https://spring.io/projects/spring-boot) |
| SpringSecurity |     认证和授权框架     |          https://spring.io/projects/spring-security          |
|    logBack     |      日志框架       |                   https://logback.qos.ch/                    |
|     Lombok     |    简化对象封装工具     | [ https://github.com/rzwitserloot/lombok](https://github.com/rzwitserloot/lombok) |
|     Nginx      | HTTP和反向代理web服务器 |                      http://nginx.org/                       |
|     Hutool     |    Java工具包类库    |                  https://hutool.cn/docs/#/                   |
|     okhttp     |    Http 客户端     |              https://github.com/square/okhttp                |
|   websocket    |  web socket 通信  |             http://www.websocket.org/             |

## 快速开始

### 【推荐】一键部署博客系统

#### 部署前必看
1. 需要吧[AuthApiController.java](src%2Fmain%2Fjava%2Fcn%2Fjianwoo%2Fchatgpt%2Fapi%2Fcontroller%2FAuthApiController.java)里的``/api1``改为``/api``
2. 根据[application-test.properties](src%2Fmain%2Fresources%2Fapplication-test.properties)配置，自建``dev`` 和``prod``  环境的配置，由于涉及到apikey等信息，把它给隐藏了，或者在[pom.xml](pom.xml)里只保留``test``环境
3. 项目里面的像 apiKey 之类的关键参数都是可以通过接口动态配置的，你可以自建一个[CacheController.java](src%2Fmain%2Fjava%2Fcn%2Fjianwoo%2Fchatgpt%2Fapi%2Fcontroller%2FCacheController.java)，然后可以更新缓存里面的配置，当然接口地址必须是隐藏的。
```bash
nohup java -jar /www/application/chatgpt-1.0.0.jar --spring.profiles.active=prod -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 > /dev/null 2>&1
```

## 介绍
ChatGPT 是由 OpenAI 开发的世界知名的对话式 AI 模型，以对各种提示和查询生成类似人类的响应而闻名。凭借其尖端功能，ChatGPT 是聊天机器人、虚拟助手和其他自然语言处理应用程序的宝贵资产。
ChatGPT API 是一个强大的工具，允许开发人员将 ChatGPT 模型集成到他们自己的应用程序中。但是，要使用此 API，用户需要拥有 OpenAI API 密钥并支付使用费。

## Open ChatGPT 倡议
ChatGPT代表了人工智能和人类科技发展的新高度。它彻底改变了我们获取知识的方式，提升了许多人的写作和表达能力。凭借其强大的语言理解和文本生成能力，ChatGPT 已成为人类生产力和创造力的工具。

然而，当我与普通大众分享 ChatGPT 时，那些没有 AI 背景的人，我收到的回应不仅仅是赞美和惊奇，还有怀疑和怀疑。

一些人声称之前见过很多聊天机器人，而 ChatGPT 只是众多聊天机器人中的一个。过去的聊天机器人无法理解人类表达背后的意图。但是 ChatGPT 不像他们。其强大的语言理解和生成能力使其成为未来不可或缺的工具。

其他人则认为人工智能无法超越人类智慧，它只是从人类创造的数据中学习。不管真相如何，人工智能并不需要超越全人类的创造力来影响我们的日常生活。事实上，只要人工智能超越了每个个体的能力，它就能更好地协助人们，为我们的工作和生活方式带来变革性的影响。

还有一些人声称 ChatGPT 会产生错误的内容，捏造虚假信息，或者进行看似合乎逻辑但错误的推理，我们需要停止使用它。但这样做就像把婴儿连同洗澡水一起倒掉。ChatGPT 确实很有用，因为它简化了查找信息的过程，因为事实核查总是比从头开始搜索信息更容易。而且，随着人工智能技术的应用越来越广泛，我们应该熟悉ChatGPT等人工智能工具，了解它们在哪些方面容易出错，而不是简单地回避，更好地避免它们。

还有一些人认为，使用 ChatGPT 自动获取答案会阻碍批判性思维和解决问题能力的发展。然而，使用 ChatGPT 并不意味着将人类的思想委托给机器或停止人类的思考。ChatGPT 无法自动提供深刻的见解或敏锐的想法。事实上，对于不常见的问题，ChatGPT 只能提供笼统而陈腐的意见。关键是用户提供意见，ChatGPT 创建连贯的文章。

另一种说法认为，ChatGPT 创建的内容是没有灵魂的，因为它在创建时缺乏意识。然而，这个论点的正确性并不重要。最重要的一点是，未来人工智能技术无疑会越来越先进。想象一下二十年后的世界，人工智能将成为日常生活中无处不在的一部分。回顾 ChatGPT，人们可能会有不同的感受。我们现在必须熟悉人工智能技术，因为它越来越深入地融入我们的日常生活。

现在，让我们仔细看看 ChatGPT 背后的技术：自然语言处理。即使在 NLP 领域本身，许多人还没有意识到 ChatGPT 的颠覆性影响。

自然语言处理研究方法论的演变，从基于规则的方法到机器学习再到深度学习，证明了人类语言的复杂性。复杂性决定了自然语言处理不是写几条规则那么简单。它需要不断的改进和探索。现在，共指消解、依存分析和词性标注都被自然语言处理抛弃了。以后连命名实体识别、语法纠错都会显得滑稽可笑。随着 ChatGPT 彻底改变了 NLP 领域，NLP 研究人员必须积极熟悉这项尖端技术以迎接这个新时代。


## 环境搭建

### 开发工具

|       工具        |       说明        |                             官网                             |
|:---------------:| :---------------: | :----------------------------------------------------------: |
|      IDEA       |    Java开发IDE    |           https://www.jetbrains.com/idea/download            |
|     Postman     |    前端开发IDE    |           https://www.postman.com/             |

### 开发环境

| 工具  | 版本号 |                             下载                             |
| :---: | :----: | :----------------------------------------------------------: |
|  JDK  |  1.8   | https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html |
| Maven | 3.3.0+ |                   http://maven.apache.org/                   |
| Nginx |  1.10  |              http://nginx.org/en/download.html               |



### 部署平台

|   平台   |           说明           |               官网               |
| :------: | :----------------------: | :------------------------------: |
| 宝塔面板 | 安全高效的服务器运维面板 | https://www.bt.cn/new/index.html |

## 贡献代码

开源项目离不开大家的支持，如果您有好的想法，遇到一些 **BUG** 并修复了，欢迎小伙伴们提交 **Pull Request** 参与开源贡献

1. **fork** 本项目到自己的 **repo**
2. 把 **fork** 过去的项目也就是你仓库中的项目 **clone** 到你的本地
3. 修改代码
4. **commit** 后 **push** 到自己的库
5. 发起**PR**（ **pull request**） 请求，提交到  **Nacos** 分支
6. 等待作者合并



## 隐私声明
1. 本项目高度重视隐私，致力于保护用户的隐私。服务器端的程序不会对账号密码、accessToken、 API-Key、用户信息等做任何存储和日志记录，这点可以放心使用。
开发者高度优先考虑您的API密钥/Access Token/账号信息的安全，并非常小心地处理它。您的密钥将专门存储在您的小程序/浏览器中，并且永远不会与任何第三方实体共享。它仅用于授权的预期用途，而不是用于任何其他未经授权的用途。 该项目的源代码可供检查以验证此声明。如何您不信任本项目,可以使用API-Key的方式，用完可以随时在官网删除相关API-Key。
2. 但是，OpenAI API 会根据其[数据使用政策](https://openai.com/policies/privacy-policy)将数据保留 30 天。

## 免责声明
1. 本程序仅供娱乐, 且仅用于测试和学习研究！禁止将源码用于商业用途，不能保证其合法性，准确性，完整性和有效性，请根据情况自行判断。
2. 开发者对任何用户操作所导致的问题概不负责，包括但不限于由任何脚本错误导致的任何损失或损害。
3. 请勿将本项目的任何内容用于商业或非法目的，否则后果自负。
4. 以任何方式查看此网站的使用者都应仔细阅读此声明。作者保留随时更改或补充此免责声明的权利。
一旦使用并操作(包括但不限于测试，部署，运行于本地或服务器)了本程序，则视为您已接受此免责声明。

## 开源协议

[Apache License 2.0](https://www.apache.org/licenses/LICENSE-2.0.html)

## 赞赏

**服务器**和**域名**等服务的购买和续费都会**产生一定的费用**，为了**维持项目的正常运作**，如果觉得本项目**对您有帮助**的话，欢迎朋友能够给予一些支持

