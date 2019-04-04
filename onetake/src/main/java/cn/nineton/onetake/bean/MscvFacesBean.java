package cn.nineton.onetake.bean;

public class MscvFacesBean {
    private int age;
    private MscvFaceRectangleBean faceRectangle;
    private String gender;

    public int getAge() {
        return this.age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return this.gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public MscvFaceRectangleBean getFaceRectangle() {
        return this.faceRectangle;
    }

    public void setFaceRectangle(MscvFaceRectangleBean faceRectangle) {
        this.faceRectangle = faceRectangle;
    }

    public String toString() {
        return "MscvFacesBean{age=" + this.age + ", gender='" + this.gender + '\'' + ", faceRectangle=" + this.faceRectangle + '}';
    }
}