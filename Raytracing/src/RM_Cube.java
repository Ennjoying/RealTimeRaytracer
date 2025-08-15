

import java.util.ArrayList;

public class RM_Cube extends VectorObject{
    Vec3f R;

    boolean isINTERSECTION;

    boolean isDemonDistorted = false;
    DEMONDISTORTION dd;

    public RM_Cube(Vec3f pos, Material material, Vec3f r) {
        super(pos, material);
        R = r;
    }

    public void setDemonDistortion(AxisSelector selectedAxis, Vec3f posToDistortAround, int turns){
        isDemonDistorted = true;
        dd = new DEMONDISTORTION(selectedAxis, posToDistortAround, turns);
    }

    @Override
    public ArrayList<Intersection> intersect(Ray ray) {
        //SDF function & raymarching algorithm2
        ArrayList<Intersection> intersections = new ArrayList<>();

        Intersection intersec = rayMarch(ray, pos.length()+Math.abs(R.length()));

        intersec.vecIntersec = ray.pos.addVector(ray.view.multiply(intersec.s));
        intersec.parentObj = intersec.obj;
        intersections.add(intersec);

        return intersections;
    }

    private Intersection rayMarch(Ray ray, float maxPossibleDistance){
        Vec3f punkt = new Vec3f(ray.pos);

        Intersection intersec = new Intersection(this,null,Float.POSITIVE_INFINITY);
        float s=0;
        float abstand;
        float epsilon = 0.01f;
        while(s < maxPossibleDistance){

            abstand = SDF(punkt);
            s+= abstand;
            if(abstand < epsilon){
                intersec.s = s;
                return intersec;
            }
            punkt = punkt.addVector(ray.view.multiply(abstand));
        }
        return intersec;
    }

    private float SDF(float x, float y, float z){
        return SDF( new Vec3f (x,y,z));
    }
    private float SDF(Vec3f p){
        //change p for dämonenverzerrung vor allem anderen

        if (isDemonDistorted) p = dd.demonDistortion(p, R.y);

        Vec3f q = p.subtractVector(pos).abs().subtractVector(R);
        return q.max(0).length()+Math.min(q.maxComp(),0);
    }

    @Override
    public Vec3f getNormale(Intersection intersec) {
        float a = 0.01f;
        float x = intersec.vecIntersec.x;
        float y = intersec.vecIntersec.y;
        float z = intersec.vecIntersec.z;

        return new Vec3f(
                SDF(x+a,y,z) - SDF(x-a,y,z),
                SDF(x,y+a,z) - SDF(x,y-a,z),
                SDF(x,y,z+a) - SDF(x,y,z-a));
    }
}
