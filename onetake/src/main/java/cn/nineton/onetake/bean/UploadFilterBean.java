package cn.nineton.onetake.bean;


import cn.nineton.onetake.util.TextUtil;

public class UploadFilterBean {
    private LeakBean Flip;
    private String Mode;
    private String Type;
    private String Value;

    public class LeakBean {
        public boolean Horizontal;
        public boolean Vertical;

        public LeakBean(boolean Horizontal, boolean Vertical) {
            this.Horizontal = Horizontal;
            this.Vertical = Vertical;
        }
    }

    public UploadFilterBean(){}

    public UploadFilterBean(String Type, String Value, String Mode) {
        if (TextUtil.isNull(Value)) {
            Value = "";
        }
        this.Value = Value;
        if (TextUtil.isNull(Type)) {
            Type = "";
        }
        this.Type = Type;
        if (TextUtil.isNull(Mode)) {
            Mode = "";
        }
        this.Mode = Mode;
    }

    public UploadFilterBean(String Type, String Value, String Mode, LeakBean Filp) {
        if (TextUtil.isNull(Type)) {
            Type = "";
        }
        this.Type = Type;
        if (TextUtil.isNull(Value)) {
            Value = "";
        }
        this.Value = Value;
        if (TextUtil.isNull(Mode)) {
            Mode = "";
        }
        this.Mode = Mode;
        this.Flip = Filp;
    }

    public UploadFilterBean(String Type, String arg1, boolean state) {
        if (TextUtil.isNull(Type)) {
            Type = "";
        }
        this.Type = Type;
        if (state) {
            if (TextUtil.isNull(arg1)) {
                arg1 = "";
            }
            this.Value = arg1;
            return;
        }
        if (TextUtil.isNull(arg1)) {
            arg1 = "";
        }
        this.Mode = arg1;
    }
}