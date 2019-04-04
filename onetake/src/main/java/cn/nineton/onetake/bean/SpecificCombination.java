package cn.nineton.onetake.bean;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.util.ArrayList;
import java.util.List;

public class SpecificCombination {
    public static final int SPECIFICCOMBINATION_ID_RESET = 0;
    private int combination_id;
    private String name_chs;
    private String name_cht;
    private String name_en;
    private List<SpecificsBean> specifics;

    public String toString() {
        return "SpecificCombination{name_chs='" + this.name_chs + '\'' + ", name_cht='" + this.name_cht + '\'' + ", name_en='" + this.name_en + '\'' + ", combination_id=" + this.combination_id + ", specifics=" + this.specifics + '}';
    }

    public static List<SpecificCombination> arraySpecificCombinationFromData(String str) {
        return (List) new Gson().fromJson(str, new TypeToken<ArrayList<SpecificCombination>>() {
        }.getType());
    }

    public String getName_chs() {
        return this.name_chs;
    }

    public void setName_chs(String name_chs) {
        this.name_chs = name_chs;
    }

    public String getName_cht() {
        return this.name_cht;
    }

    public void setName_cht(String name_cht) {
        this.name_cht = name_cht;
    }

    public String getName_en() {
        return this.name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public int getCombination_id() {
        return this.combination_id;
    }

    public void setCombination_id(int combination_id) {
        this.combination_id = combination_id;
    }

    public List<SpecificsBean> getSpecifics() {
        return this.specifics;
    }

    public void setSpecifics(List<SpecificsBean> specifics) {
        this.specifics = specifics;
    }
}