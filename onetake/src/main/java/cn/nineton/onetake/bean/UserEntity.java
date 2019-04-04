package cn.nineton.onetake.bean;


import cn.nineton.onetake.util.TextUtil;

public class UserEntity {
    private String sreen_name;

    public UserEntity(int user_id, String sreen_name) {
        if (TextUtil.isNull(sreen_name)) {
            sreen_name = "";
        }
        this.sreen_name = sreen_name;
    }

    public String getSreenName() {
        return this.sreen_name;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserEntity that = (UserEntity) o;
        if (this.sreen_name != null) {
            return this.sreen_name.equals(that.sreen_name);
        }
        if (that.sreen_name != null) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return this.sreen_name != null ? this.sreen_name.hashCode() : 0;
    }
}