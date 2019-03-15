package cn.nineton.onetake.widget;

public class ChangeShareEvent {
    public int isSucess;
    public int photoId;
    public int state;
    public String type;

    public ChangeShareEvent(int photoId, String type, int state, int isSucess) {
        this.photoId = photoId;
        this.type = type;
        this.state = state;
        this.isSucess = isSucess;
    }
}