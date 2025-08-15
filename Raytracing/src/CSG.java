import java.util.ArrayList;
import java.util.Comparator;

public abstract class CSG extends VectorObject{

    VectorObject objA;
    VectorObject objB;


    public CSG(Vec3f pos, VectorObject objA, VectorObject obj2) {
        this.objA = objA;
        this.objB = obj2;
        this.pos = pos;
        this.objA.pos = this.pos.addVector(objA.pos);
        this.objB.pos = this.pos.addVector(objB.pos);
        this.material = null;
    }

    /*intersections: objA1 = 0, objA2 = 1, objB1 = 2, objB2 = 3
     the 1. intersec is always the first one
    difference
    case 1, objA is hit first -> return intersections.get(0); // objA1
    case 2, objB is hit first then objA -> return intersections.get(3); // objB2
    case 3,objB is hit but objA not -> return  null
    case 4, objB is hit first then objA, but objA is exited before objB is exited -> return null;
    intersects
    case 1, only objA or objB is hit -> return null;
    case 2, objB is hit first then objA -> return intersections.get(0);
    case 3,objA is hit first then objB -> return intersections.get(2); */

    public static class Union extends CSG{


        public Union(Vec3f pos, VectorObject objA, VectorObject obj2) {
            super(pos, objA, obj2);
        }

        public ArrayList<Intersection> intersect(Ray ray){
            ArrayList<Intersection> intersections = new ArrayList<>();
            ArrayList<Intersection> allIntersections = new ArrayList<>();
            allIntersections.addAll(objA.intersect(ray));
            allIntersections.addAll(objB.intersect(ray));

            Intersection tmp = new Intersection(null,null, Float.POSITIVE_INFINITY);
            for (int i = 0; i < allIntersections.size(); i++) {
                if(tmp.s >= allIntersections.get(i).s) tmp = new Intersection(allIntersections.get(i));
            }

            allIntersections.sort(Comparator.comparingDouble(p -> p.s));
            intersections.add(allIntersections.get(0));
            intersections.add(allIntersections.get(allIntersections.size()-1));

            return allIntersections;
        }

        public Vec3f getNormale(Intersection obj) {
            return obj.obj.getNormale(obj);
        }

    }
    public static class Difference extends CSG {


        public Difference(Vec3f pos, VectorObject objA, VectorObject obj2) {
            super(pos, objA, obj2);
        }

        public ArrayList<Intersection> intersect(Ray ray){
            ArrayList<Intersection> allIntersections = new ArrayList<>();
            ArrayList<Intersection> intersecsA = objA.intersect(ray);
            ArrayList<Intersection> intersecsB = objB.intersect(ray);
            allIntersections.addAll(intersecsA);
            allIntersections.addAll(intersecsB);

            ArrayList<Intersection> intersections = new ArrayList<>();

            /*if(Float.isFinite(allIntersections.get(0).s) && (!Float.isFinite(allIntersections.get(2).s)  && !Float.isFinite(allIntersections.get(3).s)) || allIntersections.get(0).s <= allIntersections.get(2).s){
                intersections.add(allIntersections.get(0));
            } else if (
                    (allIntersections.get(2).s <= allIntersections.get(0).s && allIntersections.get(1).s >= allIntersections.get(3).s && allIntersections.get(2).s <= allIntersections.get(1).s) && Float.isFinite(allIntersections.get(0).s) ){
                intersections.add(allIntersections.get(3));
            }*/

            // wip code
            /*case 1, objA is hit first -> return intersections.get(0); // objA1
            case 2, objB is hit first then objA -> return intersections.get(3); // objB2
            case 3,objB is hit but objA not -> return  null
            case 4, objB is hit first then objA, but objA is exited before objB is exited -> return null;*/
            allIntersections.sort(Comparator.comparingDouble(p -> p.s));
            if ( !Float.isFinite(intersecsA.get(0).s) ){ //case 3 A is not hit
                intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));
                return intersections;
            } else if( intersecsA.contains(allIntersections.get(0)) ) {// A in
                intersections.add(allIntersections.get(0));
                return intersections;
            } else if(intersecsB.contains(allIntersections.get(0)) && intersecsA.contains(allIntersections.get(1)) ){ // B in,A in
                if(intersecsA.contains(allIntersections.get(2))) { // B, A in: A out
                    intersections.add(new Intersection(this,null, Float.POSITIVE_INFINITY));
                    return intersections;
                } else if(intersecsB.contains(allIntersections.get(2))){ // B in, A in: B out
                    intersections.add(allIntersections.get(2));
                }
                //if() // B in, A in:
            }

            intersections.sort(Comparator.comparingDouble(p -> p.s));
            return intersections;
        }



        public Vec3f getNormale(Intersection obj) {
            Vec3f n = obj.obj.getNormale(obj);
            if(obj.obj == this.objB){
                return n.multiply(-1);
            }
            return n;
        }
    }
    public static class Intersect extends CSG{

        public Intersect(Vec3f pos, VectorObject objA, VectorObject obj2) {
            super(pos, objA, obj2);
        }

        public ArrayList<Intersection> intersect(Ray ray){
            ArrayList<Intersection> allIntersections = new ArrayList<>();
            allIntersections.addAll(objA.intersect(ray));
            allIntersections.addAll(objB.intersect(ray));

            ArrayList<Intersection> intersections = new ArrayList<>();

            /*if( !Float.isFinite(allIntersections.get(0).s) || !Float.isFinite(allIntersections.get(2).s) ){
            } else*/
            if (allIntersections.get(2).s <= allIntersections.get(0).s){
                intersections.add(allIntersections.get(0));

            } else if (allIntersections.get(0).s <= allIntersections.get(2).s){
                intersections.add(allIntersections.get(2));

            }
            intersections.sort(Comparator.comparingDouble(p -> p.s));
            return intersections;
        }


        public Vec3f getNormale(Intersection obj) {
            return obj.obj.getNormale(obj);
        }
    }
}
