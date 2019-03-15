package cn.nineton.onetake.widget;

public class TextInputEvent {
    private String source;
    private String textContent;

    public TextInputEvent(String textContent, String source) {
        this.textContent = textContent;
        this.source = source;
    }

    public String getSource() {
        return this.source;
    }

    public String getTextContent() {
        return this.textContent;
    }
}