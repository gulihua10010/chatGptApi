package cn.jianwoo.chatgpt.api.constants;

public enum ChatGptServiceBean {

    /** API Key 方式 **/
    API_KEY("api", "chatGptApiService"),
    /** Access Key 方式 **/
    ACCESS_TOKEN("token", "chatGptTokenService"),
    BAIXING("baixing", "baixingGptApiService"),
    DEMO("demo", "demoFreeGptApiService"),
    ;

    private String name;
    private String bean;

    public String getName()
    {
        return this.name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getBean()
    {
        return this.bean;
    }


    public void setBean(String bean)
    {
        this.bean = bean;
    }


    public static String get(String name)
    {
        for (ChatGptServiceBean bean : ChatGptServiceBean.values())
        {
            if (bean.name.equals(name))
            {
                return bean.getBean();
            }
        }
        return ACCESS_TOKEN.getBean();
    }


    ChatGptServiceBean(String name, String bean)
    {
        this.name = name;
        this.bean = bean;
    }
}
