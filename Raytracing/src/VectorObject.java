import java.util.ArrayList;

public abstract class VectorObject {
    Vec3f pos;

    Material material;

    public VectorObject(Vec3f pos, Material material) {
        this.pos = pos;
        this.material = material;
    }

    protected VectorObject() {
    }

    public abstract ArrayList<Intersection> intersect(Ray ray);

    public abstract Vec3f getNormale(Intersection intersecObj);

}
