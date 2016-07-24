package com.shirlman.yiplayer.models;

import java.util.List;

/**
 * Created by KB-Server on 2016/7/24.
 */
public class ShooterSubtitleDetailResponse {

    /**
     * status : 0
     * sub : {"result":"succeed","action":"detail","subs":[{"filename":"S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.zip","native_name":"FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集","size":18258,"producer":{"producer":"@张纳德nerd","verifier":"","source":"原创翻译","uploader":""},"down_count":408,"view_count":512,"subtype":"Subrip(srt)","filelist":[{"url":"http://file1.assrt.net/onthefly/239398/-/1/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.ass?_=1469341998&-=f127379a7e4bd5508c6f73c24285ba1c&api=1","f":"S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.ass","s":"58KB"}],"videoname":"FRIENDS Bluray 720p S01E02 蓝光/bluray","upload_time":"2013-07-16 01:44:12","vote_score":0,"url":"http://file1.assrt.net/download/239398/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.zip?_=1469341998&-=7adf1246aa4d9d9d47c1771df53af8b4&api=1","title":"FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集/FRIENDS Bluray 720p S01E02 蓝光/bluray/S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End","id":239398,"lang":{"desc":"简 双语 其他语言","langlist":{"langdou":true,"langchs":true,"langoth":true}}}]}
     */

    private int status;
    /**
     * result : succeed
     * action : detail
     * subs : [{"filename":"S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.zip","native_name":"FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集","size":18258,"producer":{"producer":"@张纳德nerd","verifier":"","source":"原创翻译","uploader":""},"down_count":408,"view_count":512,"subtype":"Subrip(srt)","filelist":[{"url":"http://file1.assrt.net/onthefly/239398/-/1/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.ass?_=1469341998&-=f127379a7e4bd5508c6f73c24285ba1c&api=1","f":"S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.ass","s":"58KB"}],"videoname":"FRIENDS Bluray 720p S01E02 蓝光/bluray","upload_time":"2013-07-16 01:44:12","vote_score":0,"url":"http://file1.assrt.net/download/239398/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.zip?_=1469341998&-=7adf1246aa4d9d9d47c1771df53af8b4&api=1","title":"FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集/FRIENDS Bluray 720p S01E02 蓝光/bluray/S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End","id":239398,"lang":{"desc":"简 双语 其他语言","langlist":{"langdou":true,"langchs":true,"langoth":true}}}]
     */

    private SubBean sub;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public SubBean getSub() {
        return sub;
    }

    public void setSub(SubBean sub) {
        this.sub = sub;
    }

    public static class SubBean {
        private String result;
        private String action;
        /**
         * filename : S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.zip
         * native_name : FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集
         * size : 18258
         * producer : {"producer":"@张纳德nerd","verifier":"","source":"原创翻译","uploader":""}
         * down_count : 408
         * view_count : 512
         * subtype : Subrip(srt)
         * filelist : [{"url":"http://file1.assrt.net/onthefly/239398/-/1/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.ass?_=1469341998&-=f127379a7e4bd5508c6f73c24285ba1c&api=1","f":"S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.ass","s":"58KB"}]
         * videoname : FRIENDS Bluray 720p S01E02 蓝光/bluray
         * upload_time : 2013-07-16 01:44:12
         * vote_score : 0
         * url : http://file1.assrt.net/download/239398/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.zip?_=1469341998&-=7adf1246aa4d9d9d47c1771df53af8b4&api=1
         * title : FRIENDS Bluray S01E02 720p/老友记/六人行 蓝光版 第一季第二集/FRIENDS Bluray 720p S01E02 蓝光/bluray/S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End
         * id : 239398
         * lang : {"desc":"简 双语 其他语言","langlist":{"langdou":true,"langchs":true,"langoth":true}}
         */

        private List<SubsBean> subs;

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getAction() {
            return action;
        }

        public void setAction(String action) {
            this.action = action;
        }

        public List<SubsBean> getSubs() {
            return subs;
        }

        public void setSubs(List<SubsBean> subs) {
            this.subs = subs;
        }

        public static class SubsBean {
            private String filename;
            private String native_name;
            private int size;
            /**
             * producer : @张纳德nerd
             * verifier :
             * source : 原创翻译
             * uploader :
             */

            private ProducerBean producer;
            private int down_count;
            private int view_count;
            private String subtype;
            private String videoname;
            private String upload_time;
            private int vote_score;
            private String url;
            private String title;
            private int id;
            /**
             * desc : 简 双语 其他语言
             * langlist : {"langdou":true,"langchs":true,"langoth":true}
             */

            private LangBean lang;
            /**
             * url : http://file1.assrt.net/onthefly/239398/-/1/S01E02-FRIENDS(Bluray)-The%20One%20With%20the%20Sonogram%20at%20the%20End.ass?_=1469341998&-=f127379a7e4bd5508c6f73c24285ba1c&api=1
             * f : S01E02-FRIENDS(Bluray)-The One With the Sonogram at the End.ass
             * s : 58KB
             */

            private List<FilelistBean> filelist;

            public String getFilename() {
                return filename;
            }

            public void setFilename(String filename) {
                this.filename = filename;
            }

            public String getNative_name() {
                return native_name;
            }

            public void setNative_name(String native_name) {
                this.native_name = native_name;
            }

            public int getSize() {
                return size;
            }

            public void setSize(int size) {
                this.size = size;
            }

            public ProducerBean getProducer() {
                return producer;
            }

            public void setProducer(ProducerBean producer) {
                this.producer = producer;
            }

            public int getDown_count() {
                return down_count;
            }

            public void setDown_count(int down_count) {
                this.down_count = down_count;
            }

            public int getView_count() {
                return view_count;
            }

            public void setView_count(int view_count) {
                this.view_count = view_count;
            }

            public String getSubtype() {
                return subtype;
            }

            public void setSubtype(String subtype) {
                this.subtype = subtype;
            }

            public String getVideoname() {
                return videoname;
            }

            public void setVideoname(String videoname) {
                this.videoname = videoname;
            }

            public String getUpload_time() {
                return upload_time;
            }

            public void setUpload_time(String upload_time) {
                this.upload_time = upload_time;
            }

            public int getVote_score() {
                return vote_score;
            }

            public void setVote_score(int vote_score) {
                this.vote_score = vote_score;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public LangBean getLang() {
                return lang;
            }

            public void setLang(LangBean lang) {
                this.lang = lang;
            }

            public List<FilelistBean> getFilelist() {
                return filelist;
            }

            public void setFilelist(List<FilelistBean> filelist) {
                this.filelist = filelist;
            }

            public static class ProducerBean {
                private String producer;
                private String verifier;
                private String source;
                private String uploader;

                public String getProducer() {
                    return producer;
                }

                public void setProducer(String producer) {
                    this.producer = producer;
                }

                public String getVerifier() {
                    return verifier;
                }

                public void setVerifier(String verifier) {
                    this.verifier = verifier;
                }

                public String getSource() {
                    return source;
                }

                public void setSource(String source) {
                    this.source = source;
                }

                public String getUploader() {
                    return uploader;
                }

                public void setUploader(String uploader) {
                    this.uploader = uploader;
                }
            }

            public static class LangBean {
                private String desc;
                /**
                 * langdou : true
                 * langchs : true
                 * langoth : true
                 */

                private LanglistBean langlist;

                public String getDesc() {
                    return desc;
                }

                public void setDesc(String desc) {
                    this.desc = desc;
                }

                public LanglistBean getLanglist() {
                    return langlist;
                }

                public void setLanglist(LanglistBean langlist) {
                    this.langlist = langlist;
                }

                public static class LanglistBean {
                    private boolean langdou;
                    private boolean langchs;
                    private boolean langoth;

                    public boolean isLangdou() {
                        return langdou;
                    }

                    public void setLangdou(boolean langdou) {
                        this.langdou = langdou;
                    }

                    public boolean isLangchs() {
                        return langchs;
                    }

                    public void setLangchs(boolean langchs) {
                        this.langchs = langchs;
                    }

                    public boolean isLangoth() {
                        return langoth;
                    }

                    public void setLangoth(boolean langoth) {
                        this.langoth = langoth;
                    }
                }
            }

            public static class FilelistBean {
                private String url;
                private String f;
                private String s;

                public String getUrl() {
                    return url;
                }

                public void setUrl(String url) {
                    this.url = url;
                }

                public String getF() {
                    return f;
                }

                public void setF(String f) {
                    this.f = f;
                }

                public String getS() {
                    return s;
                }

                public void setS(String s) {
                    this.s = s;
                }
            }
        }
    }
}
