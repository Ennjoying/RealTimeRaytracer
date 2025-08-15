public class Intersection {
    VectorObject obj;
    VectorObject parentObj;
    Vec3f vecIntersec;
    float s;

    public Intersection(VectorObject obj, Vec3f vecIntersec, float s) {
        this.obj = obj;
        this.vecIntersec = vecIntersec;
        this.s = s;
        this.parentObj = null;
    }

    public Intersection(Intersection intersection) {
        this.obj = intersection.obj;
        this.s = intersection.s;
    }
}
