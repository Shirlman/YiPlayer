package com.shirlman.yiplayer.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by KB-Server on 2016/7/24.
 */
public class YoudaoResponse {

    /**
     * us-phonetic : 'pɝsənl
     * phonetic : 'pɜːs(ə)n(ə)l
     * uk-phonetic : 'pɜːs(ə)n(ə)l
     * explains : ["n. 人事消息栏；人称代名词","adj. 个人的；身体的；亲自的"]
     */

    private BasicBean basic;
    /**
     * translation : ["个人"]
     * basic : {"us-phonetic":"'pɝsənl","phonetic":"'pɜːs(ə)n(ə)l","uk-phonetic":"'pɜːs(ə)n(ə)l","explains":["n. 人事消息栏；人称代名词","adj. 个人的；身体的；亲自的"]}
     * query : personal
     * errorCode : 0
     * web : [{"value":["个人的","私人的","人身"],"key":"personal"},{"value":["个人所得","个人收入","个人所得"],"key":"Personal income"},{"value":["个人电脑","个人计算机","人计算机"],"key":"personal computer"}]
     */

    private String query;
    private int errorCode;
    private List<String> translation;
    /**
     * value : ["个人的","私人的","人身"]
     * key : personal
     */

    private List<WebBean> web;

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public List<WebBean> getWeb() {
        return web;
    }

    public void setWeb(List<WebBean> web) {
        this.web = web;
    }

    public static class BasicBean {
        private List<String> explains;
        @SerializedName("us-phonetic")
        private String us_phonetic;
        private String phonetic;
        @SerializedName("uk-phonetic")
        private String uk_phonetic;

        public List<String> getExplains() {
            return explains;
        }

        public void setExplains(List<String> explains) {
            this.explains = explains;
        }

        public String getUs_phonetic() {
            return us_phonetic;
        }

        public void setUs_phonetic(String us_phonetic) {
            this.us_phonetic = us_phonetic;
        }

        public String getPhonetic() {
            return phonetic;
        }

        public void setPhonetic(String phonetic) {
            this.phonetic = phonetic;
        }

        public String getUk_phonetic() {
            return uk_phonetic;
        }

        public void setUk_phonetic(String uk_phonetic) {
            this.uk_phonetic = uk_phonetic;
        }
    }

    public static class WebBean {
        private String key;
        private List<String> value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public List<String> getValue() {
            return value;
        }

        public void setValue(List<String> value) {
            this.value = value;
        }
    }
}
