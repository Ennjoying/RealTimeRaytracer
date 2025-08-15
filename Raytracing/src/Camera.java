public class Camera{
    Vec3f pos;
    Vec3f view;
    Vec3f top;
    Vec3f right;
    Vec3f bottomLeft;
    float width = 1.6f;
    float height = 0.9f;

    public Camera(Vec3f pos, Vec3f view, Vec3f topVector) {
        this.pos = pos;
        this.view = view;
        this.top = topVector.normalize();
        this.right = top.crossProduct(view).multiply(-1).normalize();
        this.bottomLeft = view.subtractVector(right.multiply(0.5f*width) ).subtractVector(top.multiply(0.5f*height));
    }

    public Vec3f getPos() {
        return pos;
    }

    public void setPos(Vec3f pos) {
        this.pos = pos;
    }

    public Vec3f getView() {
        return view;
    }

    public void setView(Vec3f view) {
        this.view = view;
    }

    public Vec3f getTop() {
        return top;
    }

    public void setTop(Vec3f top) {
        this.top = top;
    }

    public Vec3f getRight() {
        return right;
    }

    public void setRight(Vec3f right) {
        this.right = right;
    }

    public Vec3f getBottomLeft() {
        return bottomLeft;
    }

    public void setBottomLeft(Vec3f bottomLeft) {
        this.bottomLeft = bottomLeft;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }
}
