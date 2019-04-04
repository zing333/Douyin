package cn.nineton.onetake.adapter;

import java.util.ArrayList;
import java.util.List;

import cn.nineton.onetake.bean.UndoModel;

public class UndoBean {
    private List<UndoModel> undoModels;

    public ArrayList<UndoModel> getUndoModels() {
        if (this.undoModels == null) {
            this.undoModels = new ArrayList();
        }
        return (ArrayList) this.undoModels;
    }

    public void setUndoModels(List<UndoModel> undoModels) {
        if (undoModels != null) {
            this.undoModels = undoModels;
        }
    }
}