package com.shirlman.yiplayer.models;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by KB-Server on 2016/7/24.
 */
@Root(name = "dict", strict = false)
public class ICibaResponse {
    @Attribute(name = "num", required = false)
    private String num;
    @Attribute(name = "id", required = false)
    private String id;
    @Attribute(name = "name", required = false)
    private String name;
    @Element(name = "key")
    private String key;
    @ElementList(inline = true, entry="ps", required = false)
    private List<String> ps;
    @ElementList(inline = true, entry="pron", required = false)
    private List<String> pron;
    @ElementList(inline = true, entry="pos", required = false)
    private List<String> pos;
    @ElementList(inline = true, entry="acceptation", required = false)
    private List<String> acceptation;
    /**
     * orig : In ` I wish you were here', ` were'is in the subjunctive.
     * trans : 在Iwishyou were here一 句中, were 表达的是虚拟语气.
     */
    @ElementList(inline = true, entry="sent")
    private List<SentBean> sent;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public List<String> getPs() {
        return ps;
    }

    public void setPs(List<String> ps) {
        this.ps = ps;
    }

    public List<String> getPron() {
        return pron;
    }

    public void setPron(List<String> pron) {
        this.pron = pron;
    }

    public List<String> getPos() {
        return pos;
    }

    public void setPos(List<String> pos) {
        this.pos = pos;
    }

    public List<String> getAcceptation() {
        return acceptation;
    }

    public void setAcceptation(List<String> acceptation) {
        this.acceptation = acceptation;
    }

    public List<SentBean> getSent() {
        return sent;
    }

    public void setSent(List<SentBean> sent) {
        this.sent = sent;
    }

    @Element(name = "sent")
    public static class SentBean {
        @Element(name = "orig")
        private String orig;
        @Element(name = "trans")
        private String trans;

        public String getOrig() {
            return orig;
        }

        public void setOrig(String orig) {
            this.orig = orig;
        }

        public String getTrans() {
            return trans;
        }

        public void setTrans(String trans) {
            this.trans = trans;
        }
    }
}
