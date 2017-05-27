package com.lvshandian.lemeng.moudles.index.live.bullfight;

import com.alibaba.fastjson.annotation.JSONField;

import java.util.List;

/**
 * Created by dong on 2017/5/26.
 */

public class PokerResult {

    /**
     * success : true
     * code : 1
     * msg : 开拍成功
     * obj : {"playerPokerMap":{"0":{"pokers":[{"color":3,"value":11},{"color":1,"value":1},{"color":3,"value":2},{"color":4,"value":5},{"color":2,"value":10}],"result":0},"1":{"pokers":[{"color":1,"value":11},{"color":1,"value":7},{"color":1,"value":12},{"color":3,"value":13},{"color":3,"value":3}],"result":10},"2":{"pokers":[{"color":4,"value":13},{"color":2,"value":12},{"color":4,"value":3},{"color":3,"value":8},{"color":1,"value":8}],"result":0},"3":{"pokers":[{"color":3,"value":9},{"color":3,"value":7},{"color":4,"value":7},{"color":1,"value":13},{"color":1,"value":10}],"result":0}},"result":{"lose":[],"win":[1,2,3]}}
     */

    private boolean success;
    private int code;
    private String msg;
    private ObjBean obj;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ObjBean getObj() {
        return obj;
    }

    public void setObj(ObjBean obj) {
        this.obj = obj;
    }

    public static class ObjBean {
        /**
         * playerPokerMap : {"0":{"pokers":[{"color":3,"value":11},{"color":1,"value":1},{"color":3,"value":2},{"color":4,"value":5},{"color":2,"value":10}],"result":0},"1":{"pokers":[{"color":1,"value":11},{"color":1,"value":7},{"color":1,"value":12},{"color":3,"value":13},{"color":3,"value":3}],"result":10},"2":{"pokers":[{"color":4,"value":13},{"color":2,"value":12},{"color":4,"value":3},{"color":3,"value":8},{"color":1,"value":8}],"result":0},"3":{"pokers":[{"color":3,"value":9},{"color":3,"value":7},{"color":4,"value":7},{"color":1,"value":13},{"color":1,"value":10}],"result":0}}
         * result : {"lose":[],"win":[1,2,3]}
         */

        private PlayerPokerMapBean playerPokerMap;
        private ResultBean result;

        public PlayerPokerMapBean getPlayerPokerMap() {
            return playerPokerMap;
        }

        public void setPlayerPokerMap(PlayerPokerMapBean playerPokerMap) {
            this.playerPokerMap = playerPokerMap;
        }

        public ResultBean getResult() {
            return result;
        }

        public void setResult(ResultBean result) {
            this.result = result;
        }

        public static class PlayerPokerMapBean {
            /**
             * 0 : {"pokers":[{"color":3,"value":11},{"color":1,"value":1},{"color":3,"value":2},{"color":4,"value":5},{"color":2,"value":10}],"result":0}
             * 1 : {"pokers":[{"color":1,"value":11},{"color":1,"value":7},{"color":1,"value":12},{"color":3,"value":13},{"color":3,"value":3}],"result":10}
             * 2 : {"pokers":[{"color":4,"value":13},{"color":2,"value":12},{"color":4,"value":3},{"color":3,"value":8},{"color":1,"value":8}],"result":0}
             * 3 : {"pokers":[{"color":3,"value":9},{"color":3,"value":7},{"color":4,"value":7},{"color":1,"value":13},{"color":1,"value":10}],"result":0}
             */

            @JSONField(name = "L0")
            private Poker0 poker0;
            @JSONField(name = "L1")
            private Poker1 poker1;
            @JSONField(name = "L2")
            private Poker2 poker2;
            @JSONField(name = "L3")
            private Poker3 poker3;

            public Poker0 getPoker0() {
                return poker0;
            }

            public void setPoker0(Poker0 poker0) {
                this.poker0 = poker0;
            }

            public Poker1 getPoker1() {
                return poker1;
            }

            public void setPoker1(Poker1 poker1) {
                this.poker1 = poker1;
            }

            public Poker2 getPoker2() {
                return poker2;
            }

            public void setPoker2(Poker2 poker2) {
                this.poker2 = poker2;
            }

            public Poker3 getPoker3() {
                return poker3;
            }

            public void setPoker3(Poker3 poker3) {
                this.poker3 = poker3;
            }
            public static class PokersBean {
                /**
                 * color : 3
                 * value : 11
                 */

                private int color;
                private int value;

                public int getColor() {
                    return color;
                }

                public void setColor(int color) {
                    this.color = color;
                }

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }
            public static class Poker0 {
                /**
                 * pokers : [{"color":3,"value":11},{"color":1,"value":1},{"color":3,"value":2},{"color":4,"value":5},{"color":2,"value":10}]
                 * result : 0
                 */

                private int result;
                private List<PokersBean> pokers;

                public int getResult() {
                    return result;
                }

                public void setResult(int result) {
                    this.result = result;
                }

                public List<PokersBean> getPokers() {
                    return pokers;
                }

                public void setPokers(List<PokersBean> pokers) {
                    this.pokers = pokers;
                }

            }

            public static class Poker1 {
                /**
                 * pokers : [{"color":1,"value":11},{"color":1,"value":7},{"color":1,"value":12},{"color":3,"value":13},{"color":3,"value":3}]
                 * result : 10
                 */

                private int result;
                private List<PokersBean> pokers;

                public int getResult() {
                    return result;
                }

                public void setResult(int result) {
                    this.result = result;
                }

                public List<PokersBean> getPokers() {
                    return pokers;
                }

                public void setPokers(List<PokersBean> pokers) {
                    this.pokers = pokers;
                }

            }

            public static class Poker2 {
                /**
                 * pokers : [{"color":4,"value":13},{"color":2,"value":12},{"color":4,"value":3},{"color":3,"value":8},{"color":1,"value":8}]
                 * result : 0
                 */

                private int result;
                private List<PokersBean> pokers;

                public int getResult() {
                    return result;
                }

                public void setResult(int result) {
                    this.result = result;
                }

                public List<PokersBean> getPokers() {
                    return pokers;
                }

                public void setPokers(List<PokersBean> pokers) {
                    this.pokers = pokers;
                }

            }

            public static class Poker3 {
                /**
                 * pokers : [{"color":3,"value":9},{"color":3,"value":7},{"color":4,"value":7},{"color":1,"value":13},{"color":1,"value":10}]
                 * result : 0
                 */

                private int result;
                private List<PokersBean> pokers;

                public int getResult() {
                    return result;
                }

                public void setResult(int result) {
                    this.result = result;
                }

                public List<PokersBean> getPokers() {
                    return pokers;
                }

                public void setPokers(List<PokersBean> pokers) {
                    this.pokers = pokers;
                }
            }
        }

        public static class ResultBean {
            private List<?> lose;
            private List<Integer> win;

            public List<?> getLose() {
                return lose;
            }

            public void setLose(List<?> lose) {
                this.lose = lose;
            }

            public List<Integer> getWin() {
                return win;
            }

            public void setWin(List<Integer> win) {
                this.win = win;
            }
        }
    }
}
