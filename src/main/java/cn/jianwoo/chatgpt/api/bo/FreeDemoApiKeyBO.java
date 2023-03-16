package cn.jianwoo.chatgpt.api.bo;

import java.util.List;

import org.springframework.util.CollectionUtils;

import cn.jianwoo.chatgpt.api.base.BaseAuthReqBO;
import lombok.Data;
import lombok.ToString;

/**
 * @author gulihua
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 */
@Data
@ToString
public class FreeDemoApiKeyBO extends BaseAuthReqBO
{

    private static final long serialVersionUID = -6815107739882680259L;
    private List<String> apiKeyList;
    private String apiKey;
    private Integer index;

    public void addKey(String key)
    {
        this.apiKeyList.add(key);
    }


    public void delKey(String key)
    {
        this.apiKeyList.remove(key);
    }


    public void addKeyList(List<String> keyList)
    {
        this.apiKeyList.addAll(keyList);
    }


    public void delKeyList(List<String> keyList)
    {
        this.apiKeyList.removeAll(keyList);
    }


    public String getNextApiKey()
    {
        if (CollectionUtils.isEmpty(apiKeyList))
        {
            return null;
        }
        this.index = index + 1;
        if (this.index >= apiKeyList.size())
        {
            this.index = 0;
        }
        this.apiKey = apiKeyList.get(this.index);
        return this.apiKey;
    }
}
