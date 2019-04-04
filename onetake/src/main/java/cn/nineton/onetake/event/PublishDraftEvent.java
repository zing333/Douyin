package cn.nineton.onetake.event;


import cn.nineton.onetake.util.DraftModel;

public class PublishDraftEvent {
    private DraftModel draftModel;

    public PublishDraftEvent(DraftModel draftModel) {
        this.draftModel = draftModel;
    }

    public DraftModel getDraftModel() {
        return this.draftModel;
    }
}